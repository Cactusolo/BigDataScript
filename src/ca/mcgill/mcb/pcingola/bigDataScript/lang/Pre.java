package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * Pre increment / decrement operator
 *
 * E.g. :  --i or ++i
 *
 * @author pcingola
 */
public class Pre extends ExpressionUnary {

	PrePostOperation operation;

	public Pre(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		operation = PrePostOperation.parse(tree.getChild(0).getText());

		BigDataScriptNode node = factory(tree, 1);
		if ((node instanceof Reference)) expr = (Expression) node;
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		Reference ref = (Reference) expr;
		bdsThread.run(ref);
		long value = popInt(bdsThread);

		if (operation == PrePostOperation.INCREMENT) value++;
		else if (operation == PrePostOperation.DECREMENT) value--;
		else throw new RuntimeException("Unknown operator " + operation);

		ref.setValue(bdsThread, value);
		bdsThread.push(value);
	}

	@Override
	protected void sanityCheck(CompilerMessages compilerMessages) {
		if (!(expr instanceof Reference)) compilerMessages.add(this, "Only variable reference can be used with ++ or -- operators", MessageType.ERROR);
	}

	@Override
	public String toString() {
		return expr.toString() + operation.toStringCode();
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		if (!expr.isInt()) compilerMessages.add(this, "Only int variables can be used with ++ or -- operators", MessageType.ERROR);
	}

}
