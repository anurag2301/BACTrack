package com.pervasive_computing.bactrackapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public abstract class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @Override
    public void setContentView(int layoutResID) {
        DrawerLayout drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.drawer_layout, null);
        NavigationView navigationView = drawerLayout.findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setBackgroundColor(Color.WHITE);
        CoordinatorLayout coordinatorLayout = drawerLayout.findViewById(R.id.coordinator_layout);
        getLayoutInflater().inflate(layoutResID, coordinatorLayout, true);
        super.setContentView(drawerLayout);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.activity_container);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            //super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = handleMenu(item.getItemId());
        return result || super.onOptionsItemSelected(item);
    }

    private boolean handleMenu(int menuItemId) {

        switch (menuItemId) {
            case R.id.view_contacts:
                startActivity(new Intent(getApplicationContext(), ListContacts.class));
                return true;
            case R.id.help:
                return true;
            case R.id.add_contact:
                startActivity(new Intent(getApplicationContext(), ContactPicker.class));
                return true;
            case R.id.back_to_main:
                //startActivity(new Intent(getApplicationContext(), FirstPageActivity.class));
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        handleMenu(item.getItemId());
        DrawerLayout drawer = findViewById(R.id.activity_container);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
