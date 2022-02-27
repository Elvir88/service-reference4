package ru.ufanet.servicereference.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ServiceInPacketDiscount.
 */
@Entity
@Table(name = "service_in_packet_discount")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServiceInPacketDiscount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "coefficient", nullable = false)
    private Float coefficient;

    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Service service;

    @JsonIgnoreProperties(value = { "tariffGroups" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Tariff tariff;

    @ManyToOne
    @JsonIgnoreProperties(value = { "serviceInPacketDiscounts" }, allowSetters = true)
    private PacketDiscount packetDiscount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceInPacketDiscount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getCoefficient() {
        return this.coefficient;
    }

    public ServiceInPacketDiscount coefficient(Float coefficient) {
        this.setCoefficient(coefficient);
        return this;
    }

    public void setCoefficient(Float coefficient) {
        this.coefficient = coefficient;
    }

    public Service getService() {
        return this.service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public ServiceInPacketDiscount service(Service service) {
        this.setService(service);
        return this;
    }

    public Tariff getTariff() {
        return this.tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public ServiceInPacketDiscount tariff(Tariff tariff) {
        this.setTariff(tariff);
        return this;
    }

    public PacketDiscount getPacketDiscount() {
        return this.packetDiscount;
    }

    public void setPacketDiscount(PacketDiscount packetDiscount) {
        this.packetDiscount = packetDiscount;
    }

    public ServiceInPacketDiscount packetDiscount(PacketDiscount packetDiscount) {
        this.setPacketDiscount(packetDiscount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceInPacketDiscount)) {
            return false;
        }
        return id != null && id.equals(((ServiceInPacketDiscount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceInPacketDiscount{" +
            "id=" + getId() +
            ", coefficient=" + getCoefficient() +
            "}";
    }
}
