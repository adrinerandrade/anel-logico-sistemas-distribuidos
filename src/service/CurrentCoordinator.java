package service;

/**
 * Somente para utilização no main
 */
public class CurrentCoordinator {

    private static String coordinatorId;

    public static void setCoordinatorId(String coordinatorId) {
        System.out.print(String.format("\nNew coordinator: %s", coordinatorId));
        CurrentCoordinator.coordinatorId = coordinatorId;
    }

    public static String getCoordinatorId() {
        return coordinatorId;
    }

}
