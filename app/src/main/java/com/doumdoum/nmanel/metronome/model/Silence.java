package com.doumdoum.nmanel.metronome.model;

public abstract class Silence extends AbstractRythm{

    public Silence(int timeValue, int timeDivision) {
        super(timeValue, timeDivision);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean isSilence() {
        return true;
    }

}
