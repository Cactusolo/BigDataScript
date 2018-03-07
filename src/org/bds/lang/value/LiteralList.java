package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;

/**
 * Expression 'Literal'
 *
 * @author pcingola
 */
public class LiteralList extends Literal {

	Expression values[];

	public LiteralList(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public Type getElementType() {
		return ((TypeList) returnType).getElementType();
	}

	@Override
	protected void parse(ParseTree tree) {
		int listSize = (tree.getChildCount() - 1) / 2;
		values = new Expression[listSize];
		for (int i = 1, j = 0; j < listSize; i += 2, j++) { // Skip first '[' and comma separators
			values[j] = (Expression) factory(tree, i);
		}
	}

	@Override
	protected Object parseValue(ParseTree tree) {
		throw new RuntimeException("UNIMPLEMENTED!");
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		//---
		// Calculate elementType
		//---
		Type baseType = null;
		for (BdsNode node : values) {
			Expression expr = (Expression) node;
			Type typeExpr = expr.returnType(scope);

			if (typeExpr != null) {
				if (baseType == null) {
					baseType = typeExpr;
				} else if (!typeExpr.canCastTo(baseType)) { // Can we cast ?
					if (baseType.canCastTo(typeExpr)) { // Can we cast the other way?
						baseType = typeExpr;
					} else {
						// We have a problem...we'll report it in typeCheck.
					}
				}
			}
		}

		// Default base type if nothing found
		if (baseType == null) baseType = Types.VOID;

		// Create a list of 'elementType'
		returnType = TypeList.get(baseType);

		return returnType;
	}

	@Override
	public void runStep(BdsThread bdsThread) {
		Type elemType = getElementType();
		ValueList vlist = new ValueList(returnType, values.length);

		// Add all elements
		for (BdsNode node : values) {
			Expression expr = (Expression) node;
			bdsThread.run(expr); // Evaluate expression

			value = bdsThread.pop();
			vlist.add(elemType.cast(value));
		}

		bdsThread.push(vlist);
	}

	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		for (BdsNode csnode : values)
			if (!(csnode instanceof Expression)) compilerMessages.add(csnode, "Expecting expression instead of " + csnode.getClass().getSimpleName(), MessageType.ERROR);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");

		for (int i = 0; i < values.length; i++) {
			if (i > 0) sb.append(" , ");
			sb.append(values[i]);
		}

		sb.append(" ]");

		return sb.toString();
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		Type baseType = ((TypeList) returnType).getElementType();

		for (BdsNode node : values) {
			Expression expr = (Expression) node;
			Type typeExpr = expr.returnType(scope);

			// Can we cast ?
			if ((typeExpr != null) && !typeExpr.canCastTo(baseType)) {
				compilerMessages.add(this, "List types are not consistent. Expecting " + baseType, MessageType.ERROR);
			}
		}
	}

}