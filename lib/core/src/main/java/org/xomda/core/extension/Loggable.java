package org.xomda.core.extension;

import org.slf4j.Logger;
import org.xomda.shared.logging.LogService;

public interface Loggable {

	default Logger getLogger() {
		return LogService.getLogger(getClass());
	}

}
