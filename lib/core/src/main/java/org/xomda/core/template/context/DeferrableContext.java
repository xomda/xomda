package org.xomda.core.template.context;

public interface DeferrableContext<R extends DeferrableContext<R>> {

	R deferred();

}
