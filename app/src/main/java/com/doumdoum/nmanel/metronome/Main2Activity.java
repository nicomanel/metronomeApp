package com.doumdoum.nmanel.metronome;

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import static android.media.AudioFormat.CHANNEL_OUT_MONO;
import static android.media.AudioManager.STREAM_MUSIC;

public class Main2Activity extends AppCompatActivity {

    private static final int SAMPLE_RATE = (int)(16 * Math.pow(10, 3));
    private static final int SHORT_SIZE_IN_BYTE = 2;
    private boolean ticking;
    private AudioTrack track;
    private short[] bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final Switch timerSwitch = (Switch) findViewById(R.id.timerSwitchId);
        timerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                   @Override
                                                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                       Log.i("debug", "timerSwitch changed : " + isChecked);
                                                       hideOrDisplaySwitchSettings(timerSwitch, findViewById(R.id.durationTimerSettingsGroupId));
                                                   }
                                               });
        final Switch increaseTempoSwitch = (Switch) findViewById(R.id.increaseTempoSwitchId);
        increaseTempoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("debug", "increaseTempoSwitch changed : " + isChecked);
                hideOrDisplaySwitchSettings(increaseTempoSwitch, findViewById(R.id.increaseSettingsGroupId));
            }
        });

        hideOrDisplaySwitchSettings(timerSwitch, findViewById(R.id.durationTimerSettingsGroupId));
        hideOrDisplaySwitchSettings(increaseTempoSwitch, findViewById(R.id.increaseSettingsGroupId));

        Switch skipSwitch = (Switch) findViewById(R.id.skipMeasureSwitchId);
        skipSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                      Log.i("debug", "skipSwitch changed : " + isChecked);
                                                  }
                                              }
        );

        this.ticking = false;

    }

    protected void hideOrDisplaySwitchSettings(Switch switchWithSettings, View settingsView)
    {
        if (switchWithSettings.isChecked())
        {
            settingsView.setVisibility(View.VISIBLE);

            settingsView.animate().alpha(1);
        }
        else
        {
            settingsView.setVisibility(View.GONE);
            settingsView.animate().alpha(0);
        }
    }


    public void startStopClickAction(View view)
    {
        Button startStopButton = (Button) findViewById(R.id.startStopButtonId);

        if (!ticking) {
            startTicking(startStopButton);
            return;
        }
        stopTicking(startStopButton);
    }

    private void initTrack() {
        int tempo = Integer.decode(((EditText) findViewById(R.id.tempoValueId)).getText().toString()).intValue();
        BarGenerator barGenerator = new BarGenerator(tempo, SAMPLE_RATE);
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

}
