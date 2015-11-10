package iagl.opl.rendu.one.samples;

import org.junit.Test;

public class CompareToProcessorTest {
    @Test
    public void test() {
    }

    void compareFoo(Integer a, Integer b) {
        if ((a.compareTo(b)) == (-1)) {
            a = 2;
        } 
        a = -2;
    }
}

