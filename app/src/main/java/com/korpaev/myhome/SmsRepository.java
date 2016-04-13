package com.korpaev.myhome;

import io.realm.Realm;
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

    public void addSms(RawSms rawSms)
    {
        realm = Realm.getInstance(context);
        realm.beginTransaction();

        //Объект для БД
        RawSmsDb rawSmsDB = new RawSmsDb();
        rawSmsDB.set_hProtocolVer(rawSms.get_hProtocolVer());
        rawSmsDB.set_hTimeStamp(rawSms.get_hTimeStamp());
        rawSmsDB.set_hNumSensor(rawSms.get_hNumSensor());
        rawSmsDB.set_hLenBody(rawSms.get_hLenBody());

        rawSmsDB.set_bLocationSensor(rawSms.get_bLocationSensor());
        rawSmsDB.set_bValSensor(rawSms.get_bValSensor());
        rawSmsDB.set_bNumRelay(rawSms.get_bNumRelay());
        rawSmsDB.set_bLocationRelay(rawSms.get_bLocationRelay());
        rawSmsDB.set_bPinRelay(rawSms.get_bPinRelay());
        rawSmsDB.set_bStateRelay(rawSms.get_bStateRelay());

        //Далее отписываем в БД то, что распарсили
        realm.copyToRealm(rawSmsDB);
        //коммитим
        realm.commitTransaction();
        //Посмотрим что лежит в БД после записи данных
        RealmResults<RawSmsDb> results = realm.where(RawSmsDb.class).findAll();
    }

    public RawSms findSms(int smsId)
    {
        RawSms rawSms = new RawSms();
        return rawSms;
    }
}
