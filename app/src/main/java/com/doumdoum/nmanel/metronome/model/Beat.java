package com.doumdoum.nmanel.metronome.model;

import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import com.doumdoum.nmanel.metronome.DefaultSettings;
import com.doumdoum.nmanel.metronome.MetronomeHelper;
import com.doumdoum.nmanel.metronome.R;

/**
 * Created by nmanel on 1/20/2017.
 */
public class Beat implements Cloneable {
    private Style beatStyle;

    public Beat(Style style)
    {
        beatStyle = style;
    }

    public int getSamplesCount(int tempo, int sampleRate)
    {
        return 60 * sampleRate / tempo;
    }

    public short[] generateSamples(int tempo, int sampleRate)
    {
        short[] samples = new short[getSamplesCount(tempo, sampleRate)];
//        Log.i(getClass().toString(), "samples : " + samples.length);
        fillSamples(samples, sampleRate);
        return samples;
    }

    private void fillSamples(final short[] samples, int sampleRate) {
        short[] soundSamples = MetronomeHelper.generatePureSound(sampleRate, DefaultSettings.BEAT_LENGTH_IN_MS, beatStyle.getFrequency());
        Log.i("samples", "samples : " + samples.length);
        Log.i("samples", "soundSamples : " + soundSamples.length);
        for(int sampleIndex = 0; sampleIndex < soundSamples.length; sampleIndex++)
        {
            samples[sampleIndex] = soundSamples[sampleIndex];
        }
    }

    public Style getBeatStyle()
    {
        return beatStyle;
    }

    public String toString()
    {
        return "Beat(" + getBeatStyle().name() + ")";
    }

    public Bar clone() {
        Bar clonedBeat = null;
        try {
            clonedBeat = (Bar) super.clone();
        } catch (CloneNotSupportedException exception) {
            Log.e("Beat", "not clonable");
        }
        return clonedBeat;
    }

    public enum Style {

        Accent1(880),
        Silent(0),
        Normal(440),
        Ghost(220),
        Accent2(660),;

        protected int frequency;

        Style(int i) {
            frequency = i;
        }
        public int getFrequency() {
            return frequency;
        }
    }
}
