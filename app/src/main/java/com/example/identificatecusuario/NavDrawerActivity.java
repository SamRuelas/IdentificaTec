package com.example.identificatecusuario;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView textName;
    TextView textEmail;

    DatabaseReference _db;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "hola with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        changeFragment( new MyAccountFragment(), R.id.container_fragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            //finish();
            //Intent intent = Intent(this, My::class.java);
            //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            //startActivity(intent)
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.micredencial) {
            changeFragment( new MyAccountFragment(), R.id.container_fragment);
        } else if (id == R.id.micuenta) {
            changeFragment(new MovimientosFragment(), R.id.container_fragment);
        } else if (id == R.id.lostandfound) {
            changeFragment(new LostandFound(), R.id.container_fragment);

        } else if (id == R.id.cargarsaldo) {
            changeFragment(new CargarSaldo(), R.id.container_fragment);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void changeFragment(Fragment fragment, int idContainer) {
        getSupportFragmentManager().beginTransaction()
                .replace(idContainer, fragment).commit();
    }
    private void loadUserInformation() {
        FirebaseAuth mAuth  = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        _db = FirebaseDatabase.getInstance().getReference("/users");


        if (user != null) {
            if (user.getUid() != null) {
                uid = user.getUid();
            }
            if (user.getEmail() != null) {
                //var displayName : String = user!!.getDisplayName()!!
                //nav_view.getHeaderView(0).mail_text.text = user!!.email
            }
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

           /*ref.addValueEventListener(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                             String userName = dataSnapshot.child("users").child(uid).child("username").getValue().toString();
                                             if (userName != "null") {
                                                 setContentView(R.layout.nav_header_nav_drawer);
                                                 //NavigationView(R.layout.)
                                                 TextView textView = findViewById(R.id.n);



                                             }
                                         }

                                         @Override
                                         public void onCancelled(@NonNull DatabaseError databaseError) {

                                         }
                                     }*/


        }

    }
}
