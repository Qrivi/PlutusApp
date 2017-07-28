package be.plutus.core.service;

import be.plutus.core.model.Label;

import java.util.List;

public interface LabelService{

    List<Label> getAllLabels();

    List<Label> getLabelsByEnglishTranslation( String translation );

    List<Label> getLabelsByFrenchTranslation( String translation );

    List<Label> getLabelsByDutchTranslation( String translation );

    List<Label> getLabelsByGermanTranslation( String translation );

    List<Label> getLabelsBySpanishTranslation( String translation );

    List<Label> getLabelsByPortugueseTranslation( String translation );

    Label getLabelById( Integer id );

    Label getLabelByLabel( String label );

    Label createLabel( String label, String english, String french, String dutch, String german, String spanish, String portuguese );

    void updateLabel( int id, String english, String french, String dutch, String german, String spanish, String portuguese );

    void removeLabel( int id );
}
