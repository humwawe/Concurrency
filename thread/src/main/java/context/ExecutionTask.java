package context;

/**
 * @author hum
 */
public class ExecutionTask implements Runnable {

    private QueryNameFromDBAction queryAction = new QueryNameFromDBAction();

    private QueryIdFromHttpAction httpAction = new QueryIdFromHttpAction();

    @Override
    public void run() {

        queryAction.execute();
        System.out.println("The name query successful");
        httpAction.execute();
        System.out.println("The card id query successful");

        Context context = ActionContext.getActionContext().getContext();
        System.out.println("The Name is " + context.getName() + " and CardId " + context.getCardId());
    }
}
