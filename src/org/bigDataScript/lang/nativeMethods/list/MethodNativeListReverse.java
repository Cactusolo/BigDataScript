package org.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;
import java.util.Collections;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;
import org.bigDataScript.run.BdsThread;

/**
 * Sort: Create a new list and reverse it
 * 
 * @author pcingola
 */
public class MethodNativeListReverse extends MethodNativeList {

	public MethodNativeListReverse(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "reverse";
		classType = TypeList.get(baseType);
		returnType = TypeList.get(baseType);

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;

		// Empty list? => Nothing to do
		if (list.size() <= 0) return new ArrayList();

		// Create new list and sort it
		ArrayList newList = new ArrayList(list.size());
		newList.addAll(list);
		Collections.reverse(newList);

		return newList;
	}
}