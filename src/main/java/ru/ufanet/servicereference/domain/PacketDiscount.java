package ru.ufanet.servicereference.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PacketDiscount.
 */
@Entity
@Table(name = "packet_discount")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PacketDiscount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "cost", precision = 21, scale = 2, nullable = false)
    private BigDecimal cost;

    @OneToMany(mappedBy = "packetDiscount")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "service", "tariff", "packetDiscount" }, allowSetters = true)
    private Set<ServiceInPacketDiscount> serviceInPacketDiscounts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PacketDiscount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public PacketDiscount title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public PacketDiscount cost(BigDecimal cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Set<ServiceInPacketDiscount> getServiceInPacketDiscounts() {
        return this.serviceInPacketDiscounts;
    }

    public void setServiceInPacketDiscounts(Set<ServiceInPacketDiscount> serviceInPacketDiscounts) {
        if (this.serviceInPacketDiscounts != null) {
            this.serviceInPacketDiscounts.forEach(i -> i.setPacketDiscount(null));
        }
        if (serviceInPacketDiscounts != null) {
            serviceInPacketDiscounts.forEach(i -> i.setPacketDiscount(this));
        }
        this.serviceInPacketDiscounts = serviceInPacketDiscounts;
    }

    public PacketDiscount serviceInPacketDiscounts(Set<ServiceInPacketDiscount> serviceInPacketDiscounts) {
        this.setServiceInPacketDiscounts(serviceInPacketDiscounts);
        return this;
    }

    public PacketDiscount addServiceInPacketDiscount(ServiceInPacketDiscount serviceInPacketDiscount) {
        this.serviceInPacketDiscounts.add(serviceInPacketDiscount);
        serviceInPacketDiscount.setPacketDiscount(this);
        return this;
    }

    public PacketDiscount removeServiceInPacketDiscount(ServiceInPacketDiscount serviceInPacketDiscount) {
        this.serviceInPacketDiscounts.remove(serviceInPacketDiscount);
        serviceInPacketDiscount.setPacketDiscount(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PacketDiscount)) {
            return false;
        }
        return id != null && id.equals(((PacketDiscount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacketDiscount{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", cost=" + getCost() +
            "}";
    }
}
