package iagl.opl.rendu.one.samples;

import static org.junit.Assert.fail;

import org.junit.Test;

public class UnusedPrivateFieldsProcessorTest {
  private int b;
  int a;

  @Test
  public void test() {
    int a;
    b = 2;
    a = b;
    fail("Not yet implemented");
  }

}
