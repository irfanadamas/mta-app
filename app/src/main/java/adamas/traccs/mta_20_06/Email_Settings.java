package adamas.traccs.mta_20_06;

/**
 * Created by arshad on 07/12/2016.
 */

public class Email_Settings {

    private String POP3Server;
    private String POP3User;
    private String POP3Password;
    private String SMTPServer;
    private String SMTPUser;
    private String SMTPPassword;
    private String FromEmail;
    private String FromDisplayName;
    private String  SMTP_Port;

    public void setPOP3Server (String value){
        this.POP3Server=value;
    }

    public String getPOP3Server (){
        return this.POP3Server;
    }

    public void setPOP3User (String value){
        this.POP3User=value;
    }

    public String getPOP3User (){
        return this.POP3User;
    }

    public void setPOP3Password (String value){
        this.POP3Password=value;
    }

    public String getPOP3Password (){
        return this.POP3Password;
    }

    public void setSMTPServer (String value){
        this.SMTPServer=value;
    }

    public String getSMTPServer (){
        return this.SMTPServer;
    }
    public void setSMTPUser(String value){
        this.SMTPUser=value;
    }

    public String getSMTPUser (){
        return this.SMTPUser;
    }
    public void setSMTPPassword(String value){
        this.SMTPPassword=value;
    }

    public String getSMTPPassword (){
        return this.SMTPPassword;
    }
    public void setFromEmail(String value){
        this.FromEmail=value;
    }

    public String getFromEmail (){
        return this.FromEmail;
    }
    public void setFromDisplayName(String value){
        this.FromDisplayName=value;
    }

    public String getFromDisplayName (){
        return this.FromDisplayName;
    }

    public void setSMTP_Port(String value){
        this.SMTP_Port=value;
    }

    public String getSMTP_Port (){
        return this.SMTP_Port;
    }

}
