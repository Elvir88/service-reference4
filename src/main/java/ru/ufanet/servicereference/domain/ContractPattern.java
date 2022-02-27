package ru.ufanet.servicereference.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ContractPattern.
 */
@Entity
@Table(name = "contract_pattern")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContractPattern implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "pattern_id", nullable = false)
    private Integer patternId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContractPattern id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public ContractPattern title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPatternId() {
        return this.patternId;
    }

    public ContractPattern patternId(Integer patternId) {
        this.setPatternId(patternId);
        return this;
    }

    public void setPatternId(Integer patternId) {
        this.patternId = patternId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractPattern)) {
            return false;
        }
        return id != null && id.equals(((ContractPattern) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractPattern{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", patternId=" + getPatternId() +
            "}";
    }
}
