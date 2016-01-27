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
