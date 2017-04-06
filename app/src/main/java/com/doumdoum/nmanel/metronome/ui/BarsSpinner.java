package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.doumdoum.nmanel.metronome.R;
import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Bars;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by nmanel on 3/3/2017.
 */

public class BarsSpinner extends Spinner implements Observer {
    private final static String LOG = BarsSpinner.class.toString();
    private Bars bars;

    public BarsSpinner(Context context, Bars bars) {
        super(context);
        setBars(bars);
    }

    public BarsSpinner(Context context, AttributeSet set) {
        super(context, set);
    }

    public void setBars(final Bars bars) {
        this.bars = bars;

        final ArrayAdapter<Bar> adapter = new ArrayAdapter<Bar>(getContext(), R.layout.spinner_item, bars.getBars()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View rowView = LayoutInflater.from(getContext()).inflate(R.layout.bar_list_view, parent, false);
                BarView barView = (BarView) rowView.findViewById(R.id.barListViewId);
                barView.setBar(getItem(position));
                return rowView;
            }

            @Override
            public View getDropDownView(final int position, View convertView, ViewGroup parent) {
                BarView barView = new BarView(getContext(), getItem(position));
                barView.setClickable(true);
                barView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 128));
                return barView;
            }
        };


        setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void update(Observable o, Object arg) {
        setBars(bars);
    }
}
