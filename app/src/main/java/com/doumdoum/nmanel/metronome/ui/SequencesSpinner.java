package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.doumdoum.nmanel.metronome.R;
import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Bars;

/**
 * Created by nmanel on 3/3/2017.
 */

public class SequencesSpinner extends Spinner {
    private Bars bars;

    public SequencesSpinner(Context context, Bars bars) {
        super(context);
        setBars(bars);
    }

    public SequencesSpinner(Context context, AttributeSet set) {
        super(context, set);
    }

    public void setBars(Bars bars) {
        this.bars = bars;
        ArrayAdapter<Bar> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, bars.getBars());
        setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
