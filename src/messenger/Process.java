package messenger;

public interface Process {

    /**
     * @return The unique identifier of a process.
     */
    String getId();

    void onStart();

    void onMessage(Message message);

}
