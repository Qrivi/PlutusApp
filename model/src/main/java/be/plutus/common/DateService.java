package be.plutus.common;

import be.plutus.core.config.Config;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateService{

    public static long unix(){
        return Instant.now().getEpochSecond();
    }

    public static ZonedDateTime now(){
        return ZonedDateTime.now( Config.DEFAULT_TIMEZONE );
    }

    public static ZonedDateTime convert( Date date ){
        return ZonedDateTime.ofInstant( date.toInstant(), Config.DEFAULT_TIMEZONE );
    }
}