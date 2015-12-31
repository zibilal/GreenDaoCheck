package com.zibilal.data.worker;

import com.zibilal.dao.Contact;
import com.zibilal.model.ContactModel;
import com.zibilal.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;

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
            Random random = new Random();

            for(int i=0; i < len; i++) {

                Contact contact = new Contact(null,
                        StringUtils.getSaltString(80),
                        StringUtils.getSaltString(20),
                        StringUtils.getSaltString(20),
                        StringUtils.getSaltString(20),
                        StringUtils.getSaltString(20),
                        StringUtils.getSaltString(20),
                        random.nextInt(),
                        random.nextLong(),
                        new Date() );


            }
        });
    }
}
