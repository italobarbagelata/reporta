package cl.ibapps.reportapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Color.
 */
@Entity
@Table(name = "color")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Color implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "color")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "user",
            "label",
            "exporter",
            "phytoChina",
            "dispatchs",
            "packages",
            "weight",
            "specie",
            "variety",
            "color",
            "finalOverall",
            "listSizes",
        },
        allowSetters = true
    )
    private Set<Inspection> inspections = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Color id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Color description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Inspection> getInspections() {
        return this.inspections;
    }

    public void setInspections(Set<Inspection> inspections) {
        if (this.inspections != null) {
            this.inspections.forEach(i -> i.setColor(null));
        }
        if (inspections != null) {
            inspections.forEach(i -> i.setColor(this));
        }
        this.inspections = inspections;
    }

    public Color inspections(Set<Inspection> inspections) {
        this.setInspections(inspections);
        return this;
    }

    public Color addInspection(Inspection inspection) {
        this.inspections.add(inspection);
        inspection.setColor(this);
        return this;
    }

    public Color removeInspection(Inspection inspection) {
        this.inspections.remove(inspection);
        inspection.setColor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Color)) {
            return false;
        }
        return id != null && id.equals(((Color) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Color{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
