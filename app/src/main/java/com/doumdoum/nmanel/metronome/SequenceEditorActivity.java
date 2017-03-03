package com.doumdoum.nmanel.metronome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Bars;
import com.doumdoum.nmanel.metronome.model.BarsManager;
import com.doumdoum.nmanel.metronome.model.Beat;
import com.doumdoum.nmanel.metronome.ui.BeatView;
import com.doumdoum.nmanel.metronome.ui.SequencesSpinner;

import java.util.LinkedList;
import java.util.Map;

public class SequenceEditorActivity extends AppCompatActivity {
    public static final String LOG = "Loop Editor";
    LinkedList<BeatView> beats;
    Map<Beat.Style, Drawable> styles;

    private Bars bars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sequence_editor);

        bars = (new BarsManager(getApplicationContext()).loadBars());
        ((SequencesSpinner) findViewById(R.id.sequencesSpinnerId)).setBars(bars);




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
                        BeatView newBeatView = new BeatView(getBaseContext(), new Beat(Beat.Style.Normal));
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
            newBar.addBeat(view.getBeat());
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


    public void newSequenceAction(View view) {
    }

    public void removeSequenceAction(View view) {
        final Bar barToRemove = (Bar) ((SequencesSpinner) findViewById(R.id.sequencesSpinnerId)).getSelectedItem();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BarsManager manager = new BarsManager(getApplicationContext());
                bars.removeBar(barToRemove);
                manager.saveBars(bars);
                ((SequencesSpinner) findViewById(R.id.sequencesSpinnerId)).setBars(bars);


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.setMessage("You are about to remove '" + barToRemove.getName() + "', Are you sure ?")
                .setTitle("Remove a sequence");

        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
