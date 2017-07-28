package be.plutus.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties( ignoreUnknown = true )
public class ProfileDTO{

    @JsonProperty( "fullName" )
    private String name;

    @JsonProperty( "departmentName" )
    private String department;

    @JsonProperty( "languageCode" )
    private String language;

    @JsonProperty( "personalBalance" )
    private String balance;

    @JsonProperty( "widgetSpent" )
    private String weekSpent;

    public ProfileDTO(){
    }

    public String getName(){
        return name;
    }

    public String getDepartment(){
        return department;
    }

    public String getLanguage(){
        return language;
    }

    public String getBalance(){
        return balance;
    }

    public String getWeekSpent(){
        return weekSpent;
    }
}