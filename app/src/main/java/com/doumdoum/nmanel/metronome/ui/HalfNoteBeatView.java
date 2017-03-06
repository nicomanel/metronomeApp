package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;

import com.doumdoum.nmanel.metronome.R;
import com.doumdoum.nmanel.metronome.model.Beat;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by nmanel on 3/6/2017.
 */

public class HalfNoteBeatView extends AbstractBeatView {
    public HalfNoteBeatView(Context context, Beat beat) {
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
        styles.put(Beat.Style.Normal, context.getDrawable(R.drawable.half_note));
        styles.put(Beat.Style.Accent1, context.getDrawable(R.drawable.accented_half_note));
        styles.put(Beat.Style.Ghost, context.getDrawable(R.drawable.ghost_half_note));
        styles.put(Beat.Style.Silent, context.getDrawable(R.drawable.silent_half_note));
    }
}
