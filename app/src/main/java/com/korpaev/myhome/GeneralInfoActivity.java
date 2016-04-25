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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

public class GeneralInfoActivity extends Activity
{
    private final String IDFIELDNAME = "_idDevice"; //Имя поля БД
    private final String EMPTYDATA = "Данные отсутствуют";

    ImageButton ibUpdate;
    Realm realm;
    SharedPreferences sharedPref;
    TextView tvPhoneArdGenInf, tvNameAdrGenInf, tvAddressArdGenInf;

    private String _sPhoneArdGenInf, _sNameAdrGenInf, _sAddressArdGenInf,
                   _sBundleIdDevice; //Это ИД девайса который может прийти с другого активити
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

        //realm = Realm.getInstance(getBaseContext());
        //realm.beginTransaction();
        //Очистим БД от старых данных
        //realm.where(SensorsInfoDb.class).findAll().clear();
        //realm.commitTransaction();
        //MessageService ms = new MessageService();
        //String str = "SH1;1460473840;1;8;RN;0C;RN;SH1;1460473840;2;8;RN;0C;RN;SH1;1460473840;3;8;RN;0C;RN;SH1;1460473840;4;8;RN;0C;RN;SH1;1460473840;5;8;RN;0C;RN;";
        //String str = "INFSH;1;Dom;Dimitrova;+79998887766;1;1;1;+79998886655;1;1;1;\"\";0;0;0;\"\";0;0;0";
        //ms.WriteDataToDB(str, getBaseContext());
        FillData();
    }

    //region FillData() Заполняем вьюхи данными
    private void FillData()
    {
        //Используем созданный файл данных SharedPreferences:
        sharedPref = getSharedPreferences("IdDevicePref", MODE_PRIVATE);
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
        RealmResults<DevicesInfoDb> results = realm.where(DevicesInfoDb.class).equalTo(IDFIELDNAME, idRow).findAll();
        for (int i = 0; i < results.size(); i++)
        {
            _sPhoneArdGenInf = results.get(i).get_phoneNumbArduino();
            _sNameAdrGenInf = results.get(i).get_nameDevice();
            _sAddressArdGenInf = results.get(i).get_address();

            tvPhoneArdGenInf.setText(_sPhoneArdGenInf);
            tvNameAdrGenInf.setText(_sNameAdrGenInf);
            tvAddressArdGenInf.setText(_sAddressArdGenInf);
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

    //region FindViews() Поиск всех объектов на экране
    protected void FindViews()
    {
        ibUpdate = (ImageButton) findViewById(R.id.bUpd);
        tvPhoneArdGenInf = (TextView)findViewById(R.id.tvPhoneArdGenInf);
        tvNameAdrGenInf = (TextView)findViewById(R.id.tvNameAdrGenInf);
        tvAddressArdGenInf = (TextView)findViewById(R.id.tvAddressArdGenInf);
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
