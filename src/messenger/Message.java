package messenger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Message {

    private final static String SOURCE_PROCESS_ID_KEY = "SOURCE_PROCESS_ID";
    private final static String TARGET_PROCESS_ID_KEY = "TARGET_PROCESS_ID";

    private final int id;
    private Map<String, Object> headers;
    private Payload payload;
    private MessageType type;

    public Message(String targetProcessId, Payload payload) {
        this(MessageIdProvider.newId(), targetProcessId, new HashMap<>(), payload, MessageType.REQUEST);
    }

    Message(int id, String targetProcessId, Map<String, Object> headers, Payload payload, MessageType type) {
        this.id = id;
        this.headers = headers;
        setTargetProcessId(targetProcessId);
        this.payload = payload;
        this.type = type;
    }

    String getTargetProcessId() {
        return getHeader(TARGET_PROCESS_ID_KEY);
    }

    String getSourceProcessId() {
        return getHeader(SOURCE_PROCESS_ID_KEY);
    }

    private void setTargetProcessId(String targetProcessId) {
        this.headers.put(TARGET_PROCESS_ID_KEY, targetProcessId);
    }

    void setSourceProcessId(String sourceProcessId) {
        this.headers.put(SOURCE_PROCESS_ID_KEY, sourceProcessId);
    }

    Map<String, Object> getHeaders() {
        return headers;
    }

    public int getId() {
        return id;
    }

    public MessageType getType() {
        return type;
    }

    public Payload getPayload() {
        return payload;
    }

    public Message addHeader(String key, Object value) {
        this.headers.put(key, value);
        return this;
    }

    public <T> T getHeader(String key) {
        return (T) this.headers.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", headers=" + headers +
                ", payload=" + payload +
                ", type=" + type +
                '}';
    }
}
