package be.plutus.core.model;

public enum CardStatus{

    NEW,            // Card has just been added and transactions are still being fetched
    ACTIVE,         // Card exists and data was fetched
    BROKEN,         // Fetching data fails; was password changed?
    BLOCKED         // Card was blocked for use with Plutus
}
