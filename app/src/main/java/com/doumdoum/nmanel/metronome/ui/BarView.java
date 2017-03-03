package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.widget.LinearLayout;

import com.doumdoum.nmanel.metronome.model.Bar;

/**
 * Created by nico on 03/03/17.
 */

public class BarView extends LinearLayout {
    private Bar bar;
    public BarView(Context context, Bar bar) {
        super(context);
        this.bar = bar;
//        setLayoutParams();
    }

    public void setBar(Bar bar)
    {

    }
}
