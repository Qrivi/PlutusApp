package be.plutus.core.model;

import be.plutus.common.CryptoService;
import be.plutus.common.Identifiable;
import be.plutus.common.IdentifierService;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table( name = "credentials" )
public class Credentials extends Identifiable{

    @NotBlank( message = "{NotBlank.Credentials.cardNumber}" )
    @Size( min = 8, max = 8, message = "{Size.Credentials.cardNumber}" )
    @Column( name = "card_number", unique = true )
    private String cardNumber;

    @NotBlank( message = "{NotBlank.Credentials.uuid}" )
    @Size( max = 36, message = "{Size.Credentials.uuid}" )
    @Column( name = "uuid", unique = true )
    private String uuid;

    @NotBlank( message = "{NotBlank.Credentials.password}" )
    @Size( max = 255, message = "{Size.Credentials.password}" ) // TODO set better length
    @Column( name = "password" )
    private String password;

    @NotNull( message = "{NotNull.Credentials.status}")
    @Enumerated( EnumType.STRING)
    private CredentialsStatus status;

    @Column( name = "customer_id", unique = true )
    private Integer customerId;

    @Size( min = 64, max = 64, message = "{Size.Credentials.key}" )
    @Column( name = "key" )
    private String key;

    @NotBlank( message = "{NotBlank.Credentials.deviceId}" )
    @Size( max = 36, message = "{Size.Credentials.deviceId}" )
    @Column( name = "device_id" )
    private String deviceId;

    @NotBlank( message = "{NotBlank.Credentials.deviceName}" )
    @Column( name = "device_name" )
    private String deviceName;

    @NotBlank( message = "{NotBlank.Credentials.userAgent}" )
    @Column( name = "user_agent" )
    private String userAgent;

    public Credentials(){
    }

    public String getCardNumber(){
        return cardNumber;
    }

    public void setCardNumber( String cardNumber ){
        this.cardNumber = cardNumber;
    }

    public String getPassword(){
        return CryptoService.decrypt( password, uuid );
    }

    public void setPassword( String plainTextPassword ){
        this.uuid = IdentifierService.generateUUID();
        this.password = CryptoService.encrypt( plainTextPassword, this.uuid );
    }

    public boolean isPasswordValid( String plainTextPassword ){
        if( plainTextPassword == null || this.password == null )
            return false;
        return Objects.equals( this.password, CryptoService.encrypt( plainTextPassword, this.uuid ) );

    }

    public CredentialsStatus getStatus(){
        return status;
    }

    public void setStatus( CredentialsStatus status ){
        this.status = status;
    }

    public Integer getCustomerId(){
        return customerId;
    }

    public void setCustomerId( Integer customerId ){
        this.customerId = customerId;
    }

    public String getKey(){
        return key;
    }

    public void setKey( String key ){
        this.key = key;
    }

    public String getDeviceId(){
        return deviceId;
    }

    public void setDeviceId( String deviceId ){
        this.deviceId = deviceId;
    }

    public String getDeviceName(){
        return deviceName;
    }

    public void setDeviceName( String deviceName ){
        this.deviceName = deviceName;
    }

    public String getUserAgent(){
        return userAgent;
    }

    public void setUserAgent( String userAgent ){
        this.userAgent = userAgent;
    }

    public boolean areUsable(){
        return this.status == CredentialsStatus.CORRECT && this.key != null && this.customerId != null;
    }
}
