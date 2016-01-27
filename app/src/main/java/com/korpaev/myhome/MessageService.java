package com.korpaev.myhome;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import io.realm.Realm;
import io.realm.RealmResults;

public class MessageService extends IntentService
{
    String smsFrom = "", smsBody = "";
    Realm realm;

    public MessageService()
    {
        super("MessageService");
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        //Прочитали то что передал рессивер
        Bundle extras = intent.getExtras();
        if(extras != null)
        {
            // получили текст и номер сообщения
            smsBody = extras.getString("sms_body");
            smsFrom = extras.getString("sms_from");
            //Разбиваем смс на строки
            String[] splitSmsBodyLines;
            splitSmsBodyLines = smsBody.split("\n");

            //Парсим и пишем в БД данные смс
            WriteDataToDB(splitSmsBodyLines);
            //Посмотрим что лежит в БД после записи данных
            RealmResults<RawSmsDb> results = realm.where(RawSmsDb.class).findAll();

            //---------------------------------------ДОПИСАТЬ
            //Запускаем главный активити с переданными из БД данными
            Intent intentMain = new Intent(getBaseContext(), MainActivity.class);
            intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);         // Pass in data
            getApplication().startActivity(intentMain);
            //---------------------------------------ДОПИСАТЬ
        }
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void WriteDataToDB(String[] string)
    {
        //Формат передаваемой строки: 1;SH;BR;27C;4;BLR;25;0
        //                        или 1;SH;BR;27C;RN
        //                    или газ 6;SH;GAS;150;2;GR;22;1
        //                            6;SH;GAS;150;RN
        //Парсим смс построчно и пишем в БД
        realm = Realm.getInstance(getBaseContext());
        realm.beginTransaction();
        //Очистим БД от старых данных
        realm.where(RawSmsDb.class).findAll().clear();

        for (int i = 0; i < string.length; i++)
        {
            RawSms rawSms = new RawSms();
            //Парсим отдельную строку из всех строк смс
            rawSms.ParseSms(string[i]);

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
        }
        //коммитим
        realm.commitTransaction();
    }

    public static Intent getIntentForLongSms (Context context, LongSms longSms)
    {
        Intent service = new Intent(context, MessageService.class);

        service.putExtra("sms_from", longSms.getNumber());
        service.putExtra("sms_body", longSms.getMessage());
        return service;
    }
}

