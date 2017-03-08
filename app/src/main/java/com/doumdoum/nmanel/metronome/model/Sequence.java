package com.doumdoum.nmanel.metronome.model;

import java.util.Observable;

/**
 * Created by nmanel on 3/9/2017.
 */

public class Sequence extends Observable {
    private Bar bars;
    private String name;
    private int tempo;

    public Sequence(String name) {
        bars = null;
        this.name = name;
        setChanged();
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
        setChanged();
    }

    public void addBar(Bar bar) {

        if (bars == null) {
            bars = bar;
        }

        Bar lastBar = bars;
        while (lastBar.getNextBar() != null) {
            lastBar = lastBar.getNextBar();
        }
        lastBar.setNextBar(bar);
        setChanged();
    }

    public void replaceBar(Bar barToReplace, bar replacedBy) {

    }

}
