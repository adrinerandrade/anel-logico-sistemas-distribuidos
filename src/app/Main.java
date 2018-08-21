package app;

import messenger.Messenger;
import messenger.ProcessesHandler;
import service.CurrentCoordinator;
import service.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    private static Map<String, Service> services = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        Messenger.log(true);
        schedule(Main::newProcess, 0L, ScenarioEventsPeriod.NEW_PROCESS.getMillis());
        schedule(() -> findAnyService().ifPresent(Service::messageToCoordinator), ScenarioEventsPeriod.MESSAGE_TO_COORDINATOR.getMillis(), ScenarioEventsPeriod.MESSAGE_TO_COORDINATOR.getMillis());
        schedule(() -> killCoordinator(), ScenarioEventsPeriod.COORDINATOR_INACTIVE.getMillis(), ScenarioEventsPeriod.COORDINATOR_INACTIVE.getMillis());
        schedule(() -> killAnyService(), ScenarioEventsPeriod.PROCESS_INACTIVE.getMillis(), ScenarioEventsPeriod.PROCESS_INACTIVE.getMillis());
    }

    private static void schedule(Runnable runnable, long delay, long period) {
        new Timer().scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runnable.run();
            }

        }, delay, period);
    }

    private static void newProcess() {
        Service service = new Service();
        ProcessesHandler.newProcess(service);
        System.out.print(String.format("\nNEW PROCESS: %s\n", service.getId()));
        services.put(service.getId(), service);
        System.out.print(String.format("\nCURRENT PROCESSES: %s", services.keySet()));
    }

    private static void killCoordinator() {
        Optional<Service> coordinatorOptional = getCoordinator();
        if (!coordinatorOptional.isPresent()) {
            System.out.print(String.format("\nCOORDINATOR NOT MAPPED %s\n", CurrentCoordinator.getCoordinatorId()));
        }

        coordinatorOptional.ifPresent(coordinator -> {
            Main.killProcess(coordinator, false);
            System.out.print(String.format("\nCOORDINADOR '%s' ELIMINATED!\n", coordinator.getId()));
        });
    }

    private static Optional<Service> getCoordinator() {
        return Optional.ofNullable(services.get(CurrentCoordinator.getCoordinatorId()));
    }

    private static void killProcess(Service process) {
        killProcess(process, true);
    }

    private static void killProcess(Service process, boolean log) {
        ProcessesHandler.killProcess(process.getId());
        services.remove(process.getId());
        process.onKill();
        if (log) {
            System.out.println(String.format("\nPROCESS '%s' ELIMINATED!\n", process.getId()));
        }
        System.out.println(String.format("\nCURRENT PROCESSES: %s", services.keySet()));
    }

    private static Optional<Service> findAnyService() {
        return services.keySet()
                .stream()
                .filter(key -> !key.equals(CurrentCoordinator.getCoordinatorId()))
                .findAny()
                .map(services::get);
    }

    private static void killAnyService() {
        findAnyService().ifPresent(Main::killProcess);
    }

}
