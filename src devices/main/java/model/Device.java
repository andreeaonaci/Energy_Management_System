package model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "maxhourlyenergyconsumption")
    private Double maxhourlyenergyconsumption;

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getMaxhourlyenergyconsumption() {
        return maxhourlyenergyconsumption;
    }

    public void setMaxhourlyenergyconsumption(Double maxHourlyEnergyConsumption) {
        this.maxhourlyenergyconsumption = maxHourlyEnergyConsumption;
    }

    public void setId(Long id) {
        this.id = id;
    }
}