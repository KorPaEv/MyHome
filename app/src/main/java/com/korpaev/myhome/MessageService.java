package com.korpaev.myhome;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

public class MessageService extends IntentService
{
    String smsFrom = "", smsBody = "";

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
            //Парсим и пишем в БД данные смс
            //WriteDataToDB(smsBody);

            //---------------------------------------ДОПИСАТЬ
            //Запускаем главный активити с переданными из БД данными
            Intent intentMain = new Intent(getBaseContext(), MainActivityTabs.class);
            intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);         // Pass in data
            getApplication().startActivity(intentMain);
            //---------------------------------------ДОПИСАТЬ
        }
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }

    public void WriteDataToDB(String string, Context context)
    {
        //Разбиваем смс на строки
        String[] splitSmsBodyLines;
        if (string.startsWith("SH", 0))
        {
            splitSmsBodyLines = string.split("SH");
            //Формат передаваемой строки: Pver;Timestamp;NumSensor;LenBody;Gas;limitGas;gasRelay
            //                    или     Pver;Timestamp;NumSensor;LenBody;Gas;curGasValue;idRelay;locationR;relayPin;stateGas
            //Парсим смс построчно и пишем в БД
            for (int i = 0; i < splitSmsBodyLines.length; i++)
            {
                if (splitSmsBodyLines[i].isEmpty())
                {
                    continue;
                }
                RawSms rawSms = new RawSms();
                //Парсим отдельную строку из всех строк смс
                rawSms.ParseSms(splitSmsBodyLines[i]);
                //SmsRepository smsRepository = new SmsRepository(getBaseContext());
                SmsRepository smsRepository = new SmsRepository(context);
                smsRepository.addSms(rawSms);
            }
        }
        else if (string.startsWith("INFSH", 0))
        {
            //Формат передаваемой строки: INFSH;Dom;Dimitrova;1;+79998887766;1;1;1;+79998886655;1;1;1;"";0;0;0;"";0;0;0
        }
    }

    public static Intent getIntentForLongSms (Context context, LongSms longSms)
    {
        Intent service = new Intent(context, MessageService.class);

        service.putExtra("sms_from", longSms.getNumber());
        service.putExtra("sms_body", longSms.getMessage());

        return service;
    }
}