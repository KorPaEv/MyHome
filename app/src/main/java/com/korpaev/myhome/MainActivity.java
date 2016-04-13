package com.korpaev.myhome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
{
    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getInstance(getBaseContext());
        realm.beginTransaction();
        //Очистим БД от старых данных
        realm.where(RawSmsDb.class).findAll().clear();
        realm.commitTransaction();
        MessageService ms = new MessageService();
        String str = "SH1;1460473840;1;8;RN;0C;RN;SH1;1460473840;2;8;RN;0C;RN;SH1;1460473840;3;8;RN;0C;RN;SH1;1460473840;4;8;RN;0C;RN;SH1;1460473840;5;8;RN;0C;RN;";
        ms.WriteDataToDB(str, getBaseContext());
        FillData();
    }

    public void FillData()
    {
        realm = Realm.getInstance(getBaseContext());
        realm.beginTransaction();
        //Посмотрим что лежит в БД после записи данных
        RealmResults<RawSmsDb> results = realm.where(RawSmsDb.class).findAll();
        realm.commitTransaction();
    }
}
