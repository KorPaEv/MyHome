package com.korpaev.myhome;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.telephony.SmsMessage;

//В этом классе реализуется абстрактный метод onReceive(), который вызывается системой каждый раз при получении сообщения.
public class MessageReceiver extends WakefulBroadcastReceiver
{
    SoundManage soundManage;
    SmsCheckPDUS smsCheckPDUS;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Передаем текущий context классу и выключаем звук - далее что то делаем и включаем его.
        soundManage = new SoundManage(context);
        soundManage.SetSoundOff();

        //Проверяем что нам пришла смс
        smsCheckPDUS = new SmsCheckPDUS(intent);

        //Дальше проверим флаг что это нужная смс и если нужная то запустим сервис где будет парсинг смс
        if (smsCheckPDUS.get_smsBody().contains("SH") && smsCheckPDUS.get_smsBody() != null)
        {
            //Стартуем сервис для обработки смс
            Intent service = new Intent(context, MessageService.class);

            service.putExtra("sms_from", smsCheckPDUS.get_smsFrom());
            service.putExtra("sms_body", smsCheckPDUS.get_smsBody());

            startWakefulService(context, service);
            //прерываем обработку смс стандартным манагером
            abortBroadcast();
        }
        soundManage.SetSoundOn();
    }
}