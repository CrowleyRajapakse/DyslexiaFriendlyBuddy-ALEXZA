package com.fsociety2.dyslexiafriendlybuddy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import java.io.IOException;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Scalar;
import org.opencv.videoio.VideoCapture;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class OCRActivity extends AppCompatActivity  implements CameraBridgeViewBase.CvCameraViewListener2{

    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    TextRecognizer textRecognizer;

    JavaCameraView javaCameraView;
    Mat mRgba, imgGray, imgCanny;

    static {

        if (OpenCVLoader.initDebug())

        {
            Log.i("TAG", "OpenCv Loaded");

        } else

        {
            Log.i("TAG", "OpenCv Failed");

        }
    }

    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:{
                    javaCameraView.enableView();
                    break;
                }
                default:{
                    super.onManagerConnected(status);
                    break;
                }
            }

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case RequestCameraPermissionID:
            {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);



        javaCameraView = findViewById(R.id.opencv_view1);
        javaCameraView.setVisibility(SurfaceView.INVISIBLE);
       // cameraView = javaCameraView;
        //
       // cameraView = findViewById(R.id.opencv_view);


        javaCameraView.setCvCameraViewListener(this);

        cameraView = findViewById(R.id.surface_view);
        textView = findViewById(R.id.text_view);

        textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();


        //TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if(! textRecognizer.isOperational()){
            Log.w("MainActivity","Detector dependencies are not yet available");
        }else{

            cameraSource = new CameraSource.Builder(getApplicationContext(),textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280,1024)
                    .setRequestedFps(30.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback(){
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder){
                    try{
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(OCRActivity.this,
                                    new String[] {Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);

                        }
                        cameraSource.start(cameraView.getHolder());

                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2){


                }


                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder){
                    cameraSource.stop();
                }

            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>(){
                @Override
                public void release(){

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections){
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size() != 0){
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i=0;i<items.size();++i){
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                textView.setText(stringBuilder.toString());
                            }
                        });
                    }
                }

            });

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSend);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainServiceActivity.class);
                intent.putExtra("data",textView.getText());
                startActivity(intent);
            }
        });
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fabOpenCv);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),OcrOpenCV.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(javaCameraView != null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(javaCameraView != null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug()){
            Log.i("TAG","OpenCv Loaded");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }else{
            Log.i("TAG","OpenCv Failed");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION,this,mLoaderCallback);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height,width,CvType.CV_8UC4);
        imgGray = new Mat(height,width,CvType.CV_8UC1);
        imgCanny = new Mat(height,width,CvType.CV_8UC1);



    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        Imgproc.cvtColor(mRgba,imgGray,Imgproc.COLOR_RGB2GRAY);
        Imgproc.Canny(imgGray,imgCanny,50,150);

        textRecognizer.setProcessor(new Detector.Processor<TextBlock>(){
            @Override
            public void release(){

            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections){
                final SparseArray<TextBlock> items = detections.getDetectedItems();
                if(items.size() != 0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder stringBuilder = new StringBuilder();
                            for(int i=0;i<items.size();++i){
                                TextBlock item = items.valueAt(i);
                                stringBuilder.append(item.getValue());
                                stringBuilder.append("\n");
                            }
                            textView.setText(stringBuilder.toString());
                        }
                    });
                }
            }
        });

        return imgCanny;
    }


}
