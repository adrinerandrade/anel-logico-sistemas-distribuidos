package service;

import messenger.Message;

import java.util.function.Consumer;

public class CancelElectionMessage implements ServiceMessage {

    @Override
    public Consumer<Message> getRequestExecutor(Service service) {
        return service::handleCancelElection;
    }

    @Override
    public Consumer<Message> getResponseExecutor(Service service) {
        return msg -> {};
    }

}
