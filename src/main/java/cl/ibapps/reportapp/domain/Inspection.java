package cl.ibapps.reportapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Inspection.
 */
@Entity
@Table(name = "inspection")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Inspection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "report_date")
    private Instant reportDate;

    @Column(name = "description")
    private String description;

    @Column(name = "place")
    private String place;

    @Column(name = "inspection_date")
    private Instant inspectionDate;

    @Column(name = "grower")
    private String grower;

    @Column(name = "packing_date")
    private Instant packingDate;

    @Column(name = "observations")
    private String observations;

    @Column(name = "final_recomendations")
    private String finalRecomendations;

    @Column(name = "extra_details")
    private String extraDetails;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inspections" }, allowSetters = true)
    private Label label;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inspections" }, allowSetters = true)
    private Exporter exporter;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inspections" }, allowSetters = true)
    private PhytoChina phytoChina;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inspections" }, allowSetters = true)
    private Dispatchs dispatchs;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inspections" }, allowSetters = true)
    private Packages packages;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inspections" }, allowSetters = true)
    private Weight weight;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inspections" }, allowSetters = true)
    private Specie specie;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inspections" }, allowSetters = true)
    private Variety variety;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inspections" }, allowSetters = true)
    private Color color;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inspections" }, allowSetters = true)
    private FinalOverall finalOverall;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inspections" }, allowSetters = true)
    private ListSizes listSizes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inspection id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getReportDate() {
        return this.reportDate;
    }

    public Inspection reportDate(Instant reportDate) {
        this.setReportDate(reportDate);
        return this;
    }

    public void setReportDate(Instant reportDate) {
        this.reportDate = reportDate;
    }

    public String getDescription() {
        return this.description;
    }

    public Inspection description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return this.place;
    }

    public Inspection place(String place) {
        this.setPlace(place);
        return this;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Instant getInspectionDate() {
        return this.inspectionDate;
    }

    public Inspection inspectionDate(Instant inspectionDate) {
        this.setInspectionDate(inspectionDate);
        return this;
    }

    public void setInspectionDate(Instant inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getGrower() {
        return this.grower;
    }

    public Inspection grower(String grower) {
        this.setGrower(grower);
        return this;
    }

    public void setGrower(String grower) {
        this.grower = grower;
    }

    public Instant getPackingDate() {
        return this.packingDate;
    }

    public Inspection packingDate(Instant packingDate) {
        this.setPackingDate(packingDate);
        return this;
    }

    public void setPackingDate(Instant packingDate) {
        this.packingDate = packingDate;
    }

    public String getObservations() {
        return this.observations;
    }

    public Inspection observations(String observations) {
        this.setObservations(observations);
        return this;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getFinalRecomendations() {
        return this.finalRecomendations;
    }

    public Inspection finalRecomendations(String finalRecomendations) {
        this.setFinalRecomendations(finalRecomendations);
        return this;
    }

    public void setFinalRecomendations(String finalRecomendations) {
        this.finalRecomendations = finalRecomendations;
    }

    public String getExtraDetails() {
        return this.extraDetails;
    }

    public Inspection extraDetails(String extraDetails) {
        this.setExtraDetails(extraDetails);
        return this;
    }

    public void setExtraDetails(String extraDetails) {
        this.extraDetails = extraDetails;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Inspection user(User user) {
        this.setUser(user);
        return this;
    }

    public Label getLabel() {
        return this.label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Inspection label(Label label) {
        this.setLabel(label);
        return this;
    }

    public Exporter getExporter() {
        return this.exporter;
    }

    public void setExporter(Exporter exporter) {
        this.exporter = exporter;
    }

    public Inspection exporter(Exporter exporter) {
        this.setExporter(exporter);
        return this;
    }

    public PhytoChina getPhytoChina() {
        return this.phytoChina;
    }

    public void setPhytoChina(PhytoChina phytoChina) {
        this.phytoChina = phytoChina;
    }

    public Inspection phytoChina(PhytoChina phytoChina) {
        this.setPhytoChina(phytoChina);
        return this;
    }

    public Dispatchs getDispatchs() {
        return this.dispatchs;
    }

    public void setDispatchs(Dispatchs dispatchs) {
        this.dispatchs = dispatchs;
    }

    public Inspection dispatchs(Dispatchs dispatchs) {
        this.setDispatchs(dispatchs);
        return this;
    }

    public Packages getPackages() {
        return this.packages;
    }

    public void setPackages(Packages packages) {
        this.packages = packages;
    }

    public Inspection packages(Packages packages) {
        this.setPackages(packages);
        return this;
    }

    public Weight getWeight() {
        return this.weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public Inspection weight(Weight weight) {
        this.setWeight(weight);
        return this;
    }

    public Specie getSpecie() {
        return this.specie;
    }

    public void setSpecie(Specie specie) {
        this.specie = specie;
    }

    public Inspection specie(Specie specie) {
        this.setSpecie(specie);
        return this;
    }

    public Variety getVariety() {
        return this.variety;
    }

    public void setVariety(Variety variety) {
        this.variety = variety;
    }

    public Inspection variety(Variety variety) {
        this.setVariety(variety);
        return this;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Inspection color(Color color) {
        this.setColor(color);
        return this;
    }

    public FinalOverall getFinalOverall() {
        return this.finalOverall;
    }

    public void setFinalOverall(FinalOverall finalOverall) {
        this.finalOverall = finalOverall;
    }

    public Inspection finalOverall(FinalOverall finalOverall) {
        this.setFinalOverall(finalOverall);
        return this;
    }

    public ListSizes getListSizes() {
        return this.listSizes;
    }

    public void setListSizes(ListSizes listSizes) {
        this.listSizes = listSizes;
    }

    public Inspection listSizes(ListSizes listSizes) {
        this.setListSizes(listSizes);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inspection)) {
            return false;
        }
        return id != null && id.equals(((Inspection) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inspection{" +
            "id=" + getId() +
            ", reportDate='" + getReportDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", place='" + getPlace() + "'" +
            ", inspectionDate='" + getInspectionDate() + "'" +
            ", grower='" + getGrower() + "'" +
            ", packingDate='" + getPackingDate() + "'" +
            ", observations='" + getObservations() + "'" +
            ", finalRecomendations='" + getFinalRecomendations() + "'" +
            ", extraDetails='" + getExtraDetails() + "'" +
            "}";
    }
}
