package com.example.davy.projetoic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.davy.projetoic.Persistence.DBService;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);

        DBService db = new DBService(this);
        String[] user = db.getUser();

        toolbar.setTitle(user[DBService.NAME]);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment fragment = Fragment_chart_Profile.newInstance(user[DBService.EMAIL], user[DBService.TOKEN]);
        Fragment fragHistory = Fragment_History.newInstance(user[DBService.EMAIL], user[DBService.TOKEN]);

        ft.add(R.id.fragment_container1, fragment, "frag1");
        ft.add(R.id.fragment_container2, fragHistory, "frag2");
        ft.commit();
    }
}
