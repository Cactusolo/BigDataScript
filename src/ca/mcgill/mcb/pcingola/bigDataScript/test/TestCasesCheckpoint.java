package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.io.File;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class TestCasesCheckpoint extends TestCasesBase {

	public static boolean debug = false;

	@Test
	public void test01() {
		runAndCheckpoint("test/checkpoint_01.bds", null, "i", "10");
	}

	@Test
	public void test02() {
		runAndCheckpoint("test/checkpoint_02.bds", null, "l", "15");
	}

	@Test
	public void test03() {
		runAndCheckpoint("test/checkpoint_03.bds", null, "s2", "After checkpoint 42");
	}

	@Test
	public void test04() {
		runAndCheckpoint("test/checkpoint_04.bds", null, "s", "one\teins");
	}

	@Test
	public void test04_2() {
		runAndCheckpoint("test/checkpoint_04.bds", null, "i", "3");
	}

	@Test
	public void test04_3() {
		runAndCheckpoint("test/checkpoint_04.bds", null, "ss", "one\ttwo");
	}

	@Test
	public void test05() {
		runAndCheckpoint("test/checkpoint_05.bds", null, "l0", "ONE");
	}

	/**
	 * How this test works:
	 * 		1) Try to delete a file that doesn't exits. Task fails, checkpoint is created and program finishes
	 * 		2) createFile is run: This creates the file to be deleted
	 * 		3) Checkpoint recovery, the task is re-executed. This time the file exists, so it runs OK. Variable 'b' is set to true
	 */
	@Test
	public void test06() {
		final String fileToDelete = "test/checkpoint_06.tmp";

		Runnable createFile = new Runnable() {

			@Override
			public void run() {
				// Create the file
				Gpr.debug("Creating file: '" + fileToDelete + "'");
				Gpr.toFile(fileToDelete, "Hello");
			}
		};

		// Make sure that the file doesn't exits
		(new File(fileToDelete)).delete();

		// Run test
		runAndCheckpoint("test/checkpoint_06.bds", "test/checkpoint_06.bds.line_8.chp", "b", "true", createFile);
	}

	@Test
	public void test07() {
		runAndCheckpoint("test/checkpoint_07.bds", null, "sloop", "three");
	}

	@Test
	public void test08() {
		// Remove old entries
		String prefix = "test/checkpoint_08";
		File txt = new File(prefix + ".txt");
		final File csv = new File(prefix + ".csv");
		final File xml = new File(prefix + ".xml");
		txt.delete();
		csv.delete();
		xml.delete();

		// Create file
		Gpr.toFile(prefix + ".txt", "TEST");

		// Run this code before checkpoint recovery
		Runnable runBeforeRecovery = new Runnable() {

			@Override
			public void run() {
				// Create the file
				Gpr.debug("Deleting files: " + csv + " and " + xml);
				csv.delete();
				xml.delete();
			}
		};

		// Run pipeline and test checkpoint
		runAndCheckpoint(prefix + ".bds", prefix + ".chp", "num", "2", runBeforeRecovery);
	}

	@Test
	public void test09() {
		// Remove old entries
		String prefix = "test/checkpoint_09";
		File txt = new File(prefix + ".txt");
		final File csv = new File(prefix + ".csv");
		final File xml = new File(prefix + ".xml");
		txt.delete();
		csv.delete();
		xml.delete();

		// Create file
		Gpr.toFile(prefix + ".txt", "TEST");

		// Run this code before checkpoint recovery
		Runnable runBeforeRecovery = new Runnable() {

			@Override
			public void run() {
				// Create the file
				Gpr.debug("Deleting file: " + csv);
				csv.delete();
			}
		};

		// Run pipeline and test checkpoint
		runAndCheckpoint(prefix + ".bds", prefix + ".chp", "num", "0", runBeforeRecovery);
	}

	@Test
	public void test10() {
		// Run pipeline and test checkpoint
		runAndCheckpoint("test/checkpoint_10.bds", "test/checkpoint_10.chp", "sumMain", "55");
	}

	@Test
	public void test11() {
		// Run pipeline and test checkpoint
		runAndCheckpoint("test/checkpoint_11.bds", "test/checkpoint_11.chp", "sumPar", "110");
	}

}