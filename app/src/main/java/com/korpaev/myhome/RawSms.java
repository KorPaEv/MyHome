package com.korpaev.myhome;

public class RawSms
{
    //                           _smsPackNum  _idSms; _locationSensor;   _valSensor;  _numRelay;   _locationRelay;  _pinRelay;  _stateRelay
    //Формат передаваемой строки:   0001;       1;         SmartHome;   BoilerRoom;      27C;          4;          Boiler;           25;           OFF
    //                        или 1;SmartHome;BoilerRoom;27C;RN
    //                    или газ 6;SmartHome;GasSensor;150;2;GasRoom;22;ON
    //
    private int _smsPackNum; //порядковый номер пакета смс
    private int _idSms; // порядковый номер смс
    private String _locationSensor; // расположение датчика
    private String _valSensor; // значение датчика
    private String _numRelay; // номер реле
    private String _locationRelay; // расположение реле
    private String _pinRelay; // пин реле
    private boolean _stateRelay; // состояние реле

    public int getSmsPackNum() { return _smsPackNum; }
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

    public void setSmsPackNum(int smsPackNum){
        _smsPackNum = smsPackNum;
    }
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
    public void setStateRelay(String stateRelay){
        if (stateRelay.contains("ON"))
            _stateRelay = true;
        if (stateRelay.contains("OFF"))
            _stateRelay = false;
    }

    public void ParseSms(String smsBody)
    {
        String[] splitSmsBody;
        splitSmsBody = smsBody.split(";");

        setSmsPackNum(Integer.parseInt(splitSmsBody[0].trim()));
        setIdSms(Integer.parseInt(splitSmsBody[1].trim()));
        setLocationNameSensor(splitSmsBody[3].trim());
        setValSensor(splitSmsBody[4].trim());

        if (splitSmsBody[5].trim().contains("RN"))
        {
            setNumRelay(splitSmsBody[5].trim());
            setLocationNameRelay("RN");
            setPinRelay("RN");
            setStateRelay("OFF");
        }
        else
        {
            setNumRelay(splitSmsBody[5].trim());
            setLocationNameRelay(splitSmsBody[6].trim());
            setPinRelay(splitSmsBody[7].trim());
            setStateRelay(splitSmsBody[8].trim());
        }
    }
}
