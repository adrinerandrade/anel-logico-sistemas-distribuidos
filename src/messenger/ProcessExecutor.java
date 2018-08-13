package messenger;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

class ProcessExecutor {

    private final Queue<Message> queue = new LinkedList<>();
    private final Process process;
    private boolean canRun;
    private Thread runner;

    ProcessExecutor(Process process) {
        this.process = Objects.requireNonNull(process);
    }

    void postMessage(Message message) {
        this.queue.add(message);
    }

    void start() {
        if (runner == null) {
            canRun = true;
            runner = new Thread(() -> runContextualized(this::run));
            runner.start();
            new Thread(() -> runContextualized(this.process::onStart)).start();
        } else {
            System.out.println(String.format("Process '%s' already in execution.", process.getId()));
        }
    }

    private void run() {
        while (canRun) {
            Message message = queue.poll();
            if (message != null) {
                process.onMessage(message);
            }
        }
    }

    private void runContextualized(Runnable runnable) {
        ProcessContext.setCurrentProcess(this.process.getId());
        runnable.run();
        ProcessContext.clearCurrentProcess();
    }


    void kill() {
        queue.clear();
        canRun = false;
        runner = null;
    }

}
