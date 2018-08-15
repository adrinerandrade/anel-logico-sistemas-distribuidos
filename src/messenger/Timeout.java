package messenger;

import java.util.*;

public class Timeout {

    private Timer timer = new Timer();
    private Queue<Runnable> onTimeout = new LinkedList<>();

    Timeout(int milliseconds) {
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                onTimeout.forEach(Runnable::run);
            }

        }, milliseconds);
    }

    public Timeout onTimeout(Runnable runnable) {
        onTimeout.add(runnable);
        return this;
    }

    public void complete() {
        timer.cancel();
    }

}
