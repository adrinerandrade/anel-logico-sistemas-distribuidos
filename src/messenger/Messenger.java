package messenger;

import java.util.HashMap;

public class Messenger {

    private static boolean log;

    public static synchronized void sendFrom(String sourceProcessId, Message message) {
        if (ProcessesHandler.getProcessExecutor(sourceProcessId) == null) {
            System.out.print(String.format("\nMessage from dead process '%s' not sended: %s", sourceProcessId, message));
            return;
        }

        message.setSourceProcessId(sourceProcessId);
        ProcessExecutor process = ProcessesHandler.getProcessExecutor(message.getTargetProcessId());
        if (process != null) {
            if (log) {
                System.out.print(String.format("\nMessage sent: %s", message));
            }
            process.postMessage(message);
        } else {
            String targetProcessId = message.getTargetProcessId();
            System.err.print(String.format("\nMessage not send from %s to %s. Reason: Target process not found.", sourceProcessId, targetProcessId));
        }
    }

    public static synchronized void send(Message message) {
        sendFrom(ProcessContext.getCurrentProcess(), message);
    }

    public static synchronized void broadcast(Message message) {
        ProcessesHandler.getAllProcesses()
                .stream()
                .filter(process -> !process.getProcess().getId().equals(ProcessContext.getCurrentProcess()))
                .map(process -> buildBroadcastMessage(message, process))
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

    private Messenger() {}

}
