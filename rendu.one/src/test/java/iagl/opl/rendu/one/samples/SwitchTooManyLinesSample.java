package iagl.opl.rendu.one.samples;

import java.io.IOException;
import java.util.Random;

public class SwitchTooManyLinesSample {

	private static final int CONSTANT = 3;

	public int foo(int a, int b) {
		int random = new Random(System.currentTimeMillis()).nextInt(3);

		switch (random) {
		case 1:
			b += CONSTANT;
			break;
		case 2:
			a = random;
			a += b * random;
			for (int i = 0; i < random; i++) {
				b *= i;
				b += i * CONSTANT;
			}
		case 3:
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

	public int bar(int a, int b) throws IOException {
		int random = new Random(System.currentTimeMillis()).nextInt(3);

		switch (random + "") {
		case "1":
			if (a == 2) {
				b++;
			}
			b++;
			b++;
			b++;
			return b;
		case "2":
			a = random;
			a += b * random;
			for (int i = 0; i < random; i++) {
				b *= i;
				b += i * CONSTANT;
			}
			if ( a > 10 ){
				throw new RuntimeException("trolololololo");
			}else{
				throw new IOException("toto");
			}
		case "3":
			b += CONSTANT;
			break;
		}
		return a + b;
	}

}
