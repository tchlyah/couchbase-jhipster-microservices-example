package com.couchbase.example.brewery.domain;

import org.springframework.data.annotation.Id;
import com.couchbase.client.java.repository.annotation.Field;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import static com.couchbase.example.brewery.config.Constants.ID_DELIMITER;
import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

/**
 * A Beer.
 */
@Document
public class Beer implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String PREFIX = "beer";

    @SuppressWarnings("unused")
    @IdPrefix
    private String prefix = PREFIX;

    @Id
    @GeneratedValue(strategy = UNIQUE, delimiter = ID_DELIMITER)
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("category")
    private String category;

    @Field("description")
    private String description;

    @Field("style")
    private String style;

    @Field("brewery")
    private String brewery;

    @Field("abv")
    private Float abv;

    @Field("ibu")
    private Integer ibu;

    @Field("srm")
    private Integer srm;

    @Field("upc")
    private Integer upc;

    @Field("updated")
    private LocalDate updated;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Beer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public Beer category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public Beer description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStyle() {
        return style;
    }

    public Beer style(String style) {
        this.style = style;
        return this;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getBrewery() {
        return brewery;
    }

    public Beer brewery(String brewery) {
        this.brewery = brewery;
        return this;
    }

    public void setBrewery(String brewery) {
        this.brewery = brewery;
    }

    public Float getAbv() {
        return abv;
    }

    public Beer abv(Float abv) {
        this.abv = abv;
        return this;
    }

    public void setAbv(Float abv) {
        this.abv = abv;
    }

    public Integer getIbu() {
        return ibu;
    }

    public Beer ibu(Integer ibu) {
        this.ibu = ibu;
        return this;
    }

    public void setIbu(Integer ibu) {
        this.ibu = ibu;
    }

    public Integer getSrm() {
        return srm;
    }

    public Beer srm(Integer srm) {
        this.srm = srm;
        return this;
    }

    public void setSrm(Integer srm) {
        this.srm = srm;
    }

    public Integer getUpc() {
        return upc;
    }

    public Beer upc(Integer upc) {
        this.upc = upc;
        return this;
    }

    public void setUpc(Integer upc) {
        this.upc = upc;
    }

    public LocalDate getUpdated() {
        return updated;
    }

    public Beer updated(LocalDate updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(LocalDate updated) {
        this.updated = updated;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Beer beer = (Beer) o;
        if (beer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), beer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Beer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", category='" + getCategory() + "'" +
            ", description='" + getDescription() + "'" +
            ", style='" + getStyle() + "'" +
            ", brewery='" + getBrewery() + "'" +
            ", abv=" + getAbv() +
            ", ibu=" + getIbu() +
            ", srm=" + getSrm() +
            ", upc=" + getUpc() +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
