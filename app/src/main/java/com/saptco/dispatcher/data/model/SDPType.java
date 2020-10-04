package com.saptco.dispatcher.data.model;

import java.io.Serializable;
import java.util.Locale;

public class SDPType  implements Serializable {

    private Long sdpId;
    private String sdpCode;
    private String sdpName;
    private Long stationID;

    public Long getSdpId() {
        return sdpId;
    }

    public void setSdpId(Long sdpId) {
        this.sdpId = sdpId;
    }

    public String getSdpCode() {
        return sdpCode;
    }

    public void setSdpCode(String sdpCode) {
        this.sdpCode = sdpCode;
    }

    @Override
    public String toString() {
        return sdpCode + " " + sdpName ;
    }

    public Long getStationID() {
        return stationID;
    }

    public void setStationID(Long stationID) {
        this.stationID = stationID;
    }

    public String getSdpName() {
        return sdpName;
    }

    public void setSdpName(String sdpName) {
        this.sdpName = sdpName;
    }
}
