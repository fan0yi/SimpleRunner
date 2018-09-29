package com.simplerunner.simplerunner;

import android.app.Activity;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    GestureDetector detector;
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 0 ;
    private int year, month, day;
    private StringBuffer date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detector = new GestureDetector(this, this);

        getGoogleFitAPI();
    }

    private void slideUp(){
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.top_in, R.anim.top_out);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float distance = e2.getY()-e1.getY();
        if(distance<-100){
            slideUp();
        }else{

        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果需要授權流程，請處理用戶的響應：
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                //readHistoryData();
                readData();
            }
        }
    }

    private void getGoogleFitAPI(){
        //創建一個FitnessOptions實例，聲明您的應用所需的Fit API數據類型和訪問權限：
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();
        //檢查用戶之前是否已授予必要的數據訪問權限，如果沒有，則啟動授權流程：
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        } else {
            //readHistoryData();
            readData();
        }
    }

    private void readData(){
        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        long total = dataSet.isEmpty()
                                ? 0
                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                        Log.i("TOTAL_STEP", total+"");
                    }
                });
    }
    /*
    private Task<DataReadResponse> readHistoryData(){
        DataReadRequest readRequest = queryFitnessData();

        return Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readData(readRequest)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        printData(dataReadResponse);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("onFailure", "There was a problem reading the data.", e);
                    }
                });
    }
    */

    private static void printData(DataReadResponse dataReadResult) {
        if(dataReadResult.getBuckets().size() > 0){
            Log.i("printData", "Number of returned buckets of DataSets is:" + dataReadResult.getBuckets().size());
            for(Bucket bucket : dataReadResult.getBuckets()){
                List<DataSet> dataSets = bucket.getDataSets();
                for(DataSet dataSet : dataSets){
                    dumpDataSet(dataSet);
                }
            }
        }
    }

    public static DataReadRequest queryFitnessData(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
        Log.i("Start", dateFormat.format(startTime));
        Log.i("End", dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        return readRequest;
    }

    private static void dumpDataSet(DataSet dataSet){
        Log.i("dumpDataSet", "Data returned for Data type: " + dataSet.getDataType().getName());
        java.text.DateFormat dateFormat = DateFormat.getTimeInstance();

        for(DataPoint dp: dataSet.getDataPoints()){
            Log.i("DataPoint", "Data points:");
            Log.i("DataPoint", "\tType: " + dp.getDataType().getName());
            Log.i("DataPoint", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i("DataPoint", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for(Field field : dp.getDataType().getFields()){
                Log.i("Field", "\tField: " + field.getName() + " Value: " + dp.getValue(field));
            }
        }
    }

    public void setDatePicker(View view) {
        DatePicker mDatePicker = null ;
        Calendar mCalendar = null ;
        mCalendar = Calendar.getInstance();
        year = mCalendar.get(Calendar.YEAR);
        month = mCalendar.get(Calendar.MONTH) + 1;
        day = mCalendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker = findViewById(R.id.datePicker);
        mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Toast.makeText(MainActivity.this,
                        year + "年" + (monthOfYear+1) + "月" + dayOfMonth + "日",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


}
