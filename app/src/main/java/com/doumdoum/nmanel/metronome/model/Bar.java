package com.doumdoum.nmanel.metronome.model;

import com.doumdoum.nmanel.metronome.SoundHelper;

import java.util.LinkedList;

public class Bar {
    LinkedList<Beat> beats;


    public Bar()
    {
        beats = new LinkedList<Beat>();
    }

    public void addBeat(Beat beat)
    {
        beats.add(beat);
    }

    public short[] generateSamples(int tempo, int sampleRate) {
        short[] result = new short[0];

        for (Beat beat : beats) {
            result = SoundHelper.concatShortArrays((beat.generateSamples(tempo, sampleRate)), result);
        }
        return result;
    }

    public int getBarSamplesNumber(int tempo, int sampleRate)
    {
        int size = 0;
        for(Beat beat : beats)
            size += beat.getSamplesCount(tempo, sampleRate);
        return size;
    }
}
