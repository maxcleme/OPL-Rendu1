package iagl.opl.rendu.one;

import spoon.Launcher;

/**
 * Hello world!
 *
 */
public class SonarFixer {

	/*
	 * 
	 * [FIND/DONE] "@Override" annotation should be used on any method
	 * overriding (since Java 5) or implementing (since Java 6) another one
	 * 
	 * TODO [FIND] "switch case" clauses should not have too many lines [FIND]
	 * Unused private fields should be removed [FIND] Strings literals should be
	 * placed on the left side when checking for equality [FIND] Exception types
	 * should not be tested using "instanceof" in catch blocks
	 *
	 * 
	 * Maven dependencies : mvn dependency:build-classpath
	 */
	public static void main(String[] args) throws Exception {
		// COMMONS LANG
		// String[] spoonArgs = { "-i",
		// "C:\\workspace\\OPL_Sample\\commons-lang\\src", "-p",
		// "iagl.opl.rendu.one.processors.SwitchTooManyLinesProcessor",
		// "--source-classpath",
		// "C:\\Users\\RMS\\.m2\\repository\\junit\\junit\\4.12\\junit-4.12.jar;C:\\Users\\RMS\\.m2\\repository\\org\\hamcrest\\hamcrest-core\\1.3\\hamcrest-core-1.3.jar;C:\\Users\\RMS\\.m2\\repository\\org\\hamcrest\\hamcrest-all\\1.3\\hamcrest-all-1.3.jar;C:\\Users\\RMS\\.m2\\repository\\commons-io\\commons-io\\2.4\\commons-io-2.4.jar;C:\\Users\\RMS\\.m2\\repository\\org\\easymock\\easymock\\3.4\\easymock-3.4.jar;C:\\Users\\RMS\\.m2\\repository\\org\\objenesis\\objenesis\\2.2\\objenesis-2.2.jar",
		// "-o", "C:\\workspace\\SPOONED\\commons-lang\\src\\main\\java",
		// "--with-imports", "-x" };
		// LOG4J
		String[] spoonArgs = { "-i", "C:\\workspace\\OPL_Sample\\log4j\\src", "-p",
				"iagl.opl.rendu.one.processors.SwitchTooManyLinesProcessor", "--source-classpath",
				"C:\\Users\\RMS\\.m2\\repository\\javax\\mail\\mail\\1.4.3\\mail-1.4.3.jar;C:\\Users\\RMS\\.m2\\repository\\javax\\activation\\activation\\1.1\\activation-1.1.jar;C:\\Users\\RMS\\.m2\\repository\\org\\apache\\openejb\\javaee-api\\5.0-2\\javaee-api-5.0-2.jar;C:\\Users\\RMS\\.m2\\repository\\oro\\oro\\2.0.8\\oro-2.0.8.jar;C:\\Users\\RMS\\.m2\\repository\\junit\\junit\\3.8.2\\junit-3.8.2.jar;C:\\Users\\RMS\\.m2\\repository\\org\\apache\\geronimo\\specs\\geronimo-jms_1.1_spec\\1.0\\geronimo-jms_1.1_spec-1.0.jar",
				"-o", "C:\\workspace\\SPOONED\\log4j\\src\\main\\java", "--with-imports"};

		// SAMPLES
		// String[] spoonArgs = { "-i",
		// "C:\\workspace\\OPL-Rendu1\\rendu.one\\src\\test\\java\\iagl\\opl\\rendu\\one\\samples",
		// "-p",
		// "iagl.opl.rendu.one.processors.SwitchTooManyLinesProcessor", "-v",
		// "--source-classpath",
		// "C:\\Users\\RMS\\.m2\\repository\\junit\\junit\\4.12\\junit-4.12.jar;C:\\Users\\RMS\\.m2\\repository\\org\\apache\\commons\\commons-lang3\\3.0.1\\commons-lang3-3.0.1.jar;C:\\Users\\RMS\\.m2\\repository\\fr\\inria\\gforge\\spoon\\spoon-core\\4.3.0\\spoon-core-4.3.0.jar;C:\\Users\\RMS\\.m2\\repository\\org\\eclipse\\jdt\\core\\compiler\\ecj\\4.4.2\\ecj-4.4.2.jar;C:\\Users\\RMS\\.m2\\repository\\com\\martiansoftware\\jsap\\2.1\\jsap-2.1.jar;C:\\Users\\RMS\\.m2\\repository\\log4j\\log4j\\1.2.17\\log4j-1.2.17.jar;C:\\Users\\RMS\\.m2\\repository\\commons-io\\commons-io\\1.3.2\\commons-io-1.3.2.jar;C:\\Users\\RMS\\.m2\\repository\\junit\\junit\\3.8.1\\junit-3.8.1.jar;C:\\Users\\RMS\\.m2\\repository\\com\\google\\guava\\guava\\18.0\\guava-18.0.jar",
		// };

		// LAUNCHER
		Launcher.main(spoonArgs);
	}
}
