package com.saptco.dispatcher.data.repositories;

import com.saptco.dispatcher.R;
import com.saptco.dispatcher.data.model.LoggedInUser;
import com.saptco.dispatcher.data.model.SDPType;
import com.saptco.dispatcher.data.model.TripInfo;
import com.saptco.dispatcher.data.model.serviceBean.CheckTripServiceObject;
import com.saptco.dispatcher.data.model.serviceBean.DispatchServiceObject;
import com.saptco.dispatcher.data.model.serviceBean.LoginServiceObject;

import org.ksoap2.*;
import org.ksoap2.transport.*;
import org.ksoap2.serialization.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class DispatcherRepository {

    private static volatile DispatcherRepository instance;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private DispatcherRepository() {
    }

    public static DispatcherRepository getInstance() {
        if (instance == null) {
            instance = new DispatcherRepository();
        }
        return instance;
    }

    public LoggedInUser login(String username, String password,String wsNS, String wsURL,Boolean isArabic) throws Exception {
        // handle login
        if (username != null && !username.equals("") &&
                password != null && !password.equals("")) {
            String NAMESPACE = wsNS;
            String METHOD_NAME = "AuthorizeUser";
            String SOAP_ACTION = wsNS + "AuthorizeUser";
            String URL = wsURL;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            LoginServiceObject myObj = new LoginServiceObject(username, password);
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setNamespace(null);
            propertyInfo.setName("request");
            propertyInfo.setValue(myObj);
            propertyInfo.setType(LoginServiceObject.class);
            request.addProperty(propertyInfo);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, METHOD_NAME, LoginServiceObject.class);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            Boolean isAuthorized = Boolean.parseBoolean(response.getProperty("IsAuthorized").toString());
            if (isAuthorized) {
                LoggedInUser loggedUser = LoggedInUser.getInstance();
                loggedUser.setUserId(Long.parseLong(response.getProperty("UserId").toString()));
                loggedUser.setDisplayName(response.getProperty("Name").toString());
                loggedUser.setUserName(username);
                loggedUser.setPassword(password);
                SoapObject sdpsResult = (SoapObject) response.getProperty("SdpStation");
                if (sdpsResult != null && sdpsResult.getPropertyCount() >0) {
                    List<SDPType> sdps = new ArrayList<SDPType>();
                    for (int i=0; i<sdpsResult.getPropertyCount(); i++) {
                        SoapObject soapSDP = (SoapObject) sdpsResult.getProperty(i);
                        SDPType sdpObj = new SDPType();
                        sdpObj.setSdpId(Long.parseLong(soapSDP.getProperty("SdpId").toString()));
                        sdpObj.setSdpCode(soapSDP.getProperty("SdpCode").toString());
                        if(isArabic)
                            sdpObj.setSdpName(soapSDP.getProperty("SdpLocaleName").toString());
                        else
                            sdpObj.setSdpName(soapSDP.getProperty("SdpForeignName").toString());
                        if (Boolean.parseBoolean(soapSDP.getProperty("IsMainSdp").toString()))
                            loggedUser.setLoggedInSDP(sdpObj.getSdpId());
                        sdpObj.setStationID(Long.parseLong(soapSDP.getProperty("MainStationId").toString()));
                        sdps.add(sdpObj);
                    }
                    loggedUser.setSdpsList(sdps);
                }
                return loggedUser;
            }
        }
        return null;
    }

    public TripInfo checkTrip(TripInfo trip, LoggedInUser userInf, String wsNS, String wsURL) throws Exception{
        String NAMESPACE = wsNS;
        String METHOD_NAME = "CheckTripStatus";
        String SOAP_ACTION = wsNS + "CheckTripStatus";
        String URL = wsURL;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        CheckTripServiceObject myObj = new CheckTripServiceObject(userInf.getUserName(), userInf.getPassword(),
                userInf.getUserId(),userInf.getLoggedInSDP(), trip.getTripCode(),trip.getTripDate());
        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.setNamespace(null);
        propertyInfo.setName("request");
        propertyInfo.setValue(myObj);
        propertyInfo.setType(CheckTripServiceObject.class);
        request.addProperty(propertyInfo);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, METHOD_NAME, CheckTripServiceObject.class);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.call(SOAP_ACTION, envelope);
        SoapObject response = (SoapObject) envelope.getResponse();
        Boolean isAuthorized = Boolean.parseBoolean(response.getProperty("DispatchAllowed").toString());
        if (isAuthorized) {
            trip.setValidCall(true);
        }else{
            trip.setValidCall(false);
            trip.setErrorMessage(response.getProperty("ReturnDesc").toString());
        }
        return trip;
    }

    public TripInfo dispatchTicket(TripInfo trip, LoggedInUser userInf, String wsNS, String wsURL) throws Exception{
        if(trip.getDispatch()){
            String NAMESPACE = wsNS;
            String METHOD_NAME = "DispatchTicket";
            String SOAP_ACTION = wsNS + "DispatchTicket";
            String URL = wsURL;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            DispatchServiceObject myObj = new DispatchServiceObject(userInf.getUserName(), userInf.getPassword(),
                    userInf.getUserId(),userInf.getLoggedInSDP(), trip.getTripCode(),trip.getTripDate(),
                    trip.getTicketNumber(),trip.getStationID());
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setNamespace(null);
            propertyInfo.setName("request");
            propertyInfo.setValue(myObj);
            propertyInfo.setType(DispatchServiceObject.class);
            request.addProperty(propertyInfo);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, METHOD_NAME, DispatchServiceObject.class);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            String errorCode = response.getProperty("ErrorCode").toString();
            if (errorCode == null || errorCode.equals("") || errorCode.equals("000000")) {
                trip.setValidCall(true);
            }else{
                trip.setValidCall(false);
                trip.setErrorMessage(response.getProperty("ErrorMsg").toString());
            }
        }else
            trip = unDispatchTicket(trip,userInf,wsNS,wsURL);
        return trip;
    }

    public TripInfo unDispatchTicket(TripInfo trip, LoggedInUser userInf, String wsNS, String wsURL) throws Exception{
        String NAMESPACE = wsNS;
        String METHOD_NAME = "UndispatchTicket";
        String SOAP_ACTION = wsNS + "UndispatchTicket";
        String URL = wsURL;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        DispatchServiceObject myObj = new DispatchServiceObject(userInf.getUserName(), userInf.getPassword(),
                userInf.getUserId(),userInf.getLoggedInSDP(), trip.getTripCode(),trip.getTripDate(),
                trip.getTicketNumber(),trip.getStationID());
        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.setNamespace(null);
        propertyInfo.setName("request");
        propertyInfo.setValue(myObj);
        propertyInfo.setType(DispatchServiceObject.class);
        request.addProperty(propertyInfo);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, METHOD_NAME, DispatchServiceObject.class);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.call(SOAP_ACTION, envelope);
        SoapObject response = (SoapObject) envelope.getResponse();
        String errorCode = response.getProperty("ErrorCode").toString();
        if (errorCode == null || errorCode.equals("") || errorCode.equals("000000")) {
            trip.setValidCall(true);
        }else{
            trip.setValidCall(false);
            trip.setErrorMessage(response.getProperty("ErrorMsg").toString());
        }
        return trip;
    }
}