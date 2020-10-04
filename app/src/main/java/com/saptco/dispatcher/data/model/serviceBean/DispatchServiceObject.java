package com.saptco.dispatcher.data.model.serviceBean;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class DispatchServiceObject implements KvmSerializable{

    public String userName;
    public String password;
    private Long userId;
    private Long sdpId;
    private String tripCode;
    private String tripDate;
    private String ticketNumber;
    private Long stationId;

    public DispatchServiceObject(String userName, String password, Long userId,
                                 Long sdpId, String tripCode, String tripDate,
                                 String ticketNumber, Long stationId){
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.sdpId = sdpId;
        this.tripCode = tripCode;
        this.tripDate = tripDate;
        this.ticketNumber = ticketNumber;
        this.stationId = stationId;
    }

    @Override
    public Object getProperty(int arg0) {
        switch(arg0)
        {
            case 0:
                return userName;
            case 1:
                return password;
            case 2:
                return userId;
            case 3:
                return sdpId;
            case 4:
                return tripCode;
            case 5:
                return tripDate;
            case 6:
                return ticketNumber;
            case 7:
                return stationId;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 8;
    }

    @Override
    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
        switch(arg0)
        {
            case 0:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "UserName";
                break;
            case 1:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "Password";
                break;
            case 2:
                arg2.type = PropertyInfo.LONG_CLASS;
                arg2.name = "UserId";
                break;
            case 3:
                arg2.type = PropertyInfo.LONG_CLASS;
                arg2.name = "SdpId";
                break;
            case 4:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "TripCode";
                break;
            case 5:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "TripDate";
                break;
            case 6:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "TicketNumber";
                break;
            case 7:
                arg2.type = PropertyInfo.LONG_CLASS;
                arg2.name = "StationId";
                break;
            default:break;
        }

    }

    @Override
    public void setProperty(int arg0, Object arg1) {
        switch(arg0)
        {
            case 0:
                userName = arg1.toString();
                break;
            case 1:
                password = arg1.toString();
                break;
            case 2:
                userId = Long.valueOf(arg1.toString());
                break;
            case 3:
                sdpId = Long.valueOf(arg1.toString());
                break;
            case 4:
                tripCode = arg1.toString();
                break;
            case 5:
                tripDate = arg1.toString();
                break;
            case 6:
                ticketNumber = arg1.toString();
                break;
            case 7:
                stationId = Long.valueOf(arg1.toString());
                break;
            default:
                break;
        }
    }
}