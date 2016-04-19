package com.korpaev.myhome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class GeneralInfoActivity extends Activity
{
    //region Основные переменные
    ImageButton ibUpdate;
    Realm realm;
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
        //realm.where(RawSmsDb.class).findAll().clear();
        //realm.commitTransaction();
        //MessageService ms = new MessageService();
        //String str = "SH1;1460473840;1;8;RN;0C;RN;SH1;1460473840;2;8;RN;0C;RN;SH1;1460473840;3;8;RN;0C;RN;SH1;1460473840;4;8;RN;0C;RN;SH1;1460473840;5;8;RN;0C;RN;";
        //ms.WriteDataToDB(str, getBaseContext());
        //FillData();
    }

    public void FillData()
    {
        Date date;
        SimpleDateFormat sdf;
        String formattedDate;
        realm = Realm.getInstance(getBaseContext());
        realm.beginTransaction();
        //Посмотрим что лежит в БД после записи данных
        RealmResults<RawSmsDb> results = realm.where(RawSmsDb.class).findAll();
        for (int i = 0; i < results.size(); i++)
        {
            int tS = results.get(i).get_hTimeStamp();
            date = new Date(tS * 1000L); // *1000 is to convert seconds to milliseconds
            sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            formattedDate = sdf.format(date);
        }
        realm.commitTransaction();
    }

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

    //событие на выбранный пункт меню
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

    //region Поиск всех объектов на экране
    protected void FindViews()
    {
        ibUpdate = (ImageButton) findViewById(R.id.bUpd);
    }
    //endregion
}
