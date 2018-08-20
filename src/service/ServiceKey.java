package service;

import java.util.Objects;

public class ServiceKey implements Comparable<ServiceKey> {

    private String serviceId;
    private Integer rank;

    public ServiceKey(String serviceId, Integer rank) {
        this.serviceId = serviceId;
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceKey that = (ServiceKey) o;
        return Objects.equals(serviceId, that.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId);
    }

    @Override
    public String toString() {
        return "{" +
                "serviceId='" + serviceId + '\'' +
                ", rank=" + rank +
                '}';
    }

    public String getServiceId() {
        return serviceId;
    }

    public Integer getRank() {
        return rank;
    }

    @Override
    public int compareTo(ServiceKey o) {
        return rank.compareTo(o.rank);
    }

}
