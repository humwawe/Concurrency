package context;

/**
 * @author hum
 */
public class QueryNameFromDBAction {

    public void execute() {
        try {
            Thread.sleep(1000L);
            String name = "hum " + Thread.currentThread().getName();
            ActionContext.getActionContext().getContext().setName(name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
