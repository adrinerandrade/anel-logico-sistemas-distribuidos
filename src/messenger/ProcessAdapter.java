package messenger;

public abstract class ProcessAdapter implements Process {

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onMessage(Message message) {
    }

}
