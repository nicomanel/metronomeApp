package com.doumdoum.nmanel.metronome;

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nico.test.R;import static android.media.AudioFormat.CHANNEL_OUT_MONO;
import static android.media.AudioManager.STREAM_MUSIC;

public class MainActivity extends AppCompatActivity {
    private static final int SAMPLE_RATE = (int)(16 * (int)Math.pow(10, 3));
    public static final int SHORT_SIZE_IN_BYTE = 2;

    private boolean ticking;

    private AudioTrack track;
    private BarGenerator barGenerator;
    private short[] bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ticking = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("PAUSE", "Pause");
        track.pause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Log.i("Resume", "resume");
        if (ticking)
        {
            ((Button) findViewById(R.id.start_stop_button)).setText("Stop");
        }
        if(track != null) {
            track.play();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }



    private void initTrack() {
        int tempo = (Integer.decode(((EditText) findViewById(R.id.tempoNumericField)).getText().toString())).intValue();
        barGenerator = new BarGenerator(tempo, SAMPLE_RATE);
        bar = barGenerator.generateFourBeatsBar();
        track = new AudioTrack(STREAM_MUSIC, SAMPLE_RATE, CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bar.length * SHORT_SIZE_IN_BYTE, AudioTrack.MODE_STREAM);
        writeBar();
        track.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
//            private static final String PLAYBACK_POSITON_UPDATE_TAG = "Playback Position Tag";
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
        track.setPositionNotificationPeriod(bar.length - 100);
    }

    private void writeBar() {
        int result = track.write(bar, 0, bar.length);
        Log.i(this.getClass().toString(), "write Result : " + result);
    }

    private void startTicking(Button startStopButton)
    {
        Log.i(this.getClass().toString(), "startTicking()");
        startStopButton.setText("Stop");
        ticking = true;
        initTrack();
        track.play();
    }

    private void stopTicking(Button startStopButton)
    {
        Log.i(this.getClass().toString(), "stopTicking()");
        track.pause();
        track.flush();
        track.stop();
        ticking = false;
        startStopButton.setText("Start");
    }

    public void startStopTicking(View view) {
        Button startStopButton = (Button) findViewById(R.id.start_stop_button);

        if (!ticking) {
            startTicking(startStopButton);
            return;
        }
        stopTicking(startStopButton);

    }
}
