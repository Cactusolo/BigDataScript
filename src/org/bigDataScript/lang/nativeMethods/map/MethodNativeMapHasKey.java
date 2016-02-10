package org.bigDataScript.lang.nativeMethods.map;

import java.util.HashMap;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeMap;
import org.bigDataScript.run.BdsThread;

/**
 * Return a list of keys
 * 
 * @author pcingola
 */
public class MethodNativeMapHasKey extends MethodNativeMap {

	public MethodNativeMapHasKey(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "hasKey";
		classType = TypeMap.get(baseType);
		returnType = Type.BOOL;

		String argNames[] = { "this", "key" };
		Type argTypes[] = { classType, Type.STRING }; // null: don't check argument (anything can be converted to 'string')
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		HashMap map = (HashMap) objThis;
		String key = csThread.getObject("key").toString();
		return map.containsKey(key);
	}
}