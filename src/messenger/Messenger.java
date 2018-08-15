package messenger;

import java.util.HashMap;

public class Messenger {

    private static boolean log;

    public static Messenger forMessage(Message message) {
        return new Messenger(message);
    }

    private Message message;

    private Messenger(Message message) {
        this.message = message;
    }

    public void send() {
        message.setSourceProcessId(ProcessContext.getCurrentProcess());
        ProcessExecutor process = ProcessesHandler.getProcessExecutor(message.getTargetProcessId());
        if (process != null) {
            if (log) {
                System.out.println(String.format("Message sent: %s", message));
            }
            process.postMessage(message);
        } else {
            System.err.print(String.format("Message not send. Reason: Process '%s' not found.", message.getTargetProcessId()));
        }
    }

    public void broadcast() {
        ProcessesHandler.getAllProcesses()
                .stream()
                .filter(process -> !process.getProcess().getId().equals(ProcessContext.getCurrentProcess()))
                .map(process -> new Messenger(buildBroadcastMessage(message, process)))
                .forEach(Messenger::send);
    }

    private static Message buildBroadcastMessage(Message message, ProcessExecutor process) {
        return new Message(
                message.getId(),
                process.getProcess().getId(),
                new HashMap<>(message.getHeaders()),
                message.getPayload(),
                message.getType()
        );
    }

    public static void log(boolean log) {
        Messenger.log = log;
    }

}
