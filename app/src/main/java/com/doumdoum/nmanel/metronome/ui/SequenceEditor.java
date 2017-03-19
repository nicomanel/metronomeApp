package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.doumdoum.nmanel.metronome.R;
import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Bars;
import com.doumdoum.nmanel.metronome.model.BarsManager;
import com.doumdoum.nmanel.metronome.model.Sequence;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by nmanel on 3/14/2017.
 */

public class SequenceEditor extends LinearLayout implements Observer {

    private static final String LOG = SequenceEditor.class.toString();
    private Button addBarToSequenceButton;
    private SequenceView sequenceView;
    private Spinner iterationNumberSpinner;
    private Sequence sequence;
    private EditText sequenceName;
    private BarsSpinner barChoiceSpinner;
    private Bars bars;

    public SequenceEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(LayoutInflater.from(context).inflate(R.layout.sequence_editor, null));
        addBarToSequenceButton = (Button) findViewById(R.id.sequenceEditorButtonAddBarsToSequenceId);

        barChoiceSpinner = (BarsSpinner) findViewById(R.id.sequenceEditorBarChoiceSpinnerId);
        iterationNumberSpinner = (Spinner) findViewById(R.id.sequenceEditorIterationNumberSpinnerId);
        barChoiceSpinner.setBars((new BarsManager(context)).loadBars());
        sequenceName = (EditText) findViewById(R.id.sequenceEditorNameValueId);
        sequenceView = (SequenceView) findViewById(R.id.sequenceEditorSequenceViewId);

        addBarToSequenceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequence == null)
                    return;

                int iteration = Integer.valueOf("" + iterationNumberSpinner.getSelectedItem()).intValue();
                Bar bar = (Bar) barChoiceSpinner.getSelectedItem();

                for (int i = 0; i < iteration; i++) {
                    sequence.addBar(bar);
                }
                sequence.notifyObservers();
            }
        });


    }

    public void setSequence(Sequence newSequence) {
        if (sequence != null) {
            sequence.deleteObserver(this);
        }
        sequence = newSequence;
        if (sequence != null) {
            sequence.addObserver(this);
        }
        updateSequenceUI();
    }

    public void setBars(Bars bars) {
        this.bars = bars;
    }

    private void updateSequenceUI() {
        if (sequence == null) {
            this.setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        sequenceName.setText(sequence.getName());
        sequenceView.setSequence(sequence);
    }


    @Override
    public void update(Observable o, Object arg) {
        updateSequenceUI();
    }
}
