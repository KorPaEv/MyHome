package com.korpaev.myhome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class GeneralSettingsActivity extends Activity
{
    //region КОНСТАНТЫ
    private final String NAMESHAREDPREF = "IdDevicePref";
    private final String IDFIELDNAME = "_idDevice"; //Имя поля БД
    private final String EMPTYDATA = "Не задано";
    private final String TIMESTAMPFIELDNAME = "_hTimeStamp";
    private final String SENSORSINFOTABLENAME = "_stateSystemRaws";
    private final int COUNTSENSORS = 6;
    private final int COUNTRELAYS = 4;
    //endregion

    //region ОБЪЯВЛЯЕМ ОБЪЕКТЫ И КНОПКИ
    Realm realm;
    SharedPreferences sharedPref;
    Button saveConfButton;
    //endregion или

    //region МАССИВЫ ID ВЬЮХ АКТИВИТИ
    final int[] rIdSensorsNameArr = {R.id.tvSensorConfNameOne,
                                     R.id.tvSensorConfNameTwo,
                                     R.id.tvSensorConfNameThree,
                                     R.id.tvSensorConfNameFour,
                                     R.id.tvSensorConfNameFive,
                                     R.id.tvSensorConfNameSix};
    final int[] rIdSensorsReNameArr = {R.id.eConfRenameSensorOne,
                                       R.id.eConfRenameSensorTwo,
                                       R.id.eConfRenameSensorThree,
                                       R.id.eConfRenameSensorFour,
                                       R.id.eConfRenameSensorFive,
                                       R.id.eConfRenameSensorSix};
    final int[] rIdRelaysNameArr = {R.id.tvRelayConfNameOne,
                                    R.id.tvRelayConfNameTwo,
                                    R.id.tvRelayConfNameThree,
                                    R.id.tvRelayConfNameFour};
    final int[] rIdRelaysReNameArr = {R.id.eConfRenameRelayOne,
                                    R.id.eConfRenameRelayTwo,
                                    R.id.eConfRenameRelayThree,
                                    R.id.eConfRenameRelayFour};
    //endregion

    //region МАССИВЫ ВЬЮХ АКТИВИТИ
    final TextView[] tvSensorsNameArr = new TextView[COUNTSENSORS];
    final EditText[] etSensorsReNameArr = new EditText[COUNTSENSORS];
    final TextView[] tvRelaysNameArr = new TextView[COUNTRELAYS];
    final EditText[] etRelaysReNameArr = new EditText[COUNTRELAYS];
    //endregion

    //region Переменные и массивы для хранения значений вьюх
    private String _sIdDevice; //Это ИД девайса который может прийти с другого активити
    final String[] sSensorsNameArr = new String[COUNTSENSORS];
    final String[] sSensorsReNameArr = new String[COUNTSENSORS];
    final String[] sRelayNameArr = new String[COUNTRELAYS];
    final String[] sRelayReNameArr = new String[COUNTRELAYS];
    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_general);
        FindViews();
        FillData();
    }

    //region FindViews() Поиск всех объектов на экране
    protected void FindViews()
    {
        saveConfButton = (Button)findViewById(R.id.bSaveConf);
        for (int i = 0; i < COUNTSENSORS; i++)
        {
            tvSensorsNameArr[i] = (TextView)findViewById(rIdSensorsNameArr[i]);
            etSensorsReNameArr[i] = (EditText)findViewById(rIdSensorsReNameArr[i]);
        }
        for (int j = 0; j < COUNTRELAYS; j++)
        {
            tvRelaysNameArr[j] = (TextView)findViewById(rIdRelaysNameArr[j]);
            etRelaysReNameArr[j] = (EditText)findViewById(rIdRelaysReNameArr[j]);
        }
    }
    //endregion

    //region FillData() Заполняем вьюхи данными
    private void FillData()
    {
        //Используем созданный файл данных SharedPreferences:
        sharedPref = getSharedPreferences(NAMESHAREDPREF, MODE_PRIVATE);
        _sIdDevice = sharedPref.getString(IDFIELDNAME, null);
        SetDefaultValDeviceInfo();
        if (!TextUtils.isEmpty(_sIdDevice))
        {
            FillViews(_sIdDevice);
        }
    }

    private void FillViews(String idRow)
    {
        realm = Realm.getInstance(getBaseContext());

        //СНАЧАЛА СЧИТЫВАЕМ ВСЕ РЕНЭЙМЫ ПО РЕЛЮХАМ, ПОТОМ ТЕ КОТОРЫЕ ПРИВЯЗАНЫ ПЕРЕПИШУТСЯ САМИ!!
        //Причем пока не сконфигурировано ардуино или не получена инфа по смс, этот список будет пуст
        //таблица заполняется только в 2 случаях - инфа по смс или конфигурирование из приложения ардуины
        RealmResults<RelayRenamesDb> relayRenamesDbs = realm.where(RelayRenamesDb.class).equalTo(IDFIELDNAME, idRow).findAll();

        for (int i = 0; i < relayRenamesDbs.size(); i++)
        {
            //получаем номер реле
            int numRelay = relayRenamesDbs.get(i).get_numRelay();
            numRelay -= 1;
            if (!TextUtils.isEmpty(relayRenamesDbs.get(numRelay).get_bLocationRelay()))
            {
                sRelayNameArr[numRelay] = relayRenamesDbs.get(numRelay).get_bLocationRelay();
            }
            else sRelayNameArr[numRelay] = null;

            if (TextUtils.isEmpty(sRelayNameArr[numRelay]))
            {
                tvRelaysNameArr[numRelay].setText(EMPTYDATA);
            }
            else tvRelaysNameArr[numRelay].setText(sRelayNameArr[numRelay]);

            sRelayReNameArr[numRelay] = relayRenamesDbs.get(numRelay).get_bLocationRelayRus();
            etRelaysReNameArr[numRelay].setText(sRelayReNameArr[numRelay]);
        }


        RealmResults<DevicesInfoDb> devicesInfoDbs;
        RealmResults<SensorsInfoDb> sensorsInfoDbs = realm.where(SensorsInfoDb.class).equalTo(IDFIELDNAME, _sIdDevice).findAll();
        RealmList<SensorsInfoDb> sensorsInfoList;

        if (sensorsInfoDbs.size() > 0)
        {
            //Получаем максимальное время
            int maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

            //Получаем только свежие записи по максимальному времени
            devicesInfoDbs = realm.where(DevicesInfoDb.class)
                    .equalTo(IDFIELDNAME, _sIdDevice)
                    .equalTo(SENSORSINFOTABLENAME + "." + TIMESTAMPFIELDNAME, maxTimeStamp)
                    .findAll();

            for (int i = 0; i < devicesInfoDbs.size(); i++)
            {
                sensorsInfoList = devicesInfoDbs.get(i).get_stateSystemRaws();

                for (int j = 0; j < sensorsInfoList.size(); j++)
                {
                    //получили номер датчика
                    int numSensor = sensorsInfoList.get(j).get_hNumSensor();
                    // -1 потому что массивы с нуля а данные по номеру датчика начинаются с 1
                    numSensor -= 1;

                    sSensorsNameArr[numSensor] = sensorsInfoList.get(numSensor).get_bLocationSensor();
                    sSensorsReNameArr[numSensor] = sensorsInfoList.get(numSensor).get_bLocationSensorRus();
                    tvSensorsNameArr[numSensor].setText(sSensorsNameArr[numSensor]);
                    etSensorsReNameArr[numSensor].setText(sSensorsReNameArr[numSensor]);

                    //смотрим если есть привязка реле к датчику
                    if (!TextUtils.isEmpty(sensorsInfoList.get(numSensor).get_bNumRelay())) {
                        //то получаем номер реле которое привязано
                        int numRelay = Integer.parseInt(sensorsInfoList.get(numSensor).get_bNumRelay());
                        numRelay -= 1;
                        sRelayNameArr[numRelay] = sensorsInfoList.get(numSensor).get_bLocationRelay();
                        sRelayReNameArr[numRelay] = sensorsInfoList.get(numSensor).get_bLocationRelayRus();
                        tvRelaysNameArr[numRelay].setText(sRelayNameArr[numRelay]);
                        etRelaysReNameArr[numRelay].setText(sRelayReNameArr[numRelay]);
                    }
                }
            }
        }
    }

    private void SetDefaultValDeviceInfo()
    {
        for (int i = 0; i < COUNTSENSORS; i++)
        {
            sSensorsNameArr[i] = null;
            sSensorsReNameArr[i] = null;
            tvSensorsNameArr[i].setText(EMPTYDATA);
            etSensorsReNameArr[i].setText(sSensorsReNameArr[i]);
        }
        for (int j = 0; j < COUNTRELAYS; j++)
        {
            sRelayNameArr[j] = null;
            sRelayReNameArr[j] = null;
            tvRelaysNameArr[j].setText(EMPTYDATA);
            etRelaysReNameArr[j].setText(sRelayReNameArr[j]);
        }
    }
    //endregion

    //region SaveConfButtonClick(View view) Кнопка сохранения настроек переименования
    public void SaveConfButtonClick(View view)
    {
        WriteDataToDb();
    }

    private void WriteDataToDb()
    {
        if (!TextUtils.isEmpty(_sIdDevice))
        {
            realm = Realm.getInstance(getBaseContext());
            realm.beginTransaction();

            //Причем пока не сконфигурировано ардуино или не получена инфа по смс, этот список будет пуст
            //таблица заполняется только в 2 случаях - инфа по смс или конфигурирование из приложения ардуины
            RealmResults<RelayRenamesDb> relayRenamesDbs = realm.where(RelayRenamesDb.class).equalTo(IDFIELDNAME, _sIdDevice).findAll();
            if (relayRenamesDbs.size() > 0)
            {
                //ВОТ ТУТ НАДО СНАЧАЛА ЗАПИСАТЬ ВСЕ РЕНЭЙМЫ В ТАБЛИЦУ ПО РЕЛЮХАМ, ПОТОМ ТЕ КОТОРЫЕ ПРИВЯЗАНЯ ПЕРЕПИШУТСЯ САМИ!!!
                for (int i = 0; i < COUNTRELAYS; i++) {
                    RelayRenamesDb relayRenamesDb = new RelayRenamesDb();
                    relayRenamesDb.set_numRelay(i + 1);
                    relayRenamesDb.set_idDevice(_sIdDevice);
                    sRelayReNameArr[i] = etRelaysReNameArr[i].getText().toString();
                    relayRenamesDb.set_bLocationRelayRus(sRelayReNameArr[i]);
                    relayRenamesDb.set_bLocationRelay(sRelayNameArr[i]);
                    realm.copyToRealmOrUpdate(relayRenamesDb);
                }
            }

            //Получаем наш объект если он уже создан и хотим редактировать
            RealmResults<DevicesInfoDb> devicesInfoDbs;
            RealmResults<SensorsInfoDb> sensorsInfoDbs = realm.where(SensorsInfoDb.class).equalTo(IDFIELDNAME, _sIdDevice).findAll();
            RealmList<SensorsInfoDb> sensorsInfoList;

            if (sensorsInfoDbs.size() > 0)
            {
                //Получаем максимальное время
                int maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

                //Получаем только свежие записи по максимальному времени
                devicesInfoDbs = realm.where(DevicesInfoDb.class)
                        .equalTo(IDFIELDNAME, _sIdDevice)
                        .equalTo(SENSORSINFOTABLENAME + "." + TIMESTAMPFIELDNAME, maxTimeStamp)
                        .findAll();

                for (int i = 0; i < devicesInfoDbs.size(); i++)
                {
                    sensorsInfoList = devicesInfoDbs.get(i).get_stateSystemRaws();

                    for (int j = 0; j < sensorsInfoList.size(); j++)
                    {
                        int numSensor = sensorsInfoList.get(j).get_hNumSensor();
                        numSensor -= 1;

                        sSensorsReNameArr[numSensor] = etSensorsReNameArr[numSensor].getText().toString();
                        sensorsInfoList.get(j).set_bLocationSensorRus(sSensorsReNameArr[numSensor]);

                        if (!TextUtils.isEmpty(sensorsInfoList.get(j).get_bNumRelay()))
                        {
                            int numRelay = Integer.parseInt(sensorsInfoList.get(j).get_bNumRelay());
                            numRelay -= 1;
                            sRelayReNameArr[numRelay] = etRelaysReNameArr[numRelay].getText().toString();
                            sensorsInfoList.get(j).set_bLocationRelayRus(sRelayReNameArr[numRelay]);
                        }
                    }
                }
            }
            realm.commitTransaction();
        }
    }
    //endregion

    //создаем меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        //заполняем меню
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (menu != null)
            menu.findItem(R.id.conf).setVisible(false);
        return true;
    }

    //событие на выбранный пункт меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;
        // получаем все пункты меню
        //пункт item определен в menu_main
        int itemId = item.getItemId();

        // ищем наш пункт меню
        switch (itemId)
        {
            case R.id.about:
                 intent = new Intent(GeneralSettingsActivity.this, AboutActivity.class);
                 startActivity(intent);
                 return true;
            case R.id.main_inf:
                 intent = new Intent(GeneralSettingsActivity.this, MainActivityTabs.class);
                 startActivity(intent);
                 finish();
                 return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FillData();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        WriteDataToDb();
    }
}