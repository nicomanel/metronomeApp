package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

import com.doumdoum.nmanel.metronome.model.Sequences;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by nmanel on 3/11/2017.
 */


public class SequencesSpinner extends android.support.v7.widget.AppCompatSpinner implements Observer {
    private Sequences sequences;

    public SequencesSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSequences(Sequences sequences) {
        this.sequences = sequences;
        this.sequences.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
