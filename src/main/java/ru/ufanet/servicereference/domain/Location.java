package ru.ufanet.servicereference.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Локация
 */
@Schema(description = "Локация")
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "location" }, allowSetters = true)
    private Set<House> houses = new HashSet<>();

    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "service", "tariffGroup", "pattern", "location" }, allowSetters = true)
    private Set<ServiceOnLocation> serviceOnLocations = new HashSet<>();

    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "packetDiscount", "marketingResearch", "location" }, allowSetters = true)
    private Set<PromotionOnLocation> promotionOnLocations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Location id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Location title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<House> getHouses() {
        return this.houses;
    }

    public void setHouses(Set<House> houses) {
        if (this.houses != null) {
            this.houses.forEach(i -> i.setLocation(null));
        }
        if (houses != null) {
            houses.forEach(i -> i.setLocation(this));
        }
        this.houses = houses;
    }

    public Location houses(Set<House> houses) {
        this.setHouses(houses);
        return this;
    }

    public Location addHouse(House house) {
        this.houses.add(house);
        house.setLocation(this);
        return this;
    }

    public Location removeHouse(House house) {
        this.houses.remove(house);
        house.setLocation(null);
        return this;
    }

    public Set<ServiceOnLocation> getServiceOnLocations() {
        return this.serviceOnLocations;
    }

    public void setServiceOnLocations(Set<ServiceOnLocation> serviceOnLocations) {
        if (this.serviceOnLocations != null) {
            this.serviceOnLocations.forEach(i -> i.setLocation(null));
        }
        if (serviceOnLocations != null) {
            serviceOnLocations.forEach(i -> i.setLocation(this));
        }
        this.serviceOnLocations = serviceOnLocations;
    }

    public Location serviceOnLocations(Set<ServiceOnLocation> serviceOnLocations) {
        this.setServiceOnLocations(serviceOnLocations);
        return this;
    }

    public Location addServiceOnLocation(ServiceOnLocation serviceOnLocation) {
        this.serviceOnLocations.add(serviceOnLocation);
        serviceOnLocation.setLocation(this);
        return this;
    }

    public Location removeServiceOnLocation(ServiceOnLocation serviceOnLocation) {
        this.serviceOnLocations.remove(serviceOnLocation);
        serviceOnLocation.setLocation(null);
        return this;
    }

    public Set<PromotionOnLocation> getPromotionOnLocations() {
        return this.promotionOnLocations;
    }

    public void setPromotionOnLocations(Set<PromotionOnLocation> promotionOnLocations) {
        if (this.promotionOnLocations != null) {
            this.promotionOnLocations.forEach(i -> i.setLocation(null));
        }
        if (promotionOnLocations != null) {
            promotionOnLocations.forEach(i -> i.setLocation(this));
        }
        this.promotionOnLocations = promotionOnLocations;
    }

    public Location promotionOnLocations(Set<PromotionOnLocation> promotionOnLocations) {
        this.setPromotionOnLocations(promotionOnLocations);
        return this;
    }

    public Location addPromotionOnLocation(PromotionOnLocation promotionOnLocation) {
        this.promotionOnLocations.add(promotionOnLocation);
        promotionOnLocation.setLocation(this);
        return this;
    }

    public Location removePromotionOnLocation(PromotionOnLocation promotionOnLocation) {
        this.promotionOnLocations.remove(promotionOnLocation);
        promotionOnLocation.setLocation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
