package iagl.opl.rendu.one.samples;


public class CompareToProcessorTest {
    @org.junit.Test
    public void test() {
    }

    void compareFoo(java.lang.Integer a, java.lang.Integer b) {
        if ((a.compareTo(b)) == (-1)) {
            a = 2;
        } 
        a = -2;
    }
}

