package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;

import com.doumdoum.nmanel.metronome.R;
import com.doumdoum.nmanel.metronome.model.Beat;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by nico on 05/03/17.
 */

public class QuarterNoteBeatView extends AbstractBeatView {
    public QuarterNoteBeatView(Context context, Beat beat) {
        super(context, beat);
    }

    @Override
    protected void initStyleBindings(Context context) {
        styleOrders = new LinkedList<>();
        styleOrders.add(Beat.Style.Normal);
        styleOrders.add(Beat.Style.Accent1);
        styleOrders.add(Beat.Style.Ghost);
        styleOrders.add(Beat.Style.Silent);
        styles = new HashMap<>();
        styles.put(Beat.Style.Normal, context.getDrawable(R.drawable.quarter_note));
        styles.put(Beat.Style.Accent1, context.getDrawable(R.drawable.accented_quarter_note));
        styles.put(Beat.Style.Ghost, context.getDrawable(R.drawable.ghost_quarter_note));
        styles.put(Beat.Style.Silent, context.getDrawable(R.drawable.silent_quarter_note));
    }
}
