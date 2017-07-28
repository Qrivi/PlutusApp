package be.plutus.core.service;

import be.plutus.core.exception.DuplicateLabelException;
import be.plutus.core.exception.InvalidLabelIdentifierException;
import be.plutus.core.model.Label;
import be.plutus.core.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class LabelJPAService implements LabelService{

    private final LabelRepository repository;

    @Autowired
    public LabelJPAService( LabelRepository repository ){
        this.repository = repository;
    }

    @Override
    public List<Label> getAllLabels(){
        return repository.findAll();
    }

    @Override
    public List<Label> getLabelsByEnglishTranslation( String translation ){
        return repository.findByEnglishIgnoreCase( translation );
    }

    @Override
    public List<Label> getLabelsByFrenchTranslation( String translation ){
        return repository.findByFrenchIgnoreCase( translation );
    }

    @Override
    public List<Label> getLabelsByDutchTranslation( String translation ){
        return repository.findByDutchIgnoreCase( translation );
    }

    @Override
    public List<Label> getLabelsByGermanTranslation( String translation ){
        return repository.findByGermanIgnoreCase( translation );
    }

    @Override
    public List<Label> getLabelsBySpanishTranslation( String translation ){
        return repository.findBySpanishIgnoreCase( translation );
    }

    @Override
    public List<Label> getLabelsByPortugueseTranslation( String translation ){
        return repository.findByPortugueseIgnoreCase( translation );
    }

    @Override
    public Label getLabelById( Integer id ){
        if( id == null )
            throw new InvalidLabelIdentifierException();
        return repository.findOne( id );
    }

    @Override
    public Label getLabelByLabel( String label ){
        return repository.findByLabel( label );
    }

    @Override
    public Label createLabel( String label, String english, String french, String dutch, String german, String spanish, String portuguese ){
        Label labelObj = new Label();

        if( this.getLabelByLabel( label ) != null )
            throw new DuplicateLabelException( label );

        labelObj.setEnglish( english );
        labelObj.setFrench( french );
        labelObj.setDutch( dutch );
        labelObj.setGerman( german );
        labelObj.setSpanish( spanish );
        labelObj.setPortuguese( portuguese );

        return repository.save( labelObj );
    }

    @Override
    public void updateLabel( int id, String english, String french, String dutch, String german, String spanish, String portuguese ){
        Label labelObj = this.getLabelById( id );

        if( english != null )
            labelObj.setEnglish( english );
        if( french != null )
            labelObj.setFrench( french );
        if( dutch != null )
            labelObj.setDutch( dutch );
        if( german != null )
            labelObj.setGerman( german );
        if( spanish != null )
            labelObj.setSpanish( spanish );
        if( portuguese != null )
            labelObj.setPortuguese( portuguese );

        repository.save( labelObj );
    }

    @Override
    public void removeLabel( int id ){
        Label label = this.getLabelById( id );

        repository.delete( label );
    }
}
