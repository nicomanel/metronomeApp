package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
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

    public BarEditor(Context context, AttributeSet set) {
        super(context, set);
        addView(LayoutInflater.from(context).inflate(R.layout.bar_editor, null));
        barView = (BarView) findViewById(R.id.editorBarViewId);
        barView.setEditable(true);

        barNameEditText = (EditText) findViewById(R.id.barNameId);


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
                if(beatsNumber > barToEdit.getBeatsNumber())
                {
                    while(beatsNumber > barToEdit.getBeatsNumber())
                    {
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
        barToEdit.addObserver(barView);
        barView.setBar(barToEdit);

    }


}
