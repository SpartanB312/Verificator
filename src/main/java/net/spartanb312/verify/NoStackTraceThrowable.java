package net.spartanb312.verify;


public class NoStackTraceThrowable extends RuntimeException {

    public NoStackTraceThrowable(final String msg) {
        super(msg);
        this.setStackTrace(new StackTraceElement[0]);
    }

    @Override
    public String toString() {
        return "Fuck off nigga!";
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
