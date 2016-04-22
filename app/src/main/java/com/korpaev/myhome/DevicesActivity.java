package com.korpaev.myhome;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;

public class DevicesActivity extends Activity implements OnItemClickListener
{

    final String IDFIELDNAME = "_idDevice"; //Имя поля БД

    Realm realm;
    Button bNewDevice;
    private ListView _listViewDevices;
    private ArrayList<DeviceInfoRow> _arrListDevices;
    DeviceInfoAdapter deviceInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        FindViews();
        FillData();
        registerForContextMenu(_listViewDevices);
        _listViewDevices.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Toast.makeText(this, "ТЫ СУКА НАЖАЛ НА ITEM!!!", Toast.LENGTH_SHORT).show();
    }

    private void FindViews()
    {
        bNewDevice = (Button)findViewById(R.id.newDeviceButton);
        _listViewDevices = (ListView)findViewById(R.id.listViewDevices);
    }

    public void FillData()
    {
        _arrListDevices = new ArrayList<DeviceInfoRow>();
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
        deviceInfoAdapter = new DeviceInfoAdapter(this, _arrListDevices);
        _listViewDevices.setAdapter(deviceInfoAdapter);
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int indexRow = info.position;
        String idRow = _arrListDevices.get(indexRow).getId();

        if(item.getTitle() == getString(R.string.editItem))
        {
            Intent intent;
            intent = new Intent(getBaseContext(), AddDeviceActivity.class);
            intent.putExtra(IDFIELDNAME, idRow);
            startActivity(intent);
        }
        if(item.getTitle() == getString(R.string.deleteItem))
        {
            RealmResults<AutorizedPhonesDb> resAutorized = realm.where(AutorizedPhonesDb.class).equalTo(IDFIELDNAME, idRow).findAll();
            RealmResults<DevicesInfoDb> resDevInfo = realm.where(DevicesInfoDb.class).equalTo(IDFIELDNAME, idRow).findAll();
            realm.beginTransaction();
            for (int i = 0; i < resDevInfo.size(); i++)
            {
                for (int j = 0; j < resAutorized.size(); j++)
                {
                    AutorizedPhonesDb autP = resAutorized.get(j);
                    autP.removeFromRealm();
                    resAutorized.clear();
                }
                DevicesInfoDb di = resDevInfo.get(i);
                di.removeFromRealm();
                resDevInfo.clear();
            }
            realm.commitTransaction();
        }
        FillData();
        return true;
    }
}