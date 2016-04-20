package com.korpaev.myhome;

import io.realm.RealmObject;

public class RawSmsDb extends RealmObject
{
    private int _idDevice;
    private int _hProtocolVer; // версия протокола
    private int _hTimeStamp; // время юниксовое - передавалось в секундах uint32
    private int _hNumSensor; // номер датчика
    private int _hLenBody; // длина тела смс после заголовочной инфы

    private String _bLocationSensor; // расположение датчика
    private String _bValSensor; // значение датчика
    private String _bNumRelay; // номер реле
    private String _bLocationRelay; // расположение реле
    private String _bPinRelay; // пин реле
    private boolean _bStateRelay; // состояние реле

    //Тут имена полей и имена методов get и set должны совпадать!!! Это особенность либы
    public int get_idDevice() { return _idDevice; }
    public int get_hProtocolVer() { return _hProtocolVer; }
    public int get_hTimeStamp() { return _hTimeStamp; }
    public int get_hNumSensor() { return _hNumSensor; }
    public int get_hLenBody() { return _hLenBody; }

    public String get_bLocationSensor() { return _bLocationSensor; }
    public String get_bValSensor() { return _bValSensor; }
    public String get_bNumRelay() { return _bNumRelay; }
    public String get_bLocationRelay() { return _bLocationRelay; }
    public String get_bPinRelay() { return _bPinRelay; }
    public boolean get_bStateRelay() { return _bStateRelay; }

    public void set_idDevice(int idDevice) { this._idDevice = idDevice; }
    public void set_hProtocolVer(int hProtocolVer) { this._hProtocolVer = hProtocolVer; }
    public void set_hTimeStamp(int hTimeStamp) { this._hTimeStamp = hTimeStamp; }
    public void set_hNumSensor(int hNumSensor) { this._hNumSensor = hNumSensor; }
    public void set_hLenBody(int hLenBody) { this._hLenBody = hLenBody; }

    public void set_bLocationSensor(String bLocationSensor) { this._bLocationSensor = bLocationSensor; }
    public void set_bValSensor(String bValSensor) { this._bValSensor = bValSensor; }
    public void set_bNumRelay(String bNumRelay) { this._bNumRelay = bNumRelay; }
    public void set_bLocationRelay(String bLocationRelay) { this._bLocationRelay = bLocationRelay; }
    public void set_bPinRelay(String bPinRelay) { this._bPinRelay = bPinRelay; }
    public void set_bStateRelay(boolean bStateRelay){ this._bStateRelay = bStateRelay; }
}
