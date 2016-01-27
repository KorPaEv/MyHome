package com.korpaev.myhome;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

public final class SmsUtils
{
    private SmsUtils()
    {

    }

    public static LongSms extractFromIntent (Intent intent)
    {
        Bundle bundle = intent.getExtras();
        //Здесь мы получаем сообщение с помощью метода intent.getExtras().get("pdus"),
        // который возвращает массив объектов в формате PDU — эти объекты мы потом приводим к типу SmsMessage с помощью метода createFromPdu().
        if (intent != null && intent.getAction() != null &&
            "android.provider.Telephony.SMS_RECEIVED".compareToIgnoreCase(intent.getAction()) == 0)
        {
            Object[] pduArray = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pduArray.length];
            for (int i = 0; i < pduArray.length; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    String format = bundle.getString("format");
                    messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i], format);
                }
                else
                {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
                }
            }
            //короче получили 1 смс и вывели номер и текст
            SmsMessage sms = messages[0];
            String address = messages[0].getDisplayOriginatingAddress();
            String message;
            //Здесь мы составляем текст сообщения (в случае, когда сообщение было длинным
            // и пришло в нескольких смс-ках, каждая отдельная часть хранится в messages[i]) и вызываем метод abortBroadcast(),
            // чтобы предотвратить дальнейшую обработку сообщения другими приложениями.
            try
            {
                if (messages.length == 1 || sms.isReplace())
                {
                    message = sms.getMessageBody();
                }
                else
                {
                    StringBuilder bodyText = new StringBuilder();
                    for (int i = 0; i < messages.length; i++)
                    {
                        bodyText.append(messages[i].getMessageBody());
                    }
                    message = bodyText.toString();
                }
                return new LongSms(address, message);
            }
            catch (Exception e)
            {
                //Здесь бы вообще ошибку проверять, но пока хрен с ним просто вернем null типо не получилось
                return null;
            }
        }
    return null;
    }
}
