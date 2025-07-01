/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.dto;

/**
 *
 * @author Khanh
 */
import java.io.Serializable;
import java.math.BigDecimal;

public class ServiceDTO implements Serializable {
    private int serviceId;
    private String serviceName;
    private BigDecimal price;
    private int garageId;
    private boolean isActive;

    public ServiceDTO() {
    }

    public ServiceDTO(int serviceId, String serviceName, BigDecimal price, int garageId, boolean isActive) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.price = price;
        this.garageId = garageId;
        this.isActive = isActive;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getGarageId() {
        return garageId;
    }

    public void setGarageId(int garageId) {
        this.garageId = garageId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

