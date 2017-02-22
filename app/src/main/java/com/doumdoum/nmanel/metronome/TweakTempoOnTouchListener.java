package com.doumdoum.nmanel.metronome;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by nmanel on 2/22/2017.
 */

public class TweakTempoOnTouchListener implements View.OnTouchListener {
    private final Handler handler = new Handler();
    private String TAG = "ontouch";
    private boolean increaseOrDecrease;
    private EditText tempoValueEditText;
    private final Runnable increaseOrDecreaseAction = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "pressed");
            handler.postDelayed(this, 500);
            if (increaseOrDecrease) {
                tempoValueEditText.setText("" + (Integer.valueOf(tempoValueEditText.getText().toString()) + 5));
            } else {
                tempoValueEditText.setText("" + (Integer.valueOf(tempoValueEditText.getText().toString()) - 5));
            }
        }
    };

    public TweakTempoOnTouchListener(EditText tempoValueEditText, boolean increaseOrDecrease) {
        this.increaseOrDecrease = increaseOrDecrease;
        this.tempoValueEditText = tempoValueEditText;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                handler.postDelayed(increaseOrDecreaseAction, 1000);
                Log.i(TAG, "Action was DOWN");
                break;

            case MotionEvent.ACTION_UP:
                Log.i(TAG, "Action was UP");
                handler.removeCallbacks(increaseOrDecreaseAction);

                break;
        }

        return false;
    }
}

