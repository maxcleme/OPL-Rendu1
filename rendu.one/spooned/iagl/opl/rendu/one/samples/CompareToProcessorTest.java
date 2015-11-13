package iagl.opl.rendu.one.samples;

import org.junit.Test;

public class CompareToProcessorTest {
    private int b;

    private Integer a;

    @Test
    public void test() {
        this.b = 3;
        this.a = 1;
    }

    boolean compareFoo(Integer a, Integer b) {
        this.b = 2;
        a = this.b;
        if ((a.compareTo(b)) == (-1)) {
            a = 2;
        } 
        a = -2;
        boolean yolo = (this.a.compareTo(b)) > 0;
        return (this.b) > 2;
    }

    public void toto(int a) {
    }
}

