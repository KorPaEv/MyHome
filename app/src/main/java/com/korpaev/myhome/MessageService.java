package com.korpaev.myhome;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

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
            smsBody = extras.getString("sms_body");
            smsFrom = extras.getString("sms_from");
            //Парсим смс
            //Формат передаваемой строки: 0001;1;SmartHome;BoilerRoom;27C;4;Boiler;25;OFF
            //                        или 0001;1;SmartHome;BoilerRoom;27C;RN
            //                    или газ 0001;6;SmartHome;GasSensor;150;2;GasRoom;22;ON
            //                            0001;6;SmartHome;GasSensor;150;RN

            RawSms rawSms = new RawSms();
            rawSms.ParseSms(smsBody);

            //Realm realm = Realm.getInstance(getBaseContext());
            //realm.beginTransaction();
            //realm.copyToRealm();
            //realm.commitTransaction();

            //---------------------------------------ДОПИСАТЬ
            //Далее отписываем в БД то, что распарсили
            //Запускаем главный активити с переданными из БД данными
            // Intent intentMain = new Intent(getBaseContext(), MainActivity.class);
            // intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);         // Pass in data
            //getApplication().startActivity(intentMain);
            //---------------------------------------ДОПИСАТЬ
        }
        MessageReceiver.completeWakefulIntent(intent);
    }
}

