package com.korpaev.myhome;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

public class MessageService extends IntentService
{
    String smsFrom = "", smsBody = "";
    int idSms = 0;
    public MessageService() {
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

            //Формат передаваемой строки: 1;SH;BR;27C;4;BLR;25;0
            //                        или 1;SH;BR;27C;RN
            //                    или газ 6;SH;GAS;150;2;GR;22;1
            //                            6;SH;GAS;150;RN
            //Парсим смс построчно и создаем объекты

            Realm realm = Realm.getInstance(getBaseContext());
            realm.beginTransaction();
            //Посмотрим что лежит в БД
            RealmResults<RawSmsDB> results = realm.where(RawSmsDB.class).findAll();

            for (int i = 0; i < splitSmsBodyLines.length; i++)
            {
                RawSms rawSms = new RawSms();
                rawSms.ParseSms(splitSmsBodyLines[i]);

                RawSmsDB rawSmsDB = new RawSmsDB();
                rawSmsDB.set_idSms(rawSms.get_idSms());
                rawSmsDB.set_locationSensor(rawSms.get_locationSensor());
                rawSmsDB.set_valSensor(rawSms.get_valSensor());
                rawSmsDB.set_numRelay(rawSms.get_numRelay());
                rawSmsDB.set_locationRelay(rawSms.get_locationRelay());
                rawSmsDB.set_pinRelay(rawSms.get_pinRelay());
                rawSmsDB.set_stateRelay(rawSms.get_stateRelay());

                //Далее отписываем в БД то, что распарсили
                realm.copyToRealmOrUpdate(rawSmsDB);
            }
            //коммитим
            realm.commitTransaction();

            //---------------------------------------ДОПИСАТЬ
            //Запускаем главный активити с переданными из БД данными
            // Intent intentMain = new Intent(getBaseContext(), MainActivity.class);
            // intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);         // Pass in data
            //getApplication().startActivity(intentMain);
            //---------------------------------------ДОПИСАТЬ
        }
        MessageReceiver.completeWakefulIntent(intent);
    }
}

