package iagl.opl.rendu.one.samples;


public class SwitchTooManyLinesSample {
    private static final int CONSTANT = 3;

    public boolean foo(int a, int b) {
        int random = new java.util.Random(java.lang.System.currentTimeMillis()).nextInt(3);
        java.lang.String str = "toto";
        switch (random) {
            case 1 :
                b += CONSTANT;
                break;
            case 2 :
                foo_case_19696495(a, random, b);
            case 3 :
                foo_case_19696526(a, b);
            case 4 :
                foo_case_19696557(str);
                break;
        }
        return (a + b) > 0;
    }

    public int bar(int a, int b) throws java.io.IOException {
        int random = new java.util.Random(java.lang.System.currentTimeMillis()).nextInt(3);
        switch (random + "") {
            case "1" :
                bar_case_1747994638(a, b);
            case "2" :
                bar_case_1747995599(a, random, b);
            case "3" :
                b += CONSTANT;
                break;
        }
        return a + b;
    }

    private int bar_case_1747994638(int a, int b) {
        if (a == 2) {
            b++;
        } 
        b++;
        b++;
        b++;
        return b;
    }

    private java.lang.Boolean foo_case_19696557(java.lang.String str) {
        final char ch0 = str.charAt(0);
        if ((((ch0 == 'y') || (ch0 == 'Y')) || (ch0 == 't')) || (ch0 == 'T')) {
            return java.lang.Boolean.TRUE;
        } 
        if ((((ch0 == 'n') || (ch0 == 'N')) || (ch0 == 'f')) || (ch0 == 'F')) {
            return java.lang.Boolean.FALSE;
        } 
        break;
    }

    private void bar_case_1747995599(int a, int random, int b) throws java.io.IOException, java.lang.RuntimeException {
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

    private void foo_case_19696495(int a, int random, int b) {
        a = random;
        a += b * random;
        for (int i = 0 ; i < random ; i++) {
            b *= i;
            b += i * (CONSTANT);
        }
    }

    private void foo_case_19696526(int a, int b) {
        if (a == 2) {
            b++;
            break;
        } 
        b++;
        b++;
        b++;
        break;
    }
}

