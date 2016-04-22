package com.korpaev.myhome;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.UnsupportedEncodingException;

import io.realm.Realm;
import io.realm.RealmResults;

//Класс Добавить новое устройство
public class AddDeviceActivity extends Activity
{
    Realm realm;
    Button bCancel, bAddDevice;
    ToggleButton rootToggleB;
    ImageButton getDeviceInfoButton;

    final int LENPHONENUM = 10; //длина номера телефона без кода страны или 8ки
    final int COUNTAUTORIZED = 4; //количество разрешенных номеров
    final String PHONENUMARDUINO = "_phoneNumbArduino"; //Имя поля БД
    final String ETHINTTEXT = "Введите значение";

    EditText etPhoneArduino, etNameDevice, etLocationAddr, etProtocolVer;

    final int[] rIdAutorizedNumArr = {R.id.eAutorizePhoneOne, R.id.eAutorizePhoneTwo, R.id.eAutorizePhoneThree, R.id.eAutorizePhoneFour};
    final int[] rIdChbSendSmsArr = {R.id.sendSmsChBOne, R.id.sendSmsChBTwo, R.id.sendSmsChBThree, R.id.sendSmsChBFour};
    final int[] rIdChbSendCallArr = {R.id.sendCallChBOne, R.id.sendCallChBTwo, R.id.sendCallChBThree, R.id.sendCallChBFour};
    final int[] rIdChbIsAdmNumbArr = {R.id.isAdminChBOne, R.id.isAdminChBTwo, R.id.isAdminChBThree, R.id.isAdminChBFour};
    final int[] rIdTextViewArr = {R.id.autorizeNumTvOne, R.id.autorizeNumTvTwo, R.id.autorizeNumTvThree, R.id.autorizeNumTvFour};

    final EditText[] etAutorizedPhoneArr = new EditText[COUNTAUTORIZED];
    final TextView[] tvAutorizedPhoneArr = new TextView[COUNTAUTORIZED];
    final CheckBox[] chbSendSmsArray = new CheckBox[COUNTAUTORIZED];
    final CheckBox[] chbSendCallArray = new CheckBox[COUNTAUTORIZED];
    final CheckBox[] chbIsAdminNumArray = new CheckBox[COUNTAUTORIZED];

    //Переменные для хранения значений вьюх
    String _sPhoneArduino, _sNameDevice, _sLocationAddr, _sProtocolVer,
           _sIdDeviceBase64;
    final String[] sAutorizePhoneArray = new String[COUNTAUTORIZED];
    final boolean[] bSendSmsRightArr = new boolean[COUNTAUTORIZED];
    final boolean[] bSendCallRightArr = new boolean[COUNTAUTORIZED];
    final boolean[] bIsAdmNumArr = new boolean[COUNTAUTORIZED];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        //Ищем вьюхи на экране
        FindViews();
        InfoButtonCheckEnable();
        SetVisibleViews(false);

        etPhoneArduino.addTextChangedListener(textWatcher);

        //region Обработчик нажатия на кнопку получения данных и сохранения номера - меняем картинку при нажатии
        getDeviceInfoButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        getDeviceInfoButton.setImageResource(R.mipmap.ic_bupd_1);
                        //HeadButtonsClick(v);
                        return true;

                    case MotionEvent.ACTION_UP:
                        getDeviceInfoButton.setImageResource(R.mipmap.ic_bupd);
                        break;
                }
                // tell the system that we handled the event but a further processing is required
                return false;
            }
        });

    }

    TextWatcher textWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            InfoButtonCheckEnable();
        }

        @Override
        public void afterTextChanged(Editable s)
        {

        }
    };

    private void InfoButtonCheckEnable()
    {
        if (TextUtils.isEmpty(etPhoneArduino.getText()))
        {
            getDeviceInfoButton.setImageResource(R.mipmap.ic_bupd_disable);
            getDeviceInfoButton.setEnabled(false);
        }
        else
        {
            getDeviceInfoButton.setImageResource(R.mipmap.ic_bupd);
            getDeviceInfoButton.setEnabled(true);
        }
    }

    private void FindViews()
    {
        getDeviceInfoButton = (ImageButton)findViewById(R.id.getDeviceInfoImButton);
        rootToggleB = (ToggleButton)findViewById(R.id.rootRightsToggleB);
        bAddDevice = (Button)findViewById(R.id.addDeviceButton);
        bCancel = (Button)findViewById(R.id.cancelButton);
        etPhoneArduino = (EditText)findViewById(R.id.ePhoneNumber);
        etNameDevice = (EditText)findViewById(R.id.eNameDevice);
        etLocationAddr = (EditText)findViewById(R.id.eAddressDevice);
        etProtocolVer = (EditText)findViewById(R.id.eProtocolVerDevice);
        for (int i = 0; i < COUNTAUTORIZED; i++)
        {
            etAutorizedPhoneArr[i] = (EditText)findViewById(rIdAutorizedNumArr[i]);
            chbSendSmsArray[i] = (CheckBox)findViewById(rIdChbSendSmsArr[i]);
            chbSendCallArray[i] = (CheckBox)findViewById(rIdChbSendCallArr[i]);
            chbIsAdminNumArray[i] = (CheckBox)findViewById(rIdChbIsAdmNumbArr[i]);
            tvAutorizedPhoneArr[i] = (TextView)findViewById(rIdTextViewArr[i]);
        }
    }

    private void SetVisibleViews(boolean isVisible)
    {
        if (!isVisible)
        {
            bAddDevice.setVisibility(View.INVISIBLE);
            etNameDevice.setEnabled(false);
            etNameDevice.setHint(null);
            etLocationAddr.setEnabled(false);
            etLocationAddr.setHint(null);
            etProtocolVer.setEnabled(false);
            etProtocolVer.setHint(null);
            for (int i = 0; i < COUNTAUTORIZED; i++)
            {
                etAutorizedPhoneArr[i].setVisibility(View.INVISIBLE);
                chbSendSmsArray[i].setVisibility(View.INVISIBLE);
                chbSendCallArray[i].setVisibility(View.INVISIBLE);
                chbIsAdminNumArray[i].setVisibility(View.INVISIBLE);
                tvAutorizedPhoneArr[i].setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            bAddDevice.setVisibility(View.VISIBLE);
            etNameDevice.setEnabled(true);
            etNameDevice.setHint(ETHINTTEXT);
            etLocationAddr.setEnabled(true);
            etLocationAddr.setHint(ETHINTTEXT);
            etProtocolVer.setEnabled(true);
            etProtocolVer.setHint(ETHINTTEXT);
            for (int i = 0; i < COUNTAUTORIZED; i++)
            {
                etAutorizedPhoneArr[i].setVisibility(View.VISIBLE);
                chbSendSmsArray[i].setVisibility(View.VISIBLE);
                chbSendCallArray[i].setVisibility(View.VISIBLE);
                chbIsAdminNumArray[i].setVisibility(View.VISIBLE);
                tvAutorizedPhoneArr[i].setVisibility(View.VISIBLE);
            }
        }
    }

    public void RootToggleButtonClick(View v)
    {
        if (rootToggleB.isChecked())
        {
            SetVisibleViews(true);
        }
        else SetVisibleViews(false);
    }

    public void CancelButtonClick(View v)
    {
        AddDeviceActivity.this.finish();
    }

    //Добавление нового утсройства
    public void SaveDeviceButtonClick(View v)
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
        else IsNotEmptyAutorizedNumArray();
        return false;
    }

    private boolean IsNotEmptyAutorizedNumArray()
    {
        for (int i = 0; i < COUNTAUTORIZED; i++)
        {
            if (!TextUtils.isEmpty(sAutorizePhoneArray[i]))
            {
                return true;
            }
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

        for (int i = 0; i < COUNTAUTORIZED; i++)
        {
            if (TextUtils.isEmpty(sAutorizePhoneArray[i]))
            {
                continue;
            }
            AutorizedPhonesDb autorizedPhonesDb = new AutorizedPhonesDb();
            autorizedPhonesDb.set_idDevice(_sIdDeviceBase64);
            autorizedPhonesDb.set_phoneNumber(sAutorizePhoneArray[i]);
            autorizedPhonesDb.set_sendSmsRights(bSendSmsRightArr[i]);
            autorizedPhonesDb.set_callRights(bSendCallRightArr[i]);
            autorizedPhonesDb.set_isAdmNumb(bIsAdmNumArr[i]);
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
        for (int i = 0; i < COUNTAUTORIZED; i++)
        {
            sAutorizePhoneArray[i] = etAutorizedPhoneArr[i].getText().toString().trim();
            bSendSmsRightArr[i] = chbSendSmsArray[i].isChecked();
            bSendCallRightArr[i] = chbSendCallArray[i].isChecked();
            bIsAdmNumArr[i] = chbIsAdminNumArray[i].isChecked();
        }
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