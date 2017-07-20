package be.plutus.core.model;

import java.util.Locale;

public enum CardLanguage{

    EN( "en", "English", "English" ),
    NL( "nl", "Dutch", "Nederlands" ),
    DE( "de", "German", "Deutsch" ),
    ES( "es", "Spanish", "Español" ),
    PT( "pt", "Portuguese", "Português" ),
    FR( "fr", "French", "français" );

    private final String tag;
    private final String name;
    private final String localName;

    CardLanguage( String tag, String name, String localName ){
        this.tag = tag;
        this.name = name;
        this.localName = localName;
    }

    @Override
    public String toString(){
        return name;
    }

    public String toLocalString(){
        return localName;
    }

    public String toTag(){
        return tag;
    }

    public Locale toLocale(){
        return new Locale( tag );w
    }
}
