package com.develop.filhan.filhandennis_1202150079_modul6;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Attribut untuk mendefinisian kode Intent Login
    public static final int RESUlT_CODE_LOGIN=1000;

    //Attribut untuk Komponen View
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProgressDialog loading;

    //Attribut untuk FireBase Object
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabase;

    public static List<PhotoModel> photosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inisialisasi Komponen View Loading
        loading=new ProgressDialog(this);
        photosList=new ArrayList<>();

        //Inisialisasi Auth untuk FireBase Sebagai Lib untuk membantu User Login/Register
        auth=FirebaseAuth.getInstance();
        //Pengecekan User, jika belum ada diarahkan ke intent SignIn
        if(auth.getCurrentUser()==null){
            startActivity(new Intent(getApplicationContext(),SignInActivity.class));
            finish();
        }

        //Inisialisasi Komponen View bagian Tab
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        //Inisialisasi Komponen View
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_new);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_my);

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        //this.photosList = loadData();
    }

    /*
    * Method untuk keperluan Menu
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
    * Method untuk keperluan menu
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(auth.getCurrentUser()!=null){menu.findItem(R.id.action_myuser).setTitle(makeUsername(auth.getCurrentUser().getEmail()));}
        return true;
    }


    /*
        * Method untuk keperluan menu
        * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            auth.signOut();

            startActivity(new Intent(getApplicationContext(),SignInActivity.class));
            finish();
            return true;
        }else if(id==R.id.action_refresh){
            //loadData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * Method untuk mengatur konfigurasi awal tampilan Tab
    * */
    private void setupViewPager(ViewPager pager){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new RecentFragment(), "Recent");
        viewPagerAdapter.addFragment(new MyFragment(), "My Gallery");
        pager.setAdapter(viewPagerAdapter);
    }

    /*
    * Class sebagai Adapter ViewPager
    * Berisikan keperluan yang dibutuhkan untuk KOmponen Tab Layout
    * */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    /*
    * Method untuk mengubah Email menjadi DisplayName tanpa @...
    * */
    public String makeUsername(String email){
        return email.substring(0,email.lastIndexOf("@"));
    }

}
