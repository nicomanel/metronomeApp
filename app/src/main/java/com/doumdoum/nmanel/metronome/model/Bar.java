package com.doumdoum.nmanel.metronome.model;

import android.os.Parcelable;
import android.util.Log;

import com.doumdoum.nmanel.metronome.MetronomeHelper;

import java.util.LinkedList;

public class Bar implements Cloneable {
    private String name;
    private LinkedList<Beat> beats;
    private Bar nextBar;

    public Bar(String name) {
        this.name = name;
        beats = new LinkedList<Beat>();
        nextBar = null;
    }

    public void addBeat(Beat beat) {
        beats.add(beat);
    }

    public void setNextBar(Bar nextBar) {
        this.nextBar = nextBar;
    }

    public short[] generateSamples(int tempo, int sampleRate) {
        short[] result = new short[0];

        for (Beat beat : beats) {
            Log.i(getClass().toString(), "generateSamples : " + beat.getBeatStyle());
            result = MetronomeHelper.concatShortArrays(result, (beat.generateSamples(tempo, sampleRate)));
        }
        if (nextBar != null) {
            result = MetronomeHelper.concatShortArrays(result, (nextBar.generateSamples(tempo, sampleRate)));
        }
        return result;
    }

    public int getBarSamplesNumber(int tempo, int sampleRate) {
        int size = 0;
        for (Beat beat : beats)
            size += beat.getSamplesCount(tempo, sampleRate);
        return size;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }

    public void forgeSilentNextBar() {
        Bar nextSilentBar = new Bar("Silent Clone");
        for (Beat beat : beats) {
            nextSilentBar.addBeat(new Beat(Beat.Style.Silent));
        }
        setNextBar(nextSilentBar);
    }

    public int getBeatSamplesNumbers(int tempo, int sampleRate) {
        return beats.getFirst().getSamplesCount(tempo, sampleRate);
    }

    public Bar clone() {
        Bar clonedBar = null;
        try {
            clonedBar = (Bar) super.clone();
        } catch (CloneNotSupportedException exception) {
            Log.e("Bar", "Bar not cloneable");
        }

        return clonedBar;
    }
}
