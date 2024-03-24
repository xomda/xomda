package org.xomda.lib.java.renderer;

import java.util.ArrayList;

import org.xomda.lib.java.ast.BlockBean;
import org.xomda.lib.java.ast.Method;

public class CreationUtils {

	public static void addBodyText(Method method, String text) {
		if (method.getBlock() == null) {
			method.setBlock(new BlockBean());
		}
		if (null == method.getBlock().getTextList()) {
			method.getBlock().setTextList(new ArrayList<>());
		}
		method.getBlock().getTextList().add(text);
	}

}
