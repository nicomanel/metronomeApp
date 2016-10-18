package metronome.test;

import metronome.BarTick;
import metronome.rythms.EigthNote;
import metronome.rythms.QuarterNote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by nico on 17/10/2016 for ${PROJET_NAME}
 */
public class BarTickTest {
    private BarTick barTickTobeTested;

    @Test
    public void isCompleteWithNoRythm() throws Exception {
        Assert.assertEquals(barTickTobeTested.isComplete(), false);
    }

    @Test
    public void isCompleteWith1QuarterNote()
    {
        barTickTobeTested.addRythm(new QuarterNote());
        Assert.assertEquals(barTickTobeTested.isComplete(), true);
    }

    @Test
    public void isCompleteWith2QuarterNote()
    {
        barTickTobeTested.addRythm(new EigthNote());
        barTickTobeTested.addRythm(new QuarterNote());
        Assert.assertFalse(barTickTobeTested.isComplete());
    }

    @Test
    public void isCompleteWith1EigthNote()
    {
        barTickTobeTested.addRythm(new EigthNote());
        Assert.assertEquals(barTickTobeTested.isComplete(), false);
    }

    @Test
    public void addRythmWhenAlreadyComplete() throws Exception {
        barTickTobeTested.addRythm(new QuarterNote());
        Assert.assertFalse(barTickTobeTested.addRythm(new QuarterNote()));
    }

    @Test
    public void addRythmWhenNotComplete() throws Exception {
        Assert.assertTrue(barTickTobeTested.addRythm(new QuarterNote()));
    }

    @Test
    public void computeCommonDiviser() throws Exception {
        barTickTobeTested.addRythm(new EigthNote());
        Assert.assertEquals(barTickTobeTested.computeCommonDiviser(), 2);
        barTickTobeTested.addRythm(new QuarterNote());
        Assert.assertEquals(barTickTobeTested.computeCommonDiviser(), 2);
    }

    @Before
    public void setUp() throws Exception {
        barTickTobeTested = new BarTick();
    }
}