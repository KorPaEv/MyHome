package com.korpaev.myhome;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivityTabs extends TabActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabs);

        // получаем TabHost
        TabHost tabHost = getTabHost();

        // инициализация была выполнена в getTabHost
        // метод setup вызывать не нужно

        Intent intentDevices = new Intent().setClass(this, DevicesActivity.class);
        TabHost.TabSpec tabSpecDevices = tabHost
                .newTabSpec("Devices")
                .setIndicator("Мои устройства")
                .setContent(intentDevices);
        tabHost.addTab(tabSpecDevices);

        TextView tv = (TextView)tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tv.setTextSize(8);

        Intent intentGeneralInfo = new Intent().setClass(this, GeneralInfoActivity.class);
        TabHost.TabSpec tabSpecGeneralInfo = tabHost
                .newTabSpec("GeneralInfo")
                .setIndicator("Контроль")
                .setContent(intentGeneralInfo);
        tabHost.addTab(tabSpecGeneralInfo);
        tabHost.setCurrentTab(1);

        tv = (TextView)tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tv.setTextSize(8);

        Intent intentRelayManage = new Intent().setClass(this, RelayManageActivity.class);
        TabHost.TabSpec tabSpecRelayManage = tabHost
                .newTabSpec("RelayManage")
                .setIndicator("Управление")
                .setContent(intentRelayManage);
        tabHost.addTab(tabSpecRelayManage);

        tv = (TextView)tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
        tv.setTextSize(8);

        Intent intentHistory = new Intent().setClass(this, HistoryActivity.class);
        TabHost.TabSpec tabSpecHistory = tabHost
                .newTabSpec("History")
                .setIndicator("История")
                .setContent(intentHistory);
        tabHost.addTab(tabSpecHistory);

        tv = (TextView)tabHost.getTabWidget().getChildAt(3).findViewById(android.R.id.title);
        tv.setTextSize(8);
    }
}
