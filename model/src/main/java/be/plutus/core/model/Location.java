package be.plutus.core.model;

import be.plutus.common.Identifiable;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table( name = "location" )
public class Location extends Identifiable{

    @NotBlank( message = "{NotBlank.Location.label}" )
    @Column( name = "label", unique = true )
    private String label;

    @Column( name = "name", unique = true )
    private String name;

    @Valid
    @NotNull( message = "{NotNull.Location.campus}" )
    @ManyToOne
    @JoinColumn( name = "campus_id" )
    private Campus campus;

    public Location(){
    }

    public String getLabel(){
        return label;
    }

    public void setLabel( String label ){
        this.label = label;
    }

    public String getName(){
        return name;
    }

    public void setName( String name ){
        this.name = name;
    }

    public Campus getCampus(){
        return campus;
    }

    public void setCampus( Campus campus ){
        this.campus = campus;
    }
}
