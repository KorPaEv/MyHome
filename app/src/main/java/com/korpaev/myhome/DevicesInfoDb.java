package com.korpaev.myhome;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DevicesInfoDb extends RealmObject
{
    private RealmList<RawSmsDb> _stateSystemRaws; //возвращается лист из общей таблички инфы по датчикам для текущего устройства
    private RealmList<AutorizedPhonesDb> _autorizedPhoneNumRaws; //лист разрешенных номеров для текущего устройства
    @PrimaryKey
    private String _idDevice;

    private int _hProtocolVer; // версия протокола
    private String _nameDevice; // имя железки РУССКИЙ ЯЗЫК
    private String _nameDeviceTranslit; // имя железки ТРАНСЛИТ
    private String _address; // адрес где стоит
    private String _addressTranslit; // адрес где стоит
    private String _phoneNumbArduino; //номер сим карты в ардуино

    //Тут имена полей и имена методов get и set должны совпадать!!! Это особенность либы
    public String get_idDevice() { return _idDevice; }
    public int get_hProtocolVer() { return _hProtocolVer; }
    public String get_nameDevice() { return _nameDevice; }
    public String get_nameDeviceTranslit() { return _nameDeviceTranslit; }
    public String get_address() { return _address; }
    public String get_addressTranslit() { return _addressTranslit; }
    public String get_phoneNumbArduino() { return _phoneNumbArduino; }
    public RealmList<RawSmsDb> get_stateSystemRaws() { return _stateSystemRaws; }
    public RealmList<AutorizedPhonesDb> get_autorizedPhoneNumRaws() { return _autorizedPhoneNumRaws; }

    public void set_idDevice(String idDevice) { this._idDevice = idDevice; }
    public void set_hProtocolVer(int hProtocolVer) { this._hProtocolVer = hProtocolVer; }
    public void set_nameDevice(String nameDevice) { this._nameDevice = nameDevice; }
    public void set_nameDeviceTranslit(String nameDeviceTranslit) { this._nameDeviceTranslit = nameDeviceTranslit; }
    public void set_address(String address) { this._address = address; }
    public void set_addressTranslit(String addressTranslit) { this._addressTranslit = addressTranslit; }
    public void set_phoneNumbArduino(String phoneNumbArduino) { this._phoneNumbArduino = phoneNumbArduino; }
    public void set_stateSystemRaws(RealmList<RawSmsDb> stateSystemRaws) { this._stateSystemRaws = stateSystemRaws; }
    public void set_autorizedPhoneNumRaws(RealmList<AutorizedPhonesDb> autorizedPhoneNumRaws) { this._autorizedPhoneNumRaws = autorizedPhoneNumRaws; }
}
