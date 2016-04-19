package com.korpaev.myhome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class RelayManageActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relay_manage);
    }

    //создаем меню
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

    //событие на выбранный пункт меню
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
}