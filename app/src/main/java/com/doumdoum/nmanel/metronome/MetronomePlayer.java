package com.doumdoum.nmanel.metronome;

import com.doumdoum.nmanel.metronome.model.Bar;

import static com.doumdoum.nmanel.metronome.DefaultSettings.BUFFER_SIZE;
import static com.doumdoum.nmanel.metronome.DefaultSettings.SAMPLERATE;

/**
 * Created by nico on 23/02/17.
 */

public class MetronomePlayer {
    private StopPlayingListener listener;
    private AndroidAudioDevice device;
    private Thread tickingThread;

    private int sampleRate;
    private int bufferSize;


    public MetronomePlayer() {
        listener = new StopPlayingListener() {
            @Override
            public void hasStopped() {

            }
        };

        tickingThread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });

        sampleRate = SAMPLERATE;
        bufferSize = BUFFER_SIZE;
        device = new AndroidAudioDevice(sampleRate);
    }


    public void stop() {
        if (tickingThread.isAlive())
            tickingThread.interrupt();
        device.stop();
        listener.hasStopped();
    }

    public void play(final Bar bar, int tempo, final int timerValue, int tempoIncrement, int measureNumberBeforeIncrement) {
        device.start();
        final BarGenerator generator = new BarGenerator(tempo, sampleRate, tempoIncrement == 0, tempoIncrement, measureNumberBeforeIncrement, bufferSize);
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

                stop();
            }

            private boolean keepWriting() {
                int timerValueInSamples = (timerValue * SAMPLERATE);
                return (timerValueInSamples == 0) || ((timerValueInSamples > 0) && writtenSamplesCounter < timerValueInSamples);
            }
        });
        tickingThread.start();
    }

    public void addStopPlayingListener(StopPlayingListener listener) {
        this.listener = listener;
    }


}
