package be.plutus.consumer;

import be.plutus.common.CryptoService;
import be.plutus.consumer.config.Config;

import java.lang.reflect.Field;

public class Resource{

    private Endpoint endpoint;

    private Integer customerId;
    private String deviceid;
    private String devicedescription;
    private String locationcode;
    private Long UTC;

    private String username;
    private String password;

    private String firststart;
    private String appVersion;

    private Integer limit;
    private Integer offset;

    private String signature;

    public Resource( Endpoint endpoint, Integer customerId, String deviceid, String devicedescription, String locationcode, Long UTC, String username, String password, String firststart, String appVersion, Integer limit, Integer offset ){
        this.endpoint = endpoint;
        this.customerId = customerId;
        this.deviceid = deviceid;
        this.devicedescription = devicedescription;
        this.locationcode = locationcode;
        this.UTC = UTC;
        this.username = username;
        this.password = password;
        this.firststart = firststart;
        this.appVersion = appVersion;
        this.limit = limit;
        this.offset = offset;
    }

    public void sign( String key ){
        this.signature = null;
        this.signature = CryptoService.sha256( this.toURL(), key );
    }

    public String toURL(){
        Field[] fields = this.getClass().getDeclaredFields();
        StringBuilder output = new StringBuilder();
        output.append( endpoint.url );

        try{
            for( int i = 0; i < fields.length; i++ ){
                Field field = fields[i];
                field.setAccessible( true );

                output.append( i == 0 ? "?" : "&" )
                        .append( field.getName() )
                        .append( "=" )
                        .append( field.get( this ) );
            }
        }catch( IllegalAccessException e ){
            e.printStackTrace();
            return "error";
            // I think in reality it's impossible to get here
        }

        return output.toString();
    }

    enum Endpoint{
        REQUEST_KEY( Config.API_HOST + Config.API_REQUEST_KEY ),
        USER_PROFILE( Config.API_HOST + Config.API_USER_PROFILE ),
        TRANSACTINS( Config.API_HOST + Config.API_TRANSACTIONS );

        String url;

        Endpoint( String url ){
            this.url = url;
        }
    }

    public static class Builder{

        private Endpoint endpoint;

        private Integer customerId;
        private String deviceid;
        private String devicedescription;
        private String locationcode;
        private Long UTC;

        private String username;
        private String password;

        private String firststart;
        private String appVersion;

        private Integer limit;
        private Integer offset;

        public Builder endpoint( Endpoint endpoint ){
            this.endpoint = endpoint;
            return this;
        }

        public Builder customerId( Integer customerId ){
            this.customerId = customerId;
            return this;
        }

        public Builder deviceid( String deviceid ){
            this.deviceid = deviceid;
            return this;
        }

        public Builder devicedescription( String devicedescription ){
            this.devicedescription = devicedescription;
            return this;
        }

        public Builder locationcode( String locationcode ){
            this.locationcode = locationcode;
            return this;
        }

        public Builder UTC( Long UTC ){
            this.UTC = UTC;
            return this;
        }

        public Builder username( String username ){
            this.username = username;
            return this;
        }

        public Builder password( String password ){
            this.password = password;
            return this;
        }

        public Builder firststart( String firststart ){
            this.firststart = firststart;
            return this;
        }

        public Builder appVersion( String appVersion ){
            this.appVersion = appVersion;
            return this;
        }

        public Builder limit( Integer limit ){
            this.limit = limit;
            return this;
        }

        public Builder offset( Integer offset ){
            this.offset = offset;
            return this;
        }

        public Resource build(){
            return new Resource( endpoint, customerId, deviceid, devicedescription, locationcode, UTC, username, password, firststart, appVersion, limit, offset );
        }
    }
}
