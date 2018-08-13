package messenger;

import java.util.Map;

public class Reply {

    private Message toReply;

    public Reply(Message toReply) {
        this.toReply = toReply;
    }

    public Message withPayload(Payload payload) {
        return buildMessage(payload);
    }

    public Message withoutPayload() {
        return buildMessage(new Payload());
    }

    private Message buildMessage(Payload payload) {
        int id = toReply.getId();
        String sourceProcessId = toReply.getSourceProcessId();
        final Map<String, Object> headers = toReply.getHeaders();
        return new Message(id, sourceProcessId, headers, payload, MessageType.RESPONSE);
    }

}
