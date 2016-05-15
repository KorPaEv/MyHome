package com.korpaev.myhome;

public class SensorsInfoRow
{
    final String EMPTYDATA = "ArdNoNameS-";
    //Формат передаваемой строки ДАЛЛАС: Pver;Timestamp;NumSensor;LenBody;locSensor;currTemp;idRelay;locationR;relayPin;pinState
    //                           или     Pver;Timestamp;NumSensor;LenBody;locSensor;currTemp;idRelay(RN UNDEFINE)
    //Формат передаваемой строки ГАЗ:    Pver;Timestamp;NumSensor;LenBody;Gas;curGasValue;idRelay;locationR;relayPin;stateGas
    //                           или     Pver;Timestamp;NumSensor;LenBody;Gas;limitGas;gasRelay(RN UNDEFINE)
    private String idDevice;
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

    public String getId() { return idDevice; }
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

    public void setId(String idDev) { idDevice = idDev; }
    public void set_hProtocolVer(int hProtocolVer) { _hProtocolVer = hProtocolVer; }
    public void set_hTimeStamp(int hTimeStamp) { _hTimeStamp = hTimeStamp; }
    public void set_hNumSensor(int hNumSensor) { _hNumSensor = hNumSensor; }
    public void set_hLenBody(int hLenBody) { _hLenBody = hLenBody; }
    public void set_bLocationSensor(String bLocationSensor) { _bLocationSensor = bLocationSensor; }
    public void set_bLocationSensorRus(String bLocationSensorRus) { this._bLocationSensorRus = bLocationSensorRus; }
    public void set_bValSensor(String bValSensor) { _bValSensor = bValSensor; }
    public void set_bNumRelay(String bNumRelay) { _bNumRelay = bNumRelay; }
    public void set_bLocationRelay(String bLocationRelay) { _bLocationRelay = bLocationRelay; }
    public void set_bPinRelay(String bPinRelay) { _bPinRelay = bPinRelay; }
    public void set_bStateRelay(String bStateRelay) { _bStateRelay = Boolean.getBoolean(bStateRelay); }
    public void set_bManualManageRelay(String bManualManageRelay) { _bManualManageRelay = Boolean.getBoolean(bManualManageRelay); }
    public void set_bLocationRelayRus(String bLocationRelayRus) { this._bLocationRelayRus = bLocationRelayRus; }
    public void set_minTempEdge(int minTempEdge) { _minTempEdge = minTempEdge; }
    public void set_maxTempEdge(int maxTempEdge) { _maxTempEdge = maxTempEdge; }
    public void set_turnOnRelayWithSensor(boolean turnOnRelayWithSensor) { _turnOnRelayWithSensor = turnOnRelayWithSensor; }
    public void set_turnOffRelayWithSensor(boolean turnOffRelayWithSensor) { _turnOffRelayWithSensor = turnOffRelayWithSensor; }

    public void ParseSms(String smsBody)
    {
        String[] splitSmsBody;
        splitSmsBody = smsBody.split(";");

        set_hProtocolVer(Integer.parseInt(splitSmsBody[0].trim()));
        set_hTimeStamp(Integer.parseInt(splitSmsBody[1].trim()));
        set_hNumSensor(Integer.parseInt(splitSmsBody[2].trim()));
        set_hLenBody(Integer.parseInt(splitSmsBody[3].trim()));

        if (splitSmsBody[4].trim().equals("RN"))
        {
            set_bLocationSensor(EMPTYDATA + String.valueOf(get_hNumSensor()));
        }
        else set_bLocationSensor(splitSmsBody[4].trim());

        set_bValSensor(splitSmsBody[5].trim());

        if (splitSmsBody[6].trim().equals("RN"))
        {
            set_bNumRelay(null);
            set_bLocationRelay(null);
            set_bPinRelay(null);
            set_bStateRelay("0");
            set_bManualManageRelay("1");//по умолчанию если данных нет автоматика вкл
        }
        else
        {
            set_bNumRelay(splitSmsBody[6].trim());
            set_bLocationRelay(splitSmsBody[7].trim());
            set_bPinRelay(splitSmsBody[8].trim());
            set_bStateRelay(splitSmsBody[9].trim());
            set_bManualManageRelay(splitSmsBody[10].trim());
        }
    }
}
