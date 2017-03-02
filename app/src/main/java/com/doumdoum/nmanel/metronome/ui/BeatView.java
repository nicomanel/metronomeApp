package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
    private HashMap<Beat.Style, Drawable> styles = new HashMap<>();
    private LinkedList<Beat.Style> styleOrders;
    private Beat.Style style;




    public BeatView(Context context, Beat.Style style) {
        super(context);

        styleOrders = new LinkedList<>();
        styleOrders.add(Beat.Style.Normal);
        styleOrders.add(Beat.Style.Accent1);
        styleOrders.add(Beat.Style.Ghost);
        styleOrders.add(Beat.Style.Silent);
        styles.put(Beat.Style.Normal, context.getDrawable(R.drawable.quarter_note));
        styles.put(Beat.Style.Accent1, context.getDrawable(R.drawable.accented_quarter_note));
        styles.put(Beat.Style.Ghost, context.getDrawable(R.drawable.ghost_note));
        styles.put(Beat.Style.Silent, context.getDrawable(R.drawable.silent_note));

        setStyle(style);
    }

    public void setStyle(Beat.Style style)
    {
        this.style = style;
        setImageDrawable(styles.get(style));
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
        return style;
    }
}
