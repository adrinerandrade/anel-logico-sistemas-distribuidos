package service;

public enum ServiceAction {

    NEW_PROCESS(new NewProcessMessage());

    private ServiceMessage serviceMessage;

    ServiceAction(ServiceMessage serviceMessage) {
        this.serviceMessage = serviceMessage;
    }

    public ServiceMessage getHandler() {
        return serviceMessage;
    }
}
