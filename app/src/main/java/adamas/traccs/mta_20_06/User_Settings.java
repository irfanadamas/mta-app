package adamas.traccs.mta_20_06;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class User_Settings
{
	Date d= new Date();
	  DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");

	public User_Settings(){
		this._AllowSetTime="0";
		this._TMMode="0";
		this._MobileFutureLimit="0";
		this._AllowPicUpload="0";
		this._Apply_Goe_Location_Setting="False";
		this._KMAgainstTravelOnly="0";
		this._mobilegeocodelimit="5";
		this._StaffLocationUpdateInterval="0";
		this._AllowIncidentEntry="0";
		this._AllowTravelEntry="0";
		this._AllowClientNoteEntry="0";
		this._AllowRosterNoteEntry="0";
		this._StaffCode="";
		this._AllowRegisterSign="0";
		this._toDate= dateFormat2.format(d); // d.getYear() + "/" + set_leading_zero(d.getMonth(),2) + "/" + set_leading_zero( d.getDay(),2);
		this._time=d.getHours() + ":" + d.getMinutes();
		this._UserSessionLimit="10";
		this._ShowClientPhoneInApp="1";
		this._TA_TRAVELDEFAULT="-";
		this._CheckAlertInterval="0";
		this._AllowOPNote="0";
		this._AllowCaseNote="0";
		this._AllowIncidentNote="0";
		this._AppUsesSMTP="0";
		this._RosterRequested="0";
		this._AllowLeaveEntry="0";
		this._EnableRosterDelivery="0";
		this._MinimumInternetSpeedForOnline="2";
		this._GeolocateEnabled="0";
		this._HideGeolocation="0";
		this._Enable_Shift_End_Alarm="0";
		this._LeaveLeadTim="0";
		this._AllowViewGoalPlans="0";
		this._AcceptBookings="0";
		this._Coordinator_Email="-";
		this._ViewClientDocuments="0";
		this._ViewClientCareplans="0";
		this._RestrictTravelSameDay="0";
		this._HideGeolocation="0";
		this._EnableViewNoteCases="00000";
		this._EnableRosterAvailability="0";
		this._SuppressEmailOnRosterNote="0";
		this.AllowClinicalNoteEntry="false";
		this._GoogleAPI_Key= BuildConfig.GOOGLE_API_KEY;
		this.AllowMTASaveUserPass="1";
		this.Allow_OverWrite_Existing_Availability="1";
		this.UseServiceNoteAsShiftReport="0";
		this.UseOPNoteAsShiftReport="0";
		this.EmailUnavailabilityNotification="0";
		this.HideAddress="0";

		this.HideDOB="0";
		this.ApplyAcceptShift = "";
		this.enableDocExteranlView = "";
	}
	
	  private String set_leading_zero(int val,int size)
	  {
	     String new_val=String.valueOf(val);
	     size=size-(new_val.length());
	     for (int i =0; i<size; i++)
	     {
	        new_val="0" + new_val ;
	     }
	     return new_val;
	 } 
	  
	private String _AllowSetTime;
	public String getAllowSetTime () { return _AllowSetTime; }
	public void setAllowSetTime(String value) { _AllowSetTime = value; }
	
	private String _TMMode;
	public String getTMMode ()	 { return _TMMode; }
	public void	setTMMode(String value) { _TMMode = value; }
	
	private String _MobileFutureLimit;
	public String getMobileFutureLimit () { return _MobileFutureLimit; }
	public void	setMobileFutureLimit(String value) { _MobileFutureLimit = value; }
	
	private String _AllowPicUpload;
	public String getAllowPicUpload (){ return _AllowPicUpload; }
	public void setAllowPicUpload(String value)	 { _AllowPicUpload = value; }
	
	private String _Apply_Goe_Location_Setting;
	public String getApply_Goe_Location_Setting () { return _Apply_Goe_Location_Setting; }
	public void	setApply_Goe_Location_Setting(String value) { _Apply_Goe_Location_Setting = value; }
	
	private String _KMAgainstTravelOnly;
	public String getKMAgainstTravelOnly () { return _KMAgainstTravelOnly; }
	public void	setKMAgainstTravelOnly(String value) { _KMAgainstTravelOnly = value; }
	
	private String _mobilegeocodelimit;
	public String getMobilegeocodelimit () { return _mobilegeocodelimit; }
	public void	setMobilegeocodelimit(String value) { _mobilegeocodelimit = value; }
	
	private String _StaffLocationUpdateInterval;
	public String getStaffLocationUpdateInterval ()	 { return _StaffLocationUpdateInterval; }
	public void	setStaffLocationUpdateInterval(String value) { _StaffLocationUpdateInterval = value; }
	
	private String _AllowIncidentEntry;
	public String getAllowIncidentEntry () { return _AllowIncidentEntry; }
	public void	setAllowIncidentEntry(String value) { _AllowIncidentEntry = value; }
	

	private String _AllowTravelEntry;
	public String getAllowTravelEntry (){ return _AllowTravelEntry; }
	public void	setAllowTravelEntry(String value) { _AllowTravelEntry = value; }
	
	private String _AllowClientNoteEntry;
	public String getAllowClientNoteEntry () { return _AllowClientNoteEntry; }
	public void	setAllowClientNoteEntry(String value) { _AllowClientNoteEntry = value; }
	
	private String _AllowRosterNoteEntry;
	public String getAllowRosterNoteEntry () { return _AllowRosterNoteEntry; }
	public void	setAllowRosterNoteEntry(String value) { _AllowRosterNoteEntry = value; }
	
	private String _StaffCode;
	public String getStaffCode () { return _StaffCode; }
	public void	setStaffCode(String value) { _StaffCode = value; }
	
	private String _AllowRegisterSign;
	public String getAllowRegisterSign () { return _AllowRegisterSign; }
	public void	setAllowRegisterSign(String value) { _AllowRegisterSign = value; }
	
	private String _toDate;
	public String getToDate () { return _toDate; }
	public void	setToDate(String value) { _toDate = value; }
	
	
	private String _time;
	public String getTime () { return _time; }
	public void	setTime(String value) { _time = value; }
	
	private String _UserSessionLimit;
	public String getUserSessionLimit () { return _UserSessionLimit; }
	public void	setUserSessionLimit(String value) { _UserSessionLimit = value; }
	
	private String _ShowClientPhoneInApp;
	public String getShowClientPhoneInApp () { return _ShowClientPhoneInApp; }
	public void	setShowClientPhoneInApp(String value) { _ShowClientPhoneInApp = value; }

	private String _TA_TRAVELDEFAULT;
	public String getTA_TRAVELDEFAULT() { return _TA_TRAVELDEFAULT; }
	public void	setTA_TRAVELDEFAULT(String value) { _TA_TRAVELDEFAULT = value; }

	private String _CheckAlertInterval;
	public String getCheckAlertInterval() { return _CheckAlertInterval; }
	public void	setCheckAlertInterval(String value) { _CheckAlertInterval = value; }


	private String _AllowOPNote;
	public String getAllowOPNote() { return _AllowOPNote; }
	public void	setAllowOPNote(String value) { _AllowOPNote = value; }

	private String _AllowCaseNote;
	public String getAllowCaseNote() { return _AllowCaseNote; }
	public void	setAllowCaseNote(String value) { _AllowCaseNote = value; }

	private String _AllowIncidentNote;
	public String getAllowIncidentNote() { return _AllowIncidentNote; }
	public void	setAllowIncidentNote(String value) { _AllowIncidentNote = value; }
	
	private String _AppUsesSMTP;
	public String getAppUsesSMTP() { return _AppUsesSMTP; }
	public void	setAppUsesSMTP(String value) { _AppUsesSMTP = value; }
	
	private String _RosterRequested;
	public String getRosterRequested() { return _RosterRequested; }
	public void	setRosterRequested(String value) { _RosterRequested = value; }
	
	private String _AllowLeaveEntry;
	public String getAllowLeaveEntry() { return _AllowLeaveEntry; }
	public void	setAllowLeaveEntry(String value) { _AllowLeaveEntry = value; }
	
	private String _EnableRosterDelivery;
	public String getEnableRosterDelivery() { return _EnableRosterDelivery; }
	public void	setEnableRosterDelivery(String value) { _EnableRosterDelivery = value; }
	
	private String _MinimumInternetSpeedForOnline;
	public String getMinimumInternetSpeedForOnline() { return _MinimumInternetSpeedForOnline; }
	public void	setMinimumInternetSpeedForOnline(String value) { _MinimumInternetSpeedForOnline = value; }

	private String _GeolocateEnabled;
	public String getGeolocateEnabled() { return _GeolocateEnabled; }
	public void	setGeolocateEnabled(String value) { _GeolocateEnabled = value; }

	private String _HideGeolocation;
	public String getHideGeolocation() { return _HideGeolocation; }
	public void	setHideGeolocation(String value) { _HideGeolocation = value; }

	private String _Enable_Shift_Start_Alarm;
	public String getEnable_Shift_Start_Alarm() { return _Enable_Shift_Start_Alarm; }
	public void	setEnable_Shift_Start_Alarm(String value) { _Enable_Shift_Start_Alarm = value; }

	private String _Enable_Shift_End_Alarm;
	public String getEnable_Shift_End_Alarm() { return _Enable_Shift_End_Alarm; }
	public void	setEnable_Shift_End_Alarm(String value) { _Enable_Shift_End_Alarm = value; }

	private String _LeaveLeadTim;
	public String getLeaveLeadTim() { return _LeaveLeadTim; }
	public void	setLeaveLeadTim(String value) { _LeaveLeadTim = value; }



	private String _AcceptBookings;
	public String getAcceptBookings() { return _AcceptBookings; }
	public void	setAcceptBookings(String value) { _AcceptBookings = value; }


	private String _AllowViewGoalPlans;


	public String getAllowViewGoalPlans() {
		return _AllowViewGoalPlans;
	}

	public void setAllowViewGoalPlans(String value) {
		this._AllowViewGoalPlans = value;
	}

	private String _Coordinator_Email;

	public String getCoordinator_Email() {
		return _Coordinator_Email;
	}

	public void setCoordinator_Email(String coordinator_Email) {
		_Coordinator_Email = coordinator_Email;
	}

	private String _ViewClientDocuments;

	public String get_ViewClientDocuments() {
		return _ViewClientDocuments;
	}

	public void set_ViewClientDocuments(String _ViewClientDocuments) {
		this._ViewClientDocuments = _ViewClientDocuments;
	}

	private String _ViewClientCareplans;

	public String get_ViewClientCareplans() {
		return _ViewClientCareplans;
	}

	public void set_ViewClientCareplans(String _ViewClientCareplans) {
		this._ViewClientCareplans = _ViewClientCareplans;
	}

	private String _RestrictTravelSameDay;

	public String get_RestrictTravelSameDay() {
		return _RestrictTravelSameDay;
	}

	public void set_RestrictTravelSameDay(String _RestrictTravelSameDay) {
		this._RestrictTravelSameDay = _RestrictTravelSameDay;
	}

	private String HIdeCancelButton;

	public String getHIdeCancelButton() {
		return HIdeCancelButton;
	}

	public void setHIdeCancelButton(String HIdeCancelButton) {
		this.HIdeCancelButton = HIdeCancelButton;
	}

	private String _EnableViewNoteCases;

	public String get_EnableViewNoteCases() {
		return _EnableViewNoteCases;
	}

	public void set_EnableViewNoteCases(String EnableViewNoteCases) {
		this._EnableViewNoteCases = EnableViewNoteCases;
	}

	private String _EnableRosterAvailability;

	public String getEnableRosterAvailability() {
		return _EnableRosterAvailability;
	}

	public void setEnableRosterAvailability(String EnableRosterAvailability) {
		this._EnableRosterAvailability = EnableRosterAvailability;
	}

	private String _SuppressEmailOnRosterNote;
	public void setSuppressEmailOnRosterNote(String SuppressEmailOnRosterNote) {
		this._SuppressEmailOnRosterNote = SuppressEmailOnRosterNote;
	}
	public String getSuppressEmailOnRosterNote() {
		return _SuppressEmailOnRosterNote;
	}


	private String AllowClinicalNoteEntry;

	public String getAllowClinicalNoteEntry() {
		return AllowClinicalNoteEntry;
	}

	public void setAllowClinicalNoteEntry(String clinicalNoteEntry) {
		AllowClinicalNoteEntry = clinicalNoteEntry;
	}

	private String _GoogleAPI_Key;

	public String get_GoogleAPI_Key() {
		return _GoogleAPI_Key;
	}

	public void set_GoogleAPI_Key(String _GoogleAPI_Key) {
		this._GoogleAPI_Key = _GoogleAPI_Key;
	}

	private String AllowMTASaveUserPass;

	public String getAllowMTASaveUserPass() {
		return AllowMTASaveUserPass;
	}

	public void setAllowMTASaveUserPass(String allowMTASaveUserPass) {
		AllowMTASaveUserPass = allowMTASaveUserPass;
	}

	private String Allow_OverWrite_Existing_Availability ;

    public String isAllow_OverWrite_Existing_Availability() {
        return Allow_OverWrite_Existing_Availability;
    }

    public void setAllow_OverWrite_Existing_Availability(String allow_OverWrite_Existing_Availability) {
        Allow_OverWrite_Existing_Availability = allow_OverWrite_Existing_Availability;
    }



    private String UseServiceNoteAsShiftReport;

	public String getUseServiceNoteAsShiftReport() {
		return UseServiceNoteAsShiftReport;
	}

	public void setUseServiceNoteAsShiftReport(String useServiceNoteAsShiftReport) {
		UseServiceNoteAsShiftReport = useServiceNoteAsShiftReport;
	}

	private String UseOPNoteAsShiftReport;

	public String getUseOPNoteAsShiftReport() {
		return UseOPNoteAsShiftReport;
	}

	public void setUseOPNoteAsShiftReport(String useOPNoteAsShiftReport) {
		UseOPNoteAsShiftReport = useOPNoteAsShiftReport;
	}

	private String EmailUnavailabilityNotification;


	public String getEmailUnavailabilityNotification() {
		return EmailUnavailabilityNotification;
	}


	public void setEmailUnavailabilityNotification(String emailUnavailabilityNotification) {
		EmailUnavailabilityNotification = emailUnavailabilityNotification;
	}
	private String HideAddress;

	public String getHideAddress() {
		return HideAddress;
	}

	public void setHideAddress(String hideAddress) {
		HideAddress = hideAddress;
	}
	private String AgencyPortalText;

	private String AgencyPortal;

	private String PolicyLink;

	public String getAgencyPortal() {
		return AgencyPortal;
	}

	public void setAgencyPortal(String agencyPortal) {
		AgencyPortal = agencyPortal;
	}

	public String getAgencyPortalText() {
		return AgencyPortalText;
	}

	public void setAgencyPortalText(String agencyPortalText) {
		AgencyPortalText = agencyPortalText;
	}


	private String HideDOB;

    public String getHideDOB() {
        return HideDOB;
    }

    public void setHideDOB(String hideDOB) {
        HideDOB = hideDOB;
    }

    private String StaffLeaveEmailOverrides;
	private String ClientNoteEmail;
	private String ClientNoteEmailOverrides;
	private String StaffLeaveEmail;


	public String getStaffLeaveEmailOverrides() {
		return StaffLeaveEmailOverrides;
	}

	public void setStaffLeaveEmailOverrides(String staffLeaveEmailOverrides) {
		StaffLeaveEmailOverrides = staffLeaveEmailOverrides;
	}

	public String getStaffLeaveEmail() {
		return StaffLeaveEmail;
	}

	public void setStaffLeaveEmail(String staffLeaveEmail) {
		StaffLeaveEmail = staffLeaveEmail;
	}

	public String getClientNoteEmailOverrides() {
		return ClientNoteEmailOverrides;
	}

	public void setClientNoteEmailOverrides(String clientNoteEmailOverrides) {
		ClientNoteEmailOverrides = clientNoteEmailOverrides;
	}

	public String getClientNoteEmail() {
		return ClientNoteEmail;
	}

	public void setClientNoteEmail(String clientNoteEmail) {
		ClientNoteEmail = clientNoteEmail;
	}

	private String EnableEmailNotification;

	public String getEnableEmailNotification() {
		return EnableEmailNotification;
	}

	public void setEnableEmailNotification(String enableEmailNotification) {
		EnableEmailNotification = enableEmailNotification;
	}

	private String UserOverrideIncidentEmail;

	private String OverrideIncientEmail ;


	public String getOverrideIncientEmail() {
		return OverrideIncientEmail;
	}

	public void setOverrideIncientEmail(String overrideIncientEmail) {
		OverrideIncientEmail = overrideIncientEmail;
	}

	public String getUserOverrideIncidentEmail() {
		return UserOverrideIncidentEmail;
	}

	public void setUserOverrideIncidentEmail(String userOverrideIncidentEmail) {
		UserOverrideIncidentEmail = userOverrideIncidentEmail;
	}

	public String getPolicyLink() {
		return PolicyLink;
	}

	public void setPolicyLink(String policyLink) {
		PolicyLink = policyLink;
	}

	private String ApplyAcceptShift;

	public String getApplyAcceptShift() {
		return ApplyAcceptShift;
	}

	public void setApplyAcceptShift(String applyAcceptShift) {
		ApplyAcceptShift = applyAcceptShift;
	}

	//EnableDocExteranlView

	private String enableDocExteranlView;

	public String getEnableDocExteranlView() {
		return enableDocExteranlView;
	}

	public void setEnableDocExteranlView(String enableDocExteranlView) {
		enableDocExteranlView = enableDocExteranlView;
	}
}
