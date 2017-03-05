package com.doumdoum.nmanel.metronome.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by nmanel on 1/25/2017.
 */

public class BarsManager {
    public static final String BARS_FILENAME = "bars.json";
    private Context context;


    public BarsManager(Context context) {
        this.context = context;
    }

    public void saveBars(Bars bars) {
        Gson gson = new GsonBuilder().create();
        String barInString = gson.toJson(bars);
        try {
            FileOutputStream stream = context.openFileOutput(BARS_FILENAME, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(barInString);
            writer.flush();
            writer.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public Bars loadBars() {
        Gson gson = new GsonBuilder().create();
        StringBuffer buffer = new StringBuffer();
        Bars bars = null;

        try (FileInputStream stream = context.openFileInput(BARS_FILENAME)) {

            int content;
            while ((content = stream.read()) != -1) {
                buffer.append((char) content);
            }
            bars = gson.fromJson(buffer.toString(), Bars.class);
        } catch (FileNotFoundException e) {

           bars = forgeDefaultBars();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bars;
    }

    private Bars forgeDefaultBars()
    {
        Bars bars = new Bars();
        Bar bar = new Bar("4 / 4");
        bar.addBeat(new Beat(Beat.Style.Accent1));
        bar.addBeat(new Beat(Beat.Style.Normal));
        bar.addBeat(new Beat(Beat.Style.Normal));
        bar.addBeat(new Beat(Beat.Style.Normal));
        bars.addBar(bar);
        Bar bar2 = new Bar("2 / 4");
        bar2.addBeat(new Beat(Beat.Style.Accent1));
        bar2.addBeat(new Beat(Beat.Style.Accent2));
        bars.addBar(bar2);
        return bars;
    }

    public void cleanBarsFile()
    {
        context.deleteFile(BARS_FILENAME);
    }
}
