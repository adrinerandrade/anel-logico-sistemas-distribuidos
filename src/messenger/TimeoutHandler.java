package messenger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TimeoutHandler {

    private static final int DEFAULT_TIMEOUT = 3000;

    private Map<Integer, Timeout> timeouts = new HashMap<>();

    public Timeout waitForResponse(Message message) {
        Timeout timeout = new Timeout(DEFAULT_TIMEOUT);
        int messageId = message.getId();
        timeouts.put(messageId, timeout);
        timeout.onTimeout(() -> timeouts.remove(messageId));
        return timeout;
    }

    public void answered(Message message) {
        int messageId = message.getId();
        Optional.ofNullable(timeouts.get(messageId))
                .ifPresent(Timeout::complete);
        timeouts.remove(messageId);
    }

}
