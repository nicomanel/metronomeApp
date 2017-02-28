package com.doumdoum.nmanel.metronome;

import android.content.SharedPreferences;
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
import java.io.IOException;

import static com.doumdoum.nmanel.metronome.DefaultSettings.MAX_TEMPO_VALUE;


public class MainActivity extends AppCompatActivity {

    public static final String METRONOME_PREFERENCE = "com.doumdoum.nmanel.metronome";
    public static final String METRONOME_PREFERENCE_PREFIX = METRONOME_PREFERENCE + ".";
    public static final String KEY_TIMER_ENABLED = METRONOME_PREFERENCE_PREFIX + "timerenabled";
    public static final String KEY_INCREASE_TEMPO = METRONOME_PREFERENCE_PREFIX + "increasetempo";
    public static final String KEY_SKIP_MEASURE = METRONOME_PREFERENCE_PREFIX + "skipmeasure";
    public static final String KEY_TIMER_DURATION = METRONOME_PREFERENCE_PREFIX + "timerduration";
    public static final String KEY_MEASURE_NUMBER = METRONOME_PREFERENCE_PREFIX + "measureNumberBeforeIncrement";
    public static final String KEY_TEMPO_INCREMENT = METRONOME_PREFERENCE_PREFIX + "tempoincrement";
    public static final String KEY_TEMPO = METRONOME_PREFERENCE_PREFIX + "tempo";


    public static final String TEMPO_VALUE_KEY = "TEMPO_VALUE_KEY";
    private Bars bars;
    private Spinner rythmSpinner;
    private EditText tempoEditText;
    private MetronomePlayer metronomePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
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
                int tempo = 0;
                try
                {
                    tempo = Integer.valueOf(s.toString());
                }
                catch (Exception e)
                {
                    findViewById(R.id.startStopButtonId).setEnabled(false);
                    return;
                }
                if ((tempo < 0) || (tempo > MAX_TEMPO_VALUE))
                {
                    ((EditText) findViewById(R.id.tempoValueId)).setError("Invalid tempo value");
                    if (metronomePlayer.isPlaying())
                    {
                        stopTicking(false);
                        stopTickingUiUpdate();
                    }
                    findViewById(R.id.startStopButtonId).setEnabled(false);
                    return;
                }

                findViewById(R.id.startStopButtonId).setEnabled(true);
                if (metronomePlayer.isPlaying()) {
                    stopTicking(false);
                    startTicking();
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
        checkInputs();
        if (!metronomePlayer.isPlaying()) {
            startTicking();
            startTickingUiUpdate();
            return;
        }
        stopTickingWithUiUpdate();
    }

    private boolean checkInputs() {

        return true;
    }

    private void startTickingUiUpdate() {
        Button startStopButton = (Button) findViewById(R.id.startStopButtonId);
        startStopButton.setText("Stop");
        disableSleepingMode();
    }


    private void startTicking() {
        final int tempo = Integer.decode(((EditText) findViewById(R.id.tempoValueId)).getText().toString());
        final boolean increaseTempo = ((Switch) findViewById(R.id.increaseTempoSwitchId)).isChecked();
        final int tempoIncrement = increaseTempo ? Integer.decode(((EditText) findViewById(R.id.tempoIncrementValueId)).getText().toString()) : 0;
        final int measureNumberBeforeIncrement = Integer.decode(((EditText) findViewById(R.id.measureNumberBeforeIncrementValueId)).getText().toString());

        final boolean enableTimer = ((Switch) findViewById(R.id.timerSwitchId)).isChecked();
        final int timerValue = enableTimer ? Integer.decode(((EditText) findViewById(R.id.timerDurationValueId)).getText().toString()) : 0;
        final boolean skipMeasure = ((Switch) findViewById(R.id.skipMeasureSwitchId)).isChecked();
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
        Log.i("SLEEPING MODE", "ENABLED");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });

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
        Log.i("SLEEPING MODE", "DISABLED");
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStop() {
        stopTickingWithUiUpdate();
    }

    private void stopTickingWithUiUpdate() {
        stopTicking(true);
        //stopTickingUiUpdate();
    }

    private void stopTicking(boolean notification) {
        metronomePlayer.stop(notification);
    }

    @Override
    public void onResume() {
        super.onResume();
        restorePreferences();

    }

    @Override
    public void onPause() {
        super.onPause();
        stopTickingWithUiUpdate();
        storePreferences();
    }

    private void storePreferences() {
        SharedPreferences preferences = getSharedPreferences(METRONOME_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putInt(KEY_TEMPO, extractIntegerFromTextEdit(R.id.tempoValueId));
        preferencesEditor.putInt(KEY_TEMPO_INCREMENT, extractIntegerFromTextEdit(R.id.tempoIncrementValueId));
        preferencesEditor.putInt(KEY_MEASURE_NUMBER, extractIntegerFromTextEdit(R.id.measureNumberBeforeIncrementValueId));
        preferencesEditor.putInt(KEY_TIMER_DURATION, extractIntegerFromTextEdit(R.id.timerDurationValueId));
        preferencesEditor.putBoolean(KEY_SKIP_MEASURE, extractBooleanFromSwitch(R.id.skipMeasureSwitchId));
        preferencesEditor.putBoolean(KEY_INCREASE_TEMPO, extractBooleanFromSwitch(R.id.increaseTempoSwitchId));
        preferencesEditor.putBoolean(KEY_TIMER_ENABLED, extractBooleanFromSwitch(R.id.timerSwitchId));
        preferencesEditor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("DrummerMetronome", "onDestroy");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("DrummerMetronome", "onRestoreInstanceState");

    }

    private void restorePreferences() {
        SharedPreferences preferences = getSharedPreferences(METRONOME_PREFERENCE, MODE_PRIVATE);
        setEditTextFromId(R.id.tempoValueId, "" + preferences.getInt(KEY_TEMPO, 80));
        setEditTextFromId(R.id.tempoIncrementValueId, "" + preferences.getInt(KEY_TEMPO_INCREMENT, 5));
        setEditTextFromId(R.id.measureNumberBeforeIncrementValueId, "" + preferences.getInt(KEY_MEASURE_NUMBER, 4));
        setEditTextFromId(R.id.timerDurationValueId, "" + preferences.getInt(KEY_TIMER_DURATION, 60));
        setSwitchCheckedStateFromId(R.id.skipMeasureSwitchId, preferences.getBoolean(KEY_SKIP_MEASURE, false));
        setSwitchCheckedStateFromId(R.id.increaseTempoSwitchId, preferences.getBoolean(KEY_INCREASE_TEMPO, false));
        setSwitchCheckedStateFromId(R.id.timerSwitchId, preferences.getBoolean(KEY_TIMER_ENABLED, false));
    }

    private void setSwitchCheckedStateFromId(int componentId, boolean state) {
        ((Switch) findViewById(componentId)).setChecked(state);
    }

    private void setEditTextFromId(final int componentId, String value) {
        ((EditText) findViewById(componentId)).setText(value);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("DrummerMetronome", "onSaveInstanceState");
    }

    private boolean extractBooleanFromSwitch(final int switchId) {
        return ((Switch) findViewById(switchId)).isChecked();
    }

    private int extractIntegerFromTextEdit(final int editTextId) {
        return Integer.valueOf(((EditText) findViewById(editTextId)).getText().toString());
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


    @Override
    public void onStart() {
        super.onStart();
    }
}
