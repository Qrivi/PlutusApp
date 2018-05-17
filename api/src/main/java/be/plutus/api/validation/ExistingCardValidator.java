package be.plutus.api.validation;

import be.plutus.core.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistingCardValidator implements ConstraintValidator<ExistingCardId, Integer>{

    private final CardService cardService;

    @Autowired
    public ExistingCardValidator( CardService cardService ){
        this.cardService = cardService;
    }

    @Override
    public void initialize( ExistingCardId id ){
    }

    @Override
    public boolean isValid( Integer value, ConstraintValidatorContext context ){
        return cardService.getCardById( value ) != null;
    }
}