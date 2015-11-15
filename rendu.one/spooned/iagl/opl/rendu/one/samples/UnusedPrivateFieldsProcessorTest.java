package iagl.opl.rendu.one.samples;

import org.junit.Assert;
import org.junit.Test;

public class UnusedPrivateFieldsProcessorTest {
    private int b;

    private int a;

    @Test
    public void test() {
        int a;
        b = 2;
        this.a = b;
        Assert.fail("Not yet implemented");
    }

    public int getB() {
        return this.b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getA() {
        return this.a;
    }

    public void setA(int a) {
        this.a = a;
    }
}

