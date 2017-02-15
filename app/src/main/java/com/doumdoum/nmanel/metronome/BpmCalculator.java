package com.doumdoum.nmanel.metronome;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nmanel on 2/15/2017.
 */

public class BpmCalculator {
    private List<Long> taps;
    private long firstTap;
    private int timeOutInMs;
    private int tapsLength;
    private long lastTime;

    public BpmCalculator(int timeOutInMs) {
        taps = new ArrayList<Long>();
        this.tapsLength = 4;
        this.timeOutInMs = timeOutInMs;
        firstTap = -1;
    }

    public int tap(long time) {
        int bpm = 0;
        if (firstTap < 0) {
            firstTap = time;
            lastTime = time;
        }
        if ((time - lastTime) > timeOutInMs || taps.size() > tapsLength) {
            Log.i("tap", "tap");
            taps.clear();
            firstTap = time;
        }
        taps.add(new Long(time));
        lastTime = time;
        if (taps.size() >= 2)
            bpm = computeBpm();
        return bpm;
    }

    private int computeBpm() {
        int[] tempi = new int[taps.size() - 1];
        int resultBpm = 0;
        for (int tapIndex = 1; tapIndex < taps.size(); tapIndex++) {
            long delta = taps.get(tapIndex) - taps.get(tapIndex - 1);
            int tempo = (int) (60000 / delta);
            tempi[tapIndex - 1] = tempo;
        }

        for (int tempo : tempi) {
            resultBpm += tempo;
        }
        return resultBpm / tempi.length;
    }
}
