package app;

import messenger.Messenger;
import messenger.ProcessesHandler;
import service.Service;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Messenger.log(true);
        Service process = new Service();
        ProcessesHandler.newProcess(process);
        Thread.sleep(4000L);
        Service process1 = new Service();
        ProcessesHandler.newProcess(process1);
        Thread.sleep(4000L);
        Service process2 = new Service();
        ProcessesHandler.newProcess(process2);
        Thread.sleep(4000L);
    }

}
