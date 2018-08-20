package app;

public enum ScenarioEventsDelay {

    NEW_PROCESS(30000),
    MESSAGE_TO_COORDINATOR(25000),
    COORDINATOR_INACTIVE(100000),
    PROCESS_INACTIVE(80000);

    private int millis;

    ScenarioEventsDelay(int millis) {
        this.millis = millis;
    }

    public int getMillis() {
        return millis;
    }

}
