package org.bigDataScript.lang.nativeMethods.string;

import org.bigDataScript.data.Data;
import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;
import org.bigDataScript.lang.nativeMethods.MethodNative;
import org.bigDataScript.run.BdsThread;
import org.bigDataScript.util.Gpr;

public class MethodNative_string_readLines extends MethodNative {
	public MethodNative_string_readLines() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "readLines";
		classType = Type.STRING;
		returnType = TypeList.get(Type.STRING);

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		// Download data if necessary
		Data data = bdsThread.data(objThis.toString());

		// Download remote file
		if (data.isRemote() //
				&& !data.isDownloaded() //
				&& !data.download() //
				) return ""; // Download error

		return array2list(Gpr.readFile(data.getLocalPath(), false).split("\n"));
	}
}