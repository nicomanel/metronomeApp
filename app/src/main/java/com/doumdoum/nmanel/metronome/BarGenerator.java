package com.doumdoum.nmanel.metronome;

import java.util.Arrays;

/**
 * Created by nico on 24/10/2016.
 */

public class BarGenerator {
    private static final int FOUR_BEATS_BAR = 4;


    private final int sampleRate;
    private final int samplesNumberByBeat;

    public BarGenerator(int tempo, int sampleRate)
    {
        this.sampleRate = sampleRate;
        this.samplesNumberByBeat = (int)(60. / tempo * this.sampleRate);
    }


    public short[] generateFourBeatsBar()
    {
        short[] barSamples = new short[FOUR_BEATS_BAR * this.samplesNumberByBeat];
        Arrays.fill(barSamples, (short)0);

        for(int beatCounter = 0; beatCounter < FOUR_BEATS_BAR; beatCounter++)
        {
            int tickLength = 100;
            insertTick(barSamples, beatCounter * samplesNumberByBeat, tickLength);
        }
        return barSamples;
    }

    private void insertTick(short[] barSamples, int position, int tickLength) {
        for(int sampleCounter = 0; sampleCounter < tickLength; sampleCounter++)
        {
            if(sampleCounter % 2 == 0)
            {
                barSamples[position + sampleCounter] = Short.MAX_VALUE;
            }
        }
    }
}
