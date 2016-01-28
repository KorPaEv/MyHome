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
        //Очистим БД от старых данных
        realm.where(RawSmsDb.class).findAll().clear();

        //Объект для БД
        RawSmsDb rawSmsDB = new RawSmsDb();
        rawSmsDB.set_idSms(rawSms.get_idSms());
        rawSmsDB.set_locationSensor(rawSms.get_locationSensor());
        rawSmsDB.set_valSensor(rawSms.get_valSensor());
        rawSmsDB.set_numRelay(rawSms.get_numRelay());
        rawSmsDB.set_locationRelay(rawSms.get_locationRelay());
        rawSmsDB.set_pinRelay(rawSms.get_pinRelay());
        rawSmsDB.set_stateRelay(rawSms.get_stateRelay());

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
