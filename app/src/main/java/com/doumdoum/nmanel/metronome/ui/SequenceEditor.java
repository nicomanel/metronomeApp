package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.doumdoum.nmanel.metronome.R;
import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Bars;
import com.doumdoum.nmanel.metronome.model.BarsManager;
import com.doumdoum.nmanel.metronome.model.Beat;
import com.doumdoum.nmanel.metronome.model.Sequence;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by nmanel on 3/14/2017.
 */

public class SequenceEditor extends LinearLayout implements Observer {

    private static final String LOG = SequenceEditor.class.toString();
    private final BarEditor barEditor;
    private final Button addBarToSequenceFromBarEditorButton;
    private final Button addBarToSequenceAndSaveButton;
    private Button addBarToSequenceButton;
    private SequenceView sequenceView;
    private Spinner iterationNumberSpinner;
    private Sequence sequence;
    private EditText sequenceName;
    private BarsSpinner barChoiceSpinner;
    private Bars bars;
    private RadioButton useExistingBarsButton;
    private RadioButton createNewBarButton;
    private Bar barInCreation;


    public SequenceEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(LayoutInflater.from(context).inflate(R.layout.sequence_editor, null));

        addBarToSequenceButton = (Button) findViewById(R.id.sequenceEditorButtonAddBarsToSequenceId);
        addBarToSequenceFromBarEditorButton = (Button) findViewById(R.id.sequenceEditorBarEditorButtonAddBarsToSequenceId);
        addBarToSequenceAndSaveButton = (Button) findViewById(R.id.sequenceEditorBarEditorButtonAddBarsToSequenceAndSaveId);

        bars = (new BarsManager(context)).loadBars();
        barChoiceSpinner = (BarsSpinner) findViewById(R.id.sequenceEditorBarChoiceSpinnerId);
        iterationNumberSpinner = (Spinner) findViewById(R.id.sequenceEditorIterationNumberSpinnerId);
        barChoiceSpinner.setBars(bars);
        sequenceName = (EditText) findViewById(R.id.sequenceEditorNameValueId);
        sequenceView = (SequenceView) findViewById(R.id.sequenceEditorSequenceViewId);
        useExistingBarsButton = (RadioButton) findViewById(R.id.sequenceEditorUseExistingBarsButtonId);
        createNewBarButton = (RadioButton) findViewById(R.id.sequenceEditorCreateNewBarButtonId);
        barEditor = (BarEditor) findViewById(R.id.sequenceEditorBarEditorId);

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sequenceEditorUseExistingBarsButtonId:
//                        barEditor.setVisibility(GONE);
                        findViewById(R.id.sequenceEditorBarEditorLayoutId).setVisibility(GONE);

                        findViewById(R.id.sequenceEditorBarChoiceLayoutId).setVisibility(VISIBLE);
                        barChoiceSpinner.setBars(bars);
                        break;
                    case R.id.sequenceEditorCreateNewBarButtonId:
                        barInCreation = new Bar("New Bar");
                        bars.addBar(barInCreation);
                        barInCreation.addBeat(new Beat(Beat.Style.Accent1));
                        barInCreation.addBeat(new Beat(Beat.Style.Normal));
                        barInCreation.addBeat(new Beat(Beat.Style.Normal));
                        barInCreation.addBeat(new Beat(Beat.Style.Normal));
                        barEditor.setBar(barInCreation);
                        findViewById(R.id.sequenceEditorBarEditorLayoutId).setVisibility(VISIBLE);
                        findViewById(R.id.sequenceEditorBarChoiceLayoutId).setVisibility(GONE);
                        break;
                }
            }
        };
        useExistingBarsButton.setOnClickListener(listener);
        createNewBarButton.setOnClickListener(listener);


        addBarToSequenceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequence == null)
                    return;

                int iteration = Integer.valueOf(iterationNumberSpinner.getSelectedItem().toString()).intValue();
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
