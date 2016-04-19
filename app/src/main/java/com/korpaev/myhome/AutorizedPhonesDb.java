package com.korpaev.myhome;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AutorizedPhonesDb extends RealmObject
{
    private int _hProtocolVer; // версия протокола
    private String _phoneNumber; // разрешенный номер
    private boolean _sendSmsRights; // права на смс уведомление для номера
    private boolean _callRights; // права на обратный звонок для номера
    private boolean _isAdmNumb; // является ли номер админским

    //Тут имена полей и имена методов get и set должны совпадать!!! Это особенность либы
    public int get_hProtocolVer() { return _hProtocolVer; }
    public String get_phoneNumber() { return _phoneNumber; }
    public boolean get_sendSmsRights() { return _sendSmsRights; }
    public boolean get_callRights() { return _callRights; }
    public boolean get_isAdmNumb() { return _isAdmNumb; }

    public void set_hProtocolVer(int hProtocolVer) { this._hProtocolVer = hProtocolVer; }
    public void set_phoneNumber(String phoneNumber) { this._phoneNumber = phoneNumber; }
    public void set_sendSmsRights(boolean sendSmsRights) { this._sendSmsRights = sendSmsRights; }
    public void set_callRights(boolean callRights) { this._callRights = callRights; }
    public void set_isAdmNumb(boolean isAdmNumb) { this._isAdmNumb = isAdmNumb; }
}
