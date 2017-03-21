package com.doumdoum.nmanel.metronome.model;

import android.util.Log;

import java.util.Observable;

/**
 * Created by nmanel on 3/9/2017.
 */

public class Sequence extends Observable {
    private final static String LOG = Sequence.class.toString();
    private Bar bars;
    private String name;
    private int tempo;
    public Sequence(String name) {
        bars = null;
        this.name = name;
        setChanged();
    }

    public String getName() {
        return name;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
        setChanged();
    }

    public void addBar(Bar bar) {
        Bar clonedBar = bar.clone();
        if (bars == null) {
            bars = clonedBar;
            return;
        }

        Bar lastBar = bars;
        while (lastBar.getNextBar() != null) {
            lastBar = lastBar.getNextBar();
        }
        lastBar.setNextBar(clonedBar);
        setChanged();
    }

    public void replaceBar(Bar barToReplace, Bar replacedBy) {
        Bar containingBar = findContainingBar(bars, barToReplace);
        if (containingBar == null) {
            Log.i(LOG, "replaceBar : Bar not found");
            return;
        }

        containingBar.setNextBar(replacedBy);
        replacedBy.setNextBar(barToReplace.getNextBar());
        setChanged();
    }

    private Bar findContainingBar(Bar bars, Bar barToReplace) {
        Bar containingBar = bars.getNextBar();
        while (containingBar.getNextBar() == barToReplace) {
            containingBar = containingBar.getNextBar();
        }
        return containingBar;
    }

    public Bar getBars() {
        return bars;
    }

    @Override
    public String toString() {
        return getName();
    }

}
