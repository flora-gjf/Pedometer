package com.gjf.pedometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Observer;

/**
 * Created by guojunfu on 18/2/11.
 * <p>
 * 是否支持传感器：
 * getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
 */

public class StepCounterManager implements SensorEventListener {

    private static final int SENSER_TYPE_C = Sensor.TYPE_STEP_COUNTER;

    private static StepCounterManager instance;

    private final String TAG = "StepCounterManager";

    private SensorManager mSensorManager;
    private Sensor mStepCount;

    private float mCount;

    private StepCounterObservable mStepCounterObservable;
    private StepDetectorObservable mStepDetectorObservable;

    private StepCounterManager() {
        mSensorManager = (SensorManager) GlobalConfig.getAppContext().getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager == null) {
            Log.i(TAG, "StepCounterManager init error");
            return;
        }

        mStepCount = mSensorManager.getDefaultSensor(SENSER_TYPE_C);

        mStepCounterObservable = new StepCounterObservable();
        mStepDetectorObservable = new StepDetectorObservable();
    }

    public static StepCounterManager getInstance() {
        if (instance == null) {
            synchronized (StepCounterManager.class) {
                if (instance == null) {
                    instance = new StepCounterManager();
                }
            }
        }

        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void register() {
        if (mStepCount == null) {
            return;
        }

        String info = " mStepCount name = "
                + mStepCount.getName() + ", version = " + mStepCount.getVersion() + ", vendor = " + mStepCount.getVendor()
                + ", FifoMaxEventCount = " + mStepCount.getFifoMaxEventCount()
                + ", FifoReservedEventCount = " + mStepCount.getFifoReservedEventCount() + ", MinDelay = "
                + mStepCount.getMinDelay() + ", MaximumRange = " + mStepCount.getMaximumRange()
                + ", Power = " + mStepCount.getPower()
                + ", ReportingMode = " + mStepCount.getReportingMode() + ", Resolution = " + mStepCount.getResolution() + ", MaxDelay = " + mStepCount.getMaxDelay();


        Log.i(TAG, "StepCount info " + info);

        mSensorManager.registerListener(this, mStepCount, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unRegister() {
        mSensorManager.unregisterListener(this);
    }

    public void addStepCounterObserver(Observer observer) {
        mStepCounterObservable.addObserver(observer);
    }

    public void addStepDetectorObserver(Observer observer) {
        mStepDetectorObservable.addObserver(observer);
    }

    public void clearStepObserver() {
        mStepCounterObservable.deleteObservers();
    }

    public void clearStepDetectorObserver() {
        mStepDetectorObservable.deleteObservers();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void flush() {
        mSensorManager.flush(this);
    }

    private void setStepCount(float count) {
        mCount = count;
        mStepCounterObservable.sendChange(count);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != SENSER_TYPE_C) {
            return;
        }

        setStepCount(event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
