package app;

import messenger.Messenger;
import messenger.ProcessesHandler;
import service.CurrentCoordinator;
import service.Service;

import java.util.*;

public class Main {

    private static Map<String, Service> services = new HashMap<>();

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
        services.put(service.getId(), service);
        System.out.println("NOVO PROCESSO: " + service.getId());
    }

    private static void killProcess(Service process) {
        ProcessesHandler.killProcess(process.getId());
        services.remove(process.getId());
        System.out.println(String.format("Processo %s eliminado!", process.getId()));
    }

    private static Optional<Service> getCoordinator() {
        return Optional.ofNullable(services.get(CurrentCoordinator.getCoordinatorId()));
    }

    private static void killCoordinator() {
        getCoordinator().ifPresent(Main::killProcess);
        System.out.println("Coordenador eliminado!");
    }

    private static Optional<Service> findAnyService() {
        return services.keySet().stream().findAny().map(services::get);
    }

    private static void killAnyService() {
        findAnyService().ifPresent(Main::killProcess);
    }

}
