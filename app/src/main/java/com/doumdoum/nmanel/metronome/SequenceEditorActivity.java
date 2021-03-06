package com.doumdoum.nmanel.metronome;

import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Bars;
import com.doumdoum.nmanel.metronome.model.BarsManager;
import com.doumdoum.nmanel.metronome.model.Sequence;
import com.doumdoum.nmanel.metronome.model.Sequences;
import com.doumdoum.nmanel.metronome.model.SequencesManager;
import com.doumdoum.nmanel.metronome.ui.SequenceEditor;
import com.doumdoum.nmanel.metronome.ui.SequenceEditorListener;
import com.doumdoum.nmanel.metronome.ui.SequencesSpinner;

public class SequenceEditorActivity extends AppCompatActivity {
    private SequencesSpinner sequencesSpinner;
    private Sequences sequences;
    private Button removeSequenceButton;
    private SequenceEditor sequenceEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_editor);
        sequences = (new SequencesManager(getApplicationContext())).loadSequences();
        removeSequenceButton = (Button) findViewById(R.id.removeBarButtonId);
        sequenceEditor = (SequenceEditor) findViewById(R.id.sequencesEditorId);
        sequenceEditor.setSequenceEditorListener(new SequenceEditorListener() {
            @Override
            public void hasChanged(SequenceEditor editor) {
                (new SequencesManager(getApplicationContext())).saveSequences(sequences);
                (new BarsManager(getApplicationContext())).saveBars(sequenceEditor.getBars());
            }
        });

        sequencesSpinner = (SequencesSpinner) findViewById(R.id.sequencesEditorSpinnerId);
        sequencesSpinner.setSequences(sequences);
        sequencesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sequenceEditor == null) {
                    return;
                }
                if (sequences.getSequences().size() == 0) {
                    removeSequenceButton.setEnabled(false);
                    return;
                }
                removeSequenceButton.setEnabled(true);
                sequenceEditor.setSequence((Sequence) parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void removeSequenceAction(View view) {
        Sequence sequenceToRemove = (Sequence) sequencesSpinner.getSelectedItem();
        sequences.removeSequence(sequenceToRemove);
        sequences.notifyObservers();
        Sequence nextSequence = (Sequence) sequencesSpinner.getSelectedItem();
        sequenceEditor.setSequence(nextSequence);
    }

    public void createNewSequenceAction(View view) {
        Sequence sequence = new Sequence("New Sequence");
        sequences.addSequence(sequence);
        sequences.notifyObservers();
        sequenceEditor.setSequence(sequence);
        sequencesSpinner.setSelection(sequences.getSequences().size() - 1);
    }

    private void writeToGson() {
        SequencesManager manager = new SequencesManager(getApplicationContext());
        Sequences clonedSequences = sequences.clone();
        for (Sequence sequence : clonedSequences.getSequences()) {
            sequence.deleteObservers();
        }
        manager.saveSequences(clonedSequences);
    }

    @Override
    public void finish() {
        writeToGson();
        super.finish();
    }
}
