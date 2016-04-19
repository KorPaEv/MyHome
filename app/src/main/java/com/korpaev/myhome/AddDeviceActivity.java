package com.korpaev.myhome;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddDeviceActivity extends Activity
{
    final String PROTOCOLVERFIELD = "_hProtocolVer";
    Realm realm;
    Button bCancel, bAddDevice;
    EditText etPhoneArduino, etNameDevice, etLocationAddr, etProtocolVer,
             etAutorizePhoneOne, etAutorizePhoneTwo, etAutorizePhoneThree, etAutorizePhoneFour;

    String _sPhoneArduino, _sNameDevice, _sLocationAddr, _sProtocolVer,
           _sAutorizePhoneOne, _sAutorizePhoneTwo, _sAutorizePhoneThree, _sAutorizePhoneFour;

    final String[] sAutorizePhoneArray = {_sAutorizePhoneOne, _sAutorizePhoneTwo, _sAutorizePhoneThree, _sAutorizePhoneFour};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        FindViews();
        etProtocolVer.setText(String.valueOf(SetMaxProtocolVer()));
        etProtocolVer.setEnabled(false);
    }

    private void FindViews()
    {
        bCancel = (Button)findViewById(R.id.cancelButton);
        bAddDevice = (Button)findViewById(R.id.addDeviceButton);
        etPhoneArduino = (EditText)findViewById(R.id.ePhoneNumber);
        etNameDevice = (EditText)findViewById(R.id.eNameDevice);
        etLocationAddr = (EditText)findViewById(R.id.eAddressDevice);
        etProtocolVer = (EditText)findViewById(R.id.eProtocolVerDevice);
        etAutorizePhoneOne = (EditText)findViewById(R.id.eAutorizePhoneOne);
        etAutorizePhoneTwo = (EditText)findViewById(R.id.eAutorizePhoneTwo);
        etAutorizePhoneThree = (EditText)findViewById(R.id.eAutorizePhoneThree);
        etAutorizePhoneFour = (EditText)findViewById(R.id.eAutorizePhoneFour);
    }

    private int SetMaxProtocolVer()
    {
        realm = Realm.getInstance(getBaseContext());
        RealmResults res = realm.where(DevicesInfoDb.class).findAll();
        int maxProtocolV;
        if (res.max(PROTOCOLVERFIELD) == null)
        {
            return 1;
        }
        else
        {
            maxProtocolV = res.max(PROTOCOLVERFIELD).intValue();
            maxProtocolV++;
        }
        return maxProtocolV;
    }

    public void CancelButtonClick(View v)
    {
        AddDeviceActivity.this.finish();
    }

    public void AddDeviceButtonClick(View v)
    {
        if (CheckEmptyViews())
        {
            WriteDbRaws();
            Intent intent;
            intent = new Intent(getBaseContext(), MainActivityTabs.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Заполните хотя бы один разрешенный номер", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean CheckEmptyViews()
    {
        GetTextViews();
        if(TextUtils.isEmpty(_sPhoneArduino))
        {
            etPhoneArduino.setError("Обязательно к заполнению!");
            return false;
        }
        else if (TextUtils.isEmpty(_sNameDevice))
        {
            etNameDevice.setError("Обязательно к заполнению!");
            return false;
        }
        else if (TextUtils.isEmpty(_sLocationAddr))
        {
            etLocationAddr.setError("Обязательно к заполнению!");
            return false;
        }
        else if (!TextUtils.isEmpty(_sAutorizePhoneOne) ||
                 !TextUtils.isEmpty(_sAutorizePhoneTwo) ||
                 !TextUtils.isEmpty(_sAutorizePhoneThree) ||
                 !TextUtils.isEmpty(_sAutorizePhoneFour))
        {
            return true;
        }
        return false;
    }

    public void WriteDbRaws()
    {
        String _translitNameDevice = Translit.RusToLat(_sNameDevice);
        String _translitAddress = Translit.RusToLat(_sLocationAddr);

        realm = Realm.getInstance(getBaseContext());
        realm.beginTransaction();

        Integer iProtocolV = Integer.parseInt(_sProtocolVer);
        DevicesInfoDb deviceInfo = new DevicesInfoDb();
        deviceInfo.set_phoneNumbArduino(_sPhoneArduino);
        deviceInfo.set_nameDevice(_sNameDevice);
        deviceInfo.set_nameDeviceTranslit(_translitNameDevice);
        deviceInfo.set_address(_sLocationAddr);
        deviceInfo.set_addressTranslit(_translitAddress);
        deviceInfo.set_hProtocolVer(iProtocolV);

        for (int i = 0; i < 4; i++)
        {
            AutorizedPhonesDb autorizedPhonesDb = new AutorizedPhonesDb();
            if (TextUtils.isEmpty(sAutorizePhoneArray[i]))
            {
                continue;
            }
            autorizedPhonesDb.set_hProtocolVer(iProtocolV);
            autorizedPhonesDb.set_phoneNumber(sAutorizePhoneArray[i]);
            realm.copyToRealm(autorizedPhonesDb);
        }

        realm.copyToRealm(deviceInfo);
        realm.commitTransaction();
    }

    private void GetTextViews()
    {
        _sPhoneArduino = etPhoneArduino.getText().toString().trim();
        _sNameDevice = etNameDevice.getText().toString().trim();
        _sLocationAddr = etLocationAddr.getText().toString().trim();
        _sProtocolVer = etProtocolVer.getText().toString().trim();
        _sAutorizePhoneOne = etAutorizePhoneOne.getText().toString().trim();
        _sAutorizePhoneTwo = etAutorizePhoneTwo.getText().toString().trim();
        _sAutorizePhoneThree = etAutorizePhoneThree.getText().toString().trim();
        _sAutorizePhoneFour = etAutorizePhoneFour.getText().toString().trim();
        sAutorizePhoneArray[0] = _sAutorizePhoneOne;
        sAutorizePhoneArray[1] = _sAutorizePhoneTwo;
        sAutorizePhoneArray[2] = _sAutorizePhoneThree;
        sAutorizePhoneArray[3] = _sAutorizePhoneFour;
    }
}