package com.korpaev.myhome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class HistoryActivity extends Activity
{
    final String IDFIELDNAME = "_idDevice"; //Имя поля БД
    private final String NAMESHAREDPREF = "IdDevicePref";

    Realm realm;
    SharedPreferences sharedPref;

    private ListView _listViewHistory;
    private ArrayList<HistoryRow> _arrListHistoryRows;
    HistoryAdapter historyAdapter;

    private String _idDeviseBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        FindViews();
        FillData();
    }

    private void FindViews()
    {
        _listViewHistory = (ListView)findViewById(R.id.listViewHistory);
    }

    public void FillData()
    {
        //Используем созданный файл данных SharedPreferences:
        sharedPref = getSharedPreferences(NAMESHAREDPREF, MODE_PRIVATE);
        _idDeviseBase64 = sharedPref.getString(IDFIELDNAME, null);

        if (!TextUtils.isEmpty(_idDeviseBase64))
        {
            _arrListHistoryRows = new ArrayList<HistoryRow>();
            if (Realm.getInstance(getBaseContext()) != null)
            {
                String _smsBody, _dateTime;

                realm = Realm.getInstance(getBaseContext());

                RealmResults<HistoryDb> results = realm.where(HistoryDb.class).equalTo(IDFIELDNAME, _idDeviseBase64).findAll();
                for (int i = 0; i < results.size(); i++)
                {
                    _smsBody = results.get(i).get_sMessage();
                    _dateTime = results.get(i).get_timeVal();

                    HistoryRow historyRow = new HistoryRow();

                    historyRow.setId(_idDeviseBase64);
                    historyRow.setDateTime(_dateTime);
                    historyRow.setSmsBody(_smsBody);

                    _arrListHistoryRows.add(historyRow);
                }
            }
            historyAdapter = new HistoryAdapter(this, _arrListHistoryRows);
            _listViewHistory.setAdapter(historyAdapter);
        }
    }

    //создаем меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        //заполняем меню
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (menu != null)
            menu.findItem(R.id.main_inf).setVisible(false);
        return true;
    }

    //событие на выбранный пункт меню
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
            case R.id.pref:
                intent = new Intent(HistoryActivity.this, PrefActivity.class);
                startActivity(intent);
                return true;
            case R.id.about:
                intent = new Intent(HistoryActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.conf:
                intent = new Intent(HistoryActivity.this, SettingsActivityTabs.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}