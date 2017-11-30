package com.example.robin.tabbedact;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class SignatureActivity extends Activity {

    private CanvasView customCanvas;
    LinearLayout x ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_alert_sign);

        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);



    }

    public void clearCanvas(View v) {

        customCanvas.clearCanvas();

    }
    public void sendBit(View v)
    {
        LinearLayout x = (LinearLayout) findViewById(R.id.linlay);
        x.setDrawingCacheEnabled(true);
        x.buildDrawingCache();
        customCanvas.sendSignature(x);
    }
}
