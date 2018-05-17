package be.plutus.api.util.service;

public interface MessageService{

    String get( String id );

    String get( String id, Object... args );
}
