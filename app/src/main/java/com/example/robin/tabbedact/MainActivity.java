package com.example.robin.tabbedact;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;

import static android.R.id.message;

public class MainActivity extends AppCompatActivity {
    public static Socket s;
    public static PrintWriter pw;
    public static String ip ="";

    private static final String TAG = "MainActivity";
    private final String IP_FILE_NAME = "ipfile";
    private final String IP_ADDRESS = "ipaddress";



    CanvasView customCanvas;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private SharedPreferences ipPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);



        System.out.println("Rajan");


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id ==  R.id.action_IpConfig)
        {
            ipSetting(MainActivity.this);
        }
        if(id == R.id.action_sign)
        {
            Intent inte = new Intent(this,SignatureActivity.class);
            startActivity(inte);
        }

        return super.onOptionsItemSelected(item);
    }
    public void ipSetting(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Set IP");

        final Context c = context;
// Set up the input
        final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ip = input.getText().toString();
                ipPref = c.getSharedPreferences(IP_FILE_NAME,Context.MODE_PRIVATE);
                SharedPreferences.Editor ipPrefEditor = ipPref.edit();
                ipPrefEditor.putString(IP_ADDRESS,ip);
                ipPrefEditor.apply();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



    public void send(String message,Context context) {
        try {
            //Log.d(TAG,"inside try block");
            SharedPreferences ipPref = context.getSharedPreferences(IP_FILE_NAME,Context.MODE_PRIVATE);
            ip = ipPref.getString(IP_ADDRESS,"");
            if(ip.isEmpty()){
                ipSetting(context);
            }
            s = new Socket(ip, 5000);
            pw = new PrintWriter(s.getOutputStream());
            pw.write(message);
            pw.flush();
            pw.close();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
            //send(null);
        }
    }

    public void BitMapToString(Bitmap bitmap,Context context){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        //byte [] b=baos.toByteArray();
        try {
                if(ip.isEmpty()){
                    ipSetting(context);
                }
            s = new Socket(ip, 5001);
            OutputStream outputStream = s.getOutputStream();
            byte[] size = ByteBuffer.allocate(4).putInt(baos.size()).array();
            outputStream.write(size);
            outputStream.write(baos.toByteArray());
            outputStream.flush();
            s.close();

        }catch (IOException e)
        {
            e.printStackTrace();
        }


    }
    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    LineTab1 lineTab1 = new LineTab1();
                    return lineTab1;
                case 1:
                    MathTab2 mathTab2 = new MathTab2();
                    return mathTab2;
                case 2:
                    GeometryTab3 geometryTab3 = new GeometryTab3();
                    return geometryTab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Line";
                case 1:
                    return "Math";
                case 2:
                    return "Geometry";
            }
            return null;
        }
    }
}
