package org.xomda.core.template.context;

import static org.xomda.shared.util.ReflectionUtils.unchecked;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface DefaultContext<R extends DefaultContext<R>> extends DeferrableContext<R>, WritableContext<R>, TabbableContext<R> {

    default R consume(Runnable runnable) {
        runnable.run();
        return unchecked(this);
    }

    default <T> R forEach(Iterable<T> it, Consumer<T> consumer) {
        if (null != it) it.forEach(consumer);
        return unchecked(this);
    }

    default <T> R forEach(Stream<T> it, Consumer<T> consumer) {
        if (null != it) it.forEach(consumer);
        return unchecked(this);
    }

    default <T> R forEach(Supplier<Iterable<T>> supplier, Consumer<T> consumer) {
        Iterable<T> it = supplier.get();
        if (null != it) it.forEach(consumer);
        return unchecked(this);
    }

}
