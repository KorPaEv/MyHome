package com.korpaev.myhome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class AdditionalySettingsActivity extends Activity
{
    //region КОНСТАНТЫ
    private final String EMPTYDATA = "Не задано";
    private final String NAMESHAREDPREF = "IdDevicePref";
    private final String IDFIELDNAME = "_idDevice"; //Имя поля БД
    private final String TIMESTAMPFIELDNAME = "_hTimeStamp";
    private final String SENSORSINFOTABLENAME = "_stateSystemRaws";
    private final int COUNTSENSORS = 5;
    private final int COUNTRELAYS = 4;
    //endregion
    //region ОБЪЯВЛЯЕМ ОБЪЕКТЫ И КНОПКИ
    Realm realm;
    SharedPreferences sharedPref;
    //endregion
    //region МАССИВЫ ID ВЬЮХ АКТИВИТИ
    final int[] rIdCurrSensorNameArdArr = {R.id.tvSensorConfNameArdOne,
        R.id.tvSensorConfNameArdTwo,
        R.id.tvSensorConfNameArdThree,
        R.id.tvSensorConfNameArdFour,
        R.id.tvSensorConfNameArdFive,};
    final int[] rIdNewSensorNameArdArr = {R.id.eConfNameArdSensorOne,
            R.id.eConfNameArdSensorTwo,
            R.id.eConfNameArdSensorThree,
            R.id.eConfNameArdSensorFour,
            R.id.eConfNameArdSensorFive};
    final int[] rIdSaveSensorNameArdButtonArr = {R.id.btSaveSensorNameArdOne,
            R.id.btSaveSensorNameArdTwo,
            R.id.btSaveSensorNameArdThree,
            R.id.btSaveSensorNameArdFour,
            R.id.btSaveSensorNameArdFive};

    final int[] rIdCurrRelayNameArdArr = {R.id.tvRelayConfNameArdOne,
            R.id.tvRelayConfNameArdTwo,
            R.id.tvRelayConfNameArdThree,
            R.id.tvRelayConfNameArdFour};
    final int[] rIdNewRelayNameArdArr = {R.id.eConfNameArdRelayOne,
            R.id.eConfNameArdRelayTwo,
            R.id.eConfNameArdRelayThree,
            R.id.eConfNameArdRelayFour};
    final int[] rIdSaveRelayNameArdButtonArr = {R.id.btSaveRelayNameArdOne,
            R.id.btSaveRelayNameArdTwo,
            R.id.btSaveRelayNameArdThree,
            R.id.btSaveRelayNameArdFour};
    //endregion
    //region МАССИВЫ ВЬЮХ АКТИВИТИ ИЛИ ПРОСТО ВЬЮХИ
    View tvSectionConfSensorNamesArd, vSectionConfSensorNamesArd,
         tvSectionTemperatureEdges, vSectionTemperatureEdges,
         tvSectionConfSensorsWithRelay, vSectionConfSensorsWithRelay;
    final TextView[] tvCurrSensorNameArdArr = new TextView[COUNTSENSORS];
    final EditText[] etNewSensorNameArdArr = new EditText[COUNTSENSORS];
    final Button[] btSaveSensorNameArdButtonArr = new Button[COUNTSENSORS];

    final TextView[] tvCurrRelayNameArdArr = new TextView[COUNTRELAYS];
    final EditText[] etNewRelayNameArdArr = new EditText[COUNTRELAYS];
    final Button[] btSaveRelayNameArdButtonArr = new Button[COUNTRELAYS];
    //endregion
    //region Переменные и массивы для хранения значений вьюх
    private String _sIdDevice; //Это ИД девайса который может прийти с другого активити
    private String _ardPhoneNumb; //Номер ардуины

    final String[] sCurrSensorNameArdArr = new String[COUNTSENSORS];
    final String[] sNewSensorNameArdArr = new String[COUNTSENSORS];

    final String[] sCurrRelayNameArdArr = new String[COUNTRELAYS];
    final String[] sNewRelayNameArdArr = new String[COUNTRELAYS];
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_additionally);
        FindViews(); //Ищем вьюхи на экране и заполняем массивы объектами
        FillData();
        //region Оработчик раскрытия списка конфигурации имен датчиков
        tvSectionConfSensorNamesArd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vSectionConfSensorNamesArd.getVisibility() == View.GONE) {
                    tvSectionConfSensorNamesArd.setBackgroundColor(0xC73B3A48);
                    vSectionConfSensorNamesArd.setVisibility(View.VISIBLE);
                } else {
                    tvSectionConfSensorNamesArd.setBackgroundColor(0xC7060543);
                    vSectionConfSensorNamesArd.setVisibility(View.GONE);
                }
            }
        });
        //endregion
        //region Оработчик раскрытия списка температурных пределов
        tvSectionTemperatureEdges.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (vSectionTemperatureEdges.getVisibility() == View.GONE)
                {
                    tvSectionTemperatureEdges.setBackgroundColor(0xC73B3A48);
                    vSectionTemperatureEdges.setVisibility(View.VISIBLE);
                }
                else
                {
                    tvSectionTemperatureEdges.setBackgroundColor(0xC7430803);
                    vSectionTemperatureEdges.setVisibility(View.GONE);
                }
            }
        });
        //endregion
        //region Оработчик раскрытия взаимодействия датчиков и реле
        tvSectionConfSensorsWithRelay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (vSectionConfSensorsWithRelay.getVisibility() == View.GONE)
                {
                    tvSectionConfSensorsWithRelay.setBackgroundColor(0xC73B3A48);
                    vSectionConfSensorsWithRelay.setVisibility(View.VISIBLE);
                }
                else
                {
                    tvSectionConfSensorsWithRelay.setBackgroundColor(0xC7303103);
                    vSectionConfSensorsWithRelay.setVisibility(View.GONE);
                }
            }
        });
        //endregion
    }

    //region FindViews() Поиск вьюх и заполнение массивов дл этих вьюх
    private void FindViews()
    {
        tvSectionConfSensorNamesArd = findViewById(R.id.tvSectionConfSensorNamesArd);
        vSectionConfSensorNamesArd = findViewById(R.id.sectionConfSensorNamesArd);
        tvSectionTemperatureEdges = findViewById(R.id.tvSectionTemperatureEdges);
        vSectionTemperatureEdges = findViewById(R.id.sectionTemperatureEdges);
        tvSectionConfSensorsWithRelay = findViewById(R.id.tvSectionConfSensorsWithRelay);
        vSectionConfSensorsWithRelay = findViewById(R.id.sectionConfSensorsWithRelay);
        for (int i = 0; i < COUNTSENSORS; i++)
        {
            tvCurrSensorNameArdArr[i] = (TextView)findViewById(rIdCurrSensorNameArdArr[i]);
            etNewSensorNameArdArr[i] = (EditText)findViewById(rIdNewSensorNameArdArr[i]);
            btSaveSensorNameArdButtonArr[i] = (Button)findViewById(rIdSaveSensorNameArdButtonArr[i]);
        }
        for (int j = 0; j < COUNTRELAYS; j++)
        {
            tvCurrRelayNameArdArr[j] = (TextView)findViewById(rIdCurrRelayNameArdArr[j]);
            etNewRelayNameArdArr[j] = (EditText)findViewById(rIdNewRelayNameArdArr[j]);
            btSaveRelayNameArdButtonArr[j] = (Button)findViewById(rIdSaveRelayNameArdButtonArr[j]);
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

    private void FillViews(String idDev)
    {
        realm = Realm.getInstance(getBaseContext());
        GetArdPhoneNumb();
        FillRelaysName(idDev);
        FillSensorsName(idDev);
    }

    private void GetArdPhoneNumb()
    {
        RealmResults<DevicesInfoDb> devicesInfoDbs = realm.where(DevicesInfoDb.class).equalTo(IDFIELDNAME, _sIdDevice).findAll();
        for (int i = 0; i < devicesInfoDbs.size(); i++)
        {
            _ardPhoneNumb = devicesInfoDbs.get(i).get_phoneNumbArduino();
        }
    }


    private void FillSensorsName(String idDevice)
    {
        RealmResults<DevicesInfoDb> devicesInfoDbs;
        RealmResults<SensorsInfoDb> sensorsInfoDbs = realm.where(SensorsInfoDb.class).equalTo(IDFIELDNAME, idDevice).findAll();
        RealmList<SensorsInfoDb> sensorsInfoList;

        if (sensorsInfoDbs.size() > 0)
        {
            //Получаем максимальное время
            int maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

            //Получаем только свежие записи по максимальному времени
            devicesInfoDbs = realm.where(DevicesInfoDb.class)
                    .equalTo(IDFIELDNAME, idDevice)
                    .equalTo(SENSORSINFOTABLENAME + "." + TIMESTAMPFIELDNAME, maxTimeStamp)
                    .findAll();

            for (int i = 0; i < devicesInfoDbs.size(); i++)
            {
                sensorsInfoList = devicesInfoDbs.get(i).get_stateSystemRaws();
                for (int j = 0; j < sensorsInfoList.size(); j++)
                {
                    int numSensor = sensorsInfoList.get(j).get_hNumSensor();
                    numSensor -= 1;
                    sCurrSensorNameArdArr[numSensor] = sensorsInfoList.get(numSensor).get_bLocationSensor();
                    tvCurrSensorNameArdArr[numSensor].setText(sCurrSensorNameArdArr[numSensor]);

                    //смотрим если есть привязка реле к датчику
                    if (!TextUtils.isEmpty(sensorsInfoList.get(numSensor).get_bNumRelay()))
                    {
                        //то получаем номер реле которое привязано
                        int numRelay = Integer.parseInt(sensorsInfoList.get(numSensor).get_bNumRelay());
                        numRelay -= 1;

                        sCurrRelayNameArdArr[numRelay] = sensorsInfoList.get(numRelay).get_bLocationRelay();
                        tvCurrRelayNameArdArr[numRelay].setText(sCurrRelayNameArdArr[numRelay]);
                    }
                }
            }
        }
    }

    private void FillRelaysName(String idDevice)
    {
        RealmResults<RelayRenamesDb> relayRenamesDbs = realm.where(RelayRenamesDb.class).equalTo(IDFIELDNAME, idDevice).findAll();
        for (int i = 0; i < relayRenamesDbs.size(); i++)
        {
            //получаем номер реле
            int numRelay = relayRenamesDbs.get(i).get_numRelay();
            numRelay -= 1;
            sCurrRelayNameArdArr[numRelay] = relayRenamesDbs.get(numRelay).get_bLocationRelay();
            tvCurrRelayNameArdArr[numRelay].setText(sCurrRelayNameArdArr[numRelay]);
        }
    }

    private void SetDefaultValDeviceInfo()
    {
        for (int j = 0; j < COUNTSENSORS; j++)
        {
            sCurrSensorNameArdArr[j] = EMPTYDATA;
            tvCurrSensorNameArdArr[j].setText(sCurrSensorNameArdArr[j]);
            sNewSensorNameArdArr[j] = null;
            etNewSensorNameArdArr[j].setText(sNewSensorNameArdArr[j]);
        }
        for (int i = 0; i < COUNTRELAYS; i++)
        {
            sCurrRelayNameArdArr[i] = EMPTYDATA;
            tvCurrRelayNameArdArr[i].setText(sCurrRelayNameArdArr[i]);
            sNewRelayNameArdArr[i] = null;
            etNewRelayNameArdArr[i].setText(sNewRelayNameArdArr[i]);
        }
    }
    //endregion

    //region Функция отправки инфы на ардуину
    public void SendInfToArdFromAddConf(View v)
    {
        int viewId = v.getId();
        if (CheckViews(viewId))
        {
            WriteDbData(viewId);
            FillData(); //Обновили экран с новыми данными
        }
    }

    //Проверка валидности введенных данных вьюх
    private boolean CheckViews(int rIdView)
    {
        for (int i = 0; i < COUNTSENSORS; i++)
        {
            if (rIdView == rIdSaveSensorNameArdButtonArr[i] &&  TextUtils.isEmpty(etNewSensorNameArdArr[i].getText()))
            {
                etNewSensorNameArdArr[i].setError("Обязательно к заполнению!");
                return false;
            }
        }
        for (int j = 0; j < COUNTRELAYS; j++)
        {
            if (rIdView == rIdSaveRelayNameArdButtonArr[j] && TextUtils.isEmpty(etNewRelayNameArdArr[j].getText()))
            {
                etNewRelayNameArdArr[j].setError("Обязательно к заполнению!");
                return false;
            }
        }
        return true;
    }

    private void WriteDbData(int idView)
    {
        for (int i = 0; i < COUNTSENSORS; i++)
        {
            if (idView != rIdSaveSensorNameArdButtonArr[i])
            {
                continue;
            }
            SendSms(_ardPhoneNumb, "СЮДА ФОРМИРУЕМ ТЕКСТ ПО ДАТЧИКУ КОТОРЫЙ КОНФИГУРИРУЕМ"); //Отправили смс на ардину с новым именем
            WriteDbSensors(i); //Переписали БД
        }
        for (int j = 0; j < COUNTRELAYS; j++)
        {
            if (idView != rIdSaveRelayNameArdButtonArr[j])
            {
                continue;
            }
            SendSms(_ardPhoneNumb, "СЮДА ФОРМИРУЕМ ТЕКСТ ПО ДАТЧИКУ КОТОРЫЙ КОНФИГУРИРУЕМ"); //Отправили смс на ардину с новым именем
            WriteDbRelays(j);
        }
    }

    private void WriteDbSensors(int idArray)
    {
        //получаем значение элемента инфу которого отправляем ардуине и сохраняем по нему в БД НО В ЛАТИНИЦЕ!!ЭТО НАЧАЛЬНОЕ ИМЯ
        sNewSensorNameArdArr[idArray] = Translit.RusToLat(etNewSensorNameArdArr[idArray].getText().toString());

        realm = Realm.getInstance(getBaseContext());
        realm.beginTransaction();
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
                //Получили текущий лист инфы по датчикам
                sensorsInfoList = devicesInfoDbs.get(i).get_stateSystemRaws();

                for (int j = 0; j < sensorsInfoList.size(); j++)
                {
                    int numSensor = sensorsInfoList.get(j).get_hNumSensor();
                    numSensor -= 1;
                    if (numSensor != idArray)
                    {
                        continue;
                    }
                    //Если номер датчика совпал с номером индекса массива значит это наш датчик - переписываем имя
                    sensorsInfoList.get(numSensor).set_bLocationSensor(sNewSensorNameArdArr[idArray]);
                }
            }
        }
        realm.commitTransaction();
    }

    private void WriteDbRelays(int idArray)
    {
        //получаем значение элемента инфу которого отправляем ардуине и сохраняем по нему в БД НО В ЛАТИНИЦЕ!!ЭТО НАЧАЛЬНОЕ ИМЯ
        sNewRelayNameArdArr[idArray] = Translit.RusToLat(etNewRelayNameArdArr[idArray].getText().toString());

        realm = Realm.getInstance(getBaseContext());
        realm.beginTransaction();

        RelayRenamesDb relayRenamesDb = new RelayRenamesDb();
        relayRenamesDb.set_numRelay(idArray + 1);
        relayRenamesDb.set_idDevice(_sIdDevice);
        relayRenamesDb.set_bLocationRelay(sNewRelayNameArdArr[idArray]);

        realm.copyToRealmOrUpdate(relayRenamesDb);

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
                //Получили текущий лист инфы по датчикам
                sensorsInfoList = devicesInfoDbs.get(i).get_stateSystemRaws();

                for (int j = 0; j < sensorsInfoList.size(); j++)
                {
                    if (!TextUtils.isEmpty(sensorsInfoList.get(j).get_bNumRelay()) &&
                        Integer.parseInt(sensorsInfoList.get(j).get_bNumRelay()) == (idArray + 1))
                    {
                        //Если номер датчика совпал с номером индекса массива значит это наш датчик - переписываем имя
                        sensorsInfoList.get(j).set_bLocationRelay(sNewRelayNameArdArr[idArray]);
                    }
                }
            }
        }

        realm.commitTransaction();
    }
    //endregion

    //region SendSms Функция отправки смс
    public void SendSms(String number, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, null, null);
    }
    //endregion

    //region создаем меню
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
    //endregion
    //region событие на выбранный пункт меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // получаем все пункты меню
        //пункт item определен в menu_main
        int itemId = item.getItemId();
        Intent intent;

        // ищем наш пункт меню
        switch (itemId)
        {
            case R.id.about:
                intent = new Intent(AdditionalySettingsActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.main_inf:
                intent = new Intent(AdditionalySettingsActivity.this, MainActivityTabs.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    @Override
    protected void onResume()
    {
        super.onResume();
        FillData();
    }
}