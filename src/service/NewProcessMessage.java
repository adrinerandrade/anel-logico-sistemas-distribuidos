package service;

import messenger.Message;

import java.util.function.Consumer;

public class NewProcessMessage implements ServiceMessage {

    public static final String RANK = "rank";
    public static final String IS_COORDINATOR = "is_coordinator";

    @Override
    public Consumer<Message> getRequestExecutor(Service service) {
        return service::handleNewProcessRequest;
    }

    @Override
    public Consumer<Message> getResponseExecutor(Service service) {
        return service::handleNewProcessResponse;
    }

}
