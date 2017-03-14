package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    private static final String LOG = SequenceView.class.toString();
    private Sequence sequence;
    private List<BarView> barViews;

    public SequenceView(Context context, AttributeSet set) {
        super(context, set);
        setOrientation(LinearLayout.VERTICAL);
        barViews = new ArrayList<>();
        sequence = null;

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
        barViews.clear();

        Bar barToDisplay = sequence.getBars();
        while (barToDisplay != null) {
            BarView newView = new BarView(getContext(), barToDisplay);
            newView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64));
            addView(newView);
            barToDisplay = barToDisplay.getNextBar();
            Log.i(LOG, "" + barToDisplay);
        }
    }

    @Override
    public void addView(View view) {
        super.addView(view);
        barViews.add((BarView) view);
    }


    @Override
    public void update(Observable o, Object arg) {
        updateViews();
    }
}
