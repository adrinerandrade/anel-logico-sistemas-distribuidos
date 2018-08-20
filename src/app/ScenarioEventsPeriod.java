package app;

public enum ScenarioEventsPeriod {

    NEW_PROCESS(10000),
    MESSAGE_TO_COORDINATOR(6000),
    COORDINATOR_INACTIVE(35000),
    PROCESS_INACTIVE(20000);

    private int millis;

    ScenarioEventsPeriod(int millis) {
        this.millis = millis;
    }

    public long getMillis() {
        return millis;
    }

}
