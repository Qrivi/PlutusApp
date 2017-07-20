package be.plutus.core.model;

public enum CardStatus{

    ACTIVE,     // Card exists and data was fetched
    CONFIRMED,  // Card exists and e-mail has been confirmed
    INACTIVE,   // Card does not exist but data was previously fetched
    BLOCKED     // Card was blocked for use with Plutus
}
