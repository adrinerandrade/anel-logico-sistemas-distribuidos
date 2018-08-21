package messenger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TimeoutHandler {

    private static final int DEFAULT_TIMEOUT = 2000;

    private Map<Integer, Timeout> timeouts = new HashMap<>();
    private Process process;

    public TimeoutHandler(Process process) {
        this.process = process;
    }

    public Timeout waitForResponse(Message message) {
        Timeout timeout = new Timeout(process, DEFAULT_TIMEOUT);
        int messageId = message.getId();
        timeouts.put(messageId, timeout);
        timeout.onTimeout(() -> {
            System.err.print(String.format("\nMessage not answered: %s", message));
            timeouts.remove(messageId);
        });
        return timeout;
    }

    public void answered(Message message) {
        int messageId = message.getId();
        Optional.ofNullable(timeouts.get(messageId))
                .ifPresent(Timeout::complete);
        timeouts.remove(messageId);
    }

    public void clearALl() {
        timeouts.values().forEach(Timeout::complete);
        timeouts.clear();
    }

}
