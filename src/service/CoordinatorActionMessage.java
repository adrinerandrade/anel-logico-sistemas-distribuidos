package service;

import messenger.Message;

import java.util.function.Consumer;

public class CoordinatorActionMessage implements ServiceMessage {

    public static final String STATUS = "status";

    @Override
    public Consumer<Message> getRequestExecutor(Service service) {
        return service::handleCoordinatorActionRequest;
    }

    @Override
    public Consumer<Message> getResponseExecutor(Service service) {
        return service::handleCoordinatorActionResponse;
    }

}
