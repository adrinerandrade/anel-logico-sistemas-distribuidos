package messenger;

import java.util.*;

public class Timeout {

    private Process process;
    private Timer timer = new Timer();
    private Queue<Runnable> onTimeout = new LinkedList<>();

    Timeout(Process process, int milliseconds) {
        this.process = process;
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                onTimeout.forEach(Runnable::run);
            }

        }, milliseconds);
    }

    public Timeout onTimeout(Runnable runnable) {
        onTimeout.add(() -> {
            ProcessContext.setCurrentProcess(this.process.getId());
            runnable.run();
        });
        return this;
    }

    public void complete() {
        timer.cancel();
    }

}
