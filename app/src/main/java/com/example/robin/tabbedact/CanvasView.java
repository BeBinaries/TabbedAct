package com.example.robin.tabbedact;

/**
 * Created by Shashank on 17-11-2017.
 */
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.util.AttributeSet;
        import android.util.Base64;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.LinearLayout;

        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.io.OutputStream;
        import java.net.Socket;
        import java.nio.ByteBuffer;

public class CanvasView extends View {

    public int width;
    public int height;
    private Bitmap mBitmap;
    public Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw the mPath with the mPaint on the canvas when onDraw
        canvas.drawPath(mPath, mPaint);

    }
    /*public void bitMapToString(Bitmap bitmap,Context context){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        //byte [] b=baos.toByteArray();
        try {
            if(MainActivity.ip.isEmpty()){
                new MainActivity().ipSetting(context);
            }
            MainActivity.s = new Socket(MainActivity.ip, 5001);
            OutputStream outputStream = MainActivity.s.getOutputStream();
            byte[] size = ByteBuffer.allocate(4).putInt(baos.size()).array();
            outputStream.write(size);
            outputStream.write(baos.toByteArray());
            outputStream.flush();
            MainActivity.s.close();

        }catch (IOException e)
        {
            e.printStackTrace();
        }


    }*/


    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }
    public void sendSignature(LinearLayout l){

        Bitmap bmp = Bitmap.createBitmap(l.getDrawingCache());
        /*ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);

        try {
            if(MainActivity.ip.isEmpty()){
                new MainActivity().ipSetting(context);
            }
            MainActivity.s = new Socket(MainActivity.ip, 5001);
            OutputStream outputStream = MainActivity.s.getOutputStream();
            byte[] size = ByteBuffer.allocate(4).putInt(baos.size()).array();
            outputStream.write(size);
            outputStream.write(baos.toByteArray());
            outputStream.flush();
            MainActivity.s.close();

        }catch (IOException e)
        {
            e.printStackTrace();
        }*/
        new MainActivity().BitMapToString(bmp,context);

    }


    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }


    // when ACTION_UP stop touch
    private void upTouch() {
        mPath.lineTo(mX, mY);
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }
}
