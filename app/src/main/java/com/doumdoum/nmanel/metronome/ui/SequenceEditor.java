package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private final Spinner iterationNumberSpinnerForBarEditor;
    private final RadioGroup barTypeButtons;
    private Button addBarToSequenceButton;
    c
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
        iterationNumberSpinnerForBarEditor = (Spinner) findViewById(R.id.sequenceEditorBarEditorIterationNumberSpinnerId);
        barChoiceSpinner.setBars(bars);
        sequenceName = (EditText) findViewById(R.id.sequenceEditorNameValueId);
        sequenceView = (SequenceView) findViewById(R.id.sequenceEditorSequenceViewId);
        barTypeButtons = (RadioGroup) findViewById(R.id.sequenceEditorBarTypeGroupId);
        useExistingBarsButton = (RadioButton) findViewById(R.id.sequenceEditorUseExistingBarsButtonId);
        createNewBarButton = (RadioButton) findViewById(R.id.sequenceEditorCreateNewBarButtonId);
        barEditor = (BarEditor) findViewById(R.id.sequenceEditorBarEditorId);

        barTypeButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.sequenceEditorUseExistingBarsButtonId:
//                        barEditor.setVisibility(GONE);
                        findViewById(R.id.sequenceEditorBarEditorLayoutId).setVisibility(GONE);
                        findViewById(R.id.sequenceEditorBarChoiceLayoutId).setVisibility(VISIBLE);
                        barChoiceSpinner.setBars(bars);
                        break;
                    case R.id.sequenceEditorCreateNewBarButtonId:
                        barInCreation = new Bar("New Bar");
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
        });

        addBarToSequenceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequence == null)
                    return;

                int iteration = Integer.valueOf(iterationNumberSpinner.getSelectedItem().toString()).intValue();
                Bar bar = (Bar) barChoiceSpinner.getSelectedItem();

                for (int i = 0; i < iteration; i++) {
                    sequence.addBar(bar.clone());
                }
                sequence.notifyObservers();
            }
        });


        addBarToSequenceFromBarEditorButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sequence == null) {
                    return;
                }
                Bar bar = barEditor.getBar().clone();
                int iteration = Integer.valueOf(iterationNumberSpinnerForBarEditor.getSelectedItem().toString()).intValue();
                for (int i = 0; i < iteration; i++) {
                    sequence.addBar(bar);

                }
                sequence.notifyObservers();
            }
        });

        addBarToSequenceAndSaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sequence == null) {
                    return;
                }
                Bar bar = barEditor.getBar();
                int iteration = Integer.valueOf(iterationNumberSpinnerForBarEditor.getSelectedItem().toString()).intValue();
                for (int i = 0; i < iteration; i++) {
                    sequence.addBar(bar);

                }
                sequence.notifyObservers();
                bars.addBar(bar);
                bars.notifyObservers();
                barTypeButtons.check(R.id.sequenceEditorUseExistingBarsButtonId);



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
