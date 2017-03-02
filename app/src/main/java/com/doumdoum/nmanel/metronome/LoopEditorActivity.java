package com.doumdoum.nmanel.metronome;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.LinkedList;

public class LoopEditorActivity extends AppCompatActivity {
    public static final String LOG = "Loop Editor";
    LinkedList<ImageView> beats;
    LinkedList<Drawable> drawables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sequence_editor);
        beats = new LinkedList<>();
        drawables = new LinkedList<>();
        drawables.add(getDrawable(R.drawable.quarter_note));
        drawables.add(getDrawable(R.drawable.accented_quarter_note));
        drawables.add(getDrawable(R.drawable.ghost_note));
        drawables.add(getDrawable(R.drawable.silent_note));


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
                        ImageView newImageView = new ImageView(getBaseContext());
                        newImageView.setImageDrawable(drawables.getFirst());
                        newImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageView view = (ImageView) v;
                                int i = drawables.indexOf(((ImageView) v).getDrawable());
                                if (((ImageView) v).getDrawable().equals(drawables.getLast())) {
                                    i = 0;
                                } else {
                                    i++;
                                }

                                ((ImageView) v).setImageDrawable(drawables.get(i));
                            }
                        });
                        newImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                        beats.add(newImageView);
                        layout.addView(newImageView);
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

    public void saveLoopAction(View view) {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
