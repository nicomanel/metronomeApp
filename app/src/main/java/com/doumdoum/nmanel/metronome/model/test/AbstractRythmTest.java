package metronome.test;


import metronome.AbstractRythm;


import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created by nico on 17/10/2016.
 */
public class AbstractRythmTest {


    @Test
    public void getTimeDivision() throws Exception {
        AbstractRythm rythm = getAbstractRythm();
        Assert.assertEquals(rythm.getTimeDivision(), 1);

    }

    @Test
    public void getTimeValue() throws Exception {
        AbstractRythm rythm = getAbstractRythm();
        Assert.assertEquals(rythm.getTimeValue(), 1);
    }

    @Test
    public void isSilence() throws Exception {
        Assert.assertEquals(getAbstractRythm().isSilence(), false);
    }



    private AbstractRythm getAbstractRythm() {
        return new AbstractRythm(1, 1) {
            @Override
            public boolean isSilence() {
                return false;
            }
        };
    }
}