package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.doumdoum.nmanel.metronome.model.Beat;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by nico on 02/03/17.
 */

public abstract class AbstractBeatView extends android.support.v7.widget.AppCompatImageView {
    private final static String LOG = AbstractBeatView.class.toString();
    protected HashMap<Beat.Style, Drawable> styles;
    protected LinkedList<Beat.Style> styleOrders;
    protected Beat beat;
    protected boolean editable;


    public AbstractBeatView(Context context, Beat beat) {
        super(context);

        this.beat = beat;
        initStyleBindings(context);
        setStyle(beat.getBeatStyle());
    }

    protected abstract void initStyleBindings(Context context);

    public void nextStyle()
    {
        int index = styleOrders.indexOf(getStyle());
        if (styleOrders.get(index) == styleOrders.getLast()) {
            index = 0;
        } else {
            index++;
        }

        setStyle(styleOrders.get(index));
    }

    public Beat.Style getStyle()
    {
        return beat.getBeatStyle();
    }

    public void setStyle(Beat.Style style) {
        beat.setBeatStyle(style);
        setImageDrawable(styles.get(style));
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        if (editable) {
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AbstractBeatView) v).nextStyle();
                }
            });
        } else {
            setOnClickListener(null);
        }
    }

    public Beat getBeat() {
        return beat;
    }
}
