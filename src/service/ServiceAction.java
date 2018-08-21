package service;

public enum ServiceAction {

    NEW_PROCESS(new NewProcessMessage()),
    COORDINATOR_ACTION(new CoordinatorActionMessage()),
    ELECTION(new ElectionMessage()),
    COORDINATOR(new CoordinatorMessage());

    private ServiceMessage serviceMessage;

    ServiceAction(ServiceMessage serviceMessage) {
        this.serviceMessage = serviceMessage;
    }

    public ServiceMessage getHandler() {
        return serviceMessage;
    }
}
