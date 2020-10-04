package com.saptco.dispatcher.data.model.serviceBean;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class LoginServiceObject implements KvmSerializable{

    public String userName;
    public String password;

    public LoginServiceObject(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    @Override
    public Object getProperty(int arg0) {
        switch(arg0)
        {
            case 0:
                return userName;
            case 1:
                return password;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 2;
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
            default:
                break;
        }
    }

}