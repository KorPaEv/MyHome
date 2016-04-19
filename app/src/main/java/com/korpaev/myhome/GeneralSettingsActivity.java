package com.korpaev.myhome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GeneralSettingsActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_general);
    }

    //создаем меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        //заполняем меню
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (menu != null)
            menu.findItem(R.id.conf).setVisible(false);
        return true;
    }

    //событие на выбранный пункт меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;
        // получаем все пункты меню
        //пункт item определен в menu_main
        int itemId = item.getItemId();

        // ищем наш пункт меню
        switch (itemId)
        {
            case R.id.about:
                 intent = new Intent(GeneralSettingsActivity.this, AboutActivity.class);
                 startActivity(intent);
                 return true;
            case R.id.main_inf:
                 intent = new Intent(GeneralSettingsActivity.this, MainActivityTabs.class);
                 startActivity(intent);
                 finish();
                 return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}