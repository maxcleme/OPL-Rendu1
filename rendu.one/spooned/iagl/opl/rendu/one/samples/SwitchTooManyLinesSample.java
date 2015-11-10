package iagl.opl.rendu.one.samples;


public class SwitchTooManyLinesSample {
    private static final int CONSTANT = 3;

    public int foo(int a, int b) {
        int random = new java.util.Random(java.lang.System.currentTimeMillis()).nextInt(3);
        switch (random) {
            case 1 :
                b += CONSTANT;
                break;
            case 2 :
                a = random;
                a += b * random;
                for (int i = 0 ; i < random ; i++) {
                    b *= i;
                    b += i * (CONSTANT);
                }
            case 3 :
                if (a == 2) {
                    b++;
                } 
                b++;
                b++;
                b++;
                return b;
        }
        return a + b;
    }

    public int bar(int a, int b) throws java.io.IOException {
        int random = new java.util.Random(java.lang.System.currentTimeMillis()).nextInt(3);
        switch (random + "") {
            case "1" :
                if (a == 2) {
                    b++;
                } 
                b++;
                b++;
                b++;
                return b;
            case "2" :
                a = random;
                a += b * random;
                for (int i = 0 ; i < random ; i++) {
                    b *= i;
                    b += i * (CONSTANT);
                }
                if (a > 10) {
                    throw new java.lang.RuntimeException("trolololololo");
                } else {
                    throw new java.io.IOException("toto");
                }
            case "3" :
                b += CONSTANT;
                break;
        }
        return a + b;
    }

    private int bar_case_1747994638() {
        if (a == 2) {
            b++;
        } 
        b++;
        b++;
        b++;
        return b;
    }

    private int foo_case_19696526() {
        if (a == 2) {
            b++;
        } 
        b++;
        b++;
        b++;
        return b;
    }

    private void bar_case_1747995599() throws java.io.IOException, java.lang.RuntimeException {
        a = random;
        a += b * random;
        for (int i = 0 ; i < random ; i++) {
            b *= i;
            b += i * (CONSTANT);
        }
        if (a > 10) {
            throw new java.lang.RuntimeException("trolololololo");
        } else {
            throw new java.io.IOException("toto");
        }
    }

    private void foo_case_19696495() {
        a = random;
        a += b * random;
        for (int i = 0 ; i < random ; i++) {
            b *= i;
            b += i * (CONSTANT);
        }
    }
}

