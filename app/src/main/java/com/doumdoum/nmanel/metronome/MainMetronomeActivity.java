package com.doumdoum.nmanel.metronome;

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

import static android.media.AudioFormat.CHANNEL_OUT_MONO;
import static android.media.AudioManager.STREAM_MUSIC;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainMetronomeActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE="message";
    private static final int SAMPLE_RATE = (int)(8 * (int)Math.pow(10, 3));
    private static final int DURATION = 5;
    private static final int ONE_SECOND_IN_BYTES = SAMPLE_RATE * 2;
    private static final int BUFFER_SIZE_IN_BYTES = DURATION * ONE_SECOND_IN_BYTES;
    private AudioTrack track;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_metronome);
        track = new AudioTrack(STREAM_MUSIC, SAMPLE_RATE, CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE_IN_BYTES, AudioTrack.MODE_STREAM);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void handleOnClick(View view) {
        Log.println(Log.INFO, "TagTest", "onClick Handler");
        int notificationMarker = 9;
        int positionNotificationPeriod = SAMPLE_RATE;

        track.setNotificationMarkerPosition(notificationMarker);

        track.setPositionNotificationPeriod(positionNotificationPeriod * DURATION);


        track.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
            private static final String PLAYBACK_POSITON_UPDATE_TAG = "Playback Position Tag";
            @Override
            public void onMarkerReached(AudioTrack track) {
                Log.i(PLAYBACK_POSITON_UPDATE_TAG, "Marker Reached " + track.getNotificationMarkerPosition());
            }

            @Override
            public void onPeriodicNotification(AudioTrack track) {
                Log.i(PLAYBACK_POSITON_UPDATE_TAG, "Periodic Notification : " + track.getPositionNotificationPeriod());
                generateTicks(track, DURATION);
            }
        });


        generateTicks(track, DURATION);
        track.play();
        Log.d("PLAY", "" + track.getUnderrunCount());

    }

    private void generateTicks(AudioTrack track, int durationInSec) {
        final String TAG = "generateTicks";
        int sizeInBytes = SAMPLE_RATE;
//        int offsetInBytes = -1;
        for(int i = 0; i < durationInSec; i++)
        {
            int result =  track.write(forgeSample(SAMPLE_RATE, 100), 0, sizeInBytes);
            Log.d(TAG, "writing sample #"+ i + " - result = " + result);
        }
    }

    private short[] forgeSample(int size, int tickLengthInHz) {
        short[] sample = new short[size];
        Arrays.fill(sample, (byte) 0);
        for (int tickSubdivision = 0; tickSubdivision < tickLengthInHz; tickSubdivision++)
        {
            if (tickSubdivision % 4 == 0)
            {
                sample[tickSubdivision] = (int)Short.MAX_VALUE;
            }
        }

//        for (int tickSubdivision = 0; tickSubdivision < tickLengthInHz; tickSubdivision++)
//        {
//            sample[size / 2 + tickSubdivision] = Short.MIN_VALUE;
//        }
        return sample;
    }


}
