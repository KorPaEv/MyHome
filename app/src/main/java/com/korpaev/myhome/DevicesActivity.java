package com.korpaev.myhome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;

public class DevicesActivity extends Activity
{
    Realm realm;
    Button bNewDevice;
    private ListView _listViewDevices;
    private ArrayList<DeviceInfoRow> _arrListDevices = new ArrayList<DeviceInfoRow>();
    DeviceInfoAdapter deviceInfoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        FindViews();
        ReadRowsDb();
        deviceInfoAdapter = new DeviceInfoAdapter(this, _arrListDevices);
        _listViewDevices.setAdapter(deviceInfoAdapter);
        registerForContextMenu(_listViewDevices);

    }

    private void FindViews()
    {
        bNewDevice = (Button)findViewById(R.id.newDeviceButton);
        _listViewDevices = (ListView)findViewById(R.id.listViewDevices);
    }

    public void ReadRowsDb()
    {
        if (Realm.getInstance(getBaseContext()) != null)
        {
            String _idDeviseBase64;
            String _nameDevice;

            realm = Realm.getInstance(getBaseContext());
            realm.beginTransaction();

            RealmResults<DevicesInfoDb> results = realm.where(DevicesInfoDb.class).findAll();
            for (int i = 0; i < results.size(); i++)
            {
                _idDeviseBase64 = results.get(i).get_idDevice();
                _nameDevice = results.get(i).get_nameDevice();
                DeviceInfoRow dir = new DeviceInfoRow(_idDeviseBase64, _nameDevice);
                _arrListDevices.add(dir);
            }
            realm.commitTransaction();
        }
    }

    public void NewDeviceButtonClick(View view)
    {
        Intent intent;
        intent = new Intent(getApplicationContext(), AddDeviceActivity.class);
        startActivity(intent);
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
            case R.id.about:
                intent = new Intent(DevicesActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.conf:
                intent = new Intent(DevicesActivity.this, SettingsActivityTabs.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        menu.add(0, v.getId(), 0, R.string.editItem);
        menu.add(0, v.getId(), 0, R.string.deleteItem);
    }

    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getTitle() == getString(R.string.editItem))
        {

        }
        if(item.getTitle() == getString(R.string.deleteItem))
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int index = info.position;
        }
        return true;
    }
}