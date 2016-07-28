package byteshaft.com.nationalpropertyassist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.Random;

import byteshaft.com.nationalpropertyassist.account.CodeConfirmationActivity;
import byteshaft.com.nationalpropertyassist.account.LoginActivity;
import byteshaft.com.nationalpropertyassist.activities.AssistMain;
import byteshaft.com.nationalpropertyassist.fragments.Help;
import byteshaft.com.nationalpropertyassist.fragments.JobHistory;
import byteshaft.com.nationalpropertyassist.fragments.PaymentDetails;
import byteshaft.com.nationalpropertyassist.fragments.PropertyDetails;
import byteshaft.com.nationalpropertyassist.fragments.Settings;
import byteshaft.com.nationalpropertyassist.utils.BitmapWithCharacter;
import byteshaft.com.nationalpropertyassist.utils.Helpers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static MainActivity sInstance;
    private View header;
    private TextView mName;
    private TextView mEmail;

    public static MainActivity getInstance() {
        return sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInstance = this;
        if (!Helpers.isUserLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } else if (!Helpers.isUserActive()) {
            startActivity(new Intent(getApplicationContext(), CodeConfirmationActivity.class));
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Helpers.isUserLoggedIn() && Helpers.isUserActive()) {
            if (Helpers.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL).equals("")) {
                new LoginActivity.GetUserDataTask().execute();
            }
        }
        mName = (TextView) header.findViewById(R.id.nav_user_name);
        mEmail = (TextView) header.findViewById(R.id.nav_user_email);
        if (!Helpers.getStringFromSharedPreferences(AppGlobals.KEY_FIRSTNAME).equals("")) {
            String simpleName = Helpers.getStringFromSharedPreferences(AppGlobals.KEY_FIRSTNAME);
            String firstUpperCaseName = simpleName.substring(0, 1).toUpperCase() + simpleName.substring(1);
            mName.setText(firstUpperCaseName);
        } else {
            mName.setText("username");
        }
        if (!Helpers.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL).equals("")) {
            mEmail.setText(Helpers.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL));
        } else {
            mEmail.setText("abc@xyz.com");
        }

        CircularImageView circularImageView = (CircularImageView) header.findViewById(R.id.imageView);
        if (Helpers.isUserLoggedIn()) {
            final Resources res = getResources();
            int[] array = getResources().getIntArray(R.array.letter_tile_colors);
            final BitmapWithCharacter tileProvider = new BitmapWithCharacter();
            final Bitmap letterTile = tileProvider.getLetterTile(Helpers.
                            getStringFromSharedPreferences(AppGlobals.KEY_FIRSTNAME),
                    String.valueOf(array[new Random().nextInt(array.length)]), 100, 100);
            circularImageView.setImageBitmap(letterTile);
        }
        loadFragment(new AssistMain());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_property_details) {
            loadFragment(new PropertyDetails());

        } else if (id == R.id.nav_job_history) {
            loadFragment(new JobHistory());

        } else if (id == R.id.nav_payment_details) {
            loadFragment(new PaymentDetails());

        } else if (id == R.id.nav_settings) {
            loadFragment(new Settings());

        } else if (id == R.id.nav_help) {
            loadFragment(new Help());

        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Alert");
            alertDialogBuilder.setMessage("Do you really want to logout?").setCancelable(false).setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Helpers.clearSaveData();
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } else if (id == R.id.nav_np_assist) {
            loadFragment(new AssistMain());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.container, fragment);
        tx.commit();
    }
}
