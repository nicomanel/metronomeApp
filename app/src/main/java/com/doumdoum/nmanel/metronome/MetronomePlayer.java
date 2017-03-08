package com.doumdoum.nmanel.metronome;

import android.util.Log;

import com.doumdoum.nmanel.metronome.model.Bar;

import java.util.Observable;
import java.util.Observer;

import static com.doumdoum.nmanel.metronome.DefaultSettings.BUFFER_SIZE;
import static com.doumdoum.nmanel.metronome.DefaultSettings.SAMPLERATE;

/**
 * Created by nico on 23/02/17.
 */

public class MetronomePlayer implements Observer {
    private MetronomePlayerListener listener;
    private AndroidAudioDevice device;
    private Thread tickingThread;

    private int sampleRate;
    private int bufferSize;
    private boolean isPlaying;

    private int tempo;
    private boolean notificationEnabled;


    public MetronomePlayer() {
        listener = new MetronomePlayerListener() {
            @Override
            public void metronomeHasStopped() {

            }

            @Override
            public void tempoHasChanged() {

            }
        };

        tickingThread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        notificationEnabled = true;
        sampleRate = SAMPLERATE;
        bufferSize = BUFFER_SIZE;
        device = new AndroidAudioDevice(sampleRate);
    }


    public void stop(boolean notificationEnabled) {
        Log.i("MetronomePlayer", "STOP INVOKED");
        device.stop();
        isPlaying = false;
        this.notificationEnabled = notificationEnabled;

        try {
            tickingThread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void play(final Bar bar, int tempo, final int timerValue, int tempoIncrement, int measureNumberBeforeIncrement) {
        Log.i("MetronomePlayer", "START INVOKED");
        device.start();
        this.tempo = tempo;
        isPlaying = true;
        final BarGenerator generator = new BarGenerator(tempo, sampleRate, tempoIncrement != 0, tempoIncrement, measureNumberBeforeIncrement, bufferSize);
        generator.addObserver(this);
        tickingThread = new Thread(new Runnable() {
            private int writtenSamplesCounter = 0;

            @Override
            public void run() {
                generator.setBar(bar);

                while (keepWriting()) {
                    short[] newSamples = generator.getSamples();
                    device.writeSamples(newSamples);
                    writtenSamplesCounter += newSamples.length;
                }
                isPlaying = false;
                if (notificationEnabled) {
                    listener.metronomeHasStopped();
                }
                notificationEnabled = true;

            }

            private boolean keepWriting() {
                int timerValueInSamples = (timerValue * SAMPLERATE);
                boolean enabledTimerCondition = (timerValueInSamples > 0) && (writtenSamplesCounter < timerValueInSamples);
                boolean disabledTimerCondition = (timerValueInSamples == 0) && isPlaying();
                return disabledTimerCondition || enabledTimerCondition;
            }
        });
        tickingThread.start();
    }

    public void addStopPlayingListener(MetronomePlayerListener listener) {
        this.listener = listener;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.i("UPDATE", "UPDATe");
        tempo = ((BarGenerator) o).getIncrementedTempo();
        listener.tempoHasChanged();
    }

    public int getTempo() {
        return tempo;
    }
}
