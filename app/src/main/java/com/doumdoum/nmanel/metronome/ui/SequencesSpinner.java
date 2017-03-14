package com.doumdoum.nmanel.metronome.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.doumdoum.nmanel.metronome.R;
import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Sequence;
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
        ArrayAdapter<Sequence> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, sequences.getSequences());
        setAdapter(adapter);
        this.sequences.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        setSequences(sequences);
    }
}
