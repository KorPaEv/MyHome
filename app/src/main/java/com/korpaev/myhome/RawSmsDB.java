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

    //Тут имена полей и имена методов get и set должны совпадать!!! Это особенность либы
    public int get_idSms(){ return _idSms; }
    public String get_locationSensor() { return _locationSensor; }
    public String get_valSensor() { return _valSensor; }
    public String get_numRelay() { return _numRelay; }
    public String get_locationRelay() { return _locationRelay; }
    public String get_pinRelay() { return _pinRelay; }
    public boolean get_stateRelay() { return _stateRelay; }

    public void set_idSms(int idSms) { this._idSms = idSms; }
    public void set_locationSensor(String locationSensor) { this._locationSensor = locationSensor; }
    public void set_valSensor(String valSensor) { this._valSensor = valSensor; }
    public void set_numRelay(String numRelay) { this._numRelay = numRelay; }
    public void set_locationRelay(String locationRelay) { this._locationRelay = locationRelay; }
    public void set_pinRelay(String pinRelay) { this._pinRelay = pinRelay; }
    public void set_stateRelay(boolean stateRelay){ this._stateRelay = stateRelay; }
}
