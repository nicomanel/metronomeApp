package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.doumdoum.nmanel.metronome.R;
import com.doumdoum.nmanel.metronome.model.Sequence;

/**
 * Created by nmanel on 3/14/2017.
 */

public class SequenceEditor extends LinearLayout {

    private Button addBarToSequenceButton;
    private SequenceView sequenceView;
    private Spinner iterationNumberSpinner;
    private Sequence sequence;
    private EditText sequenceName;
    private BarsSpinner barChoiceSpinner;

    public SequenceEditor(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addView(LayoutInflater.from(context).inflate(R.layout.sequence_editor, null));
        addBarToSequenceButton = (Button) findViewById(R.id.sequenceEditorButtonAddBarsToSequenceId);

        barChoiceSpinner = (BarsSpinner) findViewById(R.id.sequenceEditorBarChoiceSpinnerId);
        iterationNumberSpinner = (Spinner) findViewById(R.id.sequenceEditorIterationNumberSpinnerId);


        sequenceName = (EditText) findViewById(R.id.sequenceEditorNameValueId);
        sequenceView = (SequenceView) findViewById(R.id.sequenceEditorSequenceViewId);

//        addBarToSequenceButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (sequence == null)
//                    return;
//
//                int iteration = Integer.valueOf("" + iterationNumberSpinner.getSelectedItem()).intValue();
//                Bar bar = (Bar) barChoiceSpinner.getSelectedItem();
//
//                for(int i = 0; i < iteration; i++)
//                {
//                    sequence.addBar(bar);
//                }
//                sequence.notifyObservers();
//            }
//        });

    }

    public void setSequence(Sequence newSequence) {
        sequence = newSequence;
        updateSequenceUI();
    }

    private void updateSequenceUI() {
        sequenceName.setText(sequence.getName());
        sequenceView.setSequence(sequence);
    }


}
