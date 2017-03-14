package com.doumdoum.nmanel.metronome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Sequence;
import com.doumdoum.nmanel.metronome.model.Sequences;
import com.doumdoum.nmanel.metronome.model.SequencesManager;
import com.doumdoum.nmanel.metronome.ui.SequenceView;
import com.doumdoum.nmanel.metronome.ui.SequencesSpinner;

public class SequenceEditorActivity extends AppCompatActivity {
    private SequencesSpinner sequencesSpinner;
    private Sequences sequences;
    private Bar sequenceToEdit;
    private Button removeSequenceButton;
    private SequenceView sequenceView;
    private EditText sequenceNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_editor);

        sequences = (new SequencesManager(getApplicationContext())).loadSequences();

        removeSequenceButton = (Button) findViewById(R.id.removeBarButtonId);

        sequenceNameEditText = (EditText) findViewById(R.id.sequenceEditorNameValueId);

        sequencesSpinner = (SequencesSpinner) findViewById(R.id.sequencesEditorSpinnerId);
        sequencesSpinner.setSequences(sequences);
        sequencesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sequences.getSequences().size() == 0) {
                    removeSequenceButton.setEnabled(false);
                    return;
                }
                removeSequenceButton.setEnabled(true);

//                updateSequenceEditor(parent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void updateSequenceEditor(AdapterView<?> parent) {
        Sequence selectedItem = (Sequence) parent.getSelectedItem();
        sequenceView.setSequence(selectedItem);
        sequenceNameEditText.setText(selectedItem.getName());
    }

    public void saveSequenceAction(View view) {
    }

    public void addToSequenceAction(View view) {

    }

    public void removeSequenceAction(View view) {
        sequences.removeSequence((Sequence) sequencesSpinner.getSelectedItem());
    }

    public void createNewSequenceAction(View view) {
        Bar sequenceToEdit = new Bar();
        Sequence sequence = new Sequence("New Sequence");
        sequences.addSequence(sequence);


    }
}
