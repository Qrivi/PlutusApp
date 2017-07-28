package be.plutus.core.model;

import be.plutus.common.Identifiable;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table( name = "location" )
public class Location extends Identifiable{

    @Valid
    @NotNull( message = "{NotNull.Campus.label}" )
    @OneToOne( cascade = CascadeType.ALL, orphanRemoval = true )
    @JoinColumn( name = "label_id" )
    private Label label;

    @Column( name = "name", unique = true )
    private String name;

    @Valid
    @NotNull( message = "{NotNull.Location.campus}" )
    @ManyToOne
    @JoinColumn( name = "campus_id" )
    private Campus campus;

    public Location(){
    }

    public Label getLabel(){
        return label;
    }

    public void setLabel( Label label ){
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
