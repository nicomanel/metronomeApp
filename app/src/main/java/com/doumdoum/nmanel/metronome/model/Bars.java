package com.doumdoum.nmanel.metronome.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by nmanel on 1/25/2017.
 */

public class Bars extends Observable implements Cloneable{
    private List<Bar> bars;

    public Bars() {
        bars = new ArrayList<>();
    }

    public List<Bar> getBars() {
        return bars;
    }

    public void addBar(Bar bar) {
        bars.add(bar);
        setChanged();
    }

    public void removeBar(Bar bar) {
        bars.remove(bar);
        setChanged();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Bars : ");
        boolean first = true;
        for (Bar bar : bars) {
            if (first)
                buffer.append(bar.getName());
            else
                buffer.append(", ").append(bar.getName());
            first = false;
        }
        return buffer.toString();
    }

    public Bar getBarFromName(String name)
    {
        for(Bar bar : bars)
        {
            if(bar.getName().equals(name))
            {
                return bar;
            }
        }
        return null;
    }

    public Bars clone() {
        Bars clonedBars = null;
        try {
            clonedBars = (Bars) super.clone();
        } catch (CloneNotSupportedException exception) {
            Log.e("Bars", "Bar not cloneable");
        }

        return clonedBars;
    }
}
