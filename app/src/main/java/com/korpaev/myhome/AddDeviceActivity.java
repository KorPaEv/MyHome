package com.korpaev.myhome;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import io.realm.Realm;
import io.realm.RealmResults;

//Класс Добавить новое устройство
public class AddDeviceActivity extends Activity
{
    final int LENPHONENUM = 10;
    final String PHONENUMARDUINO = "_phoneNumbArduino";
    Realm realm;
    Button bCancel, bAddDevice;
    EditText etPhoneArduino, etNameDevice, etLocationAddr, etProtocolVer,
             etAutorizePhoneOne, etAutorizePhoneTwo, etAutorizePhoneThree, etAutorizePhoneFour;

    //Переменные для хранения значений из текст-боксов
    String _sPhoneArduino, _sNameDevice, _sLocationAddr, _sProtocolVer,
           _sAutorizePhoneOne, _sAutorizePhoneTwo, _sAutorizePhoneThree, _sAutorizePhoneFour,
           _sIdDeviceBase64;

    final String[] sAutorizePhoneArray = {_sAutorizePhoneOne, _sAutorizePhoneTwo, _sAutorizePhoneThree, _sAutorizePhoneFour};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        //Ищем вьюхи на экране
        FindViews();
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

    public void CancelButtonClick(View v)
    {
        AddDeviceActivity.this.finish();
    }

    //Добавление нового утсройства
    public void AddDeviceButtonClick(View v)
    {
        //Проверяем заполнены ли все обязательные вьюхи
        if (CheckViews())
        {
            //Пишем в БД если все в порядке
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

    private boolean CheckViews()
    {
        GetTextViews();
        if (TextUtils.isEmpty(_sPhoneArduino))
        {
            etPhoneArduino.setError("Обязательно к заполнению!");
            return false;
        }
        else if (CheckUniquePhoneNum(_sPhoneArduino))
        {
            etPhoneArduino.setError("Такой номер уже используется!");
        }
        else if (TextUtils.isEmpty(_sProtocolVer))
        {
            etProtocolVer.setError("Обязательно к заполнению!");
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

    private boolean CheckUniquePhoneNum(String phone)
    {
        realm = Realm.getInstance(getBaseContext());
        realm.beginTransaction();
        RealmResults<DevicesInfoDb> results = realm.where(DevicesInfoDb.class).equalTo(PHONENUMARDUINO, phone).findAll();
        realm.commitTransaction();
        return (results.size() > 0);
    }

    public void WriteDbRaws()
    {
        Integer iProtocolV;
        //Переводим русский текст в транслит для отправки на ардуинку
        String _translitNameDevice = Translit.RusToLat(_sNameDevice);
        String _translitAddress = Translit.RusToLat(_sLocationAddr);

        realm = Realm.getInstance(getBaseContext());
        realm.beginTransaction();

        DevicesInfoDb deviceInfo = new DevicesInfoDb();

        deviceInfo.set_idDevice(_sIdDeviceBase64);
        iProtocolV = Integer.parseInt(_sProtocolVer);
        deviceInfo.set_hProtocolVer(iProtocolV);
        deviceInfo.set_phoneNumbArduino(_sPhoneArduino);
        deviceInfo.set_nameDevice(_sNameDevice);
        deviceInfo.set_nameDeviceTranslit(_translitNameDevice);
        deviceInfo.set_address(_sLocationAddr);
        deviceInfo.set_addressTranslit(_translitAddress);

        for (int i = 0; i < 4; i++)
        {
            if (TextUtils.isEmpty(sAutorizePhoneArray[i]))
            {
                continue;
            }
            AutorizedPhonesDb autorizedPhonesDb = new AutorizedPhonesDb();
            autorizedPhonesDb.set_idDevice(_sIdDeviceBase64);
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
        _sIdDeviceBase64 = CreateIdDevice(_sPhoneArduino, _sProtocolVer);
    }

    //Пишем ид устройства - телефон устройства + версия протокола в текстовом формате в base64 без кода страны
    private String CreateIdDevice(String phone, String protocol)
    {
        String res;
        byte[] base64DataArr = {0};
        int lenPhone = phone.length();
        res = phone.substring(lenPhone - LENPHONENUM, lenPhone);//убираем код страны - берем последние 10 цифр - наш момер
        res += protocol;//добавляем к номеру версию протокола
        try
        {
            base64DataArr = res.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        res = Base64.encodeToString(base64DataArr, Base64.NO_WRAP); //Получаем base64 строку
        return res.trim();
    }
}