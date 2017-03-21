package com.doumdoum.nmanel.metronome.model;

import android.util.Log;

import com.doumdoum.nmanel.metronome.MetronomeHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class Bar extends Observable implements Cloneable {
    private String name;
    private LinkedList<Beat> beats;
    private Bar nextBar;
    private TimeSignature signature;
    public Bar() {
        this("");
    }
    public Bar(String name) {
        this(name, TimeSignature.QuarterNote);
    }

    public Bar(String name, TimeSignature signature)
    {
        this.name = name;
        this.signature = signature;
        nextBar = null;
        beats = new LinkedList<>();
    }

    public Bar getNextBar() {
        return nextBar;
    }

    public void setNextBar(Bar nextBar) {
        this.nextBar = nextBar;
        setChanged();
    }

    public TimeSignature getSignature() {
        return signature;
    }

    public void setSignature(TimeSignature signature) {
        this.signature = signature;
        setChanged();
    }

    public void addBeat(Beat beat) {
        beats.add(beat);
        setChanged();
    }

    public void addBeat() {
        addBeat(new Beat());
    }

    public void removeLastBeat() {
        beats.removeLast();
        setChanged();
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

    public void setName(String name) {
        this.name = name;
        setChanged();
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
        Bar clonedBar = new Bar(getName());

        for (Beat beat : this.getBeats()) {
            clonedBar.addBeat(new Beat(beat.getBeatStyle()));
        }
        return clonedBar;
    }

    public List<Beat> getBeats() {
        return beats;
    }

    public int getBeatsNumber() {
        return beats.size();
    }

    public enum TimeSignature {
        WholeNote("1", 4),
        HalfNote("2", 2),
        QuarterNote("4", 1),
        EighthNote("8", 1 / 2),
        SixteenNote("16", 1 / 4);

        protected String notation;
        protected float multiplier;

        TimeSignature(String notation, float multiplier) {
            this.notation = notation;
            this.multiplier = multiplier;
        }

        public String getNotation() {
            return notation;
        }

        public float getMultiplier() {
            return multiplier;
        }
    }
}
