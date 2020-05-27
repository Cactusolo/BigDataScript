package org.bds.test;

import java.util.ArrayList;
import java.util.List;

import org.bds.util.Gpr;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Test cases Classes / Objects
 *
 * @author pcingola
 *
 */
public class TestCasesTaskImproper extends TestCasesBase {

	/**
	 * Execute a task: Local computer
	 */
	@Test
	public void test01() {
		Gpr.debug("Test");
		runAndCheck("test/run_task_improper_01.bds", "a", "42");
	}

	/**
	 * Execute a task: Capture exit command, stdout, stderr
	 */
	@Test
	public void test02() {
		Gpr.debug("Test");
		verbose = true;
		List<String> expected = new ArrayList<>();
		expected.add("Task improper: Before, a=42");
		expected.add("Task improper: Start, a=42");
		expected.add("Task improper: End, a=1");
		expected.add("Task improper: After, a=42");
		runAndCheckStdout("test/run_task_improper_02.bds", expected, null, false);
	}

	/**
	 * Execute two tasks: Make sure execution ends at task end and environment is inherited
	 */
	@Test
	public void test03() {
		Gpr.debug("Test");

		List<String> expected = new ArrayList<>();
		expected.add("Task improper: Before, a=42");
		for (int i = 0; i < 3; i++) {
			expected.add("Task improper: Start, a=42, i=" + i);
			expected.add("Task improper: End, a=" + (42 + i) + ", i=" + i);
		}
		expected.add("Task improper: After, a=42");

		String stdout = runAndCheckStdout("test/run_task_improper_03.bds", expected, null, false);
		Assert.assertTrue("Should finish with a 'Done' message", stdout.endsWith("Done\n"));
	}

	//	/**
	//	 * Execute a task with remote files
	//	 */
	//	@Test
	//	public void test04() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_task_improper_04.bds", "a", "42");
	//	}
	//
	//	/**
	//	 * Execute a task having an assignment sys instead of a bare sys
	//	 * 	task(...) {
	//	 * 		out = sys cat z.txt
	//	 * 	}
	//	 */
	//	@Test
	//	public void test05() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_task_improper_05.bds", "a", "42");
	//	}
	//
	//	/**
	//	 * Execute a task having an assignment sys instead of a bare sys
	//	 * 	task(...) {
	//	 * 		# These two sys should not be merged
	//	 * 		out = sys cat z.txt		# This is compiled to a 'sys' opcode
	//	 * 		sys cat z.txt			# This is compiled to a 'shell' opcode
	//	 * 	}
	//	 */
	//	@Test
	//	public void test06() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_task_improper_06.bds", "a", "42");
	//	}

}
