package com.example.robin.tabbedact;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.myscript.atk.geometry.widget.GeometryWidgetApi;
import com.myscript.atk.math.widget.MathWidgetApi;
import com.myscript.certificate.MyCertificate;

import static com.example.robin.tabbedact.MainActivity.BitMapToString;

/**
 * Created by robin on 7/10/17.
 */

public class GeometryTab3 extends Fragment implements
        GeometryWidgetApi.OnConfigureListener,
        GeometryWidgetApi.OnRecognitionListener{
    private static final String TAG = "GeometryDemo";

    private GeometryWidgetApi widget;
    private Bitmap bmp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.geometry_window3, container, false);
        Button b1 = (Button)rootView.findViewById(R.id.clearButton);
        Button b2 = (Button)rootView.findViewById(R.id.sendButton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearButtonClick(v);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendButtonClick(view);
            }
        });
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        widget = (GeometryWidgetApi)view. findViewById(R.id.geometry_widget);
        if (!widget.registerCertificate(MyCertificate.getBytes()))
        {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getActivity());
            dlgAlert.setMessage("Please use a valid certificate.");
            dlgAlert.setTitle("Invalid certificate");
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    //dismiss the dialog
                }
            });
            dlgAlert.create().show();
            return;
        }

        widget.setOnConfigureListener(this);
        widget.setOnRecognitionListener(this);

        // references assets directly from the APK to avoid extraction in application
        // file system
        widget.addSearchDir("zip://" + getActivity(). getPackageCodePath() + "!/assets/conf");

        // The configuration is an asynchronous operation. Callbacks are provided to
        // monitor the beginning and end of the configuration process and update the UI
        // of the input method accordingly.
        //
        // "shape" references the shape bundle name in conf/shape.conf file in your assets.
        // "standard" references the configuration name in shape.conf
        widget.configure("shape", "standard");
    }
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        widget.setOnRecognitionListener(null);
        widget.setOnConfigureListener(null);
        widget.clear(true);
        // release widget's resources
        widget.release();
    }
    @Override
    public void onConfigurationBegin(GeometryWidgetApi widget)
    {
    }

    @Override
    public void onConfigurationEnd(GeometryWidgetApi widget, boolean success)
    {
        if(!success)
        {
            Toast.makeText(getActivity(). getApplicationContext(), widget.getErrorString(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Unable to configure the Geometry Widget: " + widget.getErrorString());
            return;
        }
        Toast.makeText(getActivity(). getApplicationContext(), "Geometry Widget Configured", Toast.LENGTH_SHORT).show();
        if(BuildConfig.DEBUG)
            Log.d(TAG, "Geometry Widget configured!");
    }

    @Override
    public void onRecognitionBegin(GeometryWidgetApi widget)
    {
    }

    @Override
    public void onRecognitionEnd(GeometryWidgetApi widget)
    {
        Toast.makeText(getActivity(). getApplicationContext(), "Recognition update", Toast.LENGTH_SHORT).show();
        if(BuildConfig.DEBUG)
        {
            Log.d(TAG, "Geometry Widget recognition");
        }
        //bmp = widget.getResultAsImage();

        //BitMapToString(bmp);
    }
    private void onClearButtonClick(View v) {
        widget.clear(true);
    }
    private void onSendButtonClick(View v)
    {
        Context mContext = getActivity() ;
        //new MainActivity().send(widget.getResultAsLaTeX(),mContext);
        bmp = widget.getResultAsImage();
        BitMapToString(bmp);
    }
}