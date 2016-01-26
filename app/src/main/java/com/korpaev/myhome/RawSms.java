package com.korpaev.myhome;

public class RawSms
{
    //                            _idSms;                  locationSensor;  _valSensor;   _numRelay; _locationRelay;  _pinRelay;    _stateRelay
    //Формат передаваемой строки:    1;         SHome;           BR;           27C;          4;          BLR;           25;           0
    //                        или 1;SmartHome;BoilerRoom;27C;RN
    //                    или газ 6;SmartHome;GasSensor;150;2;GasRoom;22;ON
    //
    private int _idSms; // порядковый номер смс
    private String _locationSensor; // расположение датчика
    private String _valSensor; // значение датчика
    private String _numRelay; // номер реле
    private String _locationRelay; // расположение реле
    private String _pinRelay; // пин реле
    private boolean _stateRelay; // состояние реле

    public int getIdSms(){
        return _idSms;
    }
    public String getLocationNameSensor(){
        return _locationSensor;
    }
    public String getValSensor(){
        return _valSensor;
    }
    public String getNumRelay(){
        return _numRelay;
    }
    public String getLocationNameRelay(){
        return _locationRelay;
    }
    public String getPinRelay(){
        return _pinRelay;
    }
    public boolean getStateRelay(){ return _stateRelay; }

    public void setIdSms(int idSms){
        _idSms = idSms;
    }
    public void setLocationNameSensor(String locationSensor){
        _locationSensor = locationSensor;
    }
    public void setValSensor(String valSensor){
        _valSensor = valSensor;
    }
    public void setNumRelay(String numRelay){
        _numRelay = numRelay;
    }
    public void setLocationNameRelay(String locationRelay){
        _locationRelay = locationRelay;
    }
    public void setPinRelay(String pinRelay){
        _pinRelay = pinRelay;
    }
    public void setStateRelay(String stateRelay){ _stateRelay = Boolean.getBoolean(stateRelay); }

    public void ParseSms(String smsBody)
    {
        String[] splitSmsBody;
        splitSmsBody = smsBody.split(";");

        setIdSms(Integer.parseInt(splitSmsBody[0].trim()));
        setLocationNameSensor(splitSmsBody[2].trim());
        setValSensor(splitSmsBody[3].trim());

        if (splitSmsBody[4].trim().contains("RN"))
        {
            setNumRelay(splitSmsBody[4].trim());
            setLocationNameRelay("RN");
            setPinRelay("RN");
            setStateRelay("0");
        }
        else
        {
            setNumRelay(splitSmsBody[4].trim());
            setLocationNameRelay(splitSmsBody[5].trim());
            setPinRelay(splitSmsBody[6].trim());
            setStateRelay(splitSmsBody[7].trim());
        }
    }
}
