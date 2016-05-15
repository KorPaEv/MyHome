package com.korpaev.myhome;

import io.realm.RealmObject;

public class SensorsInfoDb extends RealmObject
{
    private String _idDevice;
    private int _hProtocolVer; // версия протокола
    private int _hTimeStamp; // время юниксовое - передавалось в секундах uint32
    private int _hNumSensor; // номер датчика
    private int _hLenBody; // длина тела смс после заголовочной инфы

    private String _bLocationSensor; // расположение датчика
    private String _bLocationSensorRus; // расположение датчика на русском для переименования
    private String _bValSensor; // значение датчика
    private String _bNumRelay; // номер реле
    private String _bLocationRelay; // расположение реле
    private String _bLocationRelayRus; // расположение реле на русском для переименования привязанного
    private String _bPinRelay; // пин реле
    private boolean _bStateRelay; // состояние реле
    private boolean _bManualManageRelay; //автоматика
    private int _minTempEdge; // минимальный предел температуры
    private int _maxTempEdge; // максимальный предел температуры
    private boolean _turnOnRelayWithSensor; // щелкать ли реле при превышении температурных порогов
    private boolean _turnOffRelayWithSensor; // щелкать ли реле при показаниях температуры в пределах заданных границ

    //Тут имена полей и имена методов get и set должны совпадать!!! Это особенность либы
    public String get_idDevice() { return _idDevice; }
    public int get_hProtocolVer() { return _hProtocolVer; }
    public int get_hTimeStamp() { return _hTimeStamp; }
    public int get_hNumSensor() { return _hNumSensor; }
    public int get_hLenBody() { return _hLenBody; }

    public String get_bLocationSensor() { return _bLocationSensor; }
    public String get_bLocationSensorRus() { return _bLocationSensorRus; }
    public String get_bValSensor() { return _bValSensor; }
    public String get_bNumRelay() { return _bNumRelay; }
    public String get_bLocationRelay() { return _bLocationRelay; }
    public String get_bLocationRelayRus() { return _bLocationRelayRus; }
    public String get_bPinRelay() { return _bPinRelay; }
    public boolean get_bStateRelay() { return _bStateRelay; }
    public boolean get_bManualManageRelay() { return _bManualManageRelay; }
    public int get_minTempEdge() { return _minTempEdge; }
    public int get_maxTempEdge() { return _maxTempEdge; }
    public boolean get_turnOnRelayWithSensor() { return _turnOnRelayWithSensor; }
    public boolean get_turnOffRelayWithSensor() { return _turnOffRelayWithSensor; }

    public void set_idDevice(String idDevice) { this._idDevice = idDevice; }
    public void set_hProtocolVer(int hProtocolVer) { this._hProtocolVer = hProtocolVer; }
    public void set_hTimeStamp(int hTimeStamp) { this._hTimeStamp = hTimeStamp; }
    public void set_hNumSensor(int hNumSensor) { this._hNumSensor = hNumSensor; }
    public void set_hLenBody(int hLenBody) { this._hLenBody = hLenBody; }

    public void set_bLocationSensor(String bLocationSensor) { this._bLocationSensor = bLocationSensor; }
    public void set_bLocationSensorRus(String bLocationSensorRus) { this._bLocationSensorRus = bLocationSensorRus; }
    public void set_bValSensor(String bValSensor) { this._bValSensor = bValSensor; }
    public void set_bNumRelay(String bNumRelay) { this._bNumRelay = bNumRelay; }
    public void set_bLocationRelay(String bLocationRelay) { this._bLocationRelay = bLocationRelay; }
    public void set_bLocationRelayRus(String bLocationRelayRus) { this._bLocationRelayRus = bLocationRelayRus; }
    public void set_bPinRelay(String bPinRelay) { this._bPinRelay = bPinRelay; }
    public void set_bStateRelay(boolean bStateRelay){ this._bStateRelay = bStateRelay; }
    public void set_bManualManageRelay(boolean bManualManageRelay) { _bManualManageRelay = bManualManageRelay; }
    public void set_minTempEdge(int minTempEdge) { _minTempEdge = minTempEdge; }
    public void set_maxTempEdge(int maxTempEdge) { _maxTempEdge = maxTempEdge; }
    public void set_turnOnRelayWithSensor(boolean turnOnRelayWithSensor) { _turnOnRelayWithSensor = turnOnRelayWithSensor; }
    public void set_turnOffRelayWithSensor(boolean turnOffRelayWithSensor) { _turnOffRelayWithSensor = turnOffRelayWithSensor; }
}
