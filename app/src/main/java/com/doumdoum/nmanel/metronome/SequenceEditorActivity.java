package com.doumdoum.nmanel.metronome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Bars;
import com.doumdoum.nmanel.metronome.model.BarsManager;
import com.doumdoum.nmanel.metronome.ui.BarsSpinner;
import com.doumdoum.nmanel.metronome.ui.SequenceView;

public class SequenceEditorActivity extends AppCompatActivity {
    private BarsSpinner sequencesSpinner;
    private Bars sequences;
    private Bar sequenceToEdit;
    private Button removeSequenceButton;
    private SequenceView sequenceView;
    private EditText sequenceNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_editor);


        sequences = (new BarsManager(getApplicationContext())).loadSequences();

        removeSequenceButton = (Button) findViewById(R.id.removeSequenceButtonId);



        sequenceNameEditText = (EditText) findViewById(R.id.sequenceEditorNameValue);




   sequencesSpinner = (BarsSpinner) findViewById(R.id.sequencesSpinnerId);
        sequencesSpinner.setBars(sequences);
        sequencesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sequences.getBars().size() == 0)
                {
                    removeSequenceButton.setEnabled(false);
                    return;
                }
                    removeSequenceButton.setEnabled(true);

                updateSequenceEditor(parent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    protected void updateSequenceEditor(AdapterView<?> parent) {
        Bar selectedItem = (Bar) parent.getSelectedItem();
        sequenceView.setBar(selectedItem);
        sequenceNameEditText.setText(selectedItem.getName());

    }

    public void saveSequenceAction(View view) {
    }

    public void addToSequenceAction(View view) {

    }

    public void removeSequenceAction(View view) {
        sequences.removeBar((Bar)sequencesSpinner.getSelectedItem());
    }

    public void createNewSequenceAction(View view) {
        Bar sequenceToEdit = new Bar();



    }
}
