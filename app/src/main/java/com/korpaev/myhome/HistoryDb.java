package com.korpaev.myhome;

import java.sql.Time;

import io.realm.RealmObject;

public class HistoryDb extends RealmObject
{
    private String _idDevice;
    private String _sMessage; // расположение реле
    private String _timeVal;

    //Тут имена полей и имена методов get и set должны совпадать!!! Это особенность либы
    public String get_idDevice() { return _idDevice; }
    public String get_sMessage() { return _sMessage; }
    public String get_timeVal() { return _timeVal.toString(); }

    public void set_idDevice(String idDevice) { this._idDevice = idDevice; }
    public void set_sMessage(String sMessage) { _sMessage = sMessage; }
    public void set_timeVal(String timeVal) { _timeVal = timeVal; }
}
