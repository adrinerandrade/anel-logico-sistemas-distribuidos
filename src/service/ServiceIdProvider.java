package service;

public class ServiceIdProvider {

    private static int id;

    public static synchronized int newId() {
        return id++;
    }

}
