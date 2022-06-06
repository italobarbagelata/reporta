package cl.ibapps.reportapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dispatchs.
 */
@Entity
@Table(name = "dispatchs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dispatchs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "dispatchs")
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

    public Dispatchs id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Dispatchs description(String description) {
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
            this.inspections.forEach(i -> i.setDispatchs(null));
        }
        if (inspections != null) {
            inspections.forEach(i -> i.setDispatchs(this));
        }
        this.inspections = inspections;
    }

    public Dispatchs inspections(Set<Inspection> inspections) {
        this.setInspections(inspections);
        return this;
    }

    public Dispatchs addInspection(Inspection inspection) {
        this.inspections.add(inspection);
        inspection.setDispatchs(this);
        return this;
    }

    public Dispatchs removeInspection(Inspection inspection) {
        this.inspections.remove(inspection);
        inspection.setDispatchs(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dispatchs)) {
            return false;
        }
        return id != null && id.equals(((Dispatchs) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dispatchs{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
