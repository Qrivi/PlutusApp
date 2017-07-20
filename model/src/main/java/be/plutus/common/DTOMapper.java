package be.plutus.common;

public interface DTOMapper<MODEL, DTO>{

    DTO map( MODEL model );
}
