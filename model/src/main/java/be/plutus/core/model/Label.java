package be.plutus.core.model;

import be.plutus.common.Identifiable;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table( name = "label" )
public class Label extends Identifiable{

    @NotBlank( message = "{NotBlank.label.label}" )
    @Column( name = "label", unique = true )
    private String label;

    @Column( name = "english" )
    private String english;

    @Column( name = "french" )
    private String french;

    @Column( name = "dutch" )
    private String dutch;

    @Column( name = "german" )
    private String german;

    @Column( name = "spanish" )
    private String spanish;

    @Column( name = "portuguese" )
    private String portuguese;

    public Label(){
    }

    public String getLabel(){
        return label;
    }

    public void setLabel( String label ){
        this.label = label;
    }

    public String getEnglish(){
        return english;
    }

    public void setEnglish( String english ){
        this.english = english;
    }

    public String getFrench(){
        return french;
    }

    public void setFrench( String french ){
        this.french = french;
    }

    public String getDutch(){
        return dutch;
    }

    public void setDutch( String dutch ){
        this.dutch = dutch;
    }

    public String getGerman(){
        return german;
    }

    public void setGerman( String german ){
        this.german = german;
    }

    public String getSpanish(){
        return spanish;
    }

    public void setSpanish( String spanish ){
        this.spanish = spanish;
    }

    public String getPortuguese(){
        return portuguese;
    }

    public void setPortuguese( String portuguese ){
        this.portuguese = portuguese;
    }
}
