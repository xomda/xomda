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

	private boolean emptyGroup;

	private final Appendable appendable;

	private final Stack<Object> queue;

	public DefaultJavaFormatter(Appendable appendable) {
		this.queue = new Stack<>();
		this.appendable = appendable;
	}

	public <T> void startObject(T obj) throws IOException {
		emptyGroup = false;
		queue.add(obj);
		if (isIndent(peek(-1))) {
			this.indent++;
		}
		addNewLine(getNewLinesBefore(obj));
	}

	public void startGroup(String obj) throws IOException {
		emptyGroup = true;
	}

	public void endGroup(String obj) throws IOException {
		if (emptyGroup) {
			return;
		}
		if (isIndent(queue.peek())) {
			addNewLine(1);
		}
	}

	public <T> void endObject(T obj) throws IOException {
		if (isIndent(peek(-1))) {
			this.indent--;
		}
		queue.pop();
	}

	public Object peek(int count) {
		return queue.size() > -count
				? queue.get(count + queue.size() - 1)
				: null;
	}

	private <T> boolean isIndent(T obj) {
		return obj instanceof Method
				|| obj instanceof Class
				;
	}

	private int getNewLinesBefore(Object obj) {
		if (obj instanceof Class) {
			return 2;
		}
		if (obj instanceof Import
				// class internals
				|| obj instanceof Constructor
				|| obj instanceof Method
				|| obj instanceof Field

				|| obj instanceof String && peek(-1) instanceof Block
		) {
			return 1;
		}

		return 0;
	}

	private void addNewLine(int repeat) throws IOException {
		for (int i = 0; i < repeat; i++) {
			appendable.append(NEW_LINE);
			appendable.append(this.tabCharacter.repeat(this.indent));
		}
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
