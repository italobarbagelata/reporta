package cl.ibapps.reportapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Weight.
 */
@Entity
@Table(name = "weight")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Weight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "weight")
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

    public Weight id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Weight description(String description) {
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
            this.inspections.forEach(i -> i.setWeight(null));
        }
        if (inspections != null) {
            inspections.forEach(i -> i.setWeight(this));
        }
        this.inspections = inspections;
    }

    public Weight inspections(Set<Inspection> inspections) {
        this.setInspections(inspections);
        return this;
    }

    public Weight addInspection(Inspection inspection) {
        this.inspections.add(inspection);
        inspection.setWeight(this);
        return this;
    }

    public Weight removeInspection(Inspection inspection) {
        this.inspections.remove(inspection);
        inspection.setWeight(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Weight)) {
            return false;
        }
        return id != null && id.equals(((Weight) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Weight{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
