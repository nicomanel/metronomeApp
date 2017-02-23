package com.doumdoum.nmanel.metronome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import static com.doumdoum.nmanel.metronome.DefaultSettings.MAX_TEMPO_VALUE;


public class MainActivity extends AppCompatActivity {
    private Bars bars;
    private Spinner rythmSpinner;
    private EditText tempoEditText;
    private MetronomePlayer metronomePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("DrummerMetronome", "onCreate");
        setContentView(R.layout.activity_main);
        intializeSwitches();
        intializeRythmSpinner();

        tempoEditText = (EditText) findViewById(R.id.tempoValueId);
        tempoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (metronomePlayer.isPlaying()) {
                    stopTicking();
                    startTicking();
                    startTickingUiUpdate();
                }
            }
        });

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

        Button increaseButton = (Button) findViewById(R.id.increaseTempoButtonId);
        increaseButton.setOnTouchListener(new TweakTempoOnTouchListener(tempoEditText, true));
        Button decreaseButton = (Button) findViewById(R.id.decreaseTempoButtonId);
        decreaseButton.setOnTouchListener(new TweakTempoOnTouchListener(tempoEditText, false));

        metronomePlayer = new MetronomePlayer();

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

        if (!metronomePlayer.isPlaying()) {
            startTicking();
            startTickingUiUpdate();
            return;
        }
        stopTickingWithUiUpdate();
    }

    private void startTickingUiUpdate() {
        Button startStopButton = (Button) findViewById(R.id.startStopButtonId);
        startStopButton.setText("Stop");
    }


    private void startTicking() {
        final int tempo = Integer.decode(((EditText) findViewById(R.id.tempoValueId)).getText().toString());
        final boolean increaseTempo = ((Switch) findViewById(R.id.increaseTempoSwitchId)).isChecked();
        final int tempoIncrement = increaseTempo ? Integer.decode(((EditText) findViewById(R.id.BpmIncrementId)).getText().toString()) : 0;
        final int measureNumberBeforeIncrement = Integer.decode(((EditText) findViewById(R.id.barNumberValueId)).getText().toString());

        final boolean enableTimer = ((Switch) findViewById(R.id.timerSwitchId)).isChecked();
        final int timerValue = enableTimer ? Integer.decode(((EditText) findViewById(R.id.durationTimerValueId)).getText().toString()) : 0;
        final boolean skipMeasure = ((Switch) findViewById(R.id.skipMeasureSwitchId)).isChecked();

        disableSleepingMode();

        Bar barToPlay = ((Bar) rythmSpinner.getSelectedItem()).clone();
        if (skipMeasure) {
            barToPlay.forgeSilentNextBar();
        }
        metronomePlayer.addStopPlayingListener(new MetronomePlayerListener() {
            @Override
            public void metronomeHasStopped() {
                Log.i("MainActivity", "metronomeHasStopped");
                stopTickingUiUpdate();
            }

            @Override
            public void tempoHasChanged() {
                updateCurrentTempoTextView();
            }
        });
        metronomePlayer.play(barToPlay, tempo, timerValue, tempoIncrement, measureNumberBeforeIncrement);
    }

    private void disableSleepingMode() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void stopTickingUiUpdate() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!metronomePlayer.isPlaying()) {
                    Button startStopButton = (Button) findViewById(R.id.startStopButtonId);
                    startStopButton.setText("Start");
                }
            }
        });
        enableSleepingMode();

    }

    private void enableSleepingMode() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });

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
    public void onStop() {
        super.onStop();
        stopTickingWithUiUpdate();
    }

    private void stopTickingWithUiUpdate() {
        stopTicking();
        stopTickingUiUpdate();
    }

    private void stopTicking() {
        metronomePlayer.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTickingWithUiUpdate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("DrummerMetronome", "onRestoreInstanceState");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("DrummerMetronome", "onSaveInstanceState");
    }

    public void increaseTempoAction(View view) {
        EditText tempoEditor = (EditText) findViewById(R.id.tempoValueId);
        setTempoValue(Integer.valueOf(tempoEditor.getText().toString()) + 1);
    }

    public void decreaseTempoAction(View view) {
        EditText tempoEditor = (EditText) findViewById(R.id.tempoValueId);
        setTempoValue(Integer.valueOf(tempoEditor.getText().toString()) - 1);
    }

    private void setTempoValue(int newValue) {
        EditText tempoEditor = (EditText) findViewById(R.id.tempoValueId);
        if (newValue < 1) {
            tempoEditor.setText("" + 1);
        }
        if (newValue >= MAX_TEMPO_VALUE) {
            tempoEditor.setText("" + MAX_TEMPO_VALUE);
        }
    }

    private void updateCurrentTempoTextView() {
        final TextView currentTempoView = (TextView) findViewById(R.id.currentTempoId);
        runOnUiThread(new Runnable() {
            private final String label = getResources().getString(R.string.currentTempoLabel);

            @Override
            public void run() {
                currentTempoView.setText("" + label + " " + metronomePlayer.getTempo());
            }
        });
    }
}
