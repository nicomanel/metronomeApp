package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by nico on 08/03/17.
 */

public class SequenceView extends LinearLayout implements Observer {
    private Sequence sequence;
    private List<BarView> barViews;

    public SequenceView(Context context, AttributeSet set) {
        super(context, set);
        sequence = null;
        barViews = new ArrayList<>();
    }

    public void setSequence(Sequence newSequence) {
        if (sequence != null) {
            sequence.deleteObserver(this);
        }
        sequence = newSequence;
        sequence.addObserver(this);
        updateViews();
    }

    private void updateViews() {
        for (BarView view : barViews) {
            removeView(view);
        }

        Bar barToDisplay = sequence.getBars();
        while (barToDisplay != null) {
            BarView newView = new BarView(getContext(), barToDisplay);
            addView(newView);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        updateViews();
    }
}
