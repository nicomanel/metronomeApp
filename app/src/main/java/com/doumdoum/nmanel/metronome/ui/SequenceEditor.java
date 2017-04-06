package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
    private final CompleteBarEditor completeBarEditor;
    private final Button addBarToSequenceFromBarEditorButton;
    private final Button addBarToSequenceAndSaveButton;
    private final Spinner iterationNumberForBarEditorSpinner;
    private final RadioGroup barTypeButtons;
    private Button addBarToSequenceButton;
    private SequenceView sequenceView;
    private Spinner iterationNumberSpinner;
    private Sequence sequence;
    private EditText sequenceName;
    private BarsSpinner barChoiceSpinner;
    private Bars bars;
    private int currentIteration;
    private RadioButton useExistingBarsButton;
    private RadioButton createNewBarButton;
    private Bar currentBar;
    private SequenceEditorListener listener;


    public SequenceEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(LayoutInflater.from(context).inflate(R.layout.sequence_editor, null));

        addBarToSequenceButton = (Button) findViewById(R.id.sequenceEditorButtonAddBarsToSequenceId);
        addBarToSequenceFromBarEditorButton = (Button) findViewById(R.id.sequenceEditorBarEditorButtonAddBarsToSequenceId);
        addBarToSequenceAndSaveButton = (Button) findViewById(R.id.sequenceEditorBarEditorButtonAddBarsToSequenceAndSaveId);

        bars = (new BarsManager(context)).loadBars();
        barChoiceSpinner = (BarsSpinner) findViewById(R.id.sequenceEditorBarChoiceSpinnerId);
        barChoiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentBar = (Bar) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AdapterView.OnItemSelectedListener iterationNumberOnSelectionListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentIteration = Integer.valueOf(parent.getSelectedItem().toString()).intValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        iterationNumberSpinner = (Spinner) findViewById(R.id.sequenceEditorIterationNumberSpinnerId);
        iterationNumberSpinner.setOnItemSelectedListener(iterationNumberOnSelectionListener);
        iterationNumberForBarEditorSpinner = (Spinner) findViewById(R.id.sequenceEditorBarEditorIterationNumberSpinnerId);
        iterationNumberForBarEditorSpinner.setOnItemSelectedListener(iterationNumberOnSelectionListener);
        barChoiceSpinner.setBars(bars);

        sequenceName = (EditText) findViewById(R.id.sequenceEditorNameValueId);
        sequenceView = (SequenceView) findViewById(R.id.sequenceEditorSequenceViewId);
        barTypeButtons = (RadioGroup) findViewById(R.id.sequenceEditorBarTypeGroupId);
        useExistingBarsButton = (RadioButton) findViewById(R.id.sequenceEditorUseExistingBarsButtonId);
        createNewBarButton = (RadioButton) findViewById(R.id.sequenceEditorCreateNewBarButtonId);
        completeBarEditor = (CompleteBarEditor) findViewById(R.id.sequenceEditorBarEditorId);

        barTypeButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.sequenceEditorUseExistingBarsButtonId:
//                        barEditor.setVisibility(GONE);
                        findViewById(R.id.sequenceEditorBarEditorLayoutId).setVisibility(GONE);
                        findViewById(R.id.sequenceEditorBarChoiceLayoutId).setVisibility(VISIBLE);
                        barChoiceSpinner.setBars(bars);
                        currentIteration = Integer.valueOf(iterationNumberSpinner.getSelectedItem().toString()).intValue();
                        break;
                    case R.id.sequenceEditorCreateNewBarButtonId:
                        currentBar = new Bar("New Bar");
                        currentBar.addBeat(new Beat(Beat.Style.Accent1));
                        currentBar.addBeat(new Beat(Beat.Style.Normal));
                        currentBar.addBeat(new Beat(Beat.Style.Normal));
                        currentBar.addBeat(new Beat(Beat.Style.Normal));
                        completeBarEditor.setBar(currentBar);
                        findViewById(R.id.sequenceEditorBarEditorLayoutId).setVisibility(VISIBLE);
                        findViewById(R.id.sequenceEditorBarChoiceLayoutId).setVisibility(GONE);
                        currentIteration = Integer.valueOf(iterationNumberForBarEditorSpinner.getSelectedItem().toString()).intValue();
                        break;
            }
            }
        });

        addBarToSequenceButton.setOnClickListener(new AddBarOnClickListener());
        addBarToSequenceFromBarEditorButton.setOnClickListener(new AddBarOnClickListener());
        addBarToSequenceAndSaveButton.setOnClickListener(new AddBarAndSaveOnClickListener());
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

    public Bars getBars() {
        return bars;
    }

    public void setBars(Bars bars) {
        this.bars = bars;
    }

    public void setSequenceEditorListener(SequenceEditorListener listener) {
        this.listener = listener;
    }

    private void callListenerHasChanged() {
        if (listener == null)
            return;
        listener.hasChanged(this);
    }

    protected class AddBarOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (sequence == null) {
                return;
            }

            for (int i = 0; i < currentIteration; i++) {
                sequence.addBar(currentBar);

            }
            sequence.notifyObservers();
            callListenerHasChanged();
        }
    }

    protected class AddBarAndSaveOnClickListener extends AddBarOnClickListener {
        @Override
        public void onClick(View v) {
            super.onClick(v);
            bars.addBar(currentBar);
            bars.notifyObservers();
            barTypeButtons.check(R.id.sequenceEditorUseExistingBarsButtonId);
            callListenerHasChanged();
        }
    }
}
