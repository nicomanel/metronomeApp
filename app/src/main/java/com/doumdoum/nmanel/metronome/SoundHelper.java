package com.doumdoum.nmanel.metronome;

/**
 * Created by nmanel on 1/16/2017.
 */

public class SoundHelper {
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
        int sizeInSamples = lengthInMs * samplerate / 1000;
        short[] resultSound = new short[sizeInSamples];

        for(int sampleIndex = 0; sampleIndex < sizeInSamples; sampleIndex++)
        {
            short sample = (short) (Math.sin(Math.PI * 2 * frequency * sampleIndex / samplerate) * (Short.MAX_VALUE / 2));
            resultSound[sampleIndex] = sample;
        }
        return resultSound;
    }
}

