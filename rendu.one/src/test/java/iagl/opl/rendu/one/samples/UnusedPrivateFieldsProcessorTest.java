package iagl.opl.rendu.one.samples;

import static org.junit.Assert.fail;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

public class UnusedPrivateFieldsProcessorTest {
  private int b;
  private int a;
  private static final int[][] fields = new int[][] {new int[] {Calendar.MILLISECOND}, new int[] {Calendar.SECOND}, new int[] {Calendar.MINUTE},
    new int[] {Calendar.HOUR_OF_DAY, Calendar.HOUR}, new int[] {Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM}, new int[] {Calendar.MONTH, DateUtils.SEMI_MONTH},
    new int[] {Calendar.YEAR}, new int[] {Calendar.ERA}};
  private static final int MONTE_CARLO_RUNS = 4;
  private int startRow;

  private final double[][] circlePoints = new double[][] {
    {-0.312967, 0.072366}, {-0.339248, 0.132965}, {-0.379780, 0.202724},
    {-0.390426, 0.260487}, {-0.361212, 0.328325}, {-0.346039, 0.392619},
    {-0.280579, 0.444306}, {-0.216035, 0.470009}, {-0.149127, 0.493832},
    {-0.075133, 0.483271}, {-0.007759, 0.452680}, {0.060071, 0.410235},
    {0.103037, 0.341076}, {0.118438, 0.273884}, {0.131293, 0.192201},
    {0.115869, 0.129797}, {0.072223, 0.058396}, {0.022884, 0.000718},
    {-0.053355, -0.020405}, {-0.123584, -0.032451}, {-0.216248, -0.032862},
    {-0.278592, -0.005008}, {-0.337655, 0.056658}, {-0.385899, 0.112526},
    {-0.405517, 0.186957}, {-0.415374, 0.262071}, {-0.387482, 0.343398},
    {-0.347322, 0.397943}, {-0.287623, 0.458425}, {-0.223502, 0.475513},
    {-0.135352, 0.478186}, {-0.061221, 0.483371}, {0.003711, 0.422737},
    {0.065054, 0.375830}, {0.108108, 0.297099}, {0.123882, 0.222850},
    {0.117729, 0.134382}, {0.085195, 0.056820}, {0.029800, -0.019138},
    {-0.027520, -0.072374}, {-0.102268, -0.091555}, {-0.200299, -0.106578},
    {-0.292731, -0.091473}, {-0.356288, -0.051108}, {-0.420561, 0.014926},
    {-0.471036, 0.074716}, {-0.488638, 0.182508}, {-0.485990, 0.254068},
    {-0.463943, 0.338438}, {-0.406453, 0.404704}, {-0.334287, 0.466119},
    {-0.254244, 0.503188}, {-0.161548, 0.495769}, {-0.075733, 0.495560},
    {0.001375, 0.434937}, {0.082787, 0.385806}, {0.115490, 0.323807},
    {0.141089, 0.223450}, {0.138693, 0.131703}, {0.126415, 0.049174},
    {0.066518, -0.010217}, {-0.005184, -0.070647}, {-0.080985, -0.103635},
    {-0.177377, -0.116887}, {-0.260628, -0.100258}, {-0.335756, -0.056251},
    {-0.405195, -0.000895}, {-0.444937, 0.085456}, {-0.484357, 0.175597},
    {-0.472453, 0.248681}, {-0.438580, 0.347463}, {-0.402304, 0.422428},
    {-0.326777, 0.479438}, {-0.247797, 0.505581}, {-0.152676, 0.519380},
    {-0.071754, 0.516264}, {0.015942, 0.472802}, {0.076608, 0.419077},
    {0.127673, 0.330264}, {0.159951, 0.262150}, {0.153530, 0.172681},
    {0.140653, 0.089229}, {0.078666, 0.024981}, {0.023807, -0.037022},
    {-0.048837, -0.077056}, {-0.127729, -0.075338}, {-0.221271, -0.067526}
  };

  public void start(final int rows, final int columns,
    final int startRow) {
    fields[2 - this.startRow][0] = 3;

    double[][] points = circlePoints;
    double[] weights = new double[points.length];
  }

  @Test
  public void test() {
    int a;
    final int mcRepeat = MONTE_CARLO_RUNS;
    b = 2;
    this.a = b;
    for (final int[] aField : fields) {

    }
    fail("Not yet implemented");
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
