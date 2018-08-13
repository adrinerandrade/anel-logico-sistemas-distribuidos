package messenger;

class ProcessContext {

    private static ThreadLocal<String> currentProcess = new ThreadLocal<>();

    private ProcessContext() {}

    static void setCurrentProcess(String processId) {
        currentProcess.set(processId);
    }

    static String getCurrentProcess() {
        return currentProcess.get();
    }

    static void clearCurrentProcess() {
        currentProcess.remove();
    }

}
