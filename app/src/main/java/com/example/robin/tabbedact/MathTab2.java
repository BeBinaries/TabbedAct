package com.example.robin.tabbedact;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.myscript.atk.math.widget.MathWidgetApi;
import com.myscript.certificate.MyCertificate;

import static android.R.style.Widget;
import static com.example.robin.tabbedact.MainActivity.BitMapToString;

/**
 * Created by robin on 7/10/17.
 */

public class MathTab2 extends Fragment implements
        MathWidgetApi.OnConfigureListener,
        MathWidgetApi.OnRecognitionListener {

    private MathWidgetApi widget;
    private static final String TAG = "MathDemo";
    private Bitmap bmp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.math_window2, container, false);
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
        widget = (MathWidgetApi) view.findViewById(R.id.math_widget);
        if (!widget.registerCertificate(MyCertificate.getBytes())) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
            dlgAlert.setMessage("Please use a valid certificate.");
            dlgAlert.setTitle("Invalid certificate");
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //dismiss the dialog
                }
            });
            dlgAlert.create().show();
            return;
        }
        widget.setOnConfigureListener(this);

        // references assets directly from the APK to avoid extraction in application
        // file system
        widget.addSearchDir("zip://" + getActivity().getPackageCodePath() + "!/assets/conf");

        // The configuration is an asynchronous operation. Callbacks are provided to
        // monitor the beginning and end of the configuration process and update the UI
        // of the input method accordingly.
        //
        // "math" references the conf/math/math.conf file in your assets.
        // "standard" references the configuration name in math.conf
        widget.configure("math", "standard");
        widget.setOnRecognitionListener(this);
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
    public void onConfigureBegin(MathWidgetApi widget)
    {
    }

    @Override
    public void onConfigureEnd(MathWidgetApi widget, boolean success)
    {
        if(!success)
        {
            Toast.makeText(getActivity().getApplicationContext(), widget.getErrorString(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Unable to configure the Math Widget: " + widget.getErrorString());
            return;
        }
        Toast.makeText(getActivity().getApplicationContext(), "Math Widget Configured", Toast.LENGTH_SHORT).show();
        if(BuildConfig.DEBUG)
            Log.d(TAG, "Math Widget configured!");
    }

    @Override
    public void onRecognitionBegin(MathWidgetApi widget)
    {
    }

    @Override
    public void onRecognitionEnd(final MathWidgetApi widget)
    {
        Toast.makeText(getActivity().getApplicationContext(), "Recognition update", Toast.LENGTH_SHORT).show();
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "Math Widget recognition: " + widget.getResultAsText());
        }
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
