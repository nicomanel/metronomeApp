package com.doumdoum.nmanel.metronome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.doumdoum.nmanel.metronome.model.Bar;
import com.doumdoum.nmanel.metronome.model.Bars;
import com.doumdoum.nmanel.metronome.model.BarsManager;
import com.doumdoum.nmanel.metronome.model.Beat;
import com.doumdoum.nmanel.metronome.ui.BarEditor;
import com.doumdoum.nmanel.metronome.ui.BarsSpinner;

public class BarEditorActivity extends AppCompatActivity {
    public static final String LOG = BarEditorActivity.class.toString();
    private static final int SEQUENCE_EDITOR_ACTIVITY_RETURN = 55;
    private Bars bars;
    private BarsSpinner barsSpinner;
    private BarEditor barEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_editor);
        bars = (new BarsManager(getApplicationContext()).loadBars());
        barEditor = (BarEditor) findViewById(R.id.barEditorId);
        barsSpinner = ((BarsSpinner) findViewById(R.id.sequencesSpinnerId));
        barsSpinner.setBars(bars);
        barsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                barEditor.setBar((Bar)parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void saveLoopAction(View view) {
        writeToGson();
    }

    private void writeToGson() {
        BarsManager manager = new BarsManager(getApplicationContext());
        Bars clonedBars = bars.clone();
        for(Bar bar : clonedBars.getBars())
        {
            bar.deleteObservers();
        }
        manager.saveBars(clonedBars);
    }

    public void newSequenceAction(View view) {
        Bar newBar = new Bar("New Bar");
        newBar.addBeat(new Beat(Beat.Style.Accent1));
        newBar.addBeat(new Beat(Beat.Style.Normal));
        newBar.addBeat(new Beat(Beat.Style.Normal));
        newBar.addBeat(new Beat(Beat.Style.Normal));
        bars.addBar(newBar);
        barEditor.setBar(newBar);
    }

    public void removeSequenceAction(View view) {
        final Bar barToRemove = (Bar) ((BarsSpinner) findViewById(R.id.sequencesSpinnerId)).getSelectedItem();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BarsManager manager = new BarsManager(getApplicationContext());
                bars.removeBar(barToRemove);
                manager.saveBars(bars);
                ((BarsSpinner) findViewById(R.id.sequencesSpinnerId)).setBars(bars);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setMessage("You are about to delete '" + barToRemove.getName() + "', Are you sure ?")
                .setTitle("Remove a sequence");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void finish() {
        writeToGson();
        super.finish();
    }

    public void createSequenceAction(View view) {
        Intent intentMyAccount = new Intent(BarEditorActivity.this, SequenceEditorActivity.class);
        startActivityForResult(intentMyAccount, SEQUENCE_EDITOR_ACTIVITY_RETURN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEQUENCE_EDITOR_ACTIVITY_RETURN) {

        }
    }


}
