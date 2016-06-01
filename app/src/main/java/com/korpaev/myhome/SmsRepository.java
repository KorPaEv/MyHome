package com.korpaev.myhome;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import android.content.Context;

public class SmsRepository
{
    Realm realm;
    Context context;

    SmsRepository(Context context)
    {
        this.context = context;
    }

    public SensorsInfoDb addRowSensorsInfoToDb(SensorsInfoRow sensorsInfoRow)
    {
        //Объект для БД
        SensorsInfoDb sensorsInfoDB = new SensorsInfoDb();
        sensorsInfoDB.set_idDevice(sensorsInfoRow.getId());
        sensorsInfoDB.set_hProtocolVer(sensorsInfoRow.get_hProtocolVer());
        sensorsInfoDB.set_hTimeStamp(sensorsInfoRow.get_hTimeStamp());
        sensorsInfoDB.set_hNumSensor(sensorsInfoRow.get_hNumSensor());
        sensorsInfoDB.set_hLenBody(sensorsInfoRow.get_hLenBody());

        sensorsInfoDB.set_bLocationSensor(sensorsInfoRow.get_bLocationSensor());
        sensorsInfoDB.set_bLocationSensorRus(sensorsInfoRow.get_bLocationSensorRus());
        sensorsInfoDB.set_bValSensor(sensorsInfoRow.get_bValSensor());
        sensorsInfoDB.set_bSmsNoticeSensor(sensorsInfoRow.get_bSmsNoticeSensor());
        sensorsInfoDB.set_bNumRelay(sensorsInfoRow.get_bNumRelay());
        sensorsInfoDB.set_bLocationRelay(sensorsInfoRow.get_bLocationRelay());
        sensorsInfoDB.set_bLocationRelayRus(sensorsInfoRow.get_bLocationRelayRus());
        sensorsInfoDB.set_bPinRelay(sensorsInfoRow.get_bPinRelay());
        sensorsInfoDB.set_bStateRelay(sensorsInfoRow.get_bStateRelay());
        sensorsInfoDB.set_bManualManageRelay(!sensorsInfoRow.get_bManualManageRelay());
        return sensorsInfoDB;
    }

    public AutorizedPhonesDb addAutorizedNumsToDb(AutorizedPhoneRow autorizedPhoneRow)
    {
        AutorizedPhonesDb autorizedPhonesDb = new AutorizedPhonesDb();
        autorizedPhonesDb.set_idDevice(autorizedPhoneRow.getIdDevice());
        autorizedPhonesDb.set_phoneNumber(autorizedPhoneRow.getPhoneNumber());
        autorizedPhonesDb.set_sendSmsRights(autorizedPhoneRow.getSendSmsRights());
        autorizedPhonesDb.set_callRights(autorizedPhoneRow.getCallRights());
        autorizedPhonesDb.set_isAdmNumb(autorizedPhoneRow.getIsAdmNumb());
        return autorizedPhonesDb;
    }
}
