package com.korpaev.myhome;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class SettingsActivityTabs extends TabActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_tabs);

        // получаем TabHost
        TabHost tabHost = getTabHost();

        // инициализация была выполнена в getTabHost
        // метод setup вызывать не нужно

        Intent intentMainConf = new Intent().setClass(this, GeneralSettingsActivity.class);
        TabSpec tabSpecMainConf = tabHost
                .newTabSpec("MainConf")
                .setIndicator("Основные настройки")
                .setContent(intentMainConf);
        tabHost.addTab(tabSpecMainConf);

        Intent intentAddConf = new Intent().setClass(this, AdditionalySettingsActivity.class);
        TabSpec tabSpecAddConf = tabHost
                .newTabSpec("AdditionalConf")
                .setIndicator("Дополнительные настройки")
                .setContent(intentAddConf);
        tabHost.addTab(tabSpecAddConf);
    }
}
