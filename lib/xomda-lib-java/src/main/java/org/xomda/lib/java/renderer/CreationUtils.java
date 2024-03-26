package org.xomda.lib.java.renderer;

import java.util.ArrayList;

import org.xomda.lib.java.ast.Method;
import org.xomda.lib.java.ast.impl.BlockImpl;

public class CreationUtils {

	public static void addBodyText(Method method, String text) {
		if (method.getBlock() == null) {
			method.setBlock(new BlockImpl());
		}
		if (null == method.getBlock().getTextList()) {
			method.getBlock().setTextList(new ArrayList<>());
		}
		method.getBlock().getTextList().add(text);
	}

}
