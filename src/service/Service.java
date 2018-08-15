package service;

import messenger.*;
import messenger.Process;

public class Service implements Process {

    private static final String ACTION_HEADER = "action";

    private TimeoutHandler timeoutHandler = new TimeoutHandler();

    private final int rank;
    private final String id;
    private String coordinator;
    private String successor;

    public Service() {
        this.rank = ServiceIdProvider.newId();
        this.id = String.format("Process_%s", this.rank);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void onStart() {
        Message message = new Message();
        message.addHeader("action", ServiceAction.NEW_PROCESS);
        timeoutHandler.waitForResponse(message).onTimeout(() -> this.coordinator = this.id);
        Messenger.forMessage(message).broadcast();
    }

    @Override
    public void onMessage(Message message) {
        ServiceAction action = message.getHeader(ACTION_HEADER);
        ServiceMessage handler = action.getHandler();
        MessageType type = message.getType();
        switch (type) {
            case REQUEST:
                handler.getRequestExecutor(this).accept(message);
                break;
            case RESPONSE:
                handler.getResponseExecutor(this).accept(message);
                break;
            default:
                System.out.println(String.format("Message type no defined: %s.", type));
                break;
        }
    }

    private boolean isCoordinator() {
        return this.id.equals(this.coordinator);
    }

    void handleNewProcessRequest(Message message) {
        Payload responsePayload = new Payload();
        responsePayload.put(NewProcessMessage.RANK, this.rank);
        responsePayload.put(NewProcessMessage.IS_COORDINATOR, this.isCoordinator());
        new Reply(message).withPayload(responsePayload);
    }

    void handleNewProcessResponse(Message message) {

    }

}
