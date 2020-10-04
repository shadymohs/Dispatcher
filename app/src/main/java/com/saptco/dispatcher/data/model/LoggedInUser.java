package com.saptco.dispatcher.data.model;

import com.saptco.dispatcher.data.repositories.DispatcherRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser implements Serializable {

    private static volatile LoggedInUser instance;

    private LoggedInUser(){
    }

    public static LoggedInUser getInstance() {
        if (instance == null)
            instance = new LoggedInUser();
        return instance;
    }

    private Long userId;
    private String displayName;
    private Long loggedInSDP;
    private String selectedSDP;
    private List<SDPType> sdpsList = new ArrayList<SDPType>();
    private Long stationID;
    private String userName;
    private String password;
    private Boolean isPDA = false;
    private Boolean isArabic = true;
    private Boolean dispatchChaneLang = false;

    public List<SDPType> getSdpsList() {
        return sdpsList;
    }

    public void setSdpsList(List<SDPType> sdpsList) {
        this.sdpsList = sdpsList;
    }

    public Long getLoggedInSDP() {
        return loggedInSDP;
    }

    public void setLoggedInSDP(Long loggedInSDP) {
        this.loggedInSDP = loggedInSDP;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSelectedSDP() {
        return selectedSDP;
    }

    public void setSelectedSDP(String selectedSDP) {
        this.selectedSDP = selectedSDP;
    }

    public Long getStationID() {
        return stationID;
    }

    public void setStationID(Long stationID) {
        this.stationID = stationID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getPDA() {
        return isPDA;
    }

    public void setPDA(Boolean PDA) {
        isPDA = PDA;
    }

    public Boolean getArabic() {
        return isArabic;
    }

    public void setArabic(Boolean arabic) {
        isArabic = arabic;
    }

    public Boolean getDispatchChaneLang() {
        return dispatchChaneLang;
    }

    public void setDispatchChaneLang(Boolean dispatchChaneLang) {
        this.dispatchChaneLang = dispatchChaneLang;
    }
}