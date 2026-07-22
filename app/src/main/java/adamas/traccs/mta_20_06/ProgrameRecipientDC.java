package adamas.traccs.mta_20_06;

public class ProgrameRecipientDC {

    public ProgrameRecipientDC() {

        this.AccountNo                  =   "";
        this.ClientName                 =   "";
        this.PersonId                   =   "";
        this.Program                    =   "";
        this.ServiceType                =   "";
        this.Coordinator_Email          =   "";
    }

    private String AccountNo;
    public String getAccountNo () { return AccountNo; }
    public void setAccountNo(String value) { AccountNo = value; }

    private String ClientName;
    public String getClientName () { return ClientName; }
    public void setClientName(String value) { ClientName = value; }

    private String PersonId;
    public String getPersonId () { return PersonId; }
    public void setPersonId(String value) { PersonId = value; }

    private String Program;
    public String getProgram () { return Program; }
    public void setProgram(String value) { Program = value; }

    private String ServiceType;
    public String getServiceType () { return ServiceType; }
    public void setServiceType(String value) { ServiceType = value; }

    private String Coordinator_Email;
    public String getCoordinator_Email () { return Coordinator_Email; }
    public void setCoordinator_Email(String value) { Coordinator_Email = value; }

    private String Address;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}

