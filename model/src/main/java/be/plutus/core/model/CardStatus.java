package be.plutus.core.model;

public enum CardStatus{

    ACTIVE,     // Card exists and data was fetched
    CONFIRMED,  // Card exists and e-mail has been confirmed
    UNCONFIRMED,  // Card exists but e-mail has not been confirmed
    BLOCKED     // Card was blocked for use with Plutus
}
