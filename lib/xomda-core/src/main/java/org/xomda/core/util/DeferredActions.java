package org.xomda.core.util;

import java.io.Serial;
import java.util.ArrayList;

public class DeferredActions extends ArrayList<Runnable> implements Runnable {

	@Serial
	private static final long serialVersionUID = 6367806898638386874L;

	@Override
	public void run() {
		forEach(Runnable::run);
	}
}
