package com.doumdoum.nmanel.metronome.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nmanel on 1/25/2017.
 */

public class Bars {
    private List<Bar> bars;

    public Bars() {
        bars = new ArrayList<>();
    }

    public List<Bar> getBars() {
        return bars;
    }

    public void addBar(Bar bar) {
        bars.add(bar);
    }

    public void removeBar(Bar bar) {
        bars.remove(bar);
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
}
