package ru.ufanet.servicereference.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PromotionOnLocation.
 */
@Entity
@Table(name = "promotion_on_location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PromotionOnLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "date_from", nullable = false)
    private Instant dateFrom;

    @Column(name = "date_to")
    private Instant dateTo;

    @JsonIgnoreProperties(value = { "serviceInPacketDiscounts" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private PacketDiscount packetDiscount;

    @JsonIgnoreProperties(value = { "service", "tariff" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private MarketingResearch marketingResearch;

    @ManyToOne
    @JsonIgnoreProperties(value = { "houses", "serviceOnLocations", "promotionOnLocations" }, allowSetters = true)
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PromotionOnLocation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public PromotionOnLocation title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getDateFrom() {
        return this.dateFrom;
    }

    public PromotionOnLocation dateFrom(Instant dateFrom) {
        this.setDateFrom(dateFrom);
        return this;
    }

    public void setDateFrom(Instant dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Instant getDateTo() {
        return this.dateTo;
    }

    public PromotionOnLocation dateTo(Instant dateTo) {
        this.setDateTo(dateTo);
        return this;
    }

    public void setDateTo(Instant dateTo) {
        this.dateTo = dateTo;
    }

    public PacketDiscount getPacketDiscount() {
        return this.packetDiscount;
    }

    public void setPacketDiscount(PacketDiscount packetDiscount) {
        this.packetDiscount = packetDiscount;
    }

    public PromotionOnLocation packetDiscount(PacketDiscount packetDiscount) {
        this.setPacketDiscount(packetDiscount);
        return this;
    }

    public MarketingResearch getMarketingResearch() {
        return this.marketingResearch;
    }

    public void setMarketingResearch(MarketingResearch marketingResearch) {
        this.marketingResearch = marketingResearch;
    }

    public PromotionOnLocation marketingResearch(MarketingResearch marketingResearch) {
        this.setMarketingResearch(marketingResearch);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public PromotionOnLocation location(Location location) {
        this.setLocation(location);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromotionOnLocation)) {
            return false;
        }
        return id != null && id.equals(((PromotionOnLocation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromotionOnLocation{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateTo='" + getDateTo() + "'" +
            "}";
    }
}
