package com.korpaev.myhome;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

//В этом классе реализуется абстрактный метод onReceive(), который вызывается системой каждый раз при получении сообщения.
public class MessageReceiver extends WakefulBroadcastReceiver
{
    SoundManage soundManage;
    LongSms longSms;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Передаем текущий context классу и выключаем звук - далее что то делаем и включаем его.
        soundManage = new SoundManage(context);
        soundManage.SetSoundOff();

        //Проверяем что нам пришла смс
        longSms = SmsUtils.extractFromIntent(intent);

        //Дальше проверим флаг что это нужная смс и если нужная то запустим сервис где будет парсинг смс
        if (longSms.getMessage().contains("SH") && longSms.getMessage() != null)
        {
            //Стартуем сервис для обработки смс
            Intent service = new Intent(context, MessageService.class);

            service.putExtra("sms_from", longSms.getNumber());
            service.putExtra("sms_body", longSms.getMessage());

            startWakefulService(context, service);
            //прерываем обработку смс стандартным манагером
            abortBroadcast();
        }
        soundManage.SetSoundOn();
    }
}