package com.korpaev.myhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class GeneralInfoActivity extends Activity
{
    //region КОНСТАНТЫ
    private final String NAMESHAREDPREF = "IdDevicePref";
    private final String SENSORSINFOTABLENAME = "_stateSystemRaws";
    private final String IDFIELDNAME = "_idDevice"; //Имя поля БД
    private final String TIMESTAMPFIELDNAME = "_hTimeStamp";
    private final String EMPTYDATA = "Не задано";
    private final int COUNTSENSORS = 6;
    private final String EMPTYDATASENSOR = "Нет данных";
    private final String EMPTYNAMESENSOR = "Датчик №";
    final Calendar calendar = Calendar.getInstance();
    //endregion

    //region ОБЪЯВЛЯЕМ ОБЪЕКТЫ
    ImageButton ibUpdate;
    Realm realm;
    SharedPreferences sharedPref;
    TextView tvPhoneArdGenInf, tvNameAdrGenInf, tvAddressArdGenInf, tvUpdateTime;
    View tmpView;
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
        ibUpdate.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        ibUpdate.setImageResource(R.mipmap.ic_bupd_1);
                        tmpView = v;

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GeneralInfoActivity.this);

                        alertDialog.setTitle("Запросить данные...");
                        alertDialog.setMessage("Хотите обновить информацию?");

                        //region YES CLICK
                        alertDialog.setPositiveButton("ДА", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                GetSensorsInfoButtonClick(tmpView);
                            }
                        });
                        //endregion

                        //region NO CLICK
                        alertDialog.setNegativeButton("НЕТ", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        });
                        //endregion
                        alertDialog.show();
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
        ibUpdate = (ImageButton)findViewById(R.id.bUpd);
        tvUpdateTime = (TextView)findViewById(R.id.tvLastSaveTime);
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
        //MessageService ms = new MessageService();
        //String str = "SH1;1460473840;1;8;RN;15C;1;RN;SH1;1460473840;2;8;RN;12C;1;RN;SH1;1460473840;3;8;RN;0C;1;RN;SH1;1460473840;4;8;RN;0C;1;RN;SH1;1460473840;5;8;RN;0C;1;RN;";
        //ms.WriteDataToDB(str, getBaseContext());
        SendSms(_sPhoneArdGenInf, "STATEA");
    }
    //endregion

    //region SendSms Функция отправки смс
    public void SendSms(String number, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, null, null);
    }
    //endregion

    //region FillData() Заполняем вьюхи данными
    private void FillData()
    {
        //Используем созданный файл данных SharedPreferences:
        sharedPref = getSharedPreferences(NAMESHAREDPREF, MODE_PRIVATE);
        _sBundleIdDevice = sharedPref.getString(IDFIELDNAME, null);
        SetDefaultValDeviceInfo();
        if (!TextUtils.isEmpty(_sBundleIdDevice))
        {
            ibUpdate.setEnabled(true);
            ibUpdate.setImageResource(R.mipmap.ic_bupd);
            FillViews(_sBundleIdDevice);
        }
    }

    private void FillViews(String idRow)
    {
        realm = Realm.getInstance(getBaseContext());
        String timeStr = "Информация актуальна на: ";

        RealmResults<DevicesInfoDb> devicesInfoDbs;
        RealmResults<SensorsInfoDb> sensorsInfoDbs = realm.where(SensorsInfoDb.class).findAll();

        if (sensorsInfoDbs.size() > 0)
        {
            int maxTimeStamp = 0;
            if (sensorsInfoDbs.size() <= COUNTSENSORS)
            {
                //Получаем максимальное время
                maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

                sensorsInfoDbs = realm.where(SensorsInfoDb.class)
                        .equalTo(IDFIELDNAME, idRow)
                        .equalTo(TIMESTAMPFIELDNAME, maxTimeStamp)
                        .findAll();
            }
            else
            {
                //Получаем максимальное время
                maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

                sensorsInfoDbs = realm.where(SensorsInfoDb.class)
                        .equalTo(IDFIELDNAME, idRow)
                        .equalTo(TIMESTAMPFIELDNAME, maxTimeStamp)
                        .findAll();

                //если у нас записей в пачке мало, то выбираем целую пачку и отображаем ее
                if (sensorsInfoDbs.size() != COUNTSENSORS)
                {
                    sensorsInfoDbs = realm.where(SensorsInfoDb.class)
                            .equalTo(IDFIELDNAME, idRow)
                            .lessThan(TIMESTAMPFIELDNAME, maxTimeStamp)
                            .findAll();

                    maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

                    sensorsInfoDbs = realm.where(SensorsInfoDb.class)
                            .equalTo(IDFIELDNAME, idRow)
                            .equalTo(TIMESTAMPFIELDNAME, maxTimeStamp)
                            .findAll();
                }

            }

            tvUpdateTime.setText(timeStr + GetUpdateTime(maxTimeStamp));

            //Получаем только свежие записи по максимальному времени
            devicesInfoDbs = realm.where(DevicesInfoDb.class)
                    .equalTo(IDFIELDNAME, idRow)
                    .equalTo(SENSORSINFOTABLENAME + "." + TIMESTAMPFIELDNAME, maxTimeStamp)
                    .findAll();

            for (int i = 0; i < devicesInfoDbs.size(); i++)
            {
                _sPhoneArdGenInf = devicesInfoDbs.get(i).get_phoneNumbArduino();
                _sNameAdrGenInf = devicesInfoDbs.get(i).get_nameDevice();
                _sAddressArdGenInf = devicesInfoDbs.get(i).get_address();

                tvPhoneArdGenInf.setText(_sPhoneArdGenInf);
                tvNameAdrGenInf.setText(_sNameAdrGenInf);
                tvAddressArdGenInf.setText(_sAddressArdGenInf);

                for (int j = 0; j < sensorsInfoDbs.size(); j++)
                {
                    int numSensor = sensorsInfoDbs.get(j).get_hNumSensor();
                    numSensor -= 1;
                    if (!TextUtils.isEmpty(sensorsInfoDbs.get(j).get_bLocationSensorRus()))
                    {
                        sSensorsNameArr[numSensor] = sensorsInfoDbs.get(j).get_bLocationSensorRus();
                    }
                    else sSensorsNameArr[numSensor] = sensorsInfoDbs.get(j).get_bLocationSensor();
                    tvSensorsNameArr[numSensor].setText(sSensorsNameArr[numSensor]);

                    sSensorsValArr[numSensor] = sensorsInfoDbs.get(j).get_bValSensor();
                    tvSensorsValArr[numSensor].setText(sSensorsValArr[numSensor]);
                }
            }
        }
        else
        {
            //Получаем только записи для вывода инфы о текущем устройстве
            devicesInfoDbs = realm.where(DevicesInfoDb.class)
                    .equalTo(IDFIELDNAME, idRow)
                    .findAll();
            for (int i = 0; i < devicesInfoDbs.size(); i++)
            {
                _sPhoneArdGenInf = devicesInfoDbs.get(i).get_phoneNumbArduino();
                _sNameAdrGenInf = devicesInfoDbs.get(i).get_nameDevice();
                _sAddressArdGenInf = devicesInfoDbs.get(i).get_address();

                tvPhoneArdGenInf.setText(_sPhoneArdGenInf);
                tvNameAdrGenInf.setText(_sNameAdrGenInf);
                tvAddressArdGenInf.setText(_sAddressArdGenInf);
            }
        }
    }

    private void SetDefaultValDeviceInfo()
    {
        tvUpdateTime.setText(R.string.tvLastSaveTime);

        ibUpdate.setEnabled(false);
        ibUpdate.setImageResource(R.mipmap.ic_bupd_disable);

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

    public String GetUpdateTime(int timeStamp)
    {

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String strDate = sdf.format(calendar.getTime());
//        return strDate;

        Date date;
        SimpleDateFormat sdf;
        //Посмотрим что лежит в БД после записи данных
        RealmResults<SensorsInfoDb> results = realm.where(SensorsInfoDb.class).findAll();
        date = new Date(timeStamp * 1000L); // *1000 is to convert seconds to milliseconds
        sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(date);
    }
    //endregion

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
            case R.id.pref:
                intent = new Intent(GeneralInfoActivity.this, PrefActivity.class);
                startActivity(intent);
                return true;
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
