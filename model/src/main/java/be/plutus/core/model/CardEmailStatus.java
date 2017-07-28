package be.plutus.core.model;

public enum CardEmailStatus{

    NOT_SET,        // E-mail address not yet set or set previously, but removed
    UNCONFIRMED,    // E-mail address set but not yet confirmed
    CONFIRMED       // E-mail address set and identity confirmed
}
