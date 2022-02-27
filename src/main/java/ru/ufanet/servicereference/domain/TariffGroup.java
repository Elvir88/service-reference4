package ru.ufanet.servicereference.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TariffGroup.
 */
@Entity
@Table(name = "tariff_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TariffGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany(mappedBy = "tariffGroups")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tariffGroups" }, allowSetters = true)
    private Set<Tariff> tariffs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TariffGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public TariffGroup title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Tariff> getTariffs() {
        return this.tariffs;
    }

    public void setTariffs(Set<Tariff> tariffs) {
        if (this.tariffs != null) {
            this.tariffs.forEach(i -> i.removeTariffGroup(this));
        }
        if (tariffs != null) {
            tariffs.forEach(i -> i.addTariffGroup(this));
        }
        this.tariffs = tariffs;
    }

    public TariffGroup tariffs(Set<Tariff> tariffs) {
        this.setTariffs(tariffs);
        return this;
    }

    public TariffGroup addTariff(Tariff tariff) {
        this.tariffs.add(tariff);
        tariff.getTariffGroups().add(this);
        return this;
    }

    public TariffGroup removeTariff(Tariff tariff) {
        this.tariffs.remove(tariff);
        tariff.getTariffGroups().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TariffGroup)) {
            return false;
        }
        return id != null && id.equals(((TariffGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TariffGroup{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
