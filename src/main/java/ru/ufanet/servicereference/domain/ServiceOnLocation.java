package ru.ufanet.servicereference.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Услуга в локации
 */
@Schema(description = "Услуга в локации")
@Entity
@Table(name = "service_on_location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServiceOnLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "datefrom", nullable = false)
    private Instant datefrom;

    @Column(name = "date_to")
    private Instant dateTo;

    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Service service;

    @JsonIgnoreProperties(value = { "tariffs" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private TariffGroup tariffGroup;

    @OneToOne
    @JoinColumn(unique = true)
    private ContractPattern pattern;

    @ManyToOne
    @JsonIgnoreProperties(value = { "houses", "serviceOnLocations", "promotionOnLocations" }, allowSetters = true)
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceOnLocation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDatefrom() {
        return this.datefrom;
    }

    public ServiceOnLocation datefrom(Instant datefrom) {
        this.setDatefrom(datefrom);
        return this;
    }

    public void setDatefrom(Instant datefrom) {
        this.datefrom = datefrom;
    }

    public Instant getDateTo() {
        return this.dateTo;
    }

    public ServiceOnLocation dateTo(Instant dateTo) {
        this.setDateTo(dateTo);
        return this;
    }

    public void setDateTo(Instant dateTo) {
        this.dateTo = dateTo;
    }

    public Service getService() {
        return this.service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public ServiceOnLocation service(Service service) {
        this.setService(service);
        return this;
    }

    public TariffGroup getTariffGroup() {
        return this.tariffGroup;
    }

    public void setTariffGroup(TariffGroup tariffGroup) {
        this.tariffGroup = tariffGroup;
    }

    public ServiceOnLocation tariffGroup(TariffGroup tariffGroup) {
        this.setTariffGroup(tariffGroup);
        return this;
    }

    public ContractPattern getPattern() {
        return this.pattern;
    }

    public void setPattern(ContractPattern contractPattern) {
        this.pattern = contractPattern;
    }

    public ServiceOnLocation pattern(ContractPattern contractPattern) {
        this.setPattern(contractPattern);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ServiceOnLocation location(Location location) {
        this.setLocation(location);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceOnLocation)) {
            return false;
        }
        return id != null && id.equals(((ServiceOnLocation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceOnLocation{" +
            "id=" + getId() +
            ", datefrom='" + getDatefrom() + "'" +
            ", dateTo='" + getDateTo() + "'" +
            "}";
    }
}
