package com.doumdoum.nmanel.metronome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.doumdoum.nmanel.metronome.model.Bars;
import com.doumdoum.nmanel.metronome.model.BarsManager;
import com.doumdoum.nmanel.metronome.ui.BarsSpinner;

public class SequenceEditorActivity extends AppCompatActivity {
    private BarsSpinner barsSpinner;
    private Bars sequences;
    private Button removeSequenceButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_editor);


        sequences = (new BarsManager(getApplicationContext())).loadSequences();

        removeSequenceButton = (Button) findViewById(R.id.removeSequenceButtonId);


        barsSpinner = (BarsSpinner) findViewById(R.id.sequencesSpinnerId);
        barsSpinner.setBars();



    }
}
