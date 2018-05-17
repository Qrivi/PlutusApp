package be.plutus.api.response;

import be.plutus.common.DateService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.ZonedDateTime;

@JsonPropertyOrder( {
        "status",
        "request",
        "requestISO8601"
} )
public class Meta{

    private int status;
    private ZonedDateTime request;

    Meta( int status, ZonedDateTime requestTimestamp ){
        this.status = status;
        this.request = requestTimestamp;
    }

    public static Meta success(){
        return new Builder().success().build();
    }

    public static Meta created(){
        return new Builder().created().build();
    }

    public static Meta accepted(){
        return new Builder().accepted().build();
    }

    public static Meta noContent(){
        return new Builder().noContent().build();
    }

    public static Meta badRequest(){
        return new Builder().badRequest().build();
    }

    public static Meta forbidden(){
        return new Builder().forbidden().build();
    }

    public static Meta methodNotAllowed(){
        return new Builder().methodNotAllowed().build();
    }

    public static Meta unsupportedMediaType(){
        return new Builder().unsupportedMediaType().build();
    }

    public static Meta payloadTooLarge(){
        return new Builder().payloadTooLarge().build();
    }

    public static Meta notFound(){
        return new Builder().notFound().build();
    }

    public static Meta internalServerError(){
        return new Builder().internalServerError().build();
    }

    public int getStatus(){
        return status;
    }

    public ZonedDateTime getRequest(){
        return request;
    }

    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ" )
    public ZonedDateTime getRequestISO8601(){
        return request;
    }

    public static class Builder<B extends Builder<B>>{

        protected int status;
        protected ZonedDateTime timestamp;

        public B success(){
            this.timestamp = DateService.now();
            this.status = 200;
            return (B)this;
        }

        public B created(){
            this.timestamp = DateService.now();
            this.status = 201;
            return (B)this;
        }

        public B accepted(){
            this.timestamp = DateService.now();
            this.status = 202;
            return (B)this;
        }

        public B noContent(){
            this.timestamp = DateService.now();
            this.status = 204;
            return (B)this;
        }

        public B badRequest(){
            this.timestamp = DateService.now();
            this.status = 400;
            return (B)this;
        }

        public B unauthorized(){
            this.timestamp = DateService.now();
            this.status = 401;
            return (B)this;
        }

        public B forbidden(){
            this.timestamp = DateService.now();
            this.status = 403;
            return (B)this;
        }

        public B methodNotAllowed(){
            this.timestamp = DateService.now();
            this.status = 405;
            return (B)this;
        }

        public B unsupportedMediaType(){
            this.timestamp = DateService.now();
            this.status = 415;
            return (B)this;
        }

        public B payloadTooLarge(){
            this.timestamp = DateService.now();
            this.status = 413;
            return (B)this;
        }

        public B serverError(){
            this.timestamp = DateService.now();
            this.status = 500;
            return (B)this;
        }

        public B notFound(){
            this.timestamp = DateService.now();
            this.status = 404;
            return (B)this;
        }

        public B internalServerError(){
            this.timestamp = DateService.now();
            this.status = 500;
            return (B)this;
        }

        public Meta build(){
            return new Meta( status, timestamp );
        }
    }
}
