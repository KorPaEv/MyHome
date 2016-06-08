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
            if (!TextUtils.isEmpty(relayRenamesDbs.get(i).get_bLocationRelay()))
            {
                sRelayNameArr[numRelay] = relayRenamesDbs.get(i).get_bLocationRelay();
            }
            else sRelayNameArr[numRelay] = null;

            if (TextUtils.isEmpty(sRelayNameArr[numRelay]))
            {
                tvRelaysNameArr[numRelay].setText(EMPTYDATA);
            }
            else tvRelaysNameArr[numRelay].setText(sRelayNameArr[numRelay]);

            sRelayReNameArr[numRelay] = relayRenamesDbs.get(i).get_bLocationRelayRus();
            etRelaysReNameArr[numRelay].setText(sRelayReNameArr[numRelay]);
        }

        RealmResults<SensorsInfoDb> sensorsInfoDbs = realm.where(SensorsInfoDb.class).findAll();

        if (sensorsInfoDbs.size() > 0)
        {
            int maxTimeStamp = 0;
            if (sensorsInfoDbs.size() <= COUNTSENSORS)
            {
                //Получаем максимальное время
                maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

                sensorsInfoDbs = realm.where(SensorsInfoDb.class)
                        .equalTo(IDFIELDNAME, _sIdDevice)
                        .equalTo(TIMESTAMPFIELDNAME, maxTimeStamp)
                        .findAll();
            }
            else
            {
                //Получаем максимальное время
                maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

                sensorsInfoDbs = realm.where(SensorsInfoDb.class)
                        .equalTo(IDFIELDNAME, _sIdDevice)
                        .equalTo(TIMESTAMPFIELDNAME, maxTimeStamp)
                        .findAll();

                //если у нас записей в пачке мало, то выбираем целую пачку и отображаем ее
                if (sensorsInfoDbs.size() != COUNTSENSORS)
                {
                    sensorsInfoDbs = realm.where(SensorsInfoDb.class)
                            .equalTo(IDFIELDNAME, _sIdDevice)
                            .lessThan(TIMESTAMPFIELDNAME, maxTimeStamp)
                            .findAll();

                    maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

                    sensorsInfoDbs = realm.where(SensorsInfoDb.class)
                            .equalTo(IDFIELDNAME, _sIdDevice)
                            .equalTo(TIMESTAMPFIELDNAME, maxTimeStamp)
                            .findAll();
                }
            }

            for (int j = 0; j < sensorsInfoDbs.size(); j++)
            {
                //получили номер датчика
                int numSensor = sensorsInfoDbs.get(j).get_hNumSensor();
                // -1 потому что массивы с нуля а данные по номеру датчика начинаются с 1
                numSensor -= 1;

                sSensorsNameArr[numSensor] = sensorsInfoDbs.get(j).get_bLocationSensor();
                sSensorsReNameArr[numSensor] = sensorsInfoDbs.get(j).get_bLocationSensorRus();
                tvSensorsNameArr[numSensor].setText(sSensorsNameArr[numSensor]);
                etSensorsReNameArr[numSensor].setText(sSensorsReNameArr[numSensor]);

                //смотрим если есть привязка реле к датчику
                if (!TextUtils.isEmpty(sensorsInfoDbs.get(j).get_bNumRelay())) {
                    //то получаем номер реле которое привязано
                    int numRelay = Integer.parseInt(sensorsInfoDbs.get(j).get_bNumRelay());
                    numRelay -= 1;
                    sRelayNameArr[numRelay] = sensorsInfoDbs.get(j).get_bLocationRelay();
                    sRelayReNameArr[numRelay] = sensorsInfoDbs.get(j).get_bLocationRelayRus();
                    tvRelaysNameArr[numRelay].setText(sRelayNameArr[numRelay]);
                    etRelaysReNameArr[numRelay].setText(sRelayReNameArr[numRelay]);
                }
            }
        }
    }

    private void SetDefaultValDeviceInfo()
    {
        for (int i = 0; i < COUNTSENSORS; i++)
        {
            sSensorsNameArr[i] = "";
            sSensorsReNameArr[i] = "";
            tvSensorsNameArr[i].setText(EMPTYDATA);
            etSensorsReNameArr[i].setText(sSensorsReNameArr[i]);
        }
        for (int j = 0; j < COUNTRELAYS; j++)
        {
            sRelayNameArr[j] = "";
            sRelayReNameArr[j] = "";
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
            //ВОТ ТУТ НАДО СНАЧАЛА ЗАПИСАТЬ ВСЕ РЕНЭЙМЫ В ТАБЛИЦУ ПО РЕЛЮХАМ, ПОТОМ ТЕ КОТОРЫЕ ПРИВЯЗАНЯ ПЕРЕПИШУТСЯ САМИ!!!
            for (int i = 0; i < relayRenamesDbs.size(); i++)
            {
                int numR = relayRenamesDbs.get(i).get_numRelay();
                numR -= 1;
                sRelayReNameArr[numR] = etRelaysReNameArr[numR].getText().toString();
                relayRenamesDbs.get(i).set_bLocationRelayRus(sRelayReNameArr[numR]);
                relayRenamesDbs.get(i).set_bLocationRelay(sRelayNameArr[numR]);
            }

            //Получаем наш объект если он уже создан и хотим редактировать
            RealmResults<SensorsInfoDb> sensorsInfoDbs = realm.where(SensorsInfoDb.class).equalTo(IDFIELDNAME, _sIdDevice).findAll();

            if (sensorsInfoDbs.size() > 0)
            {
                int maxTimeStamp = 0;
                if (sensorsInfoDbs.size() <= COUNTSENSORS)
                {
                    //Получаем максимальное время
                    maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

                    sensorsInfoDbs = realm.where(SensorsInfoDb.class)
                            .equalTo(IDFIELDNAME, _sIdDevice)
                            .equalTo(TIMESTAMPFIELDNAME, maxTimeStamp)
                            .findAll();
                }
                else
                {
                    //Получаем максимальное время
                    maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

                    sensorsInfoDbs = realm.where(SensorsInfoDb.class)
                            .equalTo(IDFIELDNAME, _sIdDevice)
                            .equalTo(TIMESTAMPFIELDNAME, maxTimeStamp)
                            .findAll();

                    //если у нас записей в пачке мало, то выбираем целую пачку и отображаем ее
                    if (sensorsInfoDbs.size() != COUNTSENSORS)
                    {
                        sensorsInfoDbs = realm.where(SensorsInfoDb.class)
                                .equalTo(IDFIELDNAME, _sIdDevice)
                                .lessThan(TIMESTAMPFIELDNAME, maxTimeStamp)
                                .findAll();

                        maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

                        sensorsInfoDbs = realm.where(SensorsInfoDb.class)
                                .equalTo(IDFIELDNAME, _sIdDevice)
                                .equalTo(TIMESTAMPFIELDNAME, maxTimeStamp)
                                .findAll();
                    }
                }

                for (int j = 0; j < sensorsInfoDbs.size(); j++)
                {
                    int numSensor = sensorsInfoDbs.get(j).get_hNumSensor();
                    numSensor -= 1;

                    sSensorsReNameArr[numSensor] = etSensorsReNameArr[j].getText().toString();
                    sensorsInfoDbs.get(j).set_bLocationSensorRus(sSensorsReNameArr[numSensor]);

                    if (!TextUtils.isEmpty(sensorsInfoDbs.get(j).get_bNumRelay()))
                    {
                        int numRelay = Integer.parseInt(sensorsInfoDbs.get(j).get_bNumRelay());
                        numRelay -= 1;
                        sRelayReNameArr[numRelay] = etRelaysReNameArr[numRelay].getText().toString();
                        sensorsInfoDbs.get(j).set_bLocationRelayRus(sRelayReNameArr[numRelay]);
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
            case R.id.pref:
                intent = new Intent(GeneralSettingsActivity.this, PrefActivity.class);
                startActivity(intent);
                return true;
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