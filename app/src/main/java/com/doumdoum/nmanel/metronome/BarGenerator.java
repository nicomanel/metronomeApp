package com.doumdoum.nmanel.metronome;


import android.util.Log;

import com.doumdoum.nmanel.metronome.model.Bar;

import java.util.Observable;
import java.util.concurrent.LinkedTransferQueue;

import static com.doumdoum.nmanel.metronome.DefaultSettings.BEAT_LENGTH_IN_MS;
import static com.doumdoum.nmanel.metronome.DefaultSettings.SAMPLERATE;

/**
 * Created by nico on 24/10/2016.
 */

public class BarGenerator extends Observable{
    private LinkedTransferQueue<Short> samplesQueue;
    private boolean increaseTempo;
    private int tempoIncrement;
    private int measureNumberBeforeIncrement;
    private int measureCounterBeforeIncrement;
    private int tempo;
    private int incrementedTempo;
    private int sampleRate;
    private int bufferSize;
    private Bar bar;
    private short[] samplesToWrite;
    public BarGenerator(int tempo, int sampleRate, boolean increaseTempo, int tempoIncrement, int measureNumberBeforeIncrement, int bufferSize) {
        samplesQueue = new LinkedTransferQueue();
        this.tempo = tempo;
        this.incrementedTempo = tempo;
        this.measureCounterBeforeIncrement = 0;
        this.sampleRate = sampleRate;
        this.increaseTempo = increaseTempo;
        this.tempoIncrement = tempoIncrement;
        this.bufferSize = bufferSize;
        this.measureNumberBeforeIncrement = measureNumberBeforeIncrement;
        samplesToWrite = new short[bufferSize];

    }

    public BarGenerator(int tempo, int sampleRate, int bufferSize) {
        this(tempo, sampleRate, false, 0, 0, bufferSize);
    }

    public int getIncrementedTempo() {
        return incrementedTempo;
    }

    public short[] getSamples() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                forgeNextSamples();
            }

        }).start();

        for (int sampleIndex = 0; sampleIndex < bufferSize; sampleIndex++) {
            short sample = samplesQueue.poll();
            samplesToWrite[sampleIndex] = sample;
        }
        return samplesToWrite;
    }

    private void forgeNextSamples() {

        while (samplesQueue.size() < bufferSize * 3) {
            int tempoOfTheNextBar = determineTempoAndUpdateCounters();
            fillQueue(bar.generateSamples(tempoOfTheNextBar, sampleRate));
        }
    }

    private int determineTempoAndUpdateCounters() {

        if (!increaseTempo)
            return tempo;

        if ((measureNumberBeforeIncrement == measureCounterBeforeIncrement)) {
            measureCounterBeforeIncrement = 1;
            if (isIncrementPossible()) {

                incrementedTempo += tempoIncrement;

            }

            Log.i("incremented tempo", "incremented tempo : " + incrementedTempo);
        } else {
            measureCounterBeforeIncrement++;

        }
        setChanged();
        notifyObservers();
        return incrementedTempo;
    }

    private void fillQueue(final short[] samples) {
        for (int sampleIndex = 0; sampleIndex < samples.length; sampleIndex++) {
            samplesQueue.offer(samples[sampleIndex]);
        }
    }

    public void setBar(Bar barToGenerate) {
        this.bar = barToGenerate;
        initQueue();
    }

    private void initQueue() {
        samplesQueue.clear();
        forgeNextSamples();
    }

    private boolean isIncrementPossible() {
        int minmalBeatLength = SoundHelper.getMinimalBeatLength(SAMPLERATE, BEAT_LENGTH_IN_MS);
        int beatLength = bar.getBeatSamplesNumbers(incrementedTempo + tempoIncrement, SAMPLERATE);
        Log.i("isIncrementPossible", "minmalBeatLength: " + minmalBeatLength + ", beatLength:" + beatLength);
        return (beatLength > minmalBeatLength);
    }


}

