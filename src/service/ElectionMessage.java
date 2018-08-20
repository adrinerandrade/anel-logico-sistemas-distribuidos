package service;

import messenger.Message;

import java.util.function.Consumer;

public class ElectionMessage implements ServiceMessage {

    public static final String PIDS = "pids";
    public static final String ALREADY_ON_ELECTION = "already_on_election";

    @Override
    public Consumer<Message> getRequestExecutor(Service service) {
        return service::handleElectionRequest;
    }

    @Override
    public Consumer<Message> getResponseExecutor(Service service) {
        return service::handleElectionResponse;
    }
}
