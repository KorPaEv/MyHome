package com.korpaev.myhome;

public class SmsParser
{
    //                            _idSms;            _locationSensor;   _valSensor;  _numRelay;   _locationRelay;  _pinRelay;  _stateRelay
    //Формат передаваемой строки:   1;    SmartHome;   BoilerRoom;      27C;          4;          Boiler;           25;           OFF
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
    public boolean getStateRelay(){
        return _stateRelay;
    }

    private void setIdSms(int idSms){
        _idSms = idSms;
    }
    private void setLocationNameSensor(String locationSensor){
        _locationSensor = locationSensor;
    }
    private void setValSensor(String valSensor){
        _valSensor = valSensor;
    }
    private void setNumRelay(String numRelay){
        _numRelay = numRelay;
    }
    private void getLocationNameRelay(String locationRelay){
        _locationRelay = locationRelay;
    }
    private void setPinRelay(String pinRelay){
        _pinRelay = pinRelay;
    }
    private void setStateRelay(String stateRelay){
        if (stateRelay.contains("ON"))
            _stateRelay = true;
        if (stateRelay.contains("OFF"))
            _stateRelay = false;
    }

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
            getLocationNameRelay("RN");
            setPinRelay("RN");
            setStateRelay("OFF");
        }
        else
        {
            setNumRelay(splitSmsBody[4].trim());
            getLocationNameRelay(splitSmsBody[5].trim());
            setPinRelay(splitSmsBody[6].trim());
            setStateRelay(splitSmsBody[7].trim());
        }
    }
}
