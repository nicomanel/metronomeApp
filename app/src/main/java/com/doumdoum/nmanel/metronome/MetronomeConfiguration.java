package com.doumdoum.nmanel.metronome;

import java.io.Serializable;

/**
 * Created by nico on 23/02/17.
 */

public class MetronomeConfiguration implements Serializable{
    private int tempo;
    private boolean skipMeasure;

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public boolean isSkipMeasure() {
        return skipMeasure;
    }

    public void setSkipMeasure(boolean skipMeasure) {
        this.skipMeasure = skipMeasure;
    }

    public boolean isIncreaseTempo() {
        return increaseTempo;
    }

    public void setIncreaseTempo(boolean increaseTempo) {
        this.increaseTempo = increaseTempo;
    }

    public boolean isEnabledTimer() {
        return enabledTimer;
    }

    public void setEnabledTimer(boolean enabledTimer) {
        this.enabledTimer = enabledTimer;
    }

    public int getTempoIncrement() {
        return tempoIncrement;
    }

    public void setTempoIncrement(int tempoIncrement) {
        this.tempoIncrement = tempoIncrement;
    }

    public int getMeasurementNumberBeforeIncrement() {
        return measurementNumberBeforeIncrement;
    }

    public void setMeasurementNumberBeforeIncrement(int measurementNumberBeforeIncrement) {
        this.measurementNumberBeforeIncrement = measurementNumberBeforeIncrement;
    }

    public int getTimerInSeconds() {
        return timerInSeconds;
    }

    public void setTimerInSeconds(int timerInSeconds) {
        this.timerInSeconds = timerInSeconds;
    }

    private boolean increaseTempo;
    private boolean enabledTimer;
    private int tempoIncrement;
    private int measurementNumberBeforeIncrement;
    private int timerInSeconds;

    public MetronomeConfiguration(int tempo, boolean skipMeasure, boolean increaseTempo, boolean enabledTimer, int tempoIncrement, int measurementNumberBeforeIncrement, int timerInSeconds) {
        this.tempo = tempo;
        this.skipMeasure = skipMeasure;
        this.increaseTempo = increaseTempo;
        this.enabledTimer = enabledTimer;
        this.tempoIncrement = tempoIncrement;
        this.measurementNumberBeforeIncrement = measurementNumberBeforeIncrement;
        this.timerInSeconds = timerInSeconds;
    }
}
