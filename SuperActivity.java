package com.example.frederikeriksen.befriendy;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public abstract class SuperActivity extends AppCompatActivity implements ListView.OnItemSelectedListener {

    private DrawerLayout menuDrawer;
    private ListView drawerListView;
    private ActionBarDrawerToggle menuToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_drawer);

        menuDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        menuToggle = new ActionBarDrawerToggle(this, menuDrawer, R.string.navigationDrawer_open, R.string.navigationDrawer_closed) {

            public void onDrawerClosed(View view) {
                getActionBar().setTitle(R.string.app_name);
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(R.string.menu);
            }
        };

        menuDrawer.setDrawerListener(menuToggle);
        menuToggle.syncState();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        drawerListView = (ListView) findViewById(R.id.drawer_left);
        drawerListView.setOnItemSelectedListener(this);
        View header = getLayoutInflater().inflate(R.layout.drawer_header, null);
        drawerListView.addHeaderView(header);

        drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.menu_drawer));

    }


        @Override
        public boolean onOptionsItemSelected (MenuItem item) {

            int id = item.getItemId();

            if (id == R.id.drawer_first_item) {
                // What to do if first item on the menu is clicked...
            }

            if (id == R.id.drawer_second_item) {
                // What to do if second item on the menu is clicked...
            }

            if (id == R.id.drawer_third_item) {
                // What to do if third item on the menu is clicked...
            }

            if (id == R.id.drawer_fourth_item) {
                // What to do if fourth item on the menu is clicked...
            }
            // etc.

            DrawerLayout menuDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
            menuDrawer.closeDrawer(GravityCompat.START);
            return true;

        }


    }

