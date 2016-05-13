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
    //endregion
    //region МАССИВЫ ВЬЮХ АКТИВИТИ ИЛИ ПРОСТО ВЬЮХИ
    View tvSectionConfSensorNamesArd, vSectionConfSensorNamesArd,
         tvSectionTemperatureEdges, vSectionTemperatureEdges,
         tvSectionConfSensorsWithRelay, vSectionConfSensorsWithRelay;
    final TextView[] tvCurrSensorNameArdArr = new TextView[COUNTSENSORS];
    final EditText[] etNewSensorNameArdArr = new EditText[COUNTSENSORS];
    final Button[] btSaveSensorNameArdButtonArr = new Button[COUNTSENSORS];
    //endregion
    //region Переменные и массивы для хранения значений вьюх
    private String _sIdDevice; //Это ИД девайса который может прийти с другого активити
    final String[] sCurrSensorNameArdArr = new String[COUNTSENSORS];
    final String[] sNewSensorNameArdArr = new String[COUNTSENSORS];
    private String _ardPhoneNumb; //Номер ардуины
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
        GetArdPhoneNumb();
        
    }

    private void GetArdPhoneNumb()
    {
        RealmResults<DevicesInfoDb> devicesInfoDbs = realm.where(DevicesInfoDb.class).equalTo(IDFIELDNAME, _sIdDevice).findAll();
        for (int i = 0; i < devicesInfoDbs.size(); i++)
        {
            _ardPhoneNumb = devicesInfoDbs.get(i).get_phoneNumbArduino();
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
}