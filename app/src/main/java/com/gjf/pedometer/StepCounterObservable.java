package com.gjf.pedometer;

import java.util.Observable;

/**
 * Created by guojunfu on 18/2/11.
 */

public class StepCounterObservable extends Observable {

    public void sendChange(float progress) {
        setChanged();
        notifyObservers(progress);
    }
}
