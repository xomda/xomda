package org.xomda.core.template.context;

import static org.xomda.shared.util.ReflectionUtils.unchecked;

import java.io.PrintWriter;

import org.xomda.core.util.TemplateFormat;

public interface WritableContext<R extends WritableContext<R>> {

    PrintWriter getWriter();

    boolean isNewLine();

    void setNewLine(boolean state);

    static CharSequence format(CharSequence pattern, Object... args) {
        return TemplateFormat.format(pattern.toString(), args);
    }

    default R println() {
        setNewLine(true);
        getWriter().println();
        return unchecked(this);
    }

    default R println(CharSequence text, Object... args) {
        setNewLine(true);
        getWriter().println(format(text, args));
        return unchecked(this);
    }

    default R print(CharSequence text, Object... args) {
        setNewLine(false);
        getWriter().print(format(text, args));
        return unchecked(this);
    }

}
