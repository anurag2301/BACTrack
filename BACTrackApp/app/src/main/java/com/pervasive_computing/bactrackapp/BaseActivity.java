package com.pervasive_computing.bactrackapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/*
  Created by Pratik on 11/05/2017.
 */

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    public void setContentView(int layoutResID) {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.drawer, null);
        CoordinatorLayout activityContainer = fullView.findViewById(R.id.app_bar_main);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.activity_container);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.view_contacts:
                startActivity(new Intent(this, ListContacts.class));
                return true;
            case R.id.help:
                return true;
            case R.id.add_contact:
                startActivity(new Intent(this, ContactPicker.class));
                return true;
            case R.id.back_to_main:
                startActivity(new Intent(this, FirstPageActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.view_contacts:
                startActivity(new Intent(this, ListContacts.class));
                return true;
            case R.id.help:
                return true;
            case R.id.add_contact:
                startActivity(new Intent(this, ContactPicker.class));
                return true;
            case R.id.back_to_main:
                startActivity(new Intent(this, FirstPageActivity.class));
                return true;
        }
        DrawerLayout drawer = findViewById(R.id.activity_container);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
