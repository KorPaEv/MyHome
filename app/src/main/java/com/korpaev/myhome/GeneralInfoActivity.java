package com.korpaev.myhome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class GeneralInfoActivity extends Activity
{
    //region КОНСТАНТЫ
    private final String NAMESHAREDPREF = "IdDevicePref";
    private final String SENSORSINFOTABLENAME = "_stateSystemRaws";
    private final String IDFIELDNAME = "_idDevice"; //Имя поля БД
    private final String TIMESTAMPFIELDNAME = "_hTimeStamp";
    private final String EMPTYDATA = "Данные отсутствуют";
    private final int COUNTSENSORS = 6;
    private final String EMPTYDATASENSOR = "Нет данных";
    private final String EMPTYNAMESENSOR = "Датчик №";
    //endregion

    //region ОБЪЯВЛЯЕМ ОБЪЕКТЫ
    ImageButton ibUpdate;
    Realm realm;
    SharedPreferences sharedPref;
    TextView tvPhoneArdGenInf, tvNameAdrGenInf, tvAddressArdGenInf;
    //endregion

    //region МАССИВЫ ID ВЬЮХ АКТИВИТИ
    final int[] rIdSensorsNameArr = {R.id.tvSensorOneName,
                                     R.id.tvSensorTwoName,
                                     R.id.tvSensorThreeName,
                                     R.id.tvSensorFourName,
                                     R.id.tvSensorFiveName,
                                     R.id.tvSensorSixName};
    final int[] rIdSensorsValArr = {R.id.tvSensorOneVal,
                                    R.id.tvSensorTwoVal,
                                    R.id.tvSensorThreeVal,
                                    R.id.tvSensorFourVal,
                                    R.id.tvSensorFiveVal,
                                    R.id.tvSensorSixVal};
    //endregion

    //region МАССИВЫ ВЬЮХ АКТИВИТИ
    final TextView[] tvSensorsNameArr = new TextView[COUNTSENSORS];
    final TextView[] tvSensorsValArr = new TextView[COUNTSENSORS];
    //endregion

    //region Переменные и массивы для хранения значений вьюх
    private String _sPhoneArdGenInf, _sNameAdrGenInf, _sAddressArdGenInf,
            _sBundleIdDevice; //Это ИД девайса который может прийти с другого активити
    final String[] sSensorsNameArr = new String[COUNTSENSORS];
    final String[] sSensorsValArr = new String[COUNTSENSORS];
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_info);

        FindViews();

        //region Обработчик нажатия на кнопку получения данных и сохранения номера - меняем картинку при нажатии
        ibUpdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        ibUpdate.setImageResource(R.mipmap.ic_bupd_1);
                        GetSensorsInfoButtonClick(v);
                        return true;
                    case MotionEvent.ACTION_UP:
                        ibUpdate.setImageResource(R.mipmap.ic_bupd);
                        break;
                }
                // tell the system that we handled the event but a further processing is required
                return false;
            }
        });
        //endregion

        FillData();
    }

    //region FindViews() Поиск всех объектов на экране
    protected void FindViews()
    {
        ibUpdate = (ImageButton) findViewById(R.id.bUpd);
        tvPhoneArdGenInf = (TextView)findViewById(R.id.tvPhoneArdGenInf);
        tvNameAdrGenInf = (TextView)findViewById(R.id.tvNameAdrGenInf);
        tvAddressArdGenInf = (TextView)findViewById(R.id.tvAddressArdGenInf);
        for (int i = 0; i < COUNTSENSORS; i++)
        {
            tvSensorsNameArr[i] = (TextView)findViewById(rIdSensorsNameArr[i]);
            tvSensorsValArr[i] = (TextView)findViewById(rIdSensorsValArr[i]);
        }
    }
    //endregion

    //region GetSensorsInfoButtonClick(View v) Обработка нажатия на кнопку получения инфы по датчикам
    private void GetSensorsInfoButtonClick(View v)
    {
        MessageService ms = new MessageService();
        String str = "SH1;1460473840;1;8;RN;0C;RN;SH1;1460473840;2;8;RN;0C;RN;SH1;1460473840;3;8;RN;0C;RN;SH1;1460473840;4;8;RN;0C;RN;SH1;1460473840;5;8;RN;0C;RN;";
        ms.WriteDataToDB(str, getBaseContext());
    }
    //endregion

    //region FillData() Заполняем вьюхи данными
    private void FillData()
    {
        //Используем созданный файл данных SharedPreferences:
        sharedPref = getSharedPreferences(NAMESHAREDPREF, MODE_PRIVATE);
        _sBundleIdDevice = sharedPref.getString(IDFIELDNAME, null);

        if (!TextUtils.isEmpty(_sBundleIdDevice))
        {
            FillViews(_sBundleIdDevice);
        }
        else SetDefaultValDeviceInfo();
    }

    private void FillViews(String idRow)
    {
        realm = Realm.getInstance(getBaseContext());

        //Получаем максимальное время
        RealmResults<SensorsInfoDb> sensorsInfoDbs = realm.where(SensorsInfoDb.class).equalTo(IDFIELDNAME, idRow).findAll();
        int maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

        //Получаем только свежие записи по максимальному времени
        RealmResults<DevicesInfoDb> devicesInfoDbs = realm.where(DevicesInfoDb.class)
                                                          .equalTo(IDFIELDNAME, idRow)
                                                          .equalTo(SENSORSINFOTABLENAME + "." + TIMESTAMPFIELDNAME, maxTimeStamp)
                                                          .findAll();
        RealmList<SensorsInfoDb> sensorsInfoList;

        for (int i = 0; i < devicesInfoDbs.size(); i++)
        {
            _sPhoneArdGenInf = devicesInfoDbs.get(i).get_phoneNumbArduino();
            _sNameAdrGenInf = devicesInfoDbs.get(i).get_nameDevice();
            _sAddressArdGenInf = devicesInfoDbs.get(i).get_address();

            tvPhoneArdGenInf.setText(_sPhoneArdGenInf);
            tvNameAdrGenInf.setText(_sNameAdrGenInf);
            tvAddressArdGenInf.setText(_sAddressArdGenInf);

            sensorsInfoList = devicesInfoDbs.get(i).get_stateSystemRaws();
            for (int j = 0; j < sensorsInfoList.size(); j++)
            {
                sSensorsNameArr[j] = sensorsInfoList.get(j).get_bLocationSensor();
                sSensorsValArr[j] = sensorsInfoList.get(j).get_bValSensor();
                tvSensorsNameArr[j].setText(sSensorsNameArr[j]);
                tvSensorsValArr[j].setText(sSensorsValArr[j]);
            }
        }
    }

    private void SetDefaultValDeviceInfo()
    {
        _sPhoneArdGenInf = null;
        _sNameAdrGenInf = null;
        _sAddressArdGenInf = null;

        tvPhoneArdGenInf.setText(EMPTYDATA);
        tvNameAdrGenInf.setText(EMPTYDATA);
        tvAddressArdGenInf.setText(EMPTYDATA);

        for (int i = 0; i < COUNTSENSORS; i++)
        {
            sSensorsNameArr[i] = null;
            sSensorsValArr[i] = null;
            tvSensorsNameArr[i].setText(EMPTYNAMESENSOR + String.valueOf(i));
            tvSensorsValArr[i].setText(EMPTYDATASENSOR);
        }
    }
    //endregion

//    public void FillData()
//    {
//        Date date;
//        SimpleDateFormat sdf;
//        String formattedDate;
//        realm = Realm.getInstance(getBaseContext());
//        realm.beginTransaction();
//        //Посмотрим что лежит в БД после записи данных
//        RealmResults<SensorsInfoDb> results = realm.where(SensorsInfoDb.class).findAll();
//        for (int i = 0; i < results.size(); i++)
//        {
//            int tS = results.get(i).get_hTimeStamp();
//            date = new Date(tS * 1000L); // *1000 is to convert seconds to milliseconds
//            sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//            formattedDate = sdf.format(date);
//        }
//        realm.commitTransaction();
//    }

    //region Создаем меню и обрабатываем действие при выборе пункта меню
    @Override
    public boolean onCreateOptionsMenu(Menu currMenu) {
        super.onCreateOptionsMenu(currMenu);

        //заполняем меню
        getMenuInflater().inflate(R.menu.menu_main, currMenu);
        if (currMenu != null)
            currMenu.findItem(R.id.main_inf).setVisible(false);
        return true;
    }

    //событие на выбранный пункт меню onOptionsItemSelected(MenuItem item)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // получаем все пункты меню
        //пункт item определен в menu_main
        int itemId = item.getItemId();
        Intent intent;
        Bundle bundle = new Bundle();

        // ищем наш пункт меню
        switch (itemId) {
            case R.id.about:
                intent = new Intent(GeneralInfoActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.conf:
                intent = new Intent(getApplicationContext(), SettingsActivityTabs.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
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

    @Override
    protected void onPause()
    {
        super.onPause();
        SaveSharedPref(null);
    }

    //region SaveSharedPref(View v) Сохраняем ИД текущий с которым сейчас работаем
    private void SaveSharedPref(View v)
    {
        Toast.makeText(this, "Сохраняем...", Toast.LENGTH_SHORT).show();
        //Создаем объект Editor для создания пар имя-значение:
        sharedPref = getSharedPreferences("IdDevicePref", MODE_PRIVATE);
        //Создаем объект Editor для создания пар имя-значение:
        SharedPreferences.Editor shpEditor = sharedPref.edit();
        shpEditor.putString(IDFIELDNAME, _sBundleIdDevice);
        shpEditor.commit();
    }
    //endregion
}
