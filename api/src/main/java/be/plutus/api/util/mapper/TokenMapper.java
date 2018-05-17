package be.plutus.api.util.mapper;

import be.plutus.api.dto.response.TokenDTO;
import be.plutus.common.DTOMapper;
import be.plutus.core.model.Token;
import org.springframework.stereotype.Component;

@Component
public class TokenMapper implements DTOMapper<Token, TokenDTO>{

    @Override
    public TokenDTO map( Token token ){
        TokenDTO dto = new TokenDTO();
        dto.setToken( token.getToken() );
        dto.setExpires( token.getExpirationDate() );
        return dto;
    }
}