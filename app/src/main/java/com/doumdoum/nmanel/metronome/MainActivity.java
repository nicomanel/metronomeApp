package com.doumdoum.nmanel.metronome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Bars;
import com.doumdoum.nmanel.metronome.model.BarsManager;
import com.doumdoum.nmanel.metronome.model.Beat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.util.Observable;
import java.util.Observer;

import static com.doumdoum.nmanel.metronome.DefaultSettings.BUFFER_SIZE;
import static com.doumdoum.nmanel.metronome.DefaultSettings.SAMPLERATE;


public class MainActivity extends AppCompatActivity implements Observer {
    private boolean ticking;
    private Bars bars;
    private Spinner rythmSpinner;
    private AndroidAudioDevice device;
    private BarGenerator generator;
    private EditText tempoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("DrummerMetronome", "onCreate");
        setContentView(R.layout.activity_main);
        intializeSwitches();
        intializeRythmSpinner();

        tempoEditText = (EditText) findViewById(R.id.tempoValueId);
        tempoEditText.setOnTouchListener(new View.OnTouchListener() {
            private BpmCalculator calculator = new BpmCalculator(2000);

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int tempo = calculator.tap(event.getEventTime());
                    if (tempo != 0) {
                        tempoEditText.setText("" + tempo);
                    }
                }
                return false;
            }
        });



        this.ticking = false;
        device = new AndroidAudioDevice();
    }

    private void intializeSwitches() {
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
                findViewById(R.id.currentTempoId).setVisibility(increaseTempoSwitch.isChecked() ? View.VISIBLE : View.INVISIBLE);
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
        });
    }

    private void intializeRythmSpinner() {
        bars = (new BarsManager(getApplicationContext())).loadBars();
        rythmSpinner = (Spinner) findViewById(R.id.rythmSpinnerId);
        ArrayAdapter<Bar> adapter = new ArrayAdapter<Bar>(getApplicationContext(), R.layout.spinner_item, bars.getBars());
        rythmSpinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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


    private void startTicking(final Button startStopButton) {


        final int tempo = Integer.decode(((EditText) findViewById(R.id.tempoValueId)).getText().toString());
        final boolean increaseTempo = ((Switch) findViewById(R.id.increaseTempoSwitchId)).isChecked();
        final int tempoIncrement = Integer.decode(((EditText) findViewById(R.id.BpmIncrementId)).getText().toString());
        final int measureNumberBeforeIncrement = Integer.decode(((EditText) findViewById(R.id.barNumberValueId)).getText().toString());

        final boolean enableTimer = ((Switch) findViewById(R.id.timerSwitchId)).isChecked();
        final int timerValue = Integer.decode(((EditText) findViewById(R.id.durationTimerValueId)).getText().toString());
        final boolean skipMeasure = ((Switch) findViewById(R.id.skipMeasureSwitchId)).isChecked();
        initializeBarGenerator(tempo, increaseTempo, tempoIncrement, measureNumberBeforeIncrement);
        final MainActivity mainActivity = this;

        disableSleepingMode();

        new Thread(new Runnable() {
            private int writtenSamplesCounter = 0;

            @Override
            public void run() {
                ticking = true;

                Bar barToPlay = ((Bar) rythmSpinner.getSelectedItem()).clone();
                if (skipMeasure) {
                    barToPlay.forgeSilentNextBar();
                }

                generator.setBar(barToPlay);

                while (keepWriting()) {
                    short[] newSamples = generator.getSamples();
                    device.writeSamples(newSamples);
                    writtenSamplesCounter += newSamples.length;
                }

                if (ticking) {
                    mainActivity.stopTicking(startStopButton);
                }
            }

            private boolean keepWriting() {
                int timerValueInSamples = (timerValue * SAMPLERATE);
                return (ticking && !enableTimer) || ticking && (enableTimer && writtenSamplesCounter < timerValueInSamples);
            }


        }).start();

    }

    private void disableSleepingMode() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void initializeBarGenerator(int tempo, boolean increaseTempo, int tempoIncrement, int measureNumberBeforeIncrement) {
        if (generator != null) {
            generator.deleteObserver(this);
        }
        if (!increaseTempo)
            generator = new BarGenerator(tempo, SAMPLERATE, BUFFER_SIZE);
        else
            generator = new BarGenerator(tempo, SAMPLERATE, increaseTempo, tempoIncrement, measureNumberBeforeIncrement, BUFFER_SIZE);
        generator.addObserver(this);
    }

    private void stopTicking(final Button startStopButton) {
        Log.i(this.getClass().toString(), "stopTicking()");
        ticking = false;
        enableSleepingMode();
        device.stop();

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startStopButton.setText("Start");
            }
        });

    }

    private void enableSleepingMode() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
        final boolean increaseTempo = ((Switch) findViewById(R.id.increaseTempoSwitchId)).isChecked();

        if (increaseTempo) {
            final TextView currentTempoView = (TextView) findViewById(R.id.currentTempoId);
            this.runOnUiThread(new Runnable() {
                private final String label = getResources().getString(R.string.currentTempoLabel);

                @Override
                public void run() {
                    currentTempoView.setText("" + label + " " + generator.getIncrementedTempo());
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopTicking();
        Log.i("DrummerMetronome", "onStop");
    }

    private void stopTicking() {
        ticking = false;
        device.stop();
        Button startStopButton = (Button) findViewById(R.id.startStopButtonId);
        startStopButton.setText("Start");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("DrummerMetronome", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTicking();
        Log.i("DrummerMetronome", "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("DrummerMetronome", "onDestroy");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("DrummerMetronome", "onRestoreInstanceState");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("DrummerMetronome", "onSaveInstanceState");
    }
}
