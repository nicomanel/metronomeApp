package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.doumdoum.nmanel.metronome.R;
import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Beat;

/**
 * Created by nmanel on 3/3/2017.
 */

public class BarEditor extends LinearLayout {
    private final static String LOG = "BarEditor";
    private Bar barToEdit;
    private BarView barView;
    private Spinner beatsNumberSpinner;
    private EditText barNameEditText;
    private Spinner timeSignatureSpinner;
    private boolean simpleView;


    public BarEditor(Context context, AttributeSet set) {
        super(context, set);
        addView(LayoutInflater.from(context).inflate(R.layout.bar_editor, null));
        barView = (BarView) findViewById(R.id.barViewId);
        barView.setEditable(true);
        setSimpleView(false);
        barNameEditText = (EditText) findViewById(R.id.barNameId);
        barNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                barToEdit.setName(s.toString());
            }
        });


        timeSignatureSpinner = (Spinner) findViewById(R.id.timeSignatureSpinnerId);
        timeSignatureSpinner.setSelection(2);
        timeSignatureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Bar.TimeSignature signature = findTimeSignatureFromString(parent.getSelectedItem());
                barToEdit.setSignature(signature);
                barToEdit.notifyObservers();
            }

            private Bar.TimeSignature findTimeSignatureFromString(Object selectedItem) {
                String selectedSignature = (String) selectedItem;
                Bar.TimeSignature signature = Bar.TimeSignature.QuarterNote;
                if (selectedSignature.equals("1"))
                    signature = Bar.TimeSignature.WholeNote;
                if (selectedSignature.equals("2"))
                    signature = Bar.TimeSignature.HalfNote;
                if (selectedSignature.equals("4"))
                    signature = Bar.TimeSignature.QuarterNote;
                if (selectedSignature.equals("8"))
                    signature = Bar.TimeSignature.EighthNote;
                if (selectedSignature.equals("16"))
                    signature = Bar.TimeSignature.SixteenNote;
                return signature;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        beatsNumberSpinner = (Spinner) findViewById(R.id.beatNumberValueId);
        beatsNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (barToEdit == null)
                    return;

                int beatsNumber = Integer.valueOf((String) parent.getSelectedItem()).intValue();

                if (beatsNumber < barToEdit.getBeatsNumber()) {
                    while (beatsNumber < barToEdit.getBeatsNumber()) {
                        barToEdit.removeLastBeat();
                    }
                }
                if (beatsNumber > barToEdit.getBeatsNumber()) {
                    while (beatsNumber > barToEdit.getBeatsNumber()) {
                        barToEdit.addBeat(new Beat(Beat.Style.Normal));
                    }
                }
                barToEdit.notifyObservers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setBar(Bar bar) {
        if (barToEdit != null)
            barToEdit.deleteObservers();
        barToEdit = bar;
        barNameEditText.setText(bar.getName());
        beatsNumberSpinner.setSelection(bar.getBeatsNumber() - 1);
        timeSignatureSpinner.setSelection(findTimeSignatureIndexFromString(barToEdit.getSignature()));
        barToEdit.addObserver(barView);
        barView.setBar(barToEdit);

    }

    private int findTimeSignatureIndexFromString(Bar.TimeSignature signature) {
        int index = 0;
        switch (signature)
        {
            case WholeNote:
                index = 0;
                break;
            case HalfNote:
                index = 1;
                break;
            case QuarterNote:
                index = 2;
                break;
            case EighthNote:
                index = 3;
                break;
            case SixteenNote:
                index = 4;
                break;
        }
        return index;
    }

    public void setSimpleView(boolean simpleView) {
        if (simpleView) {
            findViewById(R.id.barNameLayoutId).setVisibility(GONE);
            findViewById(R.id.timeSignatureLayoutId).setVisibility(GONE);
        } else {
            findViewById(R.id.barNameLayoutId).setVisibility(VISIBLE);
            findViewById(R.id.timeSignatureLayoutId).setVisibility(VISIBLE);
        }
    }
}
