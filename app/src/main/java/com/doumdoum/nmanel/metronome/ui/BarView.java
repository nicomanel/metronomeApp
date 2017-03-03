package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
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
    private Bar bar;
    private List<BeatView> beatViews;
    public BarView(Context context, Bar bar) {
        super(context);
        beatViews = new ArrayList<>();
        setBar(bar);

    }

    public BarView(Context context, AttributeSet set) {
        super(context, set);
        beatViews = new ArrayList<>();
        setBar(new Bar("empty"));
    }

    public void setBar(Bar bar)
    {
        clearBeatViews();

        List<Beat> beats = bar.getBeats();
        setWeightSum(beats.size());
        this.bar = bar;
        for (Beat beat : beats) {
            BeatView view = new BeatView(this.getContext(), beat.getBeatStyle());
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
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
}
