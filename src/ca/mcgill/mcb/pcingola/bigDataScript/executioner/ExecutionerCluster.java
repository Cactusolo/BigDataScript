package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;

/**
 * Execute tasks in a cluster.
 * 
 * All commands are run using 'qsub' (or equivalent) commands
 * 
 * @author pcingola
 */
public class ExecutionerCluster extends ExecutionerSys {

	public static String FAKE_CLUSTER = "";
	// public static String FAKE_CLUSTER = Gpr.HOME + "/workspace/BigDataScript/fakeCluster/";
	public static String CLUSTER_EXEC_COMMAND[] = { FAKE_CLUSTER + "qsub" };
	public static String CLUSTER_KILL_COMMAND = FAKE_CLUSTER + "qdel";
	public static String CLUSTER_BDS_COMMAND = "bds exec ";

	public static final int MIN_EXTRA_TIME = 15;
	public static final int MAX_EXTRA_TIME = 120;

	MonitorExitFile monitorExitFile;

	public ExecutionerCluster(Config config) {
		super(config);
		monitorExitFile = new MonitorExitFile();
	}

	//	@Override
	//	protected void follow(Task task) {
	//		tail.add(clusterStdFile(task.getStdoutFile()), null, false);
	//		tail.add(clusterStdFile(task.getStderrFile()), null, true);
	//	}
	//
	//	/**
	//	 * Usually cluster management systems write STDOUT 
	//	 * & STDERR to files. We don't want the names to 
	//	 * be the same as the one we use, otherwise program's 
	//	 * output may be written twice.
	//	 *   On the other hand, we cannot trust the cluster 
	//	 * system to write those files, because sometimes 
	//	 * they don't do it properly, sometimes they add 
	//	 * headers & footers, sometimes they mixed STDOUT 
	//	 * and STDERR in a single file, etc.
	//	 *   
	//	 * @param fileName
	//	 * @return
	//	 */
	//	String clusterStdFile(String fileName) {
	//		return fileName + ".cluster";
	//	}
	//
	//	@Override
	//	protected CmdLocal createCmd(Task task) {
	//		task.createProgramFile(); // We must create a program file
	//
	//		// Create command line
	//		ArrayList<String> args = new ArrayList<String>();
	//		for (String arg : CLUSTER_EXEC_COMMAND)
	//			args.add(arg);
	//
	//		// Add resources request
	//		HostResources res = task.getResources();
	//		StringBuilder resSb = new StringBuilder();
	//		if (res.getCpus() > 0) resSb.append((resSb.length() > 0 ? "," : "") + "nodes=1:ppn=" + res.getCpus());
	//		if (res.getMem() > 0) resSb.append((resSb.length() > 0 ? "," : "") + "mem=" + res.getMem());
	//
	//		// Timeout 
	//		// We want to assign slightly larger timeout to the cluster (qsub/msub), because 
	//		// we prefer bds to kill the process (it's cleaner and we get exitCode file)
	//		int realTimeout = (int) res.getTimeout();
	//		if (realTimeout < 0) realTimeout = 0;
	//		int extraTime = (int) (realTimeout * 0.1);
	//		if (extraTime < MIN_EXTRA_TIME) extraTime = MIN_EXTRA_TIME;
	//		if (extraTime > MAX_EXTRA_TIME) extraTime = MAX_EXTRA_TIME;
	//		int clusterTimeout = realTimeout + extraTime;
	//
	//		if (realTimeout > 0) resSb.append((resSb.length() > 0 ? "," : "") + "walltime=" + clusterTimeout);
	//
	//		// Any resources requested? Add command line
	//		if (resSb.length() > 0) {
	//			args.add("-l");
	//			args.add(resSb.toString());
	//		}
	//
	//		// Stdout 
	//		args.add("-o");
	//		args.add(clusterStdFile(task.getStdoutFile()));
	//
	//		// Stderr 
	//		args.add("-e");
	//		args.add(clusterStdFile(task.getStderrFile()));
	//
	//		// Show command string
	//		String cmdStr = "";
	//		for (String arg : args)
	//			cmdStr += arg + " ";
	//
	//		// Create command to run (it feed to qsub via stdin)
	//		StringBuilder cmdStdin = new StringBuilder();
	//		cmdStdin.append(CLUSTER_BDS_COMMAND);
	//		cmdStdin.append(realTimeout + " ");
	//		cmdStdin.append("'" + task.getStdoutFile() + "' ");
	//		cmdStdin.append("'" + task.getStderrFile() + "' ");
	//		cmdStdin.append("'" + task.getExitCodeFile() + "' ");
	//		cmdStdin.append("'" + task.getProgramFileName() + "' ");
	//
	//		// Run command
	//		if (debug) Timer.showStdErr("Running command: echo \"" + cmdStdin + "\" | " + cmdStr);
	//
	//		// Create command 
	//		CmdRunnerCluster cmd = new CmdRunnerCluster(task.getId(), args.toArray(CmdLocal.ARGS_ARRAY_TYPE));
	//		cmd.setStdin(cmdStdin.toString());
	//		cmd.setReadPid(true); // We execute using "qsub" which prints a jobID to stdout
	//
	//		return cmd;
	//	}
	//
	//	@Override
	//	public synchronized boolean finished(String id) {
	//		Task task = tasksRunning.get(id);
	//		boolean ok = super.finished(id);
	//
	//		// Make sure 'cluster' files are also removed if we are not logging
	//		if (!log && (task != null)) {
	//			new File(clusterStdFile(task.getStdoutFile())).deleteOnExit();
	//			new File(clusterStdFile(task.getStderrFile())).deleteOnExit();
	//		}
	//
	//		return ok;
	//	}
	//
	//	/**
	//	 * An OS command to kill this task
	//	 * @param task
	//	 * @return
	//	 */
	//	@Override
	//	public String killCommand(Task task) {
	//		return CLUSTER_KILL_COMMAND;
	//	}
	//
	//	@Override
	//	protected boolean runExecutionerLoop() {
	//		// Nothing to do?
	//		if (!hasTaskToRun()) return false;
	//
	//		// Create a new collection to avoid 'concurrent modification error'
	//		ArrayList<Task> run = new ArrayList<Task>();
	//		run.addAll(tasksToRun);
	//
	//		// Run all tasks
	//		for (Task task : run) {
	//
	//			try {
	//				//---
	//				// Run a command to queue script in the cluster (e.g. qsub)
	//				//---
	//				CmdLocal cmd = createCmd(task);
	//				if (debug) Gpr.debug("Waiting for queuing command to finish");
	//				cmd.run();
	//				task.stateStarted(); // Mark as 'started'
	//
	//				// Queued OK?
	//				if (cmd.getExitValue() == 0) {
	//					task.setPid(cmd.getPid());
	//					taskStarted(task);
	//				} else {
	//					taskFailed(task);
	//				}
	//
	//			} catch (Exception e) {
	//				throw new RuntimeException(e);
	//			}
	//		}
	//
	//		return true;
	//	}
	//
	//	/**
	//	 * Clean up after run loop
	//	 */
	//	@Override
	//	protected void runExecutionerLoopAfter() {
	//		super.runExecutionerLoopAfter();
	//		monitorExitFile.kill(); // Kill taskDone process
	//	}
	//
	//	/**
	//	 * Initialize before run loop
	//	 */
	//	@Override
	//	protected void runExecutionerLoopBefore() {
	//		monitorExitFile.start(); // Create a 'taskDone' process (get information when a process finishes)
	//		super.runExecutionerLoopBefore();
	//	}
	//
	//	/**
	//	 * This method is called after a task was successfully started 
	//	 * @param task
	//	 */
	//	@Override
	//	protected void taskStarted(Task task) {
	//		super.taskStarted(task);
	//		monitorExitFile.add(this, task);
	//	}
	//
	//	@Override
	//	public void setDebug(boolean debug) {
	//		super.setDebug(debug);
	//		monitorExitFile.setDebug(debug);
	//	}
	//
}