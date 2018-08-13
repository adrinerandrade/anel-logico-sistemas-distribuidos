package messenger;

public class Messenger {

    private static boolean log;

    public static void send(Message message) {
        message.setSourceProcessId(ProcessContext.getCurrentProcess());
        ProcessExecutor process = ProcessesHandler.getProcess(message.getTargetProcessId());
        if (process != null) {
            if (log) {
                System.out.println(String.format("Message sent: %s", message));
            }
            process.postMessage(message);
        } else {
            System.err.print(String.format("Message not send. Reason: Process '%s' not found.", message.getTargetProcessId()));
        }
    }

    public static void log(boolean log) {
        Messenger.log = log;
    }

}
