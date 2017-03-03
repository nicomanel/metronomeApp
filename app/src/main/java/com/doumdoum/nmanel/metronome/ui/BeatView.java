package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.doumdoum.nmanel.metronome.R;
import com.doumdoum.nmanel.metronome.model.Beat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by nico on 02/03/17.
 */

public class BeatView extends ImageView {
    private final static String LOG = BeatView.class.toString();
    private HashMap<Beat.Style, Drawable> styles = new HashMap<>();
    private LinkedList<Beat.Style> styleOrders;
    private Beat beat;
    private boolean editable;


    public BeatView(Context context, Beat beat) {
        super(context);

        this.beat = beat;
        styleOrders = new LinkedList<>();
        styleOrders.add(Beat.Style.Normal);
        styleOrders.add(Beat.Style.Accent1);
        styleOrders.add(Beat.Style.Ghost);
        styleOrders.add(Beat.Style.Silent);
        styles.put(Beat.Style.Normal, context.getDrawable(R.drawable.quarter_note));
        styles.put(Beat.Style.Accent1, context.getDrawable(R.drawable.accented_quarter_note));
        styles.put(Beat.Style.Ghost, context.getDrawable(R.drawable.ghost_note));
        styles.put(Beat.Style.Silent, context.getDrawable(R.drawable.silent_note));

        setStyle(beat.getBeatStyle());
    }

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
                    ((BeatView) v).nextStyle();
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
