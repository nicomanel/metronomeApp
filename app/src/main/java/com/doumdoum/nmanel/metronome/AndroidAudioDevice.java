package com.doumdoum.nmanel.metronome;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import static android.media.AudioTrack.PLAYSTATE_STOPPED;

/**
 * Created by nmanel on 1/24/2017.
 */

public class AndroidAudioDevice
{
    private AudioTrack track;
    private short[] buffer = new short[1024];

    public AndroidAudioDevice(int sampleRate)
    {
        int minSize =AudioTrack.getMinBufferSize( sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT );
        track = new AudioTrack( AudioManager.STREAM_MUSIC, sampleRate,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                minSize, AudioTrack.MODE_STREAM);
        track.play();
    }

    public void writeSamples(short[] samples)
    {
        fillBuffer( samples );
        track.write( buffer, 0, samples.length );
    }

    private void fillBuffer( short[] samples )
    {
        if( buffer.length < samples.length )
            buffer = new short[samples.length];

        for( int i = 0; i < samples.length; i++ )
            buffer[i] = samples[i];
    }

    public void stop()
    {
        track.flush();
        track.stop();
    }

    public void start()
    {
        if (track.getPlayState() == PLAYSTATE_STOPPED)
        {
            track.play();
        }
    }
}
