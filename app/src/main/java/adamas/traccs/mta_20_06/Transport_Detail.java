package adamas.traccs.mta_20_06;

/**
 * Created by arshad on 16/11/2017.
 */

public class Transport_Detail {

    private String RecordNo;
    private String PickUpAddress1;
    private String DropOffAddress1;
    private String VehicleCode;
    private String StartODO;
    private String EndODO;
    private String Roster_Date;
    private String appointmentTime;
    private String Client_Code;
    private String Carer_Code;

    public String getRecordNo() {
        return RecordNo;
    }

    public void setRecordNo(String recordNo) {
        RecordNo = recordNo;
    }

    public String getPickUpAddress1() {
        return PickUpAddress1;
    }

    public void setPickUpAddress1(String pickUpAddress1) {
        PickUpAddress1 = pickUpAddress1;
    }

    public String getDropOffAddress1() {
        return DropOffAddress1;
    }

    public void setDropOffAddress1(String dropOffAddress1) {
        DropOffAddress1 = dropOffAddress1;
    }

    public String getVehicleCode() {
        return VehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        VehicleCode = vehicleCode;
    }

    public String getStartODO() {
        return StartODO;
    }

    public void setStartODO(String startODO) {
        StartODO = startODO;
    }

    public String getRoster_Date() {
        return Roster_Date;
    }

    public void setRoster_Date(String roster_Date) {
        Roster_Date = roster_Date;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getClient_Code() {
        return Client_Code;
    }

    public void setClient_Code(String client_Code) {
        Client_Code = client_Code;
    }

    public String getCarer_Code() {
        return Carer_Code;
    }

    public void setCarer_Code(String carer_Code) {
        Carer_Code = carer_Code;
    }

    public String getEndODO() {
        return EndODO;
    }

    public void setEndODO(String endODO) {
        EndODO = endODO;
    }

    private String Mobility;

    public String getMobility() {
        return Mobility;
    }

    public void setMobility(String mobility) {
        Mobility = mobility;
    }
}
