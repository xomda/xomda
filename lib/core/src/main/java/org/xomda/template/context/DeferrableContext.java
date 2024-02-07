package org.xomda.template.context;

public interface DeferrableContext<R extends DeferrableContext<R>> {

	R deferred();

}
