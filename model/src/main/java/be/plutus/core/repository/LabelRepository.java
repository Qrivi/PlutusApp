package be.plutus.core.repository;

import be.plutus.core.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label, Integer>{

    Label findByLabel( String label );

    List<Label> findByEnglishIgnoreCase( String english );

    List<Label> findByFrenchIgnoreCase( String french );

    List<Label> findByDutchIgnoreCase( String dutch );

    List<Label> findByGermanIgnoreCase( String german );

    List<Label> findBySpanishIgnoreCase( String spanish );

    List<Label> findByPortugueseIgnoreCase( String portuguese );
}
