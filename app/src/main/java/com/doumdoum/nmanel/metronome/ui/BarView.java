package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Beat;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by nico on 03/03/17.
 */

public class BarView extends LinearLayout implements Observer {
    private final static String LOG = BarView.class.toString();
    private Bar bar;
    private List<AbstractBeatView> abstractBeatViews;
    private boolean editable;


    public BarView(Context context, Bar bar) {
        super(context);
        abstractBeatViews = new ArrayList<>();
        setEditable(false);
        setBar(bar);

    }

    public BarView(Context context, AttributeSet set) {
        super(context, set);
        abstractBeatViews = new ArrayList<>();
        setEditable(false);
        setBar(new Bar("empty"));
    }

    public void setBar(Bar bar) {
        clearBeatViews();

        this.bar = bar;
        List<Beat> beats = bar.getBeats();
        setWeightSum(beats.size());

        for (Beat beat : beats) {
            AbstractBeatView view = createBeatView(beat);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            view.setEditable(editable);
            abstractBeatViews.add(view);
            addView(view);
        }
    }

    private AbstractBeatView createBeatView(Beat beat) {
        AbstractBeatView view = null;

        switch(bar.getSignature())
        {
            case WholeNote:
                view = new WholeNoteBeatView(this.getContext(), beat);
                break;
            case HalfNote:
                view = new HalfNoteBeatView(this.getContext(), beat);
                break;
            case QuarterNote:
                view = new QuarterNoteBeatView(this.getContext(), beat);
                break;
            case EighthNote:
                view = new EighthNoteBeatView(this.getContext(), beat);
                break;
            case SixteenNote:
                view = new SixteenthNoteBeatView(this.getContext(), beat);
                break;
        }
        return view;
    }

    private void clearBeatViews() {
        for (AbstractBeatView abstractBeatView : abstractBeatViews) {
            removeView(abstractBeatView);
        }
        abstractBeatViews.clear();
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        for (AbstractBeatView view : abstractBeatViews) {
            view.setEditable(editable);
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        Log.i(LOG, "update BarView");
        setBar(this.bar);
    }
}
