package org.bigDataScript.lang.nativeMethods.string;

import org.bigDataScript.data.Data;
import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeMethods.MethodNative;
import org.bigDataScript.run.BdsThread;

public class MethodNative_string_upload_localname extends MethodNative {

	public MethodNative_string_upload_localname() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "upload";
		classType = Type.STRING;
		returnType = Type.BOOL;

		String argNames[] = { "this", "localName" };
		Type argTypes[] = { Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String localName = bdsThread.getString("localName");

		Data data = bdsThread.data(objThis.toString());
		Data localData = bdsThread.data(localName);

		return data.upload(localData.getAbsolutePath());
	}
}