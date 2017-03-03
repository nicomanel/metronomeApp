package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Beat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nico on 03/03/17.
 */

public class BarView extends LinearLayout {
    private final static String LOG = BarView.class.toString();
    private Bar bar;
    private List<BeatView> beatViews;
    private boolean editable;


    public BarView(Context context, Bar bar) {
        super(context);
        beatViews = new ArrayList<>();
        setEditable(false);
        setBar(bar);

    }

    public BarView(Context context, AttributeSet set) {
        super(context, set);
        beatViews = new ArrayList<>();
        setEditable(false);
        setBar(new Bar("empty"));
    }

    public void setBar(Bar bar) {
        clearBeatViews();

        this.bar = bar;
        List<Beat> beats = bar.getBeats();
        setWeightSum(beats.size());

        for (Beat beat : beats) {
            BeatView view = new BeatView(this.getContext(), beat);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            view.setEditable(editable);
            beatViews.add(view);
            addView(view);
        }
    }

    private void clearBeatViews() {
        for (BeatView beatView : beatViews) {
            removeView(beatView);
        }
        beatViews.clear();
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        for (BeatView view : beatViews) {
            view.setEditable(editable);
        }
    }

}
