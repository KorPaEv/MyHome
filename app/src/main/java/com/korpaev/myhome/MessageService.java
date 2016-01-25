package com.korpaev.myhome;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MessageService extends IntentService
{
    String smsFrom = "", smsBody = "";
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
            SmsParser parserSms = new SmsParser();
            //Формат передаваемой строки: 1;SmartHome;BoilerRoom;27C;4;Boiler;25;OFF
            //                        или 1;SmartHome;BoilerRoom;27C;RN
            //                    или газ 6;SmartHome;GasSensor;150;2;GasRoom;22;ON
            //                            6;SmartHome;GasSensor;150;RN
            parserSms.ParseSms(smsBody);

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
