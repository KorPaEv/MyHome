package com.korpaev.myhome;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.provider.Telephony.Sms.Intents;

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
        if ( intent != null && intent.getAction() != null &&
                Intents.SMS_RECEIVED_ACTION.compareToIgnoreCase(intent.getAction()) == 0)
        {
            Object[] pduArray = (Object[]) bundle.get("pdus");
            //Object[] pduArray = {0x00, 0x11, 0x00, 0x0B,
            //                     0x91, 0x97, 0x32, 0x460419F80004AA1a534800000001f9580a57000000060b4741533b3939393b393939};
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

            //Положили в массив данные тела смс в пду
            byte[] usrDataArr = sms.getUserData();

            String smsFrom = messages[0].getDisplayOriginatingAddress();
            String smsBody;
            //Здесь мы составляем текст сообщения (в случае, когда сообщение было длинным
            // и пришло в нескольких смс-ках, каждая отдельная часть хранится в messages[i]) и вызываем метод abortBroadcast(),
            // чтобы предотвратить дальнейшую обработку сообщения другими приложениями.
            try
            {
                SmsUserDataPdu smsUserDataPdu = new SmsUserDataPdu();
                smsBody = smsUserDataPdu.ConvertPduToGsm(usrDataArr); //Парсим пду формат в читаемый вид
                return new LongSms(smsFrom, smsBody);
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
