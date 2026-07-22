package adamas.traccs.mta_20_06;

import java.util.List;

/**
 * Created by arshad on 07/12/2016.
 */

public class Roster_Info {

    private String RecordNo  ;
    public String getRecordNo(){ return this.RecordNo ;}
    public void setRecordNo(String val   ){  this.RecordNo=val ;}

    private String Roster_Date  ;
    public String getRoster_Date(){ return this.Roster_Date ;}
    public void setRoster_Date(String val   ){  this.Roster_Date=val ;}

    private String ServiceType  ;
    public String getServiceType(){ return this.ServiceType ;}
    public void setServiceType(String val   ){  this.ServiceType=val ;}

    private String Service_Detail  ;
    public String getService_Detail(){ return this.Service_Detail ;}
    public void setService_Detail(String val   ){  this.Service_Detail=val ;}

    private String Start_Time  ;
    public String getStart_Time(){ return this.Start_Time ;}
    public void setStart_Time(String val   ){  this.Start_Time=val ;}

    private String Duration  ;
    public String getDuration(){ return this.Duration ;}
    public void setDuration(String val   ){  this.Duration=val;}

    private String Client_code  ;
    public String getClient_code(){ return this.Client_code ;}
    public void setClient_code(String val   ){  this.Client_code=val ;}


    private String Carer_code  ;
    public String getCarer_code(){ return this.Carer_code ;}
    public void setCarer_code(String val   ){  this.Carer_code=val ;}

    private String Program  ;
    public String getProgram(){ return this.Program ;}
    public void setProgram(String val   ){  this.Program=val ;}

    public String get_Calculated_Duration(){
        double val_o=Double.parseDouble(this.Duration);
        val_o=val_o*5;

        int val=(int)val_o;

        int hours=(val)/60;
        int min=(val)%60;

        if (hours<1)  hours=0;

        String str=(hours<10 ? "0"+ hours : hours) +  ":" + (min<10 ? "0"+ min : min);
        return str ;

    }
    public String get_End_Time(){
        double val_o=Double.parseDouble(this.Duration);
        val_o=val_o*5;
        int val=(int)val_o;
        String[] strStart = this.Start_Time.split(":") ;
        String str="";
        if ((val)%60+ Integer.parseInt(strStart[1])==60)
            str=((val)/60 + Integer.parseInt( strStart[0]))+1 + ":00" ;
        else if ((val)%60+ Integer.parseInt((strStart[1]))>60)
            str=get_proper_time((val/60 + Integer.parseInt(strStart[0])) +1) + ":" + get_proper_time(Math.abs((60-((val)%60+ Integer.parseInt(strStart[1])))));
        else{
            int hr=Integer.parseInt( strStart[0]);
            str=get_proper_time((val/60 + hr)) + ":" + (((val%60==0) && (Integer.parseInt(strStart[1])==0))? "00" : get_proper_time(((val)%60+ Integer.parseInt(strStart[1]))));
            //str=((val*5)/60 + Integer.parseInt( strStart[0])) + ":" +  ((val*5)%60+ Integer.parseInt(strStart[1]));
        }
        str=str.replace("000", "00");
        return str ;
    }

    private String get_proper_time(int t_val){
        if (t_val<=0 || t_val<1)
            return t_val + "00";
        else if (t_val<10)
            return "0"+ t_val ;
        else
            return "" + t_val;
}

    private int DayNo  ;
    public int getDayNo(){ return this.DayNo ;}
    public void setDayNo(int val   ){  this.DayNo=val ;}

    private int MonthNo  ;
    public int getMonthNo(){ return this.MonthNo ;}
    public void setMonthNo(int val   ){  this.MonthNo=val ;}

    private int YearNo  ;
    public int getYearNo(){ return this.YearNo ;}
    public void setYearNo(int val   ){  this.YearNo=val ;}

    private int blockNo;
    public int getBlockNo(){ return this.blockNo ;}
    public void setBlockNo(int val   ){  this.blockNo=val ;}

    private String notes;
    public String getNotes(){ return this.notes ;}
    public void setNotes(String val   ){  this.notes=val ;}

    private String roster_type;
    public String getRoster_type(){ return this.roster_type ;}
    public void setRoster_type(String val   ){  this.roster_type=val ;}

    private String _started;
    public String getStarted(){ return this._started ;}
    public void setStarted(String val   ){  this._started=val ;}

    private String _completed;
    public String getCompleted(){ return this._completed ;}
    public void setCompleted(String val   ){  this._completed=val ;}

    private String _Actual_Client_Code;
    public String getActual_Client_Code(){ return this._Actual_Client_Code ;}
    public void setActual_Client_Code(String val   ){  this._Actual_Client_Code=val ;}

    private String _UniqueID;
    public String getUniqueID(){ return this._UniqueID ;}
    public void setUniqueID(String val   ){  this._UniqueID=val ;}


    private String _servicesetting;
    public String getServiceSetting(){ return this._servicesetting ;}
    public void setServiceSetting(String val   ){  this._servicesetting=val ;}


    private String _TA_LOGINMODE ;
    public String getTA_LOGINMODE (){ return this._TA_LOGINMODE ;}
    public void setTA_LOGINMODE (String val   ){  this._TA_LOGINMODE=val ;}


    private String _TA_EXCLUDEGEOLOCATION  ;
    public String getTA_EXCLUDEGEOLOCATION  (){ return this._TA_EXCLUDEGEOLOCATION ;}
    public void setTA_EXCLUDEGEOLOCATION (String val   ){  this._TA_EXCLUDEGEOLOCATION=val ;}


    private String _Group_Alerts  ;
    public String getGroup_Alerts(){ return this._Group_Alerts ;}
    public void setGroup_Alerts (String val   ){  this._Group_Alerts=val ;}

    private String _KM  ;
    public String getKM(){ return this._KM ;}
    public void setKM (String val   ){  this._KM=val ;}

    private String _InfoOnly="0"  ;
    public String getInfoOnly(){ return this._InfoOnly ;}
    public void setInfoOnly (String val   ){  this._InfoOnly=val ;}

    private String _Address="0"  ;
    public String getAddress(){ return this._Address ;}
    public void setAddress (String val   ){  this._Address=val ;}

    private String _Debtor="-"  ;
    public String getDebtor(){ return this._Debtor ;}
    public void setDebtor (String val   ){  this._Debtor=val ;}

    private String _Fee="0"  ;
    public String getFee(){ return this._Fee ;}
    public void setFee (String val   ){  this._Fee=val ;}

    private String _DisplayFeeInApp="0"  ;
    public String getDisplayFeeInApp(){ return this._DisplayFeeInApp ;}
    public void setDisplayFeeInApp (String val   ){  this._DisplayFeeInApp=val ;}

    private String _DisplayDebtorInApp="0"  ;
    public String getDisplayDebtorInApp(){ return this._DisplayDebtorInApp ;}
    public void setDisplayDebtorInApp (String val   ){  this._DisplayDebtorInApp=val ;}

    private String MinorGroup="";
    public String getMinorGroup() {
        return MinorGroup;
    }
    public void setMinorGroup(String minorGroup) {
        MinorGroup = minorGroup;
    }

    private String MTAServiceType="";
    public String getMTAServiceType() {
        return MTAServiceType;
    }
    public void setMTAServiceType(String mtaServiceType) {
        MTAServiceType = mtaServiceType;
    }

    private String _book_date="";
    public String getBook_date() {
        return _book_date;
    }
    public void setBook_date(String book_date) {
        _book_date = book_date;
    }

    private String _MyOwnCarStatus="";
    public String getMyOwnCarStatus() {
        return _MyOwnCarStatus;
    }
    public void setMyOwnCarStatus(String book_date) {
        _MyOwnCarStatus = book_date;
    }

    private String _Disable_Shift_Start_Alarm;
    public String getDisable_Shift_Start_Alarm() { return _Disable_Shift_Start_Alarm; }
    public void	setDisable_Shift_Start_Alarm(String value) { _Disable_Shift_Start_Alarm = value; }

    private String _Disable_Shift_End_Alarm;
    public String getDisable_Shift_End_Alarm() { return _Disable_Shift_End_Alarm; }
    public void	setDisable_Shift_End_Alarm(String value) { _Disable_Shift_End_Alarm = value; }

    private String _TA_MultiShift;
    public String getTA_MultiShift() { return _TA_MultiShift; }
    public void	setTA_MultiShift(String value) { _TA_MultiShift = value; }

    private List<Group_Recipient> _group_Recipients;
    public List<Group_Recipient> get_group_Recipients() { return _group_Recipients; }
    public void	set_group_Recipients(List<Group_Recipient> value) { _group_Recipients = value; }

    private List<Group_Recipient> _group_Recipients2;
    public List<Group_Recipient> get_group_Recipients2() { return _group_Recipients2; }
    public void	set_group_Recipients2(List<Group_Recipient> value) { _group_Recipients2 = value; }

    private String _ACCOUNTINGIDENTIFIER;

    public String get_ACCOUNTINGIDENTIFIER() {
        return _ACCOUNTINGIDENTIFIER;
    }

    public void set_ACCOUNTINGIDENTIFIER(String _ACCOUNTINGIDENTIFIER) {
        this._ACCOUNTINGIDENTIFIER = _ACCOUNTINGIDENTIFIER;
    }

    private String  RunsheetAlerts;

    public String getRunsheetAlerts() {
        return RunsheetAlerts;
    }

    public void setRunsheetAlerts(String runsheetAlerts) {
        RunsheetAlerts = runsheetAlerts;
    }
private String PersonId ;

    public String getPersonId() {
        return PersonId;
    }

    public void setPersonId(String personId) {
        PersonId = personId;
    }

    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private String  Actual_Start_Time="";
    private String  Actual_End_Time="";

    public String getActual_Start_Time() {
        return Actual_Start_Time;
    }

    public void setActual_Start_Time(String actual_Start_Time) {
        Actual_Start_Time = actual_Start_Time;
    }

    public String getActual_End_Time() {
        return Actual_End_Time;
    }

    public void setActual_End_Time(String actual_End_Time) {
        Actual_End_Time = actual_End_Time;
    }

    public String getExcludeFromAppLogging() {
        return ExcludeFromAppLogging;
    }

    private String ExcludeFromAppLogging;
    public void setExcludeFromAppLogging(String excludeFromAppLogging) {
        ExcludeFromAppLogging = excludeFromAppLogging;
    }
    private String ForceShiftReport;
    public void setForceShiftReport(String forceShiftReport) {
        ForceShiftReport = forceShiftReport;
    }

    public String getForceShiftReport() {
        return ForceShiftReport;
    }

    private String HasServiceNotes;
    public void setHasServiceNotes(String hasServiceNotes) {
        HasServiceNotes = hasServiceNotes;
    }

    public String getHasServiceNotes() {
        return HasServiceNotes;
    }

    //irfan
    private String AcceptedbyStaff;
    public void setAcceptedbyStaff(String acceptedbyStaff) {
        AcceptedbyStaff = acceptedbyStaff;
    }

    public String getAcceptedbyStaff() {
        return AcceptedbyStaff;
    }

    private String NewAcceptDate;
    public void setNewAcceptDate(String newAcceptDate) {
        NewAcceptDate = newAcceptDate;
    }

    public String getNewAcceptDate() {
        return NewAcceptDate;
    }

}
