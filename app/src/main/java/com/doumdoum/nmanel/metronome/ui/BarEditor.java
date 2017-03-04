package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.doumdoum.nmanel.metronome.R;
import com.doumdoum.nmanel.metronome.model.Bar;

/**
 * Created by nmanel on 3/3/2017.
 */

public class BarEditor extends LinearLayout {
    private Bar barToEdit;

    public BarEditor(Context context, AttributeSet set) {
        super(context, set);
        addView(LayoutInflater.from(context).inflate(R.layout.bar_editor, this));
    }


}
