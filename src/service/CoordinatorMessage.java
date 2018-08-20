package service;

import messenger.Message;

import java.util.function.Consumer;

public class CoordinatorMessage implements ServiceMessage {

    public static final String PIDS = "pids";

    @Override
    public Consumer<Message> getRequestExecutor(Service service) {
        return service::handleCoordinatorRequest;
    }

    @Override
    public Consumer<Message> getResponseExecutor(Service service) {
        return msg -> {};
    }

}
