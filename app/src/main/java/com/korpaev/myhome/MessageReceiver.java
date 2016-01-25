package com.korpaev.myhome;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.telephony.SmsMessage;

//В этом классе реализуется абстрактный метод onReceive(), который вызывается системой каждый раз при получении сообщения.
public class MessageReceiver extends WakefulBroadcastReceiver
{
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        String smsFrom = "", smsBody = "";
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        SetMute(audioManager, false);

        //Здесь мы получаем сообщение с помощью метода intent.getExtras().get("pdus"),
        // который возвращает массив объектов в формате PDU — эти объекты мы потом приводим к типу SmsMessage с помощью метода createFromPdu().
        if (intent != null && intent.getAction() != null && ACTION.compareToIgnoreCase(intent.getAction()) == 0)
        {
            Object[] pduArray = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pduArray.length];
            for (int i = 0; i < pduArray.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
            }

            //короче получили 1 смс и вывели номер и текст
            SmsMessage sms = messages[0];
            smsFrom = messages[0].getDisplayOriginatingAddress();
            smsBody = messages[0].getMessageBody();

            //Здесь мы составляем текст сообщения (в случае, когда сообщение было длинным
            // и пришло в нескольких смс-ках, каждая отдельная часть хранится в messages[i]) и вызываем метод abortBroadcast(),
            // чтобы предотвратить дальнейшую обработку сообщения другими приложениями.
            try
            {
                if (messages.length == 1 || sms.isReplace())
                {
                    smsBody = sms.getMessageBody();
                }
                else
                {
                    StringBuilder bodyText = new StringBuilder();
                    for (int i = 0; i < messages.length; i++)
                    {
                        bodyText.append(messages[i].getMessageBody());
                    }
                    smsBody = bodyText.toString();
                }
            }
            catch (Exception e)
            {

            }

            //Дальше проверим какой нить флаг что это нужная смс и если нужная то запустим сервис где будет парсинг смс
            if (smsBody.contains("SmartHome"))
            {
                Intent service = new Intent(context, MessageService.class);

                service.putExtra("sms_from", smsFrom);
                service.putExtra("sms_body", smsBody);

                startWakefulService(context, service);
                abortBroadcast();
                SetMute(audioManager, true);
            }
            else SetMute(audioManager, true);
        }
        else SetMute(audioManager, true);
    }

    public void SetMute(AudioManager manager, Boolean flag)
    {
        manager.setStreamMute(AudioManager.STREAM_NOTIFICATION, flag);
        manager.setStreamMute(AudioManager.STREAM_ALARM, flag);
        manager.setStreamMute(AudioManager.STREAM_MUSIC, flag);
        //manager.setStreamMute(AudioManager.STREAM_RING, flag);
        manager.setStreamMute(AudioManager.STREAM_SYSTEM, flag);
    }
}