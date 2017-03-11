package com.doumdoum.nmanel.metronome.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by nmanel on 3/11/2017.
 */

public class SequencesManager {
    public static final String SEQUENCES_FILENAME = "sequences.json";
    private Context context;

    public SequencesManager(Context context) {
        this.context = context;
    }

    public void saveSequences(Sequences sequences) {
        Gson gson = new GsonBuilder().create();
        String barInString = gson.toJson(cloneAndRemoveObserver(sequences));
        try {
            FileOutputStream stream = context.openFileOutput(SEQUENCES_FILENAME, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(barInString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Sequences cloneAndRemoveObserver(Sequences toClone) {
        Sequences clone = toClone.clone();
        clone.deleteObservers();
        for (Sequence sequence : clone.getSequences()) {
            sequence.deleteObservers();
            BarsManager.removeObserversFromBar(sequence.getBars());

        }
        return clone;

    }

    public Sequences loadSequences() {
        Gson gson = new GsonBuilder().create();
        StringBuffer buffer = new StringBuffer();
        Sequences sequences = forgeDefaultSequences();

        try (FileInputStream stream = context.openFileInput(SEQUENCES_FILENAME)) {

            int content;
            while ((content = stream.read()) != -1) {
                buffer.append((char) content);
            }
            sequences = gson.fromJson(buffer.toString(), Sequences.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sequences;
    }

    private Sequences forgeDefaultSequences() {
        return new Sequences();
    }
}
