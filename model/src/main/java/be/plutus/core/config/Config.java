package be.plutus.core.config;

import java.time.ZoneId;

public class Config{

    public static final ZoneId DEFAULT_TIMEZONE = ZoneId.systemDefault();

    public static final long DEFAULT_TOKEN_TTL = 14; // days
}
