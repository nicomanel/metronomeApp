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

import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Beat;

import static android.media.AudioFormat.CHANNEL_OUT_MONO;
import static android.media.AudioFormat.ENCODING_PCM_16BIT;
import static android.media.AudioManager.STREAM_MUSIC;
import static com.doumdoum.nmanel.metronome.SoundHelper.concatShortArrays;
import static com.doumdoum.nmanel.metronome.SoundHelper.generatePureSound;


public class Main2Activity extends AppCompatActivity {
    private boolean ticking;
    private AudioTrack track;

    private final int SAMPLERATE = 16000;
    private final int BUFFER_SIZE = SAMPLERATE * ENCODING_PCM_16BIT * 5 ;

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
        track = new AudioTrack(STREAM_MUSIC, SAMPLERATE, CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE, AudioTrack.MODE_STREAM);
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


    public void testSound(View view)
    {
        int SAMPLERATE = 16000;
        final int BUFFER_SIZE = SAMPLERATE * ENCODING_PCM_16BIT * 5;

        AudioTrack track = new AudioTrack(STREAM_MUSIC, 16000, CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE, AudioTrack.MODE_STREAM);
        final short[] sound = concatShortArrays(
                            concatShortArrays(
                                concatShortArrays(
                                    concatShortArrays(generatePureSound(SAMPLERATE, 500, 660),generatePureSound(SAMPLERATE, 500, 880)),
                                generatePureSound(SAMPLERATE, 3000, 440)),
                            generatePureSound(SAMPLERATE, 500, 880)),
                        generatePureSound(SAMPLERATE, 500, 440));




        Log.i(this.getClass().toString(), "getBufferSizeInFrames : " + track.getBufferSizeInFrames());
        track.setPositionNotificationPeriod(SAMPLERATE);
        track.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
            private int iterationCounter = 1;
            @Override
            public void onMarkerReached(AudioTrack track) {
                Log.i(this.getClass().toString(), "marker reach : " + track.getPlaybackHeadPosition());
                track.write(sound, 0, sound.length);
            }

            @Override
            public void onPeriodicNotification(AudioTrack track) {
                Log.i(this.getClass().toString(), "period notification reached: " + track.getPlaybackHeadPosition() + " / " + (track.getBufferSizeInFrames() * 0.8));

                if (track.getPlaybackHeadPosition() >= iterationCounter * track.getBufferSizeInFrames() * 0.8)
                {
                    Log.i(this.getClass().toString(), "reaching the end, writing new data : " + track.write(sound, 0, sound.length));
                    iterationCounter++;
                }
            }
        });

        Log.i(this.getClass().toString(), "sound.length : " + sound.length);
        Log.i(this.getClass().toString(), "data written : " + track.write(sound, 0, sound.length));

        track.play();
    }


    private void startTicking(Button startStopButton)
    {
        Log.i(this.getClass().toString(), "startTicking()");
        startStopButton.setText("Stop");
        ticking = true;
        int tempo = Integer.decode(((EditText) findViewById(R.id.tempoValueId)).getText().toString());
        boolean increasetempo = ((Switch)findViewById(R.id.increaseTempoSwitchId)).isChecked();
        boolean silenceBar = ((Switch)findViewById(R.id.skipMeasureSwitchId)).isChecked();

        final BarGenerator generator = new BarGenerator(tempo, SAMPLERATE, BUFFER_SIZE);
        Bar bar = new Bar();
        bar.addBeat(new Beat(Beat.Style.Accent2));
        bar.addBeat(new Beat(Beat.Style.Accent1));
        bar.addBeat(new Beat(Beat.Style.Accent1));
        bar.addBeat(new Beat(Beat.Style.Accent1));
        generator.setBar(bar);
        Log.i("writing", "Start Writing : " + System.currentTimeMillis());
        track.write(generator.getSamples(), 0, BUFFER_SIZE);
        Log.i("writing", "End Writing : " + System.currentTimeMillis());
        Log.i(this.getClass().toString(), "getBufferSizeInFrames : " + track.getBufferSizeInFrames());
        track.setPositionNotificationPeriod(SAMPLERATE);
        track.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
            private int iterationCounter = 1;
            private final short[] buffer = new short[BUFFER_SIZE];
            @Override
            public void onMarkerReached(AudioTrack track) {
            }

            @Override
            public void onPeriodicNotification(AudioTrack track) {
                Log.i("toto", "onPeriodicNotification begin : " + track.getPlaybackHeadPosition() );
                final AudioTrack track1 = track;
                if (track.getPlaybackHeadPosition() >= iterationCounter * track.getBufferSizeInFrames() * 0.8)
                {


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("toto", "writing new data begin " + System.currentTimeMillis());
                                track1.write(generator.getSamples(), 0, BUFFER_SIZE);
                                Log.i("toto", "writing new data end " + System.currentTimeMillis());
                            }
                        }).start();

                        iterationCounter++;
                }
                Log.i("toto", "onPeriodicNotification end");
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                track.play();
            }
        }).start();





    }

    private void stopTicking(Button startStopButton)
    {
        Log.i(this.getClass().toString(), "stopTicking()");
        ticking = false;
        track.stop();
        startStopButton.setText("Start");
    }

    public void testBarGenerator(View view) {
        final int SAMPLERATE = 16000;
        final int BUFFER_SIZE = SAMPLERATE * ENCODING_PCM_16BIT * 5;
        int tempo = 60;
        final BarGenerator generator = new BarGenerator(tempo, SAMPLERATE, BUFFER_SIZE);
        Bar bar = new Bar();
        bar.addBeat(new Beat(Beat.Style.Accent2));
        bar.addBeat(new Beat(Beat.Style.Accent1));
        bar.addBeat(new Beat(Beat.Style.Accent1));
        bar.addBeat(new Beat(Beat.Style.Accent1));
        generator.setBar(bar);

        AudioTrack track = new AudioTrack(STREAM_MUSIC, SAMPLERATE, CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE, AudioTrack.MODE_STREAM);
        short[] samples = generator.getSamples();
        track.write(generator.getSamples(), 0, BUFFER_SIZE);
        Log.i(this.getClass().toString(), "getBufferSizeInFrames : " + track.getBufferSizeInFrames());
        track.setPositionNotificationPeriod(SAMPLERATE);
        track.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
            private int iterationCounter = 1;
            @Override
            public void onMarkerReached(AudioTrack track) {
            }

            @Override
            public void onPeriodicNotification(AudioTrack track) {
                Log.i(this.getClass().toString(), "period notification reached: " + track.getPlaybackHeadPosition() + " / " + (track.getBufferSizeInFrames() * 0.8));

                if (track.getPlaybackHeadPosition() >= iterationCounter * track.getBufferSizeInFrames() * 0.8)
                {
                    Log.i(this.getClass().toString(), "reaching the end, writing new data : " + track.write(generator.getSamples(), 0, BUFFER_SIZE));
                    iterationCounter++;
                }
            }
        });

        track.play();

    }
}
