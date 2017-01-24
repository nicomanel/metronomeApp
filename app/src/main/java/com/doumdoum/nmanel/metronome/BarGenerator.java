package com.doumdoum.nmanel.metronome;


import android.util.Log;

import com.doumdoum.nmanel.metronome.model.Bar;

import java.util.concurrent.LinkedTransferQueue;

/**
 * Created by nico on 24/10/2016.
 */

public class BarGenerator {
    private LinkedTransferQueue<Short> samplesQueue;
    private boolean increaseTempo;
    private int tempoIncrement;
    private int measureNumberBeforeIncrement;
    private int tempo;
    private int sampleRate;
    private int bufferSize;
    private Bar bar;
    private short[] samplesToWrite;

    public BarGenerator(int tempo, int sampleRate, boolean increaseTempo, int tempoIncrement, int measureNumberBeforeIncrement, int bufferSize)
    {
        samplesQueue = new LinkedTransferQueue();
        this.tempo = tempo;
        this.sampleRate = sampleRate;
        this.increaseTempo = increaseTempo;
        this.tempoIncrement = tempoIncrement;
        this.bufferSize = bufferSize;
        this.measureNumberBeforeIncrement = measureNumberBeforeIncrement;
        samplesToWrite = new short[bufferSize];

    }

    public BarGenerator(int tempo, int sampleRate, int bufferSize)
    {
        this(tempo, sampleRate, false, 0, 0, bufferSize);
    }

    public short[] getSamples()
    {/*
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.i(this.getClass().toString(), "New thread to forge next samples " + System.currentTimeMillis());
                    forgeNextSamples();
                    Log.i(this.getClass().toString(), "End Thread " + System.currentTimeMillis());
                }

            }).start();

*/
        Log.i("toto", "getSamples return");
        return samplesToWrite;
    }

    private void forgeNextSamples()
    {
        Log.i(this.getClass().toString(), "forgeNextSamples BEGIN : " + samplesQueue.size());
        while(samplesQueue.size() < bufferSize)
        {
            fillQueue(bar.generateSamples(tempo, sampleRate));
        }
        for(int sampleIndex = 0; sampleIndex < bufferSize; sampleIndex++)
        {
            samplesToWrite[sampleIndex] = samplesQueue.poll();
        }
        Log.i(this.getClass().toString(), "forgeNextSamples END : " + samplesQueue.size());
    }

    private void fillQueue(final short[] samples) {
        for (int sampleIndex = 0; sampleIndex < samples.length; sampleIndex++)
        {
            samplesQueue.offer(samples[sampleIndex]);
       }
    }

    public void setBar(Bar barToGenerate)
    {
        this.bar = barToGenerate;
        initQueue();
    }

    private void initQueue() {
        samplesQueue.clear();
        forgeNextSamples();
    }

}
