package com.doumdoum.nmanel.metronome;

import android.database.DataSetObserver;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;

import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Bars;
import com.doumdoum.nmanel.metronome.model.BarsManager;
import com.doumdoum.nmanel.metronome.model.Beat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.util.Observable;
import java.util.Observer;

import static android.R.layout.simple_list_item_1;
import static android.media.AudioFormat.CHANNEL_OUT_MONO;
import static android.media.AudioFormat.ENCODING_PCM_16BIT;
import static android.media.AudioManager.STREAM_MUSIC;
import static com.doumdoum.nmanel.metronome.SoundHelper.concatShortArrays;
import static com.doumdoum.nmanel.metronome.SoundHelper.generatePureSound;


public class MainActivity extends AppCompatActivity implements Observer {
    private boolean ticking;
    private AudioTrack track;
    private Bars bars;
    private Spinner rythmSpinner;
    private final int SAMPLERATE = 16000;
    private final int BUFFER_SIZE = 1024;
    private AndroidAudioDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
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
                findViewById(R.id.currentTempoId).setVisibility(increaseTempoSwitch.isChecked()?View.VISIBLE:View.INVISIBLE);
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


        intializeRythmSpinner();

        this.ticking = false;
    }

    private void intializeRythmSpinner() {
        bars = (new BarsManager(getApplicationContext())).loadBars();
        rythmSpinner = (Spinner)  findViewById(R.id.rythmSpinnerId);
        ArrayAdapter<Bar> adapter = new ArrayAdapter<Bar>(getApplicationContext(), android.R.layout.simple_spinner_item, bars.getBars());
                rythmSpinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.i("testadapater" ,"" + adapter.getCount());
        Log.i("testadapater" ,"" + rythmSpinner.getSelectedItem());


    }

    protected void hideOrDisplaySwitchSettings(Switch switchWithSettings, View settingsView) {
        if (switchWithSettings.isChecked()) {
            settingsView.setVisibility(View.VISIBLE);

            settingsView.animate().alpha(1);
        } else {
            settingsView.setVisibility(View.GONE);
            settingsView.animate().alpha(0);
        }
    }


    public void startStopClickAction(View view) {
        Button startStopButton = (Button) findViewById(R.id.startStopButtonId);

        if (!ticking) {
            startTicking(startStopButton);
            startStopButton.setText("Stop");

            return;
        }
        stopTicking(startStopButton);
    }


     private void startTicking(Button startStopButton) {

        device = new AndroidAudioDevice();
        final int tempo = Integer.decode(((EditText) findViewById(R.id.tempoValueId)).getText().toString());
        final boolean increaseTempo = ((Switch) findViewById(R.id.increaseTempoSwitchId)).isChecked();
        final int tempoIncrement = Integer.decode(((EditText) findViewById(R.id.BpmIncrementId)).getText().toString());
        final int measureNumberBeforeIncrement = Integer.decode(((EditText) findViewById(R.id.barNumberValueId)).getText().toString());

        final boolean enableTimer = ((Switch) findViewById(R.id.timerSwitchId)).isChecked();
        final boolean skipMeasure = ((Switch) findViewById(R.id.skipMeasureSwitchId)).isChecked();


        new Thread(new Runnable() {
            @Override
            public void run() {
                final BarGenerator generator;
                if (!increaseTempo)
                    generator = new BarGenerator(tempo, SAMPLERATE, BUFFER_SIZE);
                else
                    generator = new BarGenerator(tempo, SAMPLERATE, increaseTempo, tempoIncrement, measureNumberBeforeIncrement, BUFFER_SIZE);
                ticking = true;

                Bar barToPlay = (Bar)rythmSpinner.getSelectedItem();
                if (skipMeasure) {
                    barToPlay.forgeSilentNextBar();
                }

                generator.setBar(barToPlay);

                while (ticking) {
                    device.writeSamples(generator.getSamples());
                }
            }
        }).start();
    }

    private void stopTicking(Button startStopButton) {
        Log.i(this.getClass().toString(), "stopTicking()");
        ticking = false;
        device.stop();
        startStopButton.setText("Start");
    }

    public void saveBars(View view) {
        BarsManager barsManager = new BarsManager(getApplicationContext());
        barsManager.loadBars();
    }


    public void copySaveBars(View view) {
        Bar bar = new Bar("FF");
        bar.addBeat(new Beat(Beat.Style.Accent2));
        bar.addBeat(new Beat(Beat.Style.Accent1));
        bar.addBeat(new Beat(Beat.Style.Accent1));
        bar.addBeat(new Beat(Beat.Style.Accent1));
        Bar bar2 = new Bar("Silence");
        bar2.addBeat(new Beat(Beat.Style.Silent));
        bar2.addBeat(new Beat(Beat.Style.Silent));
        bar2.addBeat(new Beat(Beat.Style.Silent));
        bar2.addBeat(new Beat(Beat.Style.Silent));
        bar.setNextBar(bar2);


        Gson gson = new GsonBuilder().create();
        String barInString = gson.toJson(bar);

        Bar newBar = gson.fromJson(barInString, Bar.class);

        try {
            String fileName = "rythm_1.json";
            FileOutputStream stream = openFileOutput(fileName, MODE_PRIVATE);
            stream.write(barInString.getBytes());
            stream.close();
            Log.i("toto", getFileStreamPath(fileName).getAbsolutePath());

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        
    }
}
