package app;

public enum ScenarioEventsPeriod {

    NEW_PROCESS(30000),
    MESSAGE_TO_COORDINATOR(25000),
    COORDINATOR_INACTIVE(100000),
    PROCESS_INACTIVE(80000);

    private int millis;

    ScenarioEventsPeriod(int millis) {
        this.millis = millis;
    }

    public long getMillis() {
        return millis;
    }

}
