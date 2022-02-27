package ru.ufanet.servicereference.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MarketingResearch.
 */
@Entity
@Table(name = "marketing_research")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MarketingResearch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Service service;

    @JsonIgnoreProperties(value = { "tariffGroups" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Tariff tariff;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MarketingResearch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public MarketingResearch title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Service getService() {
        return this.service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public MarketingResearch service(Service service) {
        this.setService(service);
        return this;
    }

    public Tariff getTariff() {
        return this.tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public MarketingResearch tariff(Tariff tariff) {
        this.setTariff(tariff);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarketingResearch)) {
            return false;
        }
        return id != null && id.equals(((MarketingResearch) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarketingResearch{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
