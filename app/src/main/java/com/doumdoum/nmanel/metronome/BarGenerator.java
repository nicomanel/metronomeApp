package com.doumdoum.nmanel.metronome;


import android.util.Log;

import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Beat;

import java.util.Observable;
import java.util.concurrent.LinkedTransferQueue;

import static com.doumdoum.nmanel.metronome.DefaultSettings.BEAT_LENGTH_IN_MS;
import static com.doumdoum.nmanel.metronome.DefaultSettings.SAMPLERATE;

/**
 * Created by nico on 24/10/2016.
 */

public class BarGenerator extends Observable {
    private boolean firstBar;
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
        firstBar = true;
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

            Short sample = samplesQueue.poll();
            if (sample == null) {
                break;
            }
            samplesToWrite[sampleIndex] = sample;
        }
        return samplesToWrite;
    }

    private void forgeNextSamples() {


        while (samplesQueue.size() < bufferSize * 3) {
            int tempoOfTheNextBar = determineTempoAndUpdateCounters();
            short[] newSamples;
            if (firstBar) {
                Bar firstBarToPlay = new Bar();
                firstBarToPlay.addBeat(new Beat(Beat.Style.Accent2));
                firstBarToPlay.addBeat(new Beat(Beat.Style.Accent2));
                firstBarToPlay.addBeat(new Beat(Beat.Style.Accent2));
                firstBarToPlay.addBeat(new Beat(Beat.Style.Accent2));
                newSamples = firstBarToPlay.generateSamples(tempoOfTheNextBar, sampleRate);
                firstBar = false;
            } else {
                newSamples = bar.generateSamples(tempoOfTheNextBar, sampleRate);
            }
            fillQueue(newSamples);
            Log.d("BarGenerator", "forging : " + samplesQueue.size());
        }
    }

    private int determineTempoAndUpdateCounters() {
        if (!increaseTempo)
            return tempo;

        if ((measureNumberBeforeIncrement == measureCounterBeforeIncrement)) {
            measureCounterBeforeIncrement = 0;
            if (isIncrementPossible()) {
                incrementedTempo += tempoIncrement;
            }
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
        int minmalBeatLength = MetronomeHelper.getMinimalBeatLength(SAMPLERATE, BEAT_LENGTH_IN_MS);
        int beatLength = bar.getBeatSamplesNumbers(incrementedTempo + tempoIncrement, SAMPLERATE);
        return (beatLength > minmalBeatLength);
    }
}

