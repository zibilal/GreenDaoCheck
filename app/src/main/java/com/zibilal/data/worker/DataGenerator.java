package com.zibilal.data.worker;

import com.zibilal.model.ContactModel;

import java.util.List;

import rx.Observable;
import rx.Subscriber;


/**
 * Created by Bilal on 12/31/2015.
 */
public class DataGenerator {

    private static DataGenerator _instance;

    private DataGenerator() {
    }

    private static DataGenerator getInstance() {
        if (_instance == null) {
            _instance = new DataGenerator();
        }
        return _instance;
    }

    public void generateContact(int len, Subscriber<List<ContactModel>> subs) {
        Observable<List<ContactModel>> observable = Observable.create(subscriber -> {

        });
    }
}
