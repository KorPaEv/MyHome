package com.korpaev.myhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RelayManageActivity extends Activity
{
    //region КОНСТАНТЫ
    private final String EMPTYDATA = "Не задано";
    private final String NAMESHAREDPREF = "IdDevicePref";
    private final String IDFIELDNAME = "_idDevice"; //Имя поля БД
    private final String TIMESTAMPFIELDNAME = "_hTimeStamp";
    private final String SENSORSINFOTABLENAME = "_stateSystemRaws";
    private final int COUNTRELAYS = 4;
    //endregion
    //region ОБЪЯВЛЯЕМ ОБЪЕКТЫ И КНОПКИ
    Realm realm;
    SharedPreferences sharedPref;
    View view;
    //endregion
    //region МАССИВЫ ID ВЬЮХ АКТИВИТИ
    final int[] rIdRelaysNameArr = {R.id.tvRelayManageNameOne,
                                    R.id.tvRelayManageNameTwo,
                                    R.id.tvRelayManageNameThree,
                                    R.id.tvRelayManageNameFour};
    final int[] rIdToggleButtonArr = {R.id.tgbRelayManageOne,
                                      R.id.tgbRelayManageTwo,
                                      R.id.tgbRelayManageThree,
                                      R.id.tgbRelayManageFour};
    final int[] rIdRelaysAutoModeArr = {R.id.chbRelayManageAutoOne,
                                        R.id.chbRelayManageAutoTwo,
                                        R.id.chbRelayManageAutoThree,
                                        R.id.chbRelayManageAutoFour};
    //endregion
    //region МАССИВЫ ВЬЮХ АКТИВИТИ
    final TextView[] tvRelaysNameArr = new TextView[COUNTRELAYS];
    final ToggleButton[] tgbRelaysStateArr = new ToggleButton[COUNTRELAYS];
    final CheckBox[] chbRelaysAutoModeArr = new CheckBox[COUNTRELAYS];
    //endregion
    //region Переменные и массивы для хранения значений вьюх
    private String _sIdDevice; //Это ИД девайса который может прийти с другого активити
    private String _ardPhoneNumb; //Номер ардуины
    final String[] sRelayNameArr = new String[COUNTRELAYS];
    final Boolean[] bRelaysStateArr = new Boolean[COUNTRELAYS];
    final Boolean[] bRelaysAutoModeArr = new Boolean[COUNTRELAYS];
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relay_manage);
        FindViews();
        FillData();
    }

    //region FindViews() Поиск всех объектов на экране
    protected void FindViews()
    {
        for (int j = 0; j < COUNTRELAYS; j++)
        {
            tvRelaysNameArr[j] = (TextView)findViewById(rIdRelaysNameArr[j]);
            tgbRelaysStateArr[j] = (ToggleButton)findViewById(rIdToggleButtonArr[j]);
            chbRelaysAutoModeArr[j] = (CheckBox)findViewById(rIdRelaysAutoModeArr[j]);
        }
    }
    //endregion

    //region FillData() Заполняем вьюхи данными
    private void FillData()
    {
        //Используем созданный файл данных SharedPreferences:
        sharedPref = getSharedPreferences(NAMESHAREDPREF, MODE_PRIVATE);
        _sIdDevice = sharedPref.getString(IDFIELDNAME, null);
        SetDefaultValDeviceInfo();
        if (!TextUtils.isEmpty(_sIdDevice))
        {
            FillViews(_sIdDevice);
        }
    }

    private void FillViews(String idRow)
    {
        realm = Realm.getInstance(getBaseContext());
        GetArdPhoneNumb();

        //СЧИТЫВАЕМ ВСЕ РЕНЭЙМЫ ПО РЕЛЮХАМ
        //Причем пока не сконфигурировано ардуино или не получена инфа по смс, этот список будет пуст
        //таблица заполняется только в 2 случаях - инфа по смс или конфигурирование из приложения ардуины
        RealmResults<RelayRenamesDb> relayRenamesDbs = realm.where(RelayRenamesDb.class).equalTo(IDFIELDNAME, idRow).findAll();
        for (int i = 0; i < relayRenamesDbs.size(); i++)
        {
            //получаем номер реле
            int numRelay = relayRenamesDbs.get(i).get_numRelay();
            numRelay -= 1;
            if (!TextUtils.isEmpty(relayRenamesDbs.get(numRelay).get_bLocationRelayRus()))
            {
                sRelayNameArr[numRelay] = relayRenamesDbs.get(numRelay).get_bLocationRelayRus();
            }
            else if (!TextUtils.isEmpty(relayRenamesDbs.get(numRelay).get_bLocationRelay()))
            {
                sRelayNameArr[numRelay] = relayRenamesDbs.get(numRelay).get_bLocationRelay();
            }
            else sRelayNameArr[numRelay] = EMPTYDATA;
            tvRelaysNameArr[numRelay].setText(sRelayNameArr[numRelay]);

            bRelaysStateArr[numRelay] = relayRenamesDbs.get(numRelay).get_bStateRelay();
            tgbRelaysStateArr[numRelay].setChecked(bRelaysStateArr[numRelay]);

            bRelaysAutoModeArr[numRelay] = relayRenamesDbs.get(numRelay).get_isAutomaticModeR();
            chbRelaysAutoModeArr[numRelay].setChecked(bRelaysAutoModeArr[numRelay]);
        }

        //ЗАПОЛНЯЕМ ВЬЮХИ ДАННЫМИ ПО ПРИВЯЗАННЫМ РЕЛЮХАМ
        SetRelaysState();

    }

    private void SetRelaysState()
    {
        RealmResults<DevicesInfoDb> devicesInfoDbs;
        RealmResults<SensorsInfoDb> sensorsInfoDbs = realm.where(SensorsInfoDb.class).equalTo(IDFIELDNAME, _sIdDevice).findAll();
        RealmList<SensorsInfoDb> sensorsInfoList;

        if (sensorsInfoDbs.size() > 0)
        {
            //Получаем максимальное время
            int maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

            //Получаем только свежие записи по максимальному времени
            devicesInfoDbs = realm.where(DevicesInfoDb.class)
                    .equalTo(IDFIELDNAME, _sIdDevice)
                    .equalTo(SENSORSINFOTABLENAME + "." + TIMESTAMPFIELDNAME, maxTimeStamp)
                    .findAll();

            for (int i = 0; i < devicesInfoDbs.size(); i++)
            {
                _ardPhoneNumb = devicesInfoDbs.get(i).get_phoneNumbArduino();
                sensorsInfoList = devicesInfoDbs.get(i).get_stateSystemRaws();

                //получили номер датчика
                int numSensor = sensorsInfoList.get(i).get_hNumSensor();
                // -1 потому что массивы с нуля а данные по номеру датчика начинаются с 1
                numSensor -= 1;

                //смотрим если есть привязка реле к датчику
                if (!TextUtils.isEmpty(sensorsInfoList.get(numSensor).get_bNumRelay()))
                {
                    //то получаем номер реле которое привязано
                    int numRelay = Integer.parseInt(sensorsInfoList.get(numSensor).get_bNumRelay());
                    numRelay -= 1;

                    if (!TextUtils.isEmpty(sensorsInfoList.get(numRelay).get_bLocationRelayRus()))
                    {
                        sRelayNameArr[numRelay] = sensorsInfoList.get(numRelay).get_bLocationRelayRus();
                    }
                    else sRelayNameArr[numRelay] = sensorsInfoList.get(numRelay).get_bLocationRelay();
                    tvRelaysNameArr[numRelay].setText(sRelayNameArr[numRelay]);

                    bRelaysStateArr[numRelay] = sensorsInfoList.get(numSensor).get_bStateRelay();
                    tgbRelaysStateArr[numRelay].setChecked(bRelaysStateArr[numRelay]);

                    bRelaysAutoModeArr[numRelay] = sensorsInfoList.get(numSensor).get_bManualManageRelay();
                    chbRelaysAutoModeArr[numRelay].setChecked(bRelaysAutoModeArr[numRelay]);
                }
            }
        }
    }

    private void GetArdPhoneNumb()
    {
        RealmResults<DevicesInfoDb> devicesInfoDbs = realm.where(DevicesInfoDb.class).equalTo(IDFIELDNAME, _sIdDevice).findAll();
        for (int i = 0; i < devicesInfoDbs.size(); i++)
        {
           _ardPhoneNumb = devicesInfoDbs.get(i).get_phoneNumbArduino();
        }
    }

    private void SetDefaultValDeviceInfo()
    {
        for (int j = 0; j < COUNTRELAYS; j++)
        {
            sRelayNameArr[j] = EMPTYDATA;
            tvRelaysNameArr[j].setText(sRelayNameArr[j]);
            bRelaysStateArr[j] = false;
            tgbRelaysStateArr[j].setChecked(bRelaysStateArr[j]);
            bRelaysAutoModeArr[j] = true;
            chbRelaysAutoModeArr[j].setChecked(bRelaysAutoModeArr[j]);
        }
    }
    //endregion

    //region Нажатие на кнопку принудительной смены состояния реле
    public void ToggleButtonClick(View v)
    {
        view = v;
        int viewId = view.getId();
        for (int j = 0; j < COUNTRELAYS; j++)
        {
            if (viewId != rIdToggleButtonArr[j])
            {
                continue;
            }
            ShowAlertDialogToggleButton(j, tgbRelaysStateArr[j].isChecked());
        }
    }
    //endregion

    //region Показать диалог с вопросом при принудительной смене состояния
    int tgbId;
    boolean tgbState;
    public void ShowAlertDialogToggleButton(int id, boolean state)
    {
        tgbId = id;
        tgbState = state;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RelayManageActivity.this);

        alertDialog.setTitle("Изменить состояние...");
        alertDialog.setMessage("Изменить состояние принудительно?");

        //region YES CLICK
        alertDialog.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                if (!TextUtils.isEmpty(_ardPhoneNumb))
                {
                    if (tgbState)
                    {
                        SendSms(_ardPhoneNumb, "СЮДА СФОРМИРОВАТЬ ТЕКСТ СМС ДЛЯ ВКЛ РЕЛЕ");
                    }
                    else SendSms(_ardPhoneNumb, "СЮДА СФОРМИРОВАТЬ ТЕКСТ СМС ДЛЯ ВЫКЛ РЕЛЕ");
                    bRelaysAutoModeArr[tgbId] = false; //выключаем автоматику при ручном управлении
                    chbRelaysAutoModeArr[tgbId].setChecked(bRelaysAutoModeArr[tgbId]);
                    WriteDataToDb();
                }
                else
                {
                    ShowToast(view);
                    ChangeStatesRelayView();
                }
            }
        });
        //endregion

        //region NO CLICK
        alertDialog.setNegativeButton("НЕТ", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                ChangeStatesRelayView();
                dialog.cancel();
            }
        });
        //endregion
        alertDialog.show();
    }

    private void ChangeStatesRelayView()
    {
        for (int j = 0; j < COUNTRELAYS; j++)
        {
            if (tgbId != j)
            {
                continue;
            }
            if (tgbState)
                tgbRelaysStateArr[tgbId].setChecked(false);
            else tgbRelaysStateArr[tgbId].setChecked(true);
        }
    }
    //endregion

    //region Нажатие на кнопку перехода на автоматику
    public void CheckBoxAutoModeClick(View v)
    {
        view = v;
        int viewId = view.getId();
        for (int j = 0; j < COUNTRELAYS; j++)
        {
            if (viewId != rIdRelaysAutoModeArr[j])
            {
                continue;
            }
            ShowAlertDialogCheckBox(j, chbRelaysAutoModeArr[j].isChecked());
        }
    }
    //endregion

    //region Показать диалог с вопросом о возврате системы на автоматику
    int chbId;
    boolean chbState;
    public void ShowAlertDialogCheckBox(int id, boolean state)
    {
        chbId = id;
        chbState = state;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RelayManageActivity.this);

        alertDialog.setTitle("Автоматика...");
        alertDialog.setMessage("Включить автоматический режим?");

        //region YES CLICK
        alertDialog.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                if (!TextUtils.isEmpty(_ardPhoneNumb))
                {
                    if (chbState)
                    {
                        SendSms(_ardPhoneNumb, "СЮДА СФОРМИРОВАТЬ ТЕКСТ СМС ДЛЯ ВКЛ АВТОМАТИКИ НА РЕЛЕ");
                    }
                    bRelaysAutoModeArr[chbId] = true; //выключаем автоматику при ручном управлении
                    chbRelaysAutoModeArr[chbId].setChecked(bRelaysAutoModeArr[chbId]);
                    WriteDataToDb();
                }
                else
                {
                    ShowToast(view);
                    ChangeStatesCheckBoxView();
                }
            }
        });
        //endregion

        //region NO CLICK
        alertDialog.setNegativeButton("НЕТ", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                ChangeStatesCheckBoxView();
                dialog.cancel();
            }
        });
        //endregion
        alertDialog.show();
    }

    private void ChangeStatesCheckBoxView()
    {
        for (int j = 0; j < COUNTRELAYS; j++)
        {
            if (chbId != j)
            {
                continue;
            }
            if (chbState)
                chbRelaysAutoModeArr[chbId].setChecked(false);
            else chbRelaysAutoModeArr[chbId].setChecked(true);
        }
    }
    //endregion

    //region WriteDataToDb() ЗАПИСЬ В БД
    private void WriteDataToDb()
    {
        if (!TextUtils.isEmpty(_sIdDevice)) {
            realm = Realm.getInstance(getBaseContext());
            realm.beginTransaction();

            //ВОТ ТУТ НАДО СНАЧАЛА ЗАПИСАТЬ ВСЕ СОСТОЯНИЯ В ТАБЛИЦУ ПО РЕЛЮХАМ, ПОТОМ ТЕ КОТОРЫЕ ПРИВЯЗАНЫ ПЕРЕПИШУТСЯ САМИ!!!
            //Причем пока не сконфигурировано ардуино или не получена инфа по смс, этот список будет пуст
            //таблица заполняется только в 2 случаях - инфа по смс или конфигурирование из приложения ардуины
            RealmResults<RelayRenamesDb> relayRenamesDbs = realm.where(RelayRenamesDb.class).equalTo(IDFIELDNAME, _sIdDevice).findAll();
            if (relayRenamesDbs.size() > 0)
            {
                for (int i = 0; i < COUNTRELAYS; i++)
                {
                    relayRenamesDbs.get(i).set_numRelay(i + 1);
                    relayRenamesDbs.get(i).set_idDevice(_sIdDevice);
                    bRelaysStateArr[i] = tgbRelaysStateArr[i].isChecked();
                    relayRenamesDbs.get(i).set_bStateRelay(bRelaysStateArr[i]);
                    bRelaysAutoModeArr[i] = chbRelaysAutoModeArr[i].isChecked();
                    relayRenamesDbs.get(i).set_isAutomaticModeR(bRelaysAutoModeArr[i]);
                }
            }

            RealmResults<DevicesInfoDb> devicesInfoDbs;
            RealmResults<SensorsInfoDb> sensorsInfoDbs = realm.where(SensorsInfoDb.class).equalTo(IDFIELDNAME, _sIdDevice).findAll();
            RealmList<SensorsInfoDb> sensorsInfoList;
            if (sensorsInfoDbs.size() > 0)
            {
                //Получаем максимальное время
                int maxTimeStamp = sensorsInfoDbs.max(TIMESTAMPFIELDNAME).intValue();

                //Получаем только свежие записи по максимальному времени
                devicesInfoDbs = realm.where(DevicesInfoDb.class)
                        .equalTo(IDFIELDNAME, _sIdDevice)
                        .equalTo(SENSORSINFOTABLENAME + "." + TIMESTAMPFIELDNAME, maxTimeStamp)
                        .findAll();

                for (int i = 0; i < devicesInfoDbs.size(); i++)
                {
                    sensorsInfoList = devicesInfoDbs.get(i).get_stateSystemRaws();

                    for (int j = 0; j < sensorsInfoList.size(); j++)
                    {
                        //ТЕ КОТОРЫЕ ПРИВЯЗАНы ПЕРЕПИСЫВАЕМ
                        for (int k = 0; k < COUNTRELAYS; k++)
                        {
                            if (!TextUtils.isEmpty(sensorsInfoList.get(k).get_bNumRelay()) &&
                                    Integer.parseInt(sensorsInfoList.get(k).get_bNumRelay()) == (k + 1))
                            {
                                bRelaysStateArr[k] = tgbRelaysStateArr[k].isChecked();
                                sensorsInfoList.get(k).set_bStateRelay(bRelaysStateArr[k]);
                                bRelaysAutoModeArr[k] = chbRelaysAutoModeArr[k].isChecked();
                                sensorsInfoList.get(k).set_bManualManageRelay(bRelaysAutoModeArr[k]);
                            }
                        }
                    }
                }
            }
            realm.commitTransaction();
        }
    }
    //endregion

    //region SendSms Функция отправки смс
    public void SendSms(String number, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, null, null);
    }
    //endregion

    private void ShowToast(View view)
    {
        //создаем и отображаем текстовое уведомление
        Toast toast = Toast.makeText(getApplicationContext(), "Добавьте устройство с номером телефона!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    //region Создаем меню
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
    //endregion
    //region Событие на выбранный пункт меню
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
                intent = new Intent(RelayManageActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.conf:
                intent = new Intent(RelayManageActivity.this, SettingsActivityTabs.class);
                startActivity(intent);
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
        WriteDataToDb();
    }
}