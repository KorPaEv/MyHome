package com.korpaev.myhome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

public class AdditionalySettingsActivity extends Activity {
    //region КОНСТАНТЫ
    private final String EMPTYDATA = "Не задано";
    private final String NAMESHAREDPREF = "IdDevicePref";
    private final String IDFIELDNAME = "_idDevice"; //Имя поля БД
    private final String TIMESTAMPFIELDNAME = "_hTimeStamp";
    private final String SENSORSINFOTABLENAME = "_stateSystemRaws";
    private final int PACKSMSCOUNT = 6;
    private final int COUNTSENSORS = 5;
    private final int COUNTRELAYS = 4;
    private final int UNDEFINED = 999;
    //endregion

    //region ОБЪЯВЛЯЕМ ОБЪЕКТЫ И КНОПКИ
    Realm realm;
    SharedPreferences sharedPref;
    //endregion

    //region МАССИВЫ ID ВЬЮХ АКТИВИТИ
    //вьюхи датчиков для группы КОНФИГ ИМЕН для дуни
    final int[] rIdCurrSensorNameArdArr = {R.id.tvSensorConfNameArdOne,
            R.id.tvSensorConfNameArdTwo,
            R.id.tvSensorConfNameArdThree,
            R.id.tvSensorConfNameArdFour,
            R.id.tvSensorConfNameArdFive};
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

    //вьюхи релюх для группы КОНФИГ ИМЕН для дуни
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

    //вьюхи датчиков для группы ТЕМПЕРАТУРНЫЕ ПРЕДЕЛЫ
    final int[] rIdSensorNameTempEdgesArdArr = {R.id.tvTempEdgesSensorOne,
            R.id.tvTempEdgesSensorTwo,
            R.id.tvTempEdgesSensorThree,
            R.id.tvTempEdgesSensorFour,
            R.id.tvTempEdgesSensorFive};
    final int[] rIdMinTempEdgesArdArr = {R.id.etMinTempEdgeSensorOne,
            R.id.etMinTempEdgeSensorTwo,
            R.id.etMinTempEdgeSensorThree,
            R.id.etMinTempEdgeSensorFour,
            R.id.etMinTempEdgeSensorFive};
    final int[] rIdMaxTempEdgesArdArr = {R.id.etMaxTempEdgeSensorOne,
            R.id.etMaxTempEdgeSensorTwo,
            R.id.etMaxTempEdgeSensorThree,
            R.id.etMaxTempEdgeSensorFour,
            R.id.etMaxTempEdgeSensorFive};
    final int[] rIdTurnOnRelayWithSensorArdArr = {R.id.chbTurnOnRelayWithSensorOne,
            R.id.chbTurnOnRelayWithSensorTwo,
            R.id.chbTurnOnRelayWithSensorThree,
            R.id.chbTurnOnRelayWithSensorFour,
            R.id.chbTurnOnRelayWithSensorFive};
    final int[] rIdTurnOffRelayWithSensorArdArr = {R.id.chbTurnOffRelayWithSensorOne,
            R.id.chbTurnOffRelayWithSensorTwo,
            R.id.chbTurnOffRelayWithSensorThree,
            R.id.chbTurnOffRelayWithSensorFour,
            R.id.chbTurnOffRelayWithSensorFive};
    final int[] rIdSaveTempEdgesButtonArdArr = {R.id.btSaveTempEdgeSensorOne,
            R.id.btSaveTempEdgeSensorTwo,
            R.id.btSaveTempEdgeSensorThree,
            R.id.btSaveTempEdgeSensorFour,
            R.id.btSaveTempEdgeSensorFive};

    //вьюхи датчиков для группы ПРИВЯЗКА РЕЛЕ К ДАТЧИКАМ
    final int[] rIdRelayWithSensorManageArdArr = {R.id.tvRelayWithSensorManageOne,
            R.id.tvRelayWithSensorManageTwo,
            R.id.tvRelayWithSensorManageThree,
            R.id.tvRelayWithSensorManageFour};
    final int[] rIdSpinnerRelayWithSensorArdArr = {R.id.spinnerRelayWithSensorOne,
            R.id.spinnerRelayWithSensorTwo,
            R.id.spinnerRelayWithSensorThree,
            R.id.spinnerRelayWithSensorFour};
    final int[] rIdSaveRelayWithSensorButtonArdArr = {R.id.btSaveRelayWithSensorOne,
            R.id.btSaveRelayWithSensorTwo,
            R.id.btSaveRelayWithSensorThree,
            R.id.btSaveRelayWithSensorFour};
    final int[] rIdSmsNoticeRelayWithSensorChbArdArr = {R.id.chbConfSmsNoticeRelayWithSensorOne,
            R.id.chbConfSmsNoticeRelayWithSensorTwo,
            R.id.chbConfSmsNoticeRelayWithSensorThree,
            R.id.chbConfSmsNoticeRelayWithSensorFour};

    final int rIdSpinnerSensorsForGas = R.id.spConfGasSensorWithRelays;
    //endregion

    //region МАССИВЫ ВЬЮХ АКТИВИТИ ИЛИ ПРОСТО ВЬЮХИ
    View tvSectionConfSensorNamesArd, vSectionConfSensorNamesArd,
         tvSectionTemperatureEdges, vSectionTemperatureEdges,
         tvSectionConfSensorsWithRelay, vSectionConfSensorsWithRelay,
         tvSectionConfGasPref, vSectionConfGasPref;
    final TextView[] tvCurrSensorNameArdArr = new TextView[COUNTSENSORS];
    final EditText[] etNewSensorNameArdArr = new EditText[COUNTSENSORS];
    final Button[] btSaveSensorNameArdButtonArr = new Button[COUNTSENSORS];

    final TextView[] tvCurrRelayNameArdArr = new TextView[COUNTRELAYS];
    final EditText[] etNewRelayNameArdArr = new EditText[COUNTRELAYS];
    final Button[] btSaveRelayNameArdButtonArr = new Button[COUNTRELAYS];

    final TextView[] tvSensorNameTempEdgesArdArr = new TextView[COUNTSENSORS];
    final EditText[] etMinTempEdgesArdArr = new EditText[COUNTSENSORS];
    final EditText[] etMaxTempEdgesArdArr = new EditText[COUNTSENSORS];
    final CheckBox[] chbTurnOnRelayWithSensorArdArr = new CheckBox[COUNTSENSORS];
    final CheckBox[] chbTurnOffRelayWithSensorArdArr = new CheckBox[COUNTSENSORS];
    final Button[] btSaveTempEdgesButtonArdArr = new Button[COUNTSENSORS];

    final TextView[] tvRelayWithSensorManageNameArdArr = new TextView[COUNTRELAYS];
    final Spinner[] spSpinnerRelayWithSensorArdArr = new Spinner[COUNTRELAYS];
    final CheckBox[] chbConfSmsNoticeRelayWithSensorArdArr = new CheckBox[COUNTRELAYS];
    final Button[] btSaveRelayWithSensorButtonArdArr = new Button[COUNTRELAYS];

    TextView tvGasName;
    EditText etGasEdge;
    Spinner spRelayForGas;
    CheckBox chbSmsNoticeGas;
    Button btSaveGasPref;
    //endregion

    //region Переменные и массивы для хранения значений вьюх
    private String _sIdDevice; //Это ИД девайса который может прийти с другого активити
    private String _ardPhoneNumb; //Номер ардуины

    final String[] sCurrSensorNameArdArr = new String[COUNTSENSORS];
    final String[] sNewSensorNameArdArr = new String[COUNTSENSORS];

    final String[] sCurrRelayNameArdArr = new String[COUNTRELAYS];
    final String[] sNewRelayNameArdArr = new String[COUNTRELAYS];

    final String[] sSensorNameTempEdgesArdArr = new String[COUNTSENSORS];
    final Integer[] sMinTempEdgesArdArr = new Integer[COUNTSENSORS];
    final Integer[] sMaxTempEdgesArdArr = new Integer[COUNTSENSORS];
    final Boolean[] sTurnOnRelayWithSensorArdArr = new Boolean[COUNTSENSORS];
    final Boolean[] sTurnOffRelayWithSensorArdArr = new Boolean[COUNTSENSORS];

    final String[] sRelayWithSensorManageNameArdArr = new String[COUNTRELAYS];
    final Boolean[] bConfSmsNoticeRelayWithSensorArdArr = new Boolean[COUNTRELAYS];

    ArrayAdapter<String> spinnerAdapterForSensors;
    ArrayAdapter<String> spinnerAdapterForRelays;
    private int _numSensorArrayElement;
    private int _numRelayArrayElement;

    private String sGasName;
    private int iGasEdge;
    private boolean bSmsNoticeGas;
    //endregion

    private void set_numSensorArrayElement(int numSensorArrayElement) { _numSensorArrayElement = numSensorArrayElement; }
    private void set_numRelayArrayElement(int numRelayArrayElement) { _numRelayArrayElement = numRelayArrayElement; }
    private int get_numSensorArrayElement() { return _numSensorArrayElement; }
    private int get_numRelayArrayElement() { return _numRelayArrayElement; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_additionally);

        //region адаптеры под спинер
        spinnerAdapterForSensors = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sCurrSensorNameArdArr);
        spinnerAdapterForSensors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerAdapterForRelays = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sCurrRelayNameArdArr);
        spinnerAdapterForRelays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //endregion

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
        tvSectionTemperatureEdges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vSectionTemperatureEdges.getVisibility() == View.GONE) {
                    tvSectionTemperatureEdges.setBackgroundColor(0xC73B3A48);
                    vSectionTemperatureEdges.setVisibility(View.VISIBLE);
                } else {
                    tvSectionTemperatureEdges.setBackgroundColor(0xC7430803);
                    vSectionTemperatureEdges.setVisibility(View.GONE);
                }
            }
        });
        //endregion

        //region Оработчик раскрытия взаимодействия датчиков и реле
        tvSectionConfSensorsWithRelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vSectionConfSensorsWithRelay.getVisibility() == View.GONE) {
                    tvSectionConfSensorsWithRelay.setBackgroundColor(0xC73B3A48);
                    vSectionConfSensorsWithRelay.setVisibility(View.VISIBLE);
                } else {
                    tvSectionConfSensorsWithRelay.setBackgroundColor(0xC7303103);
                    vSectionConfSensorsWithRelay.setVisibility(View.GONE);
                }
            }
        });
        //endregion

        //region Оработчик раскрытия списка конфигурации датчика газа
        tvSectionConfGasPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vSectionConfGasPref.getVisibility() == View.GONE) {
                    tvSectionConfGasPref.setBackgroundColor(0xC73B3A48);
                    vSectionConfGasPref.setVisibility(View.VISIBLE);
                } else {
                    tvSectionConfGasPref.setBackgroundColor(0xFF92AE99);
                    vSectionConfGasPref.setVisibility(View.GONE);
                }
            }
        });
        //endregion
    }

    private AdapterView.OnItemSelectedListener itemSpinnerSelected = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> spinnerView, View adapterView, int position, long id)
        {
            for (int i = 0; i < COUNTRELAYS; i++)
            {
                if (spinnerView.getId() == rIdSpinnerRelayWithSensorArdArr[i])
                {
                    chbConfSmsNoticeRelayWithSensorArdArr[i].setChecked(bConfSmsNoticeRelayWithSensorArdArr[i]);
                    spinnerView.setSelection(position);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0)
        {
            // TODO Auto-generated method stub
        }
    };

    //region FindViews() Поиск вьюх и заполнение массивов дл этих вьюх
    private void FindViews()
    {
        tvGasName = (TextView)findViewById(R.id.tvConfGasSensorName);
        etGasEdge = (EditText)findViewById(R.id.etConfGasSensorEdge);
        spRelayForGas = (Spinner)findViewById(rIdSpinnerSensorsForGas);
        spRelayForGas.setAdapter(spinnerAdapterForRelays);
        spRelayForGas.setOnItemSelectedListener(itemSpinnerSelected);
        chbSmsNoticeGas = (CheckBox)findViewById(R.id.chbSmsNoticeForGasSensorConf);
        btSaveGasPref = (Button)findViewById(R.id.btSavePrefForGasConf);

        tvSectionConfSensorNamesArd = findViewById(R.id.tvSectionConfSensorNamesArd);
        vSectionConfSensorNamesArd = findViewById(R.id.sectionConfSensorNamesArd);
        tvSectionTemperatureEdges = findViewById(R.id.tvSectionTemperatureEdges);
        vSectionTemperatureEdges = findViewById(R.id.sectionTemperatureEdges);
        tvSectionConfSensorsWithRelay = findViewById(R.id.tvSectionConfSensorsWithRelay);
        vSectionConfSensorsWithRelay = findViewById(R.id.sectionConfSensorsWithRelay);
        tvSectionConfGasPref = findViewById(R.id.tvSectionConfGasPref);
        vSectionConfGasPref = findViewById(R.id.sectionConfGasPref);

        for (int i = 0; i < COUNTSENSORS; i++) {
            tvCurrSensorNameArdArr[i] = (TextView) findViewById(rIdCurrSensorNameArdArr[i]);
            etNewSensorNameArdArr[i] = (EditText) findViewById(rIdNewSensorNameArdArr[i]);
            btSaveSensorNameArdButtonArr[i] = (Button) findViewById(rIdSaveSensorNameArdButtonArr[i]);
            tvSensorNameTempEdgesArdArr[i] = (TextView) findViewById(rIdSensorNameTempEdgesArdArr[i]);
            etMinTempEdgesArdArr[i] = (EditText) findViewById(rIdMinTempEdgesArdArr[i]);
            etMaxTempEdgesArdArr[i] = (EditText) findViewById(rIdMaxTempEdgesArdArr[i]);
            chbTurnOnRelayWithSensorArdArr[i] = (CheckBox) findViewById(rIdTurnOnRelayWithSensorArdArr[i]);
            chbTurnOffRelayWithSensorArdArr[i] = (CheckBox) findViewById(rIdTurnOffRelayWithSensorArdArr[i]);
            btSaveTempEdgesButtonArdArr[i] = (Button) findViewById(rIdSaveTempEdgesButtonArdArr[i]);
        }

        for (int j = 0; j < COUNTRELAYS; j++) {
            tvCurrRelayNameArdArr[j] = (TextView)findViewById(rIdCurrRelayNameArdArr[j]);
            etNewRelayNameArdArr[j] = (EditText)findViewById(rIdNewRelayNameArdArr[j]);
            btSaveRelayNameArdButtonArr[j] = (Button)findViewById(rIdSaveRelayNameArdButtonArr[j]);

            tvRelayWithSensorManageNameArdArr[j] = (TextView)findViewById(rIdRelayWithSensorManageArdArr[j]);
            btSaveRelayWithSensorButtonArdArr[j] = (Button)findViewById(rIdSaveRelayWithSensorButtonArdArr[j]);
            chbConfSmsNoticeRelayWithSensorArdArr[j] = (CheckBox)findViewById(rIdSmsNoticeRelayWithSensorChbArdArr[j]);
            spSpinnerRelayWithSensorArdArr[j] = (Spinner) findViewById(rIdSpinnerRelayWithSensorArdArr[j]);
            spSpinnerRelayWithSensorArdArr[j].setAdapter(spinnerAdapterForSensors);
            spSpinnerRelayWithSensorArdArr[j].setOnItemSelectedListener(itemSpinnerSelected);
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
        if (!TextUtils.isEmpty(_sIdDevice)) {
            FillViews(_sIdDevice);
        }
    }

    private void FillViews(String idDev) {
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
        RealmResults<SensorsInfoDb> sensorsInfoDbs = realm.where(SensorsInfoDb.class).equalTo(IDFIELDNAME, idDevice).findAll();

        if (sensorsInfoDbs.size() > 0)
        {
            int maxTimeStamp = 0;
            if (sensorsInfoDbs.size() <= PACKSMSCOUNT)
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
                if (sensorsInfoDbs.size() != PACKSMSCOUNT)
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

                    if (numSensor != 5) //проверяем не датчик ли это газа - его индекс в массивах 5, но номер его во входной смс 6
                    {
                        sCurrSensorNameArdArr[numSensor] = sensorsInfoDbs.get(j).get_bLocationSensor();
                        tvCurrSensorNameArdArr[numSensor].setText(sCurrSensorNameArdArr[numSensor]);

                        sSensorNameTempEdgesArdArr[numSensor] = sCurrSensorNameArdArr[numSensor];
                        tvSensorNameTempEdgesArdArr[numSensor].setText(sSensorNameTempEdgesArdArr[numSensor]);

                        if (sensorsInfoDbs.get(numSensor).get_minTempEdge() == 0) {
                            sMinTempEdgesArdArr[numSensor] = UNDEFINED;
                        } else
                            sMinTempEdgesArdArr[numSensor] = sensorsInfoDbs.get(j).get_minTempEdge();

                        if (sensorsInfoDbs.get(numSensor).get_maxTempEdge() == 0) {
                            sMaxTempEdgesArdArr[numSensor] = UNDEFINED;
                        } else
                            sMaxTempEdgesArdArr[numSensor] = sensorsInfoDbs.get(j).get_maxTempEdge();

                        if (sMinTempEdgesArdArr[numSensor] == UNDEFINED) {
                            etMinTempEdgesArdArr[numSensor].setText(null);
                        } else
                            etMinTempEdgesArdArr[numSensor].setText(String.valueOf(sMinTempEdgesArdArr[numSensor]));

                        if (sMaxTempEdgesArdArr[numSensor] == UNDEFINED) {
                            etMaxTempEdgesArdArr[numSensor].setText(null);
                        } else
                            etMaxTempEdgesArdArr[numSensor].setText(String.valueOf(sMaxTempEdgesArdArr[numSensor]));

                        sTurnOnRelayWithSensorArdArr[numSensor] = sensorsInfoDbs.get(j).get_turnOnRelayWithSensor();
                        sTurnOffRelayWithSensorArdArr[numSensor] = sensorsInfoDbs.get(j).get_turnOffRelayWithSensor();
                        chbTurnOnRelayWithSensorArdArr[numSensor].setChecked(sTurnOnRelayWithSensorArdArr[numSensor]);
                        chbTurnOffRelayWithSensorArdArr[numSensor].setChecked(sTurnOffRelayWithSensorArdArr[numSensor]);
                    }
                    else
                    {
                        String strLoc;
                        sGasName = sensorsInfoDbs.get(j).get_bLocationSensor();

                        if (!TextUtils.isEmpty(sensorsInfoDbs.get(j).get_bLocationSensorRus()))
                        {
                            strLoc = sensorsInfoDbs.get(j).get_bLocationSensorRus();
                            tvGasName.setText(strLoc);
                        }
                        else
                        {
                            tvGasName.setText(sGasName);
                        }
                        iGasEdge = sensorsInfoDbs.get(j).get_gasEdge();
                        etGasEdge.setText(String.valueOf(iGasEdge));
                        bSmsNoticeGas = sensorsInfoDbs.get(j).get_bSmsNoticeSensor();
                        chbSmsNoticeGas.setChecked(bSmsNoticeGas);
                    }
                    //смотрим если есть привязка реле к датчику
                    if (!TextUtils.isEmpty(sensorsInfoDbs.get(j).get_bNumRelay()))
                    {
                        //то получаем номер реле которое привязано
                        int numRelay = Integer.parseInt(sensorsInfoDbs.get(j).get_bNumRelay());
                        numRelay -= 1;

                        String locStr;

                        GradientDrawable gd = new GradientDrawable();
                        gd.setColor(0xFFA4ABAD);
                        gd.setCornerRadius(7);

                        if (numSensor != 5)
                        {
                            bConfSmsNoticeRelayWithSensorArdArr[numRelay] = sensorsInfoDbs.get(j).get_bSmsNoticeSensor();

                            sCurrRelayNameArdArr[numRelay] = sensorsInfoDbs.get(j).get_bLocationRelay();
                            tvCurrRelayNameArdArr[numRelay].setText(sCurrRelayNameArdArr[numRelay]);

                            sRelayWithSensorManageNameArdArr[numRelay] = sCurrRelayNameArdArr[numRelay];
                            if (!TextUtils.isEmpty(sensorsInfoDbs.get(j).get_bLocationRelayRus()))
                            {
                                locStr = sensorsInfoDbs.get(j).get_bLocationRelayRus();
                                tvRelayWithSensorManageNameArdArr[numRelay].setText(locStr);
                            }
                            else tvRelayWithSensorManageNameArdArr[numRelay].setText(sRelayWithSensorManageNameArdArr[numRelay]);

                            //ЗАПОЛНЯЕМ СПИННЕР ПО ДАТЧИКАМ, меняем его цвет и тд
                            spSpinnerRelayWithSensorArdArr[numRelay].setSelection(numSensor);

                            if (Build.VERSION.SDK_INT >= 16)
                                spSpinnerRelayWithSensorArdArr[numRelay].setBackground(gd);
                            else spSpinnerRelayWithSensorArdArr[numRelay].setBackgroundDrawable(gd);
                        }

                        else
                        {
                            //ЗАПОЛНЯЕМ СПИННЕР ПО ДАТЧИКУ ГАЗА - его индекс всегда 6
                            spRelayForGas.setSelection(numRelay);

                            if (Build.VERSION.SDK_INT >= 16)
                                spRelayForGas.setBackground(gd);
                            else spRelayForGas.setBackgroundDrawable(gd);
                        }
                    }
                }
        }
    }

    private void FillRelaysName(String idDevice)
    {
        RealmResults<RelayRenamesDb> relayRenamesDbs = realm.where(RelayRenamesDb.class).equalTo(IDFIELDNAME, idDevice).findAll();
        for (int i = 0; i < relayRenamesDbs.size(); i++) {
            //получаем номер реле
            int numRelay = relayRenamesDbs.get(i).get_numRelay();
            numRelay -= 1;
            String strLoc;
            if (!TextUtils.isEmpty(relayRenamesDbs.get(i).get_bLocationRelay()))
            {
                sCurrRelayNameArdArr[numRelay] = relayRenamesDbs.get(i).get_bLocationRelay();
                sRelayWithSensorManageNameArdArr[numRelay] = sCurrRelayNameArdArr[numRelay];
                tvCurrRelayNameArdArr[numRelay].setText(sCurrRelayNameArdArr[numRelay]);
                tvRelayWithSensorManageNameArdArr[numRelay].setText(sRelayWithSensorManageNameArdArr[numRelay]);
            }
            else
            {
                tvCurrRelayNameArdArr[numRelay].setText(EMPTYDATA);
                tvRelayWithSensorManageNameArdArr[numRelay].setText(EMPTYDATA);
            }

            if (!TextUtils.isEmpty(relayRenamesDbs.get(i).get_bLocationRelayRus()))
            {
                strLoc = relayRenamesDbs.get(i).get_bLocationRelayRus();
                tvRelayWithSensorManageNameArdArr[numRelay].setText(strLoc);
            }

        }
    }

    private void SetDefaultValDeviceInfo()
    {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0xFF353637);
        gd.setCornerRadius(7);

        sGasName = "";
        tvGasName.setText(EMPTYDATA);
        iGasEdge = UNDEFINED;
        etGasEdge.setText(null);
        bSmsNoticeGas = true;
        chbSmsNoticeGas.setChecked(bSmsNoticeGas);

        if (Build.VERSION.SDK_INT >= 16)
            spRelayForGas.setBackground(gd);
        else spRelayForGas.setBackgroundDrawable(gd);

        spRelayForGas.setSelection(0);

        for (int j = 0; j < COUNTSENSORS; j++)
        {
            sCurrSensorNameArdArr[j] = "";
            tvCurrSensorNameArdArr[j].setText(EMPTYDATA);
            sNewSensorNameArdArr[j] = null;
            etNewSensorNameArdArr[j].setText(sNewSensorNameArdArr[j]);

            sSensorNameTempEdgesArdArr[j] = sCurrSensorNameArdArr[j];
            tvSensorNameTempEdgesArdArr[j].setText(EMPTYDATA);
            sMinTempEdgesArdArr[j] = UNDEFINED;
            sMaxTempEdgesArdArr[j] = UNDEFINED;
            etMinTempEdgesArdArr[j].setText(null);
            etMaxTempEdgesArdArr[j].setText(null);
            sTurnOnRelayWithSensorArdArr[j] = true;
            sTurnOffRelayWithSensorArdArr[j] = true;
            chbTurnOnRelayWithSensorArdArr[j].setChecked(sTurnOnRelayWithSensorArdArr[j]);
            chbTurnOffRelayWithSensorArdArr[j].setChecked(sTurnOffRelayWithSensorArdArr[j]);
        }
        for (int i = 0; i < COUNTRELAYS; i++)
        {
            sCurrRelayNameArdArr[i] = "";
            tvCurrRelayNameArdArr[i].setText(EMPTYDATA);
            sNewRelayNameArdArr[i] = null;
            etNewRelayNameArdArr[i].setText(sNewRelayNameArdArr[i]);
            sRelayWithSensorManageNameArdArr[i] = sCurrRelayNameArdArr[i];
            tvRelayWithSensorManageNameArdArr[i].setText(EMPTYDATA);
            bConfSmsNoticeRelayWithSensorArdArr[i] = true;
            chbConfSmsNoticeRelayWithSensorArdArr[i].setChecked(bConfSmsNoticeRelayWithSensorArdArr[i]);

            if (Build.VERSION.SDK_INT >= 16)
                spSpinnerRelayWithSensorArdArr[i].setBackground(gd);
            else spSpinnerRelayWithSensorArdArr[i].setBackgroundDrawable(gd);

            spSpinnerRelayWithSensorArdArr[i].setSelection(0);
        }
    }
    //endregion

    //region SendInfToArdFromAddConf(View v) Функция отправки инфы на ардуину по кнопкам
    public void SendInfToArdFromAddConf(View v)
    {
        int viewId = v.getId();
        if (CheckViews(viewId, v))
        {
            WriteDbData(viewId, v);
            FillData(); //Обновили экран с новыми данными
        }
    }

    //Проверка валидности введенных данных вьюх
    private boolean CheckViews(int rIdView, View v)
    {
        for (int i = 0; i < COUNTSENSORS; i++)
        {
            if (rIdView == rIdSaveSensorNameArdButtonArr[i] && TextUtils.isEmpty(etNewSensorNameArdArr[i].getText()))
            {
                etNewSensorNameArdArr[i].setError("Обязательно к заполнению!");
                return false;
            }
            else if (rIdView == rIdSaveTempEdgesButtonArdArr[i] && _sIdDevice != null)
            {
                return true;
            }
            else if (_sIdDevice == null)
            {
                ShowToast(v, "Отсутствует устройство!");
                return false;
            }
        }
        for (int j = 0; j < COUNTRELAYS; j++)
        {
            if (_sIdDevice == null)
            {
                ShowToast(v, "Отсутствует устройство!");
                return false;
            }
            else if (rIdView == rIdSaveRelayNameArdButtonArr[j] &&  TextUtils.isEmpty(etNewRelayNameArdArr[j].getText()))
            {
                ShowToast(v, "Отсутствует устройство!");
                etNewRelayNameArdArr[j].setError("Обязательно к заполнению!");
                return false;
            }
            else if (rIdView == rIdSaveRelayWithSensorButtonArdArr[j] && TextUtils.isEmpty(sRelayWithSensorManageNameArdArr[j]))
            {
                ShowToast(v, "Не задано имя реле!");
                return false;
            }
        }
        return true;
    }

    private void WriteDbData(int idView, View view)
    {
        String strSms;
        for (int i = 0; i < COUNTSENSORS; i++)
        {
            if (idView == rIdSaveSensorNameArdButtonArr[i])
            {
                //DALLASRENAME;1;BathRoom
                sNewSensorNameArdArr[i] = Translit.RusToLat(etNewSensorNameArdArr[i].getText().toString());
                strSms = "DALLASRENAME;" + String.valueOf(i) + ";" + sNewSensorNameArdArr[i];
                if (WriteDbSensors(i, idView)) //Переписали БД
                {
                    SendSms(_ardPhoneNumb, strSms); //Отправили смс на ардину с новым именем
                }
            }
            else if (idView == rIdSaveTempEdgesButtonArdArr[i])
            {
                if (TextUtils.isEmpty(etMinTempEdgesArdArr[i].getText().toString()))
                {
                    sMinTempEdgesArdArr[i] = UNDEFINED;
                }
                else sMinTempEdgesArdArr[i] = Integer.parseInt(etMinTempEdgesArdArr[i].getText().toString());

                if (TextUtils.isEmpty(etMaxTempEdgesArdArr[i].getText().toString()))
                {
                    sMaxTempEdgesArdArr[i] = UNDEFINED;
                }
                else sMaxTempEdgesArdArr[i] = Integer.parseInt(etMaxTempEdgesArdArr[i].getText().toString());

                sTurnOnRelayWithSensorArdArr[i] = chbTurnOnRelayWithSensorArdArr[i].isChecked();
                sTurnOffRelayWithSensorArdArr[i] = chbTurnOffRelayWithSensorArdArr[i].isChecked();
                // DALLASEDGE;  1;         20;25;1;1
                // имя команды  №датчика   нижний-вержний-вкл-выкл(при привышении)
                strSms = "DALLASEDGE;" +
                        String.valueOf(i) + ";" +
                        String.valueOf(sMinTempEdgesArdArr[i]) + ";" +
                        String.valueOf(sMaxTempEdgesArdArr[i]) + ";" +
                        AddDeviceActivity.BoolToIntStr(sTurnOnRelayWithSensorArdArr[i]) + ";" +
                        AddDeviceActivity.BoolToIntStr(sTurnOffRelayWithSensorArdArr[i]);
                if (WriteDbSensors(i, idView))
                {
                    SendSms(_ardPhoneNumb, strSms); //Отправили смс на ардину
                }
            }
        }
        for (int j = 0; j < COUNTRELAYS; j++)
        {
            if (idView == rIdSaveRelayNameArdButtonArr[j])
            {
                // RELAYRENAME;4;SweemingPool
                sNewRelayNameArdArr[j] = Translit.RusToLat(etNewRelayNameArdArr[j].getText().toString());
                strSms = "RELAYRENAME;" + String.valueOf(j) + ";" + sNewRelayNameArdArr[j];
                SendSms(_ardPhoneNumb, strSms); //Отправили смс на ардину с новым именем
                WriteDbRelays(j);
            }
            else if (idView == rIdSaveRelayWithSensorButtonArdArr[j])
            {
                // DALLASPREF;  5;  idRelay;         1
                //                   к какому реле     смс
                bConfSmsNoticeRelayWithSensorArdArr[j] = chbConfSmsNoticeRelayWithSensorArdArr[j].isChecked();

                set_numRelayArrayElement(j);
                set_numSensorArrayElement(spSpinnerRelayWithSensorArdArr[j].getSelectedItemPosition());

                strSms = "DALLASPREF;" +
                         String.valueOf(get_numSensorArrayElement()) + ";" +
                         String.valueOf(j) + ";" +
                         AddDeviceActivity.BoolToIntStr(bConfSmsNoticeRelayWithSensorArdArr[j]);

                if (WriteDbSensors(get_numSensorArrayElement(), idView))
                {
                    SendSms(_ardPhoneNumb, strSms); //Отправили смс на ардину с привязкой
                }
                else ShowToast(view, "Выбранный датчик уже привязан к другому реле...\n" +
                                     "Или же данные по датчикам не полные!");
            }
        }
        //Отдельно по датчику газа проверяем
        if (idView == btSaveGasPref.getId())
        {
            // GASPREF;45;1;1 или GASPREF;45;999;1
            // Соответственно переписываем порог газа, привязка к какому реле или RN и смс оповещение

            if (TextUtils.isEmpty(etGasEdge.getText().toString()))
            {
                iGasEdge = UNDEFINED;
            }
            else iGasEdge = Integer.parseInt(etGasEdge.getText().toString());

            bSmsNoticeGas = chbSmsNoticeGas.isChecked();

            strSms = "GASPREF;" +
                     String.valueOf(iGasEdge) + ";" +
                     String.valueOf(get_numRelayArrayElement()) + ";" +
                     AddDeviceActivity.BoolToIntStr(bSmsNoticeGas);

            set_numRelayArrayElement(spRelayForGas.getSelectedItemPosition());
            set_numSensorArrayElement(5);
            if (WriteDbSensors(get_numSensorArrayElement(), idView))
            {
                SendSms(_ardPhoneNumb, strSms); //Отправили смс на ардину с привязкой
            }
            else ShowToast(view, "Текущее реле уже привязано к другому датчику...\n" +
                                 "Или же отсутствуют данные по реле!");
        }
    }

    private boolean WriteDbSensors(int idArraySensors, int idView)
    {
        realm = Realm.getInstance(getBaseContext());
        realm.beginTransaction();
        //Получаем наш объект если он уже создан и хотим редактировать
        RealmResults<SensorsInfoDb> sensorsInfoDbs = realm.where(SensorsInfoDb.class).equalTo(IDFIELDNAME, _sIdDevice).findAll();
        RealmResults<RelayRenamesDb> relayRenamesDbs = realm.where(RelayRenamesDb.class).equalTo(IDFIELDNAME, _sIdDevice).findAll();

        if (sensorsInfoDbs.size() > 0)
        {
            int maxTimeStamp = 0;
            if (sensorsInfoDbs.size() <= PACKSMSCOUNT)
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
                if (sensorsInfoDbs.size() != PACKSMSCOUNT)
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

                //проверим целостность данных в листе - всегда должно быть 6 строк по 6 датчикам
                if (sensorsInfoDbs.size() != 6)
                {
                    realm.commitTransaction();
                    realm.close();
                    return false;
                }

                for (int j = 0; j < sensorsInfoDbs.size(); j++)
                {
                    int numSensor = sensorsInfoDbs.get(j).get_hNumSensor();
                    numSensor -= 1;

                    if (numSensor != idArraySensors)
                    {
                        continue;
                    }

                    if (numSensor != 5)
                    {
                        if (idView == rIdSaveSensorNameArdButtonArr[numSensor])
                        {
                            //Если номер датчика совпал с номером индекса массива значит это наш датчик - переписываем имя
                            //И если ид кнопки сохранения совпала с ид из массива по текущему датчику
                            sensorsInfoDbs.get(j).set_bLocationSensor(sNewSensorNameArdArr[idArraySensors]);
                        }
                        else if (idView == rIdSaveTempEdgesButtonArdArr[numSensor])
                        {
                            sensorsInfoDbs.get(j).set_minTempEdge(sMinTempEdgesArdArr[idArraySensors]);
                            sensorsInfoDbs.get(j).set_maxTempEdge(sMaxTempEdgesArdArr[idArraySensors]);
                            sensorsInfoDbs.get(j).set_turnOnRelayWithSensor(sTurnOnRelayWithSensorArdArr[idArraySensors]);
                            sensorsInfoDbs.get(j).set_turnOffRelayWithSensor(sTurnOffRelayWithSensorArdArr[idArraySensors]);
                        }
                        else if (idView == rIdSaveRelayWithSensorButtonArdArr[get_numRelayArrayElement()])
                        {
                            int ind = get_numRelayArrayElement();
                            //Проверим сначала не привязан ли текущий датчик уже к иному реле
                            for (int k = 0; k < sensorsInfoDbs.size(); k++)
                            {
                                if (!TextUtils.isEmpty(sensorsInfoDbs.get(k).get_bNumRelay()))
                                {
                                    int checkInd = Integer.parseInt(sensorsInfoDbs.get(k).get_bNumRelay());
                                    //тут проверяем что если текущее реле = привязанному реле на ином датчике
                                    //++ не берем в расчет текущий датчик - его не проверяем сам на себя, чтобы не вывалиться в ошибку
                                    //если выбранное реле на нем уже и привязано - пропускаем текущий датчик на проверку
                                    if (ind == (checkInd - 1)  && idArraySensors != k)
                                    {
                                        realm.commitTransaction();
                                        realm.close();
                                        return false;
                                    }
                                }
                            }
                            if (relayRenamesDbs.size() > 0)
                            {
                                if (!TextUtils.isEmpty(relayRenamesDbs.get(ind).get_bLocationRelay()))
                                {
                                    String loc = relayRenamesDbs.get(ind).get_bLocationRelay();
                                    sensorsInfoDbs.get(j).set_bLocationRelayRus(loc);
                                }
                                if (!TextUtils.isEmpty(relayRenamesDbs.get(ind).get_bLocationRelayRus()))
                                {
                                    String locRus = relayRenamesDbs.get(ind).get_bLocationRelayRus();
                                    sensorsInfoDbs.get(j).set_bLocationRelayRus(locRus);
                                }
                                boolean stateR = relayRenamesDbs.get(ind).get_bStateRelay();
                                sensorsInfoDbs.get(j).set_bStateRelay(stateR);
                                boolean manualManage = relayRenamesDbs.get(ind).get_isAutomaticModeR();
                                sensorsInfoDbs.get(j).set_bManualManageRelay(manualManage);
                            }
                            sensorsInfoDbs.get(j).set_bNumRelay(String.valueOf(ind + 1));
                            sensorsInfoDbs.get(j).set_bLocationRelay(sRelayWithSensorManageNameArdArr[ind]);
                            bConfSmsNoticeRelayWithSensorArdArr[ind] = chbConfSmsNoticeRelayWithSensorArdArr[ind].isChecked();
                            sensorsInfoDbs.get(j).set_bSmsNoticeSensor(bConfSmsNoticeRelayWithSensorArdArr[ind]);
                            sensorsInfoDbs.get(j).set_bManualManageRelay(true); //При привязке по умолчанию автоматика включена
                        }
                    }
                    else if (idView == btSaveGasPref.getId())
                    {
                        int ind = get_numRelayArrayElement();
                        //Проверим сначала не привязан ли текущий датчик уже к иному реле
                        for (int k = 0; k < sensorsInfoDbs.size(); k++)
                        {
                            if (!TextUtils.isEmpty(sensorsInfoDbs.get(k).get_bNumRelay()))
                            {
                                int checkInd = Integer.parseInt(sensorsInfoDbs.get(k).get_bNumRelay());
                                //тут проверяем что если текущее реле = привязанному реле на ином датчике
                                //++ не берем в расчет текущий датчик - его не проверяем сам на себя, чтобы не вывалиться в ошибку
                                //если выбранное реле на нем уже и привязано - пропускаем текущий датчик на проверку
                                if (ind == (checkInd - 1) && idArraySensors != k)
                                {
                                    realm.commitTransaction();
                                    realm.close();
                                    return false;
                                }
                            }
                        }
                        if (TextUtils.isEmpty(spRelayForGas.getSelectedItem().toString()))
                        {
                            realm.commitTransaction();
                            realm.close();
                            return false;
                        }
                        if (relayRenamesDbs.size() > 0)
                        {
                            if (!TextUtils.isEmpty(relayRenamesDbs.get(ind).get_bLocationRelay()))
                            {
                                String loc = relayRenamesDbs.get(ind).get_bLocationRelay();
                                sensorsInfoDbs.get(j).set_bLocationRelay(loc);
                            }
                            if (!TextUtils.isEmpty(relayRenamesDbs.get(ind).get_bLocationRelayRus()))
                            {
                                String locRus = relayRenamesDbs.get(ind).get_bLocationRelayRus();
                                sensorsInfoDbs.get(j).set_bLocationRelayRus(locRus);
                            }
                            boolean stateR = relayRenamesDbs.get(ind).get_bStateRelay();
                            sensorsInfoDbs.get(j).set_bStateRelay(stateR);
                            boolean manualManage = relayRenamesDbs.get(ind).get_isAutomaticModeR();
                            sensorsInfoDbs.get(j).set_bManualManageRelay(manualManage);
                        }
                        sensorsInfoDbs.get(j).set_bNumRelay(String.valueOf(ind + 1));
                        bSmsNoticeGas = chbSmsNoticeGas.isChecked();
                        sensorsInfoDbs.get(j).set_bSmsNoticeSensor(bSmsNoticeGas);
                        sensorsInfoDbs.get(j).set_gasEdge(iGasEdge);
                        sensorsInfoDbs.get(j).set_bManualManageRelay(true); //При привязке по умолчанию автоматика включена
                    }
                }
        }
        realm.commitTransaction();
        return true;
    }

    private void WriteDbRelays(int idArray)
    {
        realm = Realm.getInstance(getBaseContext());
        realm.beginTransaction();

        RelayRenamesDb relayRenamesDb = new RelayRenamesDb();
        relayRenamesDb.set_numRelay(idArray + 1);
        relayRenamesDb.set_idDevice(_sIdDevice);
        relayRenamesDb.set_bLocationRelay(sNewRelayNameArdArr[idArray]);

        realm.copyToRealmOrUpdate(relayRenamesDb);

        //Получаем наш объект если он уже создан и хотим редактировать
        RealmResults<SensorsInfoDb> sensorsInfoDbs = realm.where(SensorsInfoDb.class).equalTo(IDFIELDNAME, _sIdDevice).findAll();

        if (sensorsInfoDbs.size() > 0)
        {
            int maxTimeStamp = 0;
            if (sensorsInfoDbs.size() <= PACKSMSCOUNT)
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
                if (sensorsInfoDbs.size() != PACKSMSCOUNT)
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
                    if (!TextUtils.isEmpty(sensorsInfoDbs.get(j).get_bNumRelay()) &&
                        Integer.parseInt(sensorsInfoDbs.get(j).get_bNumRelay()) == (idArray + 1))
                    {
                        //Если номер датчика совпал с номером индекса массива значит это наш датчик - переписываем имя
                        sensorsInfoDbs.get(j).set_bLocationRelay(sNewRelayNameArdArr[idArray]);
                    }
                }
        }
        realm.commitTransaction();
    }
    //endregion

    private void ShowToast(View view, String toastText)
    {
        //создаем и отображаем текстовое уведомление
        Toast toast = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0xFFCB3E3E);
        gd.setCornerRadius(10);

        View toastView = toast.getView();
        toastView.setPadding(10, 10, 10, 10);

        if(Build.VERSION.SDK_INT >= 16)
            toastView.setBackground(gd);
        else toastView.setBackgroundDrawable(gd);

        toast.show();
    }

    //region SendSms Функция отправки смс
    public void SendSms(String number, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, null, null);
    }
    //endregion

    //region создаем меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // получаем все пункты меню
        //пункт item определен в menu_main
        int itemId = item.getItemId();
        Intent intent;

        // ищем наш пункт меню
        switch (itemId)
        {
            case R.id.pref:
                intent = new Intent(AdditionalySettingsActivity.this, PrefActivity.class);
                startActivity(intent);
                return true;
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
    protected void onResume() {
        super.onResume();
        FillData();
    }
}