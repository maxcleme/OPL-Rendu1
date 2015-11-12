package iagl.opl.rendu.one.samples;

import org.junit.Assert;
import org.junit.Test;

public class UnusedPrivateFieldsProcessorTest {
    private int b;

    int a;

    @Test
    public void test() {
        int a;
        b = 2;
        a = b;
        Assert.fail("Not yet implemented");
    }
}

