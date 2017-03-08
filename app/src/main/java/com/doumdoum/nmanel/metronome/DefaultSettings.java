package com.doumdoum.nmanel.metronome;

/**
 * Created by nmanel on 2/6/2017.
 */

public interface DefaultSettings {
    int SAMPLERATE = 16000;
    int BEAT_LENGTH_IN_MS = 100;
    int BUFFER_SIZE = 2048;
    int MAX_TEMPO_VALUE = 400;
    int MIN_TEMP_VALUE = 1;

    int PRESS_TEMPO_CHANGE_TIME_IN_MS = 500;
}
