package com.doumdoum.nmanel.metronome.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
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

    private static void saveBarsOrSequences(Bars bars, Context context, String filename) {
        Gson gson = new GsonBuilder().create();
        String barInString = gson.toJson(bars);
        try {
            FileOutputStream stream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(barInString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeObserversFromBars(Bars bars) {
        bars.deleteObservers();
        for (Bar barToProcess : bars.getBars()) {
            removeObserversFromBar(barToProcess);
        }
    }

    public static void removeObserversFromBar(Bar bar) {
        Bar toRemove = bar;
        while (toRemove != null) {
            toRemove.deleteObservers();
            toRemove = toRemove.getNextBar();
        }
    }

    public void saveBars(Bars bars) {
        saveBarsOrSequences(bars, context, BARS_FILENAME);
    }

    public Bars loadBars() {
        return loadBarsOrSequences(context, BARS_FILENAME, forgeDefaultBars());
    }

    private Bars loadBarsOrSequences(Context context, String filename, Bars defaultValue) {
        Gson gson = new GsonBuilder().create();
        StringBuffer buffer = new StringBuffer();
        Bars bars = defaultValue;

        try (FileInputStream stream = context.openFileInput(filename)) {

            int content;
            while ((content = stream.read()) != -1) {
                buffer.append((char) content);
            }
            bars = gson.fromJson(buffer.toString(), Bars.class);
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
        bar2.addBeat(new Beat(Beat.Style.Normal));
        bars.addBar(bar2);
        return bars;
    }

    public void cleanBarsFile()
    {
        context.deleteFile(BARS_FILENAME);
    }
}
