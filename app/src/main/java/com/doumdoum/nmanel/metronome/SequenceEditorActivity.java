package com.doumdoum.nmanel.metronome;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Bars;
import com.doumdoum.nmanel.metronome.model.BarsManager;
import com.doumdoum.nmanel.metronome.model.Beat;
import com.doumdoum.nmanel.metronome.ui.BeatView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SequenceEditorActivity extends AppCompatActivity {
    public static final String LOG = "Loop Editor";
    LinkedList<BeatView> beats;
    Map<Beat.Style, Drawable> styles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sequence_editor);
        beats = new LinkedList<>();
        final LinearLayout layout = (LinearLayout) findViewById(R.id.beatsLayoutId);
        final Spinner beatsNumberSpinner = ((Spinner) findViewById(R.id.beatNumberValueId));
        beatsNumberSpinner.setSelection(3);

        beatsNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(LOG, "item selected : " + parent.getSelectedItem());
                int beatsNumber = Integer.valueOf((String) parent.getSelectedItem()).intValue();
                int currentBeatsSize = beats.size();
                layout.setWeightSum(beatsNumber);
                if (beatsNumber > beats.size()) {
                    for (int i = 0; i < beatsNumber - currentBeatsSize; i++) {
                        BeatView newBeatView = new BeatView(getBaseContext(), Beat.Style.Normal);
                        newBeatView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((BeatView)v).nextStyle();
                            }
                        });
                        newBeatView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                        beats.add(newBeatView);
                        layout.addView(newBeatView);
                    }
                }
                if (beatsNumber < beats.size()) {
                    while (beatsNumber < beats.size()) {
                        layout.removeView(beats.getLast());
                        beats.removeLast();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Bar forgeBar()
    {
        String barName = ((EditText) findViewById(R.id.sequenceNameValueId)).getText().toString();
        Bar newBar = new Bar(barName);
        for(BeatView view : beats)
        {
            newBar.addBeat(new Beat(view.getStyle()));
        }

        return newBar;
    }

    public void saveLoopAction(View view) {
        setResult(RESULT_OK);
        Bar bar = forgeBar();
        writeToGson(bar);
        finish();
    }

    private void writeToGson(Bar bar) {
        BarsManager manager = new BarsManager(getApplicationContext());
        Bars bars = manager.loadBars();
        bars.addBar(bar);
        manager.saveBars(bars);
    }


}
