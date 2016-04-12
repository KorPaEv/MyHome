package com.korpaev.myhome;

public class RawSms
{
    //Формат передаваемой строки: Pver;Timestamp;NumSensor;LenBody;Gas;limitGas;gasRelay
    //                    или     Pver;Timestamp;NumSensor;LenBody;Gas;curGasValue;idRelay;locationR;relayPin;stateGas
    private int _idSms; // порядковый номер смс
    private String _locationSensor; // расположение датчика
    private String _valSensor; // значение датчика
    private String _numRelay; // номер реле
    private String _locationRelay; // расположение реле
    private String _pinRelay; // пин реле
    private boolean _stateRelay; // состояние реле

    public int get_idSms(){
        return _idSms;
    }
    public String get_locationSensor(){
        return _locationSensor;
    }
    public String get_valSensor(){
        return _valSensor;
    }
    public String get_numRelay(){
        return _numRelay;
    }
    public String get_locationRelay(){
        return _locationRelay;
    }
    public String get_pinRelay(){
        return _pinRelay;
    }
    public boolean get_stateRelay(){ return _stateRelay; }

    public void set_idSms(int idSms){
        _idSms = idSms;
    }
    public void set_locationSensor(String locationSensor){
        _locationSensor = locationSensor;
    }
    public void set_valSensor(String valSensor){ _valSensor = valSensor; }
    public void set_numRelay(String numRelay){
        _numRelay = numRelay;
    }
    public void set_locationRelay(String locationRelay){
        _locationRelay = locationRelay;
    }
    public void set_pinRelay(String pinRelay){
        _pinRelay = pinRelay;
    }
    public void set_stateRelay(String stateRelay){ _stateRelay = Boolean.getBoolean(stateRelay); }

    public void ParseSms(String smsBody)
    {
        String[] splitSmsBody;
        splitSmsBody = smsBody.split(";");

        set_idSms(Integer.parseInt(splitSmsBody[0].trim()));
        set_locationSensor(splitSmsBody[2].trim());
        set_valSensor(splitSmsBody[3].trim());

        if (splitSmsBody[4].trim().contains("RN"))
        {
            set_numRelay(splitSmsBody[4].trim());
            set_locationRelay("RN");
            set_pinRelay("RN");
            set_stateRelay("0");
        }
        else
        {
            set_numRelay(splitSmsBody[4].trim());
            set_locationRelay(splitSmsBody[5].trim());
            set_pinRelay(splitSmsBody[6].trim());
            set_stateRelay(splitSmsBody[7].trim());
        }
    }
}
