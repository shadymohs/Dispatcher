package com.saptco.dispatcher.data.model;

import java.io.Serializable;

public class TripInfo implements Serializable {

    private static volatile TripInfo instance;

    private TripInfo(){
    }

    public static TripInfo getInstance() {
        if (instance == null)
            instance = new TripInfo();
        return instance;
    }

    private String tripCode;
    private String tripDate;
    private Long stationID;
    private String ticketNumber;
    private Boolean isValidCall = false;
    private String errorMessage;
    private Boolean isCamScanned = false;
    private Boolean isDispatch = true;

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public Long getStationID() {
        return stationID;
    }

    public void setStationID(Long stationID) {
        this.stationID = stationID;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Boolean getValidCall() {
        return isValidCall;
    }

    public void setValidCall(Boolean validCall) {
        isValidCall = validCall;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean getCamScanned() {
        return isCamScanned;
    }

    public void setCamScanned(Boolean camScanned) {
        isCamScanned = camScanned;
    }

    public Boolean getDispatch() {
        return isDispatch;
    }

    public void setDispatch(Boolean dispatch) {
        isDispatch = dispatch;
    }
}
