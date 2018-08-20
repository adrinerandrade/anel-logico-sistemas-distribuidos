package service;

import messenger.*;
import messenger.Process;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Consumer;

public class Service implements Process {

    private static final String ACTION_HEADER = "action";

    private TimeoutHandler timeoutHandler = new TimeoutHandler();
    private TreeSet<ServiceKey> allServices = new TreeSet<>();

    private final int rank;
    private final String id;
    private String coordinator;
    private boolean isOnElection;

    public Service() {
        this.rank = ServiceIdProvider.newId();
        this.id = getProcessName(this.rank);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void onStart() {
        Payload payload = new Payload();
        payload.put(NewProcessMessage.RANK, this.rank);
        Message message = new Message(payload);
        message.addHeader(ACTION_HEADER, ServiceAction.NEW_PROCESS);
        timeoutHandler.waitForResponse(message).onTimeout(() -> this.coordinator = this.id);
        Messenger.broadcast(message);
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
                timeoutHandler.answered(message);
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
        int sourceRank = message.getPayload().get(NewProcessMessage.RANK);
        mapNewProcess(message.getSourceProcessId(), sourceRank);
        Payload responsePayload = new Payload();
        responsePayload.put(NewProcessMessage.RANK, this.rank);
        responsePayload.put(NewProcessMessage.IS_COORDINATOR, this.isCoordinator());
        Messenger.send(new Reply(message).withPayload(responsePayload));
    }

    void handleNewProcessResponse(Message message) {
        Payload payload = message.getPayload();
        String sourceProcessId = message.getSourceProcessId();
        Integer sourceRank = payload.get(NewProcessMessage.RANK);
        mapNewProcess(sourceProcessId, sourceRank);
        boolean isCoordinator = payload.get(NewProcessMessage.IS_COORDINATOR);
        if (isCoordinator) {
            coordinator = sourceProcessId;
        }
    }

    public void messageToCoordinator() {
        if (coordinator == null) {
            startElection();
        } else {
            Message message = new Message(coordinator, new Payload());
            message.addHeader(ACTION_HEADER, ServiceAction.COORDINATOR_ACTION);
            timeoutHandler.waitForResponse(message).onTimeout(this::startElection);
            Messenger.send(message);
        }
    }

    void handleCoordinatorActionRequest(Message message) {
        Payload response = new Payload();
        response.put(CoordinatorActionMessage.STATUS, "ok");
        Messenger.send(new Reply(message).withPayload(response));
    }

    void handleCoordinatorActionResponse(Message message) {
        System.out.println(String.format("Status do coordenador: %s.", message.getPayload().get(CoordinatorActionMessage.STATUS)));
    }

    private void startElection() {
        System.out.println(String.format("Eleição iniciada pelo processo %s.", id));
        electionAction(new LinkedList<>());
    }

    void handleElectionRequest(Message message) {
        LinkedList<Integer> pids = message.getPayload().get(ElectionMessage.PIDS);
        if (!isOnElection) {
            if (pids.getFirst().equals(rank)) {
                coordinatorAction(pids);
            } else {
                electionAction(pids);
            }
        }

        Payload payload = new Payload();
        payload.put(ElectionMessage.ALREADY_ON_ELECTION, isOnElection);
        Messenger.send(new Reply(message).withPayload(payload));
    }

    private void electionAction(LinkedList<Integer> pids) {
        executeIfHasSuccessor(successor -> {
            pids.add(rank);
            Payload payload = new Payload();
            payload.put(ElectionMessage.PIDS, pids);
            Message message = new Message(successor.getServiceId(), payload);
            message.addHeader(ACTION_HEADER, ServiceAction.ELECTION);
            timeoutHandler.waitForResponse(message).onTimeout(() -> {
                System.out.println(String.format("Sucessor '%s' inativo.", successor.getServiceId()));
                allServices.remove(successor);
                electionAction(pids);
            });
            isOnElection = true;
            Messenger.send(message);
        });
    }

    void handleElectionResponse(Message message) {
        Payload response = message.getPayload();
        if (response.get(ElectionMessage.ALREADY_ON_ELECTION)) {
            response.<LinkedList<Integer>>get(ElectionMessage.PIDS).forEach(pid -> {
                Message cancelElectionMessage = new Message(getProcessName(pid), new Payload());
                message.addHeader(ACTION_HEADER, ServiceAction.CANCEL_ELECTION);
                Messenger.send(cancelElectionMessage);
            });
        }
    }

    void handleCancelElection(Message message) {
        isOnElection = false;
    }

    void handleCoordinatorRequest(Message message) {
        isOnElection = false;
        LinkedList<Integer> pids = message.getPayload().get(CoordinatorMessage.PIDS);
        if (!pids.getFirst().equals(rank)) {
            coordinatorAction(pids);
        }
    }

    private void coordinatorAction(LinkedList<Integer> pids) {
        executeIfHasSuccessor(successor -> pids
                .stream()
                .min(Comparator.reverseOrder())
                .ifPresent(higherPid -> {
                    this.isOnElection = false;
                    this.coordinator = getProcessName(higherPid);
                })
        );
    }

    private void executeIfHasSuccessor(Consumer<ServiceKey> runnable) {
        Optional<ServiceKey> optionalSuccessor = getSuccessor();
        if (optionalSuccessor.isPresent()) {
            runnable.accept(optionalSuccessor.get());
        } else {
            System.out.println("Nenhum sucessor encontrado!");
            this.isOnElection = false;
            this.coordinator = id;
        }
    }

    public Optional<ServiceKey> getSuccessor() {
        return Optional.ofNullable(Optional.ofNullable(allServices.higher(new ServiceKey(id, rank)))
                .orElse(allServices.first()));
    }

    private String getProcessName(int pid) {
        return String.format("Process_%s", pid);
    }

    private void mapNewProcess(String sourceProcessId, int rank) {
        allServices.add(new ServiceKey(sourceProcessId, rank));
        System.out.println(String.format("All process for %s: %s", id, allServices));
    }

}
