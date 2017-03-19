package com.doumdoum.nmanel.metronome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.doumdoum.nmanel.metronome.model.Sequence;
import com.doumdoum.nmanel.metronome.model.Sequences;
import com.doumdoum.nmanel.metronome.model.SequencesManager;
import com.doumdoum.nmanel.metronome.ui.SequenceEditor;
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

    public void saveSequenceAction(View view) {
    }

    public void addToSequenceAction(View view) {

    }

    public void removeSequenceAction(View view) {
//        if (sequences.getSequences().size() == 0)
//        {
//            return;
//        }
        Sequence sequenceToRemove = (Sequence) sequencesSpinner.getSelectedItem();
        sequences.removeSequence(sequenceToRemove);
        Sequence nextSequence = (Sequence) sequencesSpinner.getSelectedItem();
        sequenceEditor.setSequence(nextSequence);
        sequences.notifyObservers();
    }

    public void createNewSequenceAction(View view) {
        Sequence sequence = new Sequence("New Sequence");
        sequences.addSequence(sequence);
        sequenceEditor.setSequence(sequence);
        sequences.notifyObservers();
    }
}
