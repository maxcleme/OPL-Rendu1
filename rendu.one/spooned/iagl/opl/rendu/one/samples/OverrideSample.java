package iagl.opl.rendu.one.samples;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;

public class OverrideSample {
    public void toto() {
    }

    public void tata() {
    }

    public boolean equals(OverrideSample s) {
        return true;
    }

    private class Foo {
        public void foo() {
            new Comparator<String>() {
                public int compare(String o1, String o2) {
                    return 0;
                }
            };
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                }
            };
        }
    }

    public boolean equals(String s) {
        return false;
    }
}

