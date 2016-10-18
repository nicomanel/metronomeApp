package metronome;

public abstract class Note extends AbstractRythm {


    public Note(int timeValue, int timeDivision) {
        super(timeValue, timeDivision);
    }

    @Override
    public boolean isSilence() {
        return false;
    }
}
