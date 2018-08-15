package messenger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProcessesHandler {

    private static Map<String, ProcessExecutor> processes = new HashMap<>();

    public static void newProcess(Process process) {
        ProcessExecutor processExecutor = new ProcessExecutor(process);
        String id = process.getId();
        if (id == null) {
            throw new IllegalArgumentException("Process name can't be null!");
        }
        processes.put(id, processExecutor);
        processExecutor.start();
    }

    public static void killProcess(String id) {
        ProcessExecutor processExecutor = processes.get(id);
        if (processExecutor != null) {
            processes.remove(id);
            processExecutor.kill();
        }
    }

    static ProcessExecutor getProcessExecutor(String id) {
        return processes.get(id);
    }

    static Collection<ProcessExecutor> getAllProcesses() {
        return processes.values();
    }

    private ProcessesHandler() {
    }

}
