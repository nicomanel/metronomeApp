package com.doumdoum.nmanel.metronome.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by nmanel on 3/11/2017.
 */

public class Sequences extends Observable implements Cloneable {
    private List<Sequence> sequences;

    public Sequences() {
        sequences = new ArrayList<>();
    }

    public void addSequence(Sequence newSequence) {
        sequences.add(newSequence);
    }

    public void removeSequence(Sequence sequenceToRemove) {
        sequences.remove(sequenceToRemove);
    }

    public Sequences clone() {
        Sequences clone = null;
        try {
            clone = (Sequences) super.clone();
        } catch (Exception e) {

        }
        return clone;

    }

    public List<Sequence> getSequences() {
        return sequences;
    }
}
