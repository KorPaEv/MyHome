package com.korpaev.myhome;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RelayRenamesDb extends RealmObject
{
    @PrimaryKey
    private int _numRelay; // номер датчика

    private String _idDevice;
    private String _bLocationRelay; // расположение реле
    private String _bLocationRelayRus; // расположение реле на русском для переименования
    private boolean _bStateRelay; // состояние реле
    private boolean _isAutomaticModeR; // режим автоматики для текущего реле - если вручную выключаем иил вкл, то автоматика перестает работать

    //Тут имена полей и имена методов get и set должны совпадать!!! Это особенность либы
    public String get_idDevice() { return _idDevice; }
    public int get_numRelay() { return _numRelay; }
    public String get_bLocationRelay() { return _bLocationRelay; }
    public String get_bLocationRelayRus() { return _bLocationRelayRus; }
    public boolean get_bStateRelay() { return _bStateRelay; }
    public boolean get_isAutomaticModeR() { return _isAutomaticModeR; }

    public void set_idDevice(String idDevice) { this._idDevice = idDevice; }
    public void set_numRelay(int numRelay) { _numRelay = numRelay; }
    public void set_bLocationRelay(String bLocationRelay) { _bLocationRelay = bLocationRelay; }
    public void set_bLocationRelayRus(String bLocationRelayRus) { this._bLocationRelayRus = bLocationRelayRus; }
    public void set_bStateRelay(boolean bStateRelay){ this._bStateRelay = bStateRelay; }
    public void set_isAutomaticModeR(boolean isAutomaticModeR){ this._isAutomaticModeR = isAutomaticModeR; }
}
