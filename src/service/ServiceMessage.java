package service;

import messenger.Message;

import java.util.function.Consumer;

public interface ServiceMessage {

    Consumer<Message> getRequestExecutor(Service service);

    Consumer<Message> getResponseExecutor(Service service);

}
