package com.zibilal.data.worker;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import com.zibilal.dao.Contact;
import com.zibilal.data.file.FileHelper;
import com.zibilal.data.file.FileHelperException;
import com.zibilal.model.ContactModel;
import com.zibilal.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Bilal on 12/31/2015.
 */
public class DataGenerator {

    private static DataGenerator _instance;

    private DataGenerator() {
    }

    public static DataGenerator getInstance() {
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

    public void generateContactAndSave(Context context, String fileName, Subscriber<List<ContactModel>> subs) {
        Observable<List<ContactModel>> observable = Observable.create(subscriber -> {
            try {
                List<ContactModel> result = new ArrayList<>();
                byte[] bytes = FileHelper.getInstance().read(fileName);
                String str = new String(bytes);
                String[] splits = str.split("\r\n");
                int index=1;
                for (String s: splits) {
                    ContactModel model = new ContactModel();
                    String[] sp = s.split(" ");
                    String email="";
                    if (sp.length > 1) {
                        email = sp[0].toLowerCase() + "." + sp[1].toLowerCase() + "@gmail.com";
                    }
                    String phoneNumber = "+61" + StringUtils.getNumberString(15);
                    model.DisplayName=s;
                    model.PhoneNumber=phoneNumber;
                    model.EmailAddres=email;
                    Log.d("DataGenerator", model.toString());
                    saveContacts(context, index, model);
                    result.add(model);
                }
                subscriber.onNext(result);
                subscriber.onCompleted();
            } catch (FileHelperException e) {
                subscriber.onError(e);
            }
        });
        observable.subscribeOn(Schedulers.io());
        observable.observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(subs);
    }

    private void saveContacts(Context context, int index, ContactModel contactModel) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, index)
        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactModel.PhoneNumber).build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, index)
        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactModel.DisplayName)
        .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, index)
        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
        .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, contactModel.EmailAddres)
        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
        .build());

        ContentResolver contactAdder = context.getContentResolver();
        try {
            contactAdder.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }
}
