package context;

/**
 * @author hum
 * 单例
 */
public final class ActionContext {

    private static final ThreadLocal<Context> THREAD_LOCAL = new ThreadLocal<Context>() {
        @Override
        protected Context initialValue() {
            return new Context();
        }
    };

    private static class ContextHolder {
        private final static ActionContext ACTION_CONTEXT = new ActionContext();
    }

    public static ActionContext getActionContext() {
        return ContextHolder.ACTION_CONTEXT;
    }

    public Context getContext() {
        return THREAD_LOCAL.get();
    }

    private ActionContext() {

    }
}
