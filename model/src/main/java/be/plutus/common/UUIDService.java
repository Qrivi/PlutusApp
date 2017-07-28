package be.plutus.common;

import java.util.UUID;

public class UUIDService{

    public static String generate(){
        return UUID.randomUUID().toString();
    }
}