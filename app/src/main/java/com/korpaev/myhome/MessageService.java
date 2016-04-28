package com.korpaev.myhome;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MessageService extends IntentService
{
    //region ОБЪЯВЛЯЕМ ОБЪЕКТЫ
    SharedPreferences sharedPref;
    Realm realm;
    //endregion

    //region КОНСТАНТЫ
    private final String IDFIELDNAME = "_idDevice"; //Имя поля БД
    private final String TIMEFIELDNAME = "_hTimeStamp"; //Имя поля БД
    private final String NUMSENSORFIELDNAME = "_hNumSensor"; //Имя поля БД
    private final String NAMESHAREDPREF= "IdDevicePref";
    //endregion

    //region Переменные и массивы
    String smsFrom = "", smsBody = "";
    private String[] splitSmsBodyLines;
    private String pVer, idDev;
    //endregion

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
        smsFrom = "+79236440918"; //ДЛЯ ТЕСТОВ

        //Разбиваем смс на строки
        if (string.startsWith("SH", 0))
        {
            WriteSensorsInfo(string, context);
        }
        else if (string.startsWith("INFSH", 0))
        {
            //WriteDeviceInfo(string, context);
        }
        SaveSharedPref(context);

        Intent intent;
        intent = new Intent(context, MainActivityTabs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);         // Pass in data
        context.startActivity(intent);
    }

    private void WriteSensorsInfo(String string, Context context)
    {
        realm = Realm.getInstance(context);
        realm.beginTransaction();
        splitSmsBodyLines = string.split("SH");
        //Формат передаваемой строки: Pver;Timestamp;NumSensor;LenBody;Gas;limitGas;gasRelay
        //                    или     Pver;Timestamp;NumSensor;LenBody;Gas;curGasValue;idRelay;locationR;relayPin;stateGas
        //Парсим смс построчно и пишем в БД
        RealmList<SensorsInfoDb> listSensorsInfo = new RealmList<>(); //Дочерняя табличка инфы датчиков

        for (int i = 0; i < splitSmsBodyLines.length; i++)
        {
            if (splitSmsBodyLines[i].isEmpty())
            {
                continue;
            }
            SensorsInfoRow sensorsInfoRow = new SensorsInfoRow();
            //Парсим отдельную строку из всех строк смс
            sensorsInfoRow.ParseSms(splitSmsBodyLines[i]);

            pVer = String.valueOf(sensorsInfoRow.get_hProtocolVer());
            idDev = AddDeviceActivity.CreateIdDevice(smsFrom, pVer);

            sensorsInfoRow.setId(idDev);

            //Добавляем в лист записи по датчикам
            listSensorsInfo.add(new SmsRepository(context).addRowSensorsInfoToDb(sensorsInfoRow));
        }

        RealmResults<DevicesInfoDb> devicesInfoDbs = realm.where(DevicesInfoDb.class).equalTo(IDFIELDNAME, idDev).findAll();
        for (int k = 0; k <  devicesInfoDbs.size(); k++)
        {
            DevicesInfoDb devicesInfoDb = devicesInfoDbs.get(k);

            for (int j = 0; j < listSensorsInfo.size(); j++)
            {
                RealmResults<SensorsInfoDb> sensorsInfoDbs = realm.where(SensorsInfoDb.class)
                        .equalTo(IDFIELDNAME, idDev)
                        .equalTo(TIMEFIELDNAME, listSensorsInfo.get(j).get_hTimeStamp())
                        .equalTo(NUMSENSORFIELDNAME, listSensorsInfo.get(j).get_hNumSensor())
                        .findAll();
                sensorsInfoDbs.clear();
                devicesInfoDb.get_stateSystemRaws().add(listSensorsInfo.get(j));
            }
        }
        realm.commitTransaction();
    }

//    private void WriteDeviceInfo(String string, Context context)
//    {
//        splitSmsBodyLines = string.split("INFSH");
//        String subStrGenInf, subStrAutorizedNum;
//        String[] splitAutorizedNums;
//        RealmList<AutorizedPhonesDb> listAutorizedPhones = new RealmList<>(); //Дочерняя табличка номеров разрешенных
//        RealmList<SensorsInfoDb> listSensorsInfo = new RealmList<>(); //Дочерняя табличка инфы датчиков
//        //Формат передаваемой строки: INFSH;1;Dom;Dimitrova;+79998887766;1;1;1;+79998886655;1;1;1
//        for (int i = 0; i < splitSmsBodyLines.length; i++)
//        {
//            if (splitSmsBodyLines[i].isEmpty())
//            {
//                continue;
//            }
//            //Отписываем всю инфу в БД по устройству
//            //SmsRepository smsRepository = new SmsRepository(getBaseContext());
//            SmsRepository smsRepository = new SmsRepository(context);
//
//            //берем первую часть строки 1;Dom;Dimitrova;
//            subStrGenInf = splitSmsBodyLines[i].substring(1, splitSmsBodyLines[i].indexOf('+'));
//            DeviceInfoRow dir = new DeviceInfoRow();
//            //Парсим первую часть строки
//            dir.ParseRow(subStrGenInf);
//            //генерируем ИД устройства по которому пришла инфа для записи в БД устройств
//            pVer = String.valueOf(dir.getProtovolVerDev());
//            idDev = AddDeviceActivity.CreateIdDevice(smsFrom, pVer);
//            dir.setId(idDev);
//            dir.setPhoneArduino(smsFrom);
//
//            //берем вторую часть строки +79998887766;1;1;1;+79998886655;1;1;1
//            subStrAutorizedNum = splitSmsBodyLines[i].substring(splitSmsBodyLines[i].indexOf('+'));
//            splitAutorizedNums = subStrAutorizedNum.split("\\+");
//
//            for (int j = 0; j < splitAutorizedNums.length; j++)
//            {
//                if (splitAutorizedNums[j].isEmpty())
//                {
//                    continue;
//                }
//                AutorizedPhoneRow autorizedPhoneRow = new AutorizedPhoneRow();
//                autorizedPhoneRow.setIdDevice(idDev);
//                autorizedPhoneRow.ParseRow(splitAutorizedNums[j]);
//                listAutorizedPhones.add(smsRepository.addAutorizedNumsToDb(autorizedPhoneRow));
//            }
//
//            RealmResults<DevicesInfoDb> devicesInfoDbs = realm.where(DevicesInfoDb.class).equalTo(IDFIELDNAME, idDev).findAll();
//            if (devicesInfoDbs.size() > 0)
//            {
//                DevicesInfoDb devicesInfoDb = devicesInfoDbs.get(0);
//                listSensorsInfo = devicesInfoDb.get_stateSystemRaws();
//            }
//            else listSensorsInfo = null;
//
//            smsRepository.addDevInfoToDb(dir, listAutorizedPhones, listSensorsInfo);
//        }
//    }

    public static Intent getIntentForLongSms (Context context, LongSms longSms)
    {
        Intent service = new Intent(context, MessageService.class);

        service.putExtra("sms_from", longSms.getNumber());
        service.putExtra("sms_body", longSms.getMessage());

        return service;
    }

    //region SaveSharedPref() Сохраняем ID текущего устройства в SharedPref
    private void SaveSharedPref(Context context)
    {
        //Создаем объект Editor для создания пар имя-значение:
        sharedPref = context.getSharedPreferences(NAMESHAREDPREF, context.MODE_PRIVATE);
        //Создаем объект Editor для создания пар имя-значение:
        SharedPreferences.Editor shpEditor = sharedPref.edit();
        shpEditor.putString(IDFIELDNAME, idDev);
        shpEditor.commit();
    }
    //endregion
}