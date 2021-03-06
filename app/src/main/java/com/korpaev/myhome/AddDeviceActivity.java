package com.korpaev.myhome;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
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
import io.realm.RealmList;
import io.realm.RealmResults;

//Класс Добавить новое устройство
public class AddDeviceActivity extends Activity
{
    //region КОНСТАНТЫ
    private static int LENPHONENUM = 10; //длина номера телефона без кода страны или 8ки
    private static int LENPHONENUMFULL = 12; //длина номера телефона
    private final int COUNTAUTORIZED = 4; //количество разрешенных номеров
    private final String PHONENUMARDUINO = "_phoneNumbArduino"; //Имя поля БД
    private final String IDFIELDNAME = "_idDevice"; //Имя поля БД
    private final String ETHINTTEXT = "Введите значение";
    private final String NAMESHAREDPREF= "IdDevicePref";
    //endregion

    //region ОБЪЯВЛЯЕМ ОБЪЕКТЫ
    Realm realm;
    SharedPreferences sharedPref;
    Button bCancel, bAddDevice;
    ToggleButton rootToggleB;
    ImageButton getDeviceInfoButton;
    EditText etPhoneArduino, etNameDevice, etLocationAddr, etProtocolVer;
    View tvSectionAutorizedNumsHeader, vSectionAutorizedNums;
    //endregion

    //region МАССИВЫ ID ВЬЮХ АКТИВИТИ
    final int[] rIdAutorizedNumArr = {R.id.eAutorizePhoneOne, R.id.eAutorizePhoneTwo, R.id.eAutorizePhoneThree, R.id.eAutorizePhoneFour};
    final int[] rIdChbSendSmsArr = {R.id.sendSmsChBOne, R.id.sendSmsChBTwo, R.id.sendSmsChBThree, R.id.sendSmsChBFour};
    final int[] rIdChbSendCallArr = {R.id.sendCallChBOne, R.id.sendCallChBTwo, R.id.sendCallChBThree, R.id.sendCallChBFour};
    final int[] rIdChbIsAdmNumbArr = {R.id.isAdminChBOne, R.id.isAdminChBTwo, R.id.isAdminChBThree, R.id.isAdminChBFour};
    final int[] rIdTextViewArr = {R.id.autorizeNumTvOne, R.id.autorizeNumTvTwo, R.id.autorizeNumTvThree, R.id.autorizeNumTvFour};
    //endregion

    //region МАССИВЫ ВЬЮХ АКТИВИТИ
    final EditText[] etAutorizedPhoneArr = new EditText[COUNTAUTORIZED];
    final TextView[] tvAutorizedPhoneArr = new TextView[COUNTAUTORIZED];
    final CheckBox[] chbSendSmsArray = new CheckBox[COUNTAUTORIZED];
    final CheckBox[] chbSendCallArray = new CheckBox[COUNTAUTORIZED];
    final CheckBox[] chbIsAdminNumArray = new CheckBox[COUNTAUTORIZED];
    //endregion

    //region Переменные и массивы для хранения значений вьюх
    private String _sPhoneArduino, _sNameDevice, _sLocationAddr, _sProtocolVer,
                   _sIdDeviceBase64,
                   _sBundleIdDevice; //Это ИД девайса который может прийти с другого активити - если редактируем то его надо подменять
    final String[] sAutorizePhoneArray = new String[COUNTAUTORIZED];
    final boolean[] bSendSmsRightArr = new boolean[COUNTAUTORIZED];
    final boolean[] bSendCallRightArr = new boolean[COUNTAUTORIZED];
    final boolean[] bIsAdmNumArr = new boolean[COUNTAUTORIZED];
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        FindViews(); //Ищем вьюхи на экране и заполняем массивы объектами

        //InfoButtonCheckEnable(); //Проверка состояния кнопки для получения инфы о девайсе
        //Событие на edittext номера ардуины - если там не пусто то кнопка инфы активна
        //etPhoneArduino.addTextChangedListener(textWatcher);
        getDeviceInfoButton.setEnabled(false); //Пока что не реализован толком функционал этой кнопы, ее скрываем
        getDeviceInfoButton.setVisibility(View.INVISIBLE);

        SetVisibleViews(false); //По умолчанию режим рута выключается - прячем ненужные вьюхи

        //region Обработчик нажатия на кнопку получения данных и сохранения номера - меняем картинку при нажатии
        getDeviceInfoButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        getDeviceInfoButton.setImageResource(R.mipmap.ic_bupd_1);
                        //GetDeviceInfoButtonClick(v);
                        return true;
                    case MotionEvent.ACTION_UP:
                        getDeviceInfoButton.setImageResource(R.mipmap.ic_bupd);
                        break;
                }
                // tell the system that we handled the event but a further processing is required
                return false;
            }
        });
        //endregion
        //region Оработчик раскрытия списка разрешенных номеров
        tvSectionAutorizedNumsHeader.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (vSectionAutorizedNums.getVisibility() == View.GONE)
                {
                    tvSectionAutorizedNumsHeader.setBackgroundColor(0xC73B3A48);
                    vSectionAutorizedNums.setVisibility(View.VISIBLE);
                }
                else
                {
                    tvSectionAutorizedNumsHeader.setBackgroundColor(0xC7060543);
                    vSectionAutorizedNums.setVisibility(View.GONE);
                }
            }
        });
        //endregion

        FillData(); //Заполняем вьюхи данными - если пришли данные на редактирование текущего устройства вьюхи заполнятся
    }

    //region FindViews() Поиск вьюх и заполнение массивов дл этих вьюх
    private void FindViews()
    {
        tvSectionAutorizedNumsHeader = findViewById(R.id.tvSectionAutorizedNums);
        vSectionAutorizedNums = findViewById(R.id.sectionAutorizedNums);
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
    //endregion

    //region InfoButtonCheckEnable() Проверка состояния кнопки получения инфы о девайсе
//    private void InfoButtonCheckEnable()
//    {
//        if (TextUtils.isEmpty(etPhoneArduino.getText()))
//        {
//            getDeviceInfoButton.setImageResource(R.mipmap.ic_bupd_disable);
//            getDeviceInfoButton.setEnabled(false);
//        }
//        else
//        {
//            getDeviceInfoButton.setImageResource(R.mipmap.ic_bupd);
//            getDeviceInfoButton.setEnabled(true);
//        }
//    }
//
//    //Событие на edittext
//    TextWatcher textWatcher = new TextWatcher()
//    {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after)
//        {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count)
//        {
//            InfoButtonCheckEnable();
//        }
//
//        @Override
//        public void afterTextChanged(Editable s)
//        {
//
//        }
//    };
    //endregion

    //region FillData() Заполняем вьюхи данными
    private void FillData()
    {
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            _sBundleIdDevice = extras.getString(IDFIELDNAME);
            if (_sBundleIdDevice != null)
            {
                FillViews(_sBundleIdDevice);
            }
        }
    }

    private void FillViews(String idRow)
    {
        realm = Realm.getInstance(getBaseContext());
        RealmResults<DevicesInfoDb> results = realm.where(DevicesInfoDb.class).equalTo(IDFIELDNAME, idRow).findAll();
        RealmList<AutorizedPhonesDb> autorizedPhonesDbs = new RealmList<>();
        for (int i = 0; i < results.size(); i++)
        {
            _sPhoneArduino = results.get(i).get_phoneNumbArduino();
            _sNameDevice = results.get(i).get_nameDevice();
            _sLocationAddr = results.get(i).get_address();
            _sProtocolVer = String.valueOf(results.get(i).get_hProtocolVer());

            etPhoneArduino.setText(_sPhoneArduino);
            etNameDevice.setText(_sNameDevice);
            etLocationAddr.setText(_sLocationAddr);
            etProtocolVer.setText(_sProtocolVer);

            autorizedPhonesDbs = results.get(i).get_autorizedPhoneNumRaws();
            for (int j = 0; j < autorizedPhonesDbs.size(); j++)
            {
                sAutorizePhoneArray[j] = autorizedPhonesDbs.get(j).get_phoneNumber();
                bSendSmsRightArr[j] = autorizedPhonesDbs.get(j).get_sendSmsRights();
                bSendCallRightArr[j] = autorizedPhonesDbs.get(j).get_callRights();
                bIsAdmNumArr[j] = autorizedPhonesDbs.get(j).get_isAdmNumb();

                etAutorizedPhoneArr[j].setText(sAutorizePhoneArray[j]);
                chbSendSmsArray[j].setChecked(bSendSmsRightArr[j]);
                chbSendCallArray[j].setChecked(bSendCallRightArr[j]);
                chbIsAdminNumArray[j].setChecked(bIsAdmNumArr[j]);
            }
        }
    }
    //endregion

    //region SetVisibleViews(TRUE or FALSE) Установка видимости вьюх
    private void SetVisibleViews(boolean isVisible)
    {
        bAddDevice.setVisibility(View.VISIBLE);
        etNameDevice.setEnabled(true);
        etNameDevice.setHint(ETHINTTEXT);
        etLocationAddr.setEnabled(true);
        etLocationAddr.setHint(ETHINTTEXT);
        etProtocolVer.setEnabled(true);
        etProtocolVer.setHint(ETHINTTEXT);
        if (!isVisible)
        {
            //bAddDevice.setVisibility(View.INVISIBLE);
            //etNameDevice.setEnabled(false);
            //etNameDevice.setHint(null);
            //etLocationAddr.setEnabled(false);
            //etLocationAddr.setHint(null);
            //etProtocolVer.setEnabled(false);
            //etProtocolVer.setHint(null);
            for (int i = 0; i < COUNTAUTORIZED; i++)
            {
//                tvAutorizedPhoneArr[i].setVisibility(View.INVISIBLE);
//                etAutorizedPhoneArr[i].setVisibility(View.INVISIBLE);
//                chbSendSmsArray[i].setVisibility(View.INVISIBLE);
//                chbSendCallArray[i].setVisibility(View.INVISIBLE);
                chbIsAdminNumArray[i].setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            //bAddDevice.setVisibility(View.VISIBLE);
            //etNameDevice.setEnabled(true);
            //etNameDevice.setHint(ETHINTTEXT);
            //etLocationAddr.setEnabled(true);
            //etLocationAddr.setHint(ETHINTTEXT);
            //etProtocolVer.setEnabled(true);
            //etProtocolVer.setHint(ETHINTTEXT);
            for (int i = 0; i < COUNTAUTORIZED; i++)
            {
//                tvAutorizedPhoneArr[i].setVisibility(View.VISIBLE);
//                etAutorizedPhoneArr[i].setVisibility(View.VISIBLE);
//                chbSendSmsArray[i].setVisibility(View.VISIBLE);
//                chbSendCallArray[i].setVisibility(View.VISIBLE);
                chbIsAdminNumArray[i].setVisibility(View.VISIBLE);
            }
        }
    }
    //endregion

    //region Обработчик нажатия на кнопку RootToggleButton
    public void RootToggleButtonClick(View v)
    {
        if (rootToggleB.isChecked())
        {
            SetVisibleViews(true);
        }
        else SetVisibleViews(false);
    }
    //endregion

    //region Кнопа отмены действий CancelButton
    public void CancelButtonClick(View v)
    {
        this.finish();
    }
    //endregion

    //region Кнопка Сохранить addDeviceButton
    public void SaveDeviceButtonClick(View v)
    {
        GetTextViews(); //Получаем данные вьюх
        if (CheckViews() && GenIdAndCheckUniqNum()) //Проверяем заполнены ли все обязательные вьюхи
        {
            if (rootToggleB.isChecked())
            {
                // ADDNUM;0;+79132188583;1;1;1
                // ADDNUM;1;+79132188583;1;1;1
                // ADDNUM;2;+79132188583;1;1;1
                // ADDNUM;3;+79132188583;1;1;1
                // ADDINF;1;Ard001;22, Dimitrova 81 - 59
                if (CheckAutorizedNums())
                {
                    CreateAndSendSmsStr();
                }
            }
            WriteDbRaws(); //Пишем в БД если все в порядке
            SaveSharedPref();
            Intent intent;
            intent = new Intent(getBaseContext(), MainActivityTabs.class);
            startActivity(intent);
        }
    }

    private boolean CheckAutorizedNums()
    {
        for (int i = 0; i < sAutorizePhoneArray[i].length(); i++)
        {
            if (!TextUtils.isEmpty(sAutorizePhoneArray[i]) && sAutorizePhoneArray[i].length() != LENPHONENUMFULL)
            {
                etAutorizedPhoneArr[i].setError("Введите корректный формат (+7xxxxxxxxxx)");
                return false;
            }
        }
        return true;
    }

    private void CreateAndSendSmsStr()
    {
        String res = ""; //Результирующая строка или остаток того что не влезло в упаковку

        String strInf; //Строка инфы об устройстве
        int lenStrInf; //длинна строки инфы

        String strAddNum; //Строка разрешенного номера
        int lenStrAddNum; //длинна строки разрешенного номера

        String strTmpAddN = "";
        int lenStrTmpAddNum = 0;
        int shiftLen = 0;

        //Собираем сначала инфу об устройстве
        strInf = "ADDINF;" +
                _sProtocolVer + ";" +
                Translit.RusToLat(_sNameDevice) + ";" +
                Translit.RusToLat(_sLocationAddr);
        lenStrInf = strInf.length();

        for (int i = 0; i < sAutorizePhoneArray[i].length(); i++)
        {
            if (TextUtils.isEmpty(sAutorizePhoneArray[i]))
            {
                continue;
            }

            //Собираем строку разрешенного номера
            strAddNum = "ADDNUM;" +
                         String.valueOf(i) + ";" +
                         sAutorizePhoneArray[i] + ";" +
                         BoolToIntStr(bSendSmsRightArr[i]) + ";" +
                         BoolToIntStr(bSendCallRightArr[i]) + ";" +
                         BoolToIntStr(bIsAdmNumArr[i]) + "\n";
            lenStrAddNum = strAddNum.length();

            //Смотрим если длинна инфы + длинна разрешенного не влазиют в одну пачку то отправляем сначала просто инфу
            //Но строку с разрешенным номером сохраняем в остаток
            //Далее собираем пачку смс по разрешенным номерам аналогичным образон, но строка инфы уже не участвует ее обнуляем
            if (lenStrAddNum + lenStrTmpAddNum + shiftLen + lenStrInf >= 140)
            {
                if (res == "")
                {
                    res = strInf;
                }
                SendSms(_sPhoneArduino, res);
                lenStrInf = 0;
                strInf = "";
                strTmpAddN = strAddNum; //сохраняем остаток, потом дописываем этот остаток
                lenStrTmpAddNum = strTmpAddN.length(); //вычислили длину тела временной строки
                shiftLen = 0; // обнуляем курсор на который сдвигать для записи следующей строки по датчику
                res = "";
            }
            else
            {
                shiftLen += lenStrTmpAddNum + lenStrAddNum;
                res += strTmpAddN + strAddNum + strInf;
                strInf = "";
                strTmpAddN = "";
            }
        }
        if (res != "")
        {
            SendSms(_sPhoneArduino, res);
        }
    }

    public static String BoolToIntStr(boolean bVal)
    {
        return bVal ? "1" : "0";
    }

    private boolean GenIdAndCheckUniqNum()
    {
        //Генерируем ИД
        //Если мы редактируем текущую запись то ИД берем тот который редактируем, иначе генерируем новый
        if (_sBundleIdDevice != null)
        {
            _sIdDeviceBase64 = _sBundleIdDevice;
        }
        else _sIdDeviceBase64 = CreateIdDevice(_sPhoneArduino, _sProtocolVer);

        //Уникальность проверяем именно когда есть
        if (CheckUniquePhoneNum(_sPhoneArduino))
        {
            etPhoneArduino.setError("Такой номер уже используется!");
            return false;
        }
        return true;
    }
    //endregion

    //region CheckViews() Проверка валидности введенных данных вьюх
    private boolean CheckViews()
    {
        if (TextUtils.isEmpty(_sPhoneArduino))
        {
            etPhoneArduino.setError("Обязательно к заполнению!");
            return false;
        }
        else if (_sPhoneArduino.length() != LENPHONENUMFULL)
        {
            etPhoneArduino.setError("Введите корректный формат (+7xxxxxxxxxx)");
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
        else if (TextUtils.isEmpty(_sProtocolVer))
        {
            etProtocolVer.setError("Обязательно к заполнению!");
            return false;
        }
        return true;
    }
    //endregion

    //region SaveSharedPref() Сохраняем ID текущего устройства в SharedPref
    private void SaveSharedPref()
    {
        Toast.makeText(this, "Сохраняем...", Toast.LENGTH_SHORT).show();
        //Создаем объект Editor для создания пар имя-значение:
        sharedPref = getSharedPreferences(NAMESHAREDPREF, MODE_PRIVATE);
        //Создаем объект Editor для создания пар имя-значение:
        SharedPreferences.Editor shpEditor = sharedPref.edit();
        shpEditor.putString(IDFIELDNAME, _sIdDeviceBase64);
        shpEditor.commit();
    }
    //endregion

    //region GetTextViews() Получаем значения введенные во вьюхи
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
    }
    //endregion

    //region CheckUniquePhoneNum(String phone) Проверка что такой номер устройства уже есть в БД
    private boolean CheckUniquePhoneNum(String phone)
    {
        realm = Realm.getInstance(getBaseContext());
        realm.beginTransaction();
        RealmResults<DevicesInfoDb> results = realm.where(DevicesInfoDb.class)
                .equalTo(PHONENUMARDUINO, phone)
                .notEqualTo(IDFIELDNAME, _sIdDeviceBase64)
                .findAll();
        realm.commitTransaction();
        return (results.size() > 0);
    }
    //endregion

    //region WriteDbRaws() Пишем в БД если все в порядке
    public void WriteDbRaws()
    {
        Integer iProtocolV;
        //Переводим русский текст в транслит для отправки на ардуинку
        String _translitNameDevice = Translit.RusToLat(_sNameDevice);
        String _translitAddress = Translit.RusToLat(_sLocationAddr);

        realm = Realm.getInstance(getBaseContext());
        realm.beginTransaction();

        //Получаем наш объект если он уже создан и хотим редактировать
        DevicesInfoDb deviceInfo = new DevicesInfoDb();
        RealmResults<DevicesInfoDb> devicesInfoDbs = realm.where(DevicesInfoDb.class).equalTo(IDFIELDNAME, _sIdDeviceBase64).findAll();
        if (devicesInfoDbs.size() > 0)
        {
            for (int k = 0; k < devicesInfoDbs.size(); k++)
            {
                deviceInfo = devicesInfoDbs.get(k); //Объект таблички Инфы об устройстве
            }
        }

        //удаляем по нашему ИД все записи из разрешенных номеров - один фиг заново создаем этот лист
        RealmResults<AutorizedPhonesDb> autorizedPhonesDbs = realm.where(AutorizedPhonesDb.class).equalTo(IDFIELDNAME, _sIdDeviceBase64).findAll();
        autorizedPhonesDbs.clear();
        //объявляем новый лист с разрешенными номерами который мы добавим в существующий или новый объект
        RealmList<AutorizedPhonesDb> listAutorizedPhones = new RealmList<>(); //Дочерняя табличка номеров разрешенных

        //Заполняем или переписываем наш объект
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
            listAutorizedPhones.add(autorizedPhonesDb);
        }

        if (deviceInfo.get_autorizedPhoneNumRaws() != null)
        {
            //дописываем измененный или старый лист с номерами
            for (int j = 0; j < listAutorizedPhones.size(); j++)
            {
                deviceInfo.get_autorizedPhoneNumRaws().add(listAutorizedPhones.get(j));
            }
        }
        else deviceInfo.set_autorizedPhoneNumRaws(listAutorizedPhones);
        realm.copyToRealm(deviceInfo);
        realm.commitTransaction();
    }
    //endregion

    //region CreateIdDevice(phone, protocol) ID устройства - телефон устройства + версия протокола в текстовом формате в base64 без кода страны
    public static String CreateIdDevice(String phone, String protocol)
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
    //endregion

    //region SendSms Функция отправки смс
    public void SendSms(String number, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, null, null);
    }
    //endregion

    //region ЭТУ КНОПКУ ЗАПРОГРАММИРОВАТЬ В БУДУЩЕМ - ПОЛУЧЕНИЕ ИНФУ ОТ АРДУИНЫ!!! СЕЙЧАС ЗАРЕМАРИНА В КОНСТРУКТОРЕ
    private void GetDeviceInfoButtonClick(View v)
    {
        //Смотри SmsUtils строка 50 - надо получать с ардуины в формате так же своем!!!
        //Так же допиливать SmsUserDataPdu и MessageService
        MessageService ms = new MessageService();
        //String str = "SH1;1460473840;1;8;RN;0C;RN;SH1;1460473840;2;8;RN;0C;RN;SH1;1460473840;3;8;RN;0C;RN;SH1;1460473840;4;8;RN;0C;RN;SH1;1460473840;5;8;RN;0C;RN;";
        String str = "INFSH;1;Dom;Dimitrova;+79998887766;1;1;1;+79998886655;1;1;1;";
        ms.WriteDataToDB(str, getBaseContext());
    }
    //endregion
}