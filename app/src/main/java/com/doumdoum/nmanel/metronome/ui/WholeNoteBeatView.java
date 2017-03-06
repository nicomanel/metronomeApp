package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;

import com.doumdoum.nmanel.metronome.R;
import com.doumdoum.nmanel.metronome.model.Beat;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by nico on 05/03/17.
 */

public class WholeNoteBeatView extends AbstractBeatView {
    public WholeNoteBeatView(Context context, Beat beat) {
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
        styles.put(Beat.Style.Normal, context.getDrawable(R.drawable.whole_note));
        styles.put(Beat.Style.Accent1, context.getDrawable(R.drawable.accented_whole_note));
        styles.put(Beat.Style.Ghost, context.getDrawable(R.drawable.ghost_whole_note));
        styles.put(Beat.Style.Silent, context.getDrawable(R.drawable.silent_whole_note));
    }
}
