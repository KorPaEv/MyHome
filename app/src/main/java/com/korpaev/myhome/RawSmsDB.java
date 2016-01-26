package com.korpaev.myhome;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RawSmsDB extends RealmObject
{
    private int _idSms; // порядковый номер смс
    private String _locationSensor; // расположение датчика
    private String _valSensor; // значение датчика
    private String _numRelay; // номер реле
    private String _locationRelay; // расположение реле
    private String _pinRelay; // пин реле
    private boolean _stateRelay; // состояние реле

    public int getIdSms(){ return _idSms; }
    public String getLocationNameSensor() { return _locationSensor; }
    public String getValSensor() { return _valSensor; }
    public String getNumRelay() { return _numRelay; }
    public String getLocationNameRelay() { return _locationRelay; }
    public String getPinRelay() { return _pinRelay; }
    public boolean getStateRelay() { return _stateRelay; }

    public void setIdSms(int idSms) { this._idSms = idSms; }
    public void setLocationNameSensor(String locationSensor) { this._locationSensor = locationSensor; }
    public void setValSensor(String valSensor) { this._valSensor = valSensor; }
    public void setNumRelay(String numRelay) { this._numRelay = numRelay; }
    public void setLocationNameRelay(String locationRelay) { this._locationRelay = locationRelay; }
    public void setPinRelay(String pinRelay) { this._pinRelay = pinRelay; }
    public void setStateRelay(boolean stateRelay){ this._stateRelay = stateRelay; }
}
