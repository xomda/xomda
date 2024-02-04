package org.xomda.core.util;

import java.util.ArrayList;

public class DeferredActions extends ArrayList<Runnable> implements Runnable {

    @Override
    public void run() {
        forEach(Runnable::run);
    }
}
