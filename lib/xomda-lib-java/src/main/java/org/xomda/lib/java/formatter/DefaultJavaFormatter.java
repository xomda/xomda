package org.xomda.lib.java.formatter;

import java.io.IOException;
import java.util.Stack;

import org.xomda.lib.java.ast.Block;
import org.xomda.lib.java.ast.Class;
import org.xomda.lib.java.ast.Constructor;
import org.xomda.lib.java.ast.Field;
import org.xomda.lib.java.ast.Import;
import org.xomda.lib.java.ast.Method;

public class DefaultJavaFormatter implements JavaFormatter {

	private static final char NEW_LINE = '\n';
	private static final String DEFAULT_TAB = "\t";
	private String tabCharacter = DEFAULT_TAB;

	private final int nextIndent = 0;
	private int indent = 0;

	private final Appendable appendable;

	private final Stack<Object> queue;

	private Object last;

	public Object peek(int count) {
		return queue.size() > -count
				? queue.get(count + queue.size() - 1)
				: null;
	}

	public Object peek() {
		return peek(0);
	}

	public DefaultJavaFormatter(Appendable appendable) {
		this.queue = new Stack<>();
		this.appendable = appendable;
	}

	private <T> boolean isIndent(T obj) {
		Object peek = peek(-1);
		return (obj instanceof String && peek instanceof Block)
				|| obj instanceof Method
				|| peek instanceof Class
				;
	}

	private <T> boolean isNewLineBefore(T obj) {
		return obj instanceof Class
				|| obj instanceof Import
				// class internals
				|| obj instanceof Constructor
				|| obj instanceof Method
				|| obj instanceof Field

				|| obj instanceof String && peek(-1) instanceof Block
				;
	}

	private <T> boolean isNewLineAfter(T obj) {
		Object parent = peek(-1);

		return (obj instanceof String && parent instanceof Block)
				|| (obj instanceof Method)
				;
	}

	private void addNewLine() throws IOException {
		appendable.append(NEW_LINE);
		appendable.append(this.tabCharacter.repeat(this.indent));
	}

	public <T> void startObject(T obj) throws IOException {
		queue.add(obj);
		if (isIndent(obj)) {
			this.indent++;
		}
		if (isNewLineBefore(obj)) {
			addNewLine();
		}
		this.last = obj;
	}

	public <T> void endObject(T obj) throws IOException {
		if (isIndent(obj)) {
			this.indent--;
		}
		if (isNewLineAfter(obj)) {
			addNewLine();
		}
		queue.pop();
	}

	public DefaultJavaFormatter withTabCharacter(String tabCharacters) {
		this.tabCharacter = tabCharacters;
		return this;
	}

	public DefaultJavaFormatter useTabs() {
		this.tabCharacter = DEFAULT_TAB;
		return this;
	}

	public DefaultJavaFormatter useSpaces() {
		return useSpaces(2);
	}

	public DefaultJavaFormatter useSpaces(int count) {
		if (count < 1) {
			throw new IndexOutOfBoundsException("The count can not be below 0");
		}
		this.tabCharacter = " ".repeat(count);
		return this;
	}

}
