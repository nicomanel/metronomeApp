package com.doumdoum.nmanel.metronome;

import android.widget.EditText;

import static com.doumdoum.nmanel.metronome.DefaultSettings.MAX_TEMPO_VALUE;
import static com.doumdoum.nmanel.metronome.DefaultSettings.MIN_TEMP_VALUE;

/**
 * Created by nmanel on 1/16/2017.
 */

public class MetronomeHelper {
    public static short[] concatShortArrays(final short[] firstArray, final short[] secondArray)
    {
        short[] resultSound = new short[firstArray.length + secondArray.length];
        for(int sampleIndex = 0; sampleIndex < firstArray.length; sampleIndex++)
        {
            resultSound[sampleIndex] = firstArray[sampleIndex];
        }
        for(int sampleIndex = 0; sampleIndex < secondArray.length; sampleIndex++)
        {
            resultSound[firstArray.length + sampleIndex] = secondArray[sampleIndex];
        }
        return resultSound;
    }

    public static short[] generatePureSound(int samplerate, int lengthInMs, int frequency)
    {
        int sizeInSamples = getMinimalBeatLength(samplerate, lengthInMs);
        short[] resultSound = new short[sizeInSamples];

        for(int sampleIndex = 0; sampleIndex < sizeInSamples; sampleIndex++)
        {
            short sample = (short) (Math.sin(Math.PI * 2 * frequency * sampleIndex / samplerate) * (Short.MAX_VALUE / 2));
            resultSound[sampleIndex] = sample;
        }
        return resultSound;
    }

    public static int getMinimalBeatLength(int samplerate, int lengthInMs)
    {
        return (int)(lengthInMs * samplerate / 1000);
    }

    public static void changeTempo(EditText tempoEditText, int newTempo)
    {
        if (newTempo > MAX_TEMPO_VALUE)
        {
            tempoEditText.setText("" + MAX_TEMPO_VALUE);
            return;
        }
        if(newTempo < MIN_TEMP_VALUE)
        {
            tempoEditText.setText("" + MIN_TEMP_VALUE);
            return;
        }
        tempoEditText.setText("" + newTempo);
    }
}

