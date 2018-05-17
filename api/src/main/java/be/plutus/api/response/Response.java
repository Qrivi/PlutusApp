package be.plutus.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.Collection;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class Response{

    private Meta meta;
    private Object data;
    private Collection<String> errors;

    private Response( Meta meta, Object data, Collection<String> errors ){
        this.meta = meta;
        this.data = data;
        this.errors = errors;
    }

    public Meta getMeta(){
        return meta;
    }

    public Object getData(){
        return data;
    }

    public Collection<String> getErrors(){
        return errors;
    }

    public static class Builder{

        private Meta meta;
        private Object data;
        private Collection<String> errors;

        public Builder meta( Meta meta ){
            this.meta = meta;
            return this;
        }

        public Builder data( Object data ){
            this.data = data;
            return this;
        }

        public Builder errors( String... errors ){
            this.errors = Arrays.asList( errors );
            return this;
        }

        public Builder errors( Collection<String> errors ){
            this.errors = errors;
            return this;
        }

        public Response build(){
            return new Response( meta, data, errors );
        }
    }
}
