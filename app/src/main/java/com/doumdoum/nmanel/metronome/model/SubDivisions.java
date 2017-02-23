package com.doumdoum.nmanel.metronome.model;

/**
 * Created by nico on 22/02/17.
 */

public class SubDivisions {
    private boolean[] subDivisions;

    public SubDivisions(int subDivisionNumber)
    {
        setSubDivisionNumber(subDivisionNumber);
    }

    public void setSubDivisionNumber(int newSubDivisionNumber)
    {
        subDivisions = new boolean[newSubDivisionNumber];
        for(int subDivisionIndex = 0; subDivisionIndex < newSubDivisionNumber; subDivisionIndex++)
        {
            subDivisions[subDivisionIndex] = true;
        }
    }

    public boolean setSilentSubDivisions(int subDivisionIndex)
    {
        return setSubDivisionsState(subDivisionIndex, false);
    }

    public boolean setPlayedSubDivisions(int subDivisionIndex)
    {
        return setSubDivisionsState(subDivisionIndex, true);
    }

    private boolean setSubDivisionsState(int subDivisionIndex, boolean subDivisionValue) {
        if(subDivisionIndex > subDivisions.length)
            return false;
        if (subDivisionIndex >= 0)
            return false;

        subDivisions[subDivisionIndex] = subDivisionValue;
        return true;
    }


}
