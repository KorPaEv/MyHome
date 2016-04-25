package com.korpaev.myhome;

public class SensorsInfoRow
{
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
    private String _bValSensor; // значение датчика
    private String _bNumRelay; // номер реле
    private String _bLocationRelay; // расположение реле
    private String _bPinRelay; // пин реле
    private boolean _bStateRelay; // состояние реле

    public String getId() { return idDevice; }
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

    public void setId(String idDev) { idDevice = idDev; }
    public void set_hProtocolVer(int hProtocolVer) { _hProtocolVer = hProtocolVer; }
    public void set_hTimeStamp(int hTimeStamp) { _hTimeStamp = hTimeStamp; }
    public void set_hNumSensor(int hNumSensor) { _hNumSensor = hNumSensor; }
    public void set_hLenBody(int hLenBody) { _hLenBody = hLenBody; }
    public void set_bLocationSensor(String bLocationSensor) { _bLocationSensor = bLocationSensor; }
    public void set_bValSensor(String bValSensor) { _bValSensor = bValSensor; }
    public void set_bNumRelay(String bNumRelay) { _bNumRelay = bNumRelay; }
    public void set_bLocationRelay(String bLocationRelay) { _bLocationRelay = bLocationRelay; }
    public void set_bPinRelay(String bPinRelay) { _bPinRelay = bPinRelay; }
    public void set_bStateRelay(String bStateRelay) { _bStateRelay = Boolean.getBoolean(bStateRelay); }

    public void ParseSms(String smsBody)
    {
        String[] splitSmsBody;
        splitSmsBody = smsBody.split(";");

        set_hProtocolVer(Integer.parseInt(splitSmsBody[0].trim()));
        set_hTimeStamp(Integer.parseInt(splitSmsBody[1].trim()));
        set_hNumSensor(Integer.parseInt(splitSmsBody[2].trim()));
        set_hLenBody(Integer.parseInt(splitSmsBody[3].trim()));
        set_bLocationSensor(splitSmsBody[4].trim());
        set_bValSensor(splitSmsBody[5].trim());

        if (splitSmsBody[6].trim().contains("RN"))
        {
            set_bNumRelay(splitSmsBody[6].trim());
            set_bLocationRelay("RN");
            set_bPinRelay("RN");
            set_bStateRelay("0");
        }
        else
        {
            set_bNumRelay(splitSmsBody[6].trim());
            set_bLocationRelay(splitSmsBody[7].trim());
            set_bPinRelay(splitSmsBody[8].trim());
            set_bStateRelay(splitSmsBody[9].trim());
        }
    }
}
