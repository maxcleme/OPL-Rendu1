package iagl.opl.rendu.one.samples;

import org.junit.Test;

public class CompareToProcessorTest {
    private int b;

    @Test
    public void test() {
        b = 3;
    }

    boolean compareFoo(Integer a, Integer b) {
        this.b = 2;
        a = this.b;
        if ((a.compareTo(b)) == (-1)) {
            a = 2;
        } 
        a = -2;
        return (a.compareTo(b)) == 1;
    }
}

