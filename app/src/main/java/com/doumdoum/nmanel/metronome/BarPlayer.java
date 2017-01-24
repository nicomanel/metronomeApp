package com.doumdoum.nmanel.metronome;

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.util.Log;
import android.widget.EditText;

import static android.media.AudioFormat.CHANNEL_OUT_MONO;
import static android.media.AudioManager.ERROR;
import static android.media.AudioManager.STREAM_MUSIC;

/**
 * Created by nmanel on 12/8/2016.
 */

public class BarPlayer {
    private static final int SAMPLE_RATE = (int)(16 * Math.pow(10, 3));
    private static final int BUFFER_SIZE = SAMPLE_RATE * 10;

    private static final int BEAT_LENGTH_IN_MS = 100;


    private final boolean increaseBpmPeriodically;
    private BarGenerator barGenerator;
    private AudioTrack track;
    private int tempo;

    public enum SequenceType {
        BEAT_SILENCE,
        CONTINUE,
    }

    private SequenceType sequenceType;

    public BarPlayer(int tempo, boolean increaseBpmPeriodically)
    {
        this.increaseBpmPeriodically = increaseBpmPeriodically;
        barGenerator = new BarGenerator(tempo, SAMPLE_RATE, BEAT_LENGTH_IN_MS);
        this.tempo = tempo;
    }

    private void initTrack() {
        track = new AudioTrack(STREAM_MUSIC, SAMPLE_RATE, CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE, AudioTrack.MODE_STREAM);
        int length = writeBar();
        track.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
            @Override
            public void onMarkerReached(AudioTrack track) {
                Log.i(this.getClass().toString(), "Marker Reached " + track.getNotificationMarkerPosition());
            }

            @Override
            public void onPeriodicNotification(AudioTrack track) {
                Log.i(this.getClass().toString(), "Periodic Notification : " + track.getPositionNotificationPeriod());
                writeBar();
            }
        });
        track.setPositionNotificationPeriod(length - 100);
    }

    private int writeBar() {
        return 0;
    }

    public void startIncrementBPM(SequenceType sequence)
    {

    }

    public void start(SequenceType sequence)
    {
        this.sequenceType = sequence;
        initTrack();
        track.play();
    }


    public void stop()
    {
        track.pause();
        track.flush();
        track.stop();
    }


}
