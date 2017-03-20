package com.doumdoum.nmanel.metronome;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import static com.doumdoum.nmanel.metronome.DefaultSettings.PRESS_TEMPO_CHANGE_TIME_IN_MS;

/**
 * Created by nmanel on 2/22/2017.
 */

public class TweakTempoOnTouchListener implements View.OnTouchListener {
    private static final String LOG = TweakTempoOnTouchListener.class.toString();
    private final Handler handler = new Handler();
    private boolean increaseOrDecrease;
    private boolean fastClick;
    private EditText tempoValueEditText;
    private final Runnable increaseOrDecreaseAction = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, PRESS_TEMPO_CHANGE_TIME_IN_MS);
            incrementOrDecrementTempo(5);
            fastClick = false;
        }
    };

    public TweakTempoOnTouchListener(EditText tempoValueEditText, boolean increaseOrDecrease) {
        this.increaseOrDecrease = increaseOrDecrease;
        this.tempoValueEditText = tempoValueEditText;
        fastClick = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                fastClick = true;
                handler.postDelayed(increaseOrDecreaseAction, PRESS_TEMPO_CHANGE_TIME_IN_MS);
                break;

            case MotionEvent.ACTION_UP:
                handler.removeCallbacks(increaseOrDecreaseAction);
                if (fastClick)
                {
                    incrementOrDecrementTempo(1);
                }

                break;
        }

        return false;
    }

    private void incrementOrDecrementTempo(int tempoIncrement) {
        int tempo = Integer.valueOf(tempoValueEditText.getText().toString());
        MetronomeHelper.changeTempo(tempoValueEditText, tempo + (increaseOrDecrease?tempoIncrement:(-1 * tempoIncrement)));
    }
}

