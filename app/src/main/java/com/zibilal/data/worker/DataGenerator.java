package com.zibilal.data.worker;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import com.zibilal.data.file.FileHelper;
import com.zibilal.data.file.FileHelperException;
import com.zibilal.model.ContactModel;
import com.zibilal.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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

    public void generateContactAndSave(Context context, String contactsName, String companyName, Subscriber<List<ContactModel>> subs) {
        Observable<List<ContactModel>> observable = Observable.create(subscriber -> {
            try {
                List<ContactModel> result = new ArrayList<>();
                byte[] bytes = FileHelper.getInstance().read(contactsName);
                byte[] compnayBytes = FileHelper.getInstance().read(companyName);
                String str = new String(bytes);
                String str2 = new String(compnayBytes);
                String[] splits = str.split("\r\n");
                String[] companySplits = str2.split("\r\n");
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
                    model.EmailAddress =email;
                    model.CompanyName = companySplits[StringUtils.getRandomInt(companySplits.length)];
                    Log.d("DataGenerator", model.toString());
                    saveContacts(context, model);
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

    public void insertContact(Context context,  int index) {

        String DisplayName = "Name" + index;
        String MobileNumber = "123456" + index;
        String HomeNumber = "564564" + index;
        String WorkNumber = "789456" + index;
        String emailID = "Name" + index + "@gmail.com";
        String company = "Microsoft";
        String jobTitle = "Marketing";
        if (index % 2 == 0) {
            MobileNumber = "654321" + index;
            HomeNumber = "456456" + index;
            WorkNumber = "456789" + index;
            emailID = "Name" + index + "@yahoo.com";
            company = "Amazon";
            jobTitle = "Marketing Associate";
        }

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (DisplayName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName).build());
        }

        //------------------------------------------------------ Mobile Number
        if (MobileNumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        //------------------------------------------------------ Home Numbers
        if (HomeNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }

        //------------------------------------------------------ Work Numbers
        if (WorkNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Email
        if (emailID != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Organization
        if (!company.equals("") && !jobTitle.equals("")) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }

        // Asking the Contact provider to create a new contact
        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveContacts(Context context, ContactModel contactModel) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account acc : accounts){
            Log.d("Data Generator", "account name = " + acc.name + ", type = " + acc.type);
        }

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, accounts[0].type)
        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, accounts[0].name).build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactModel.PhoneNumber).build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactModel.DisplayName)
        .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
        .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, contactModel.EmailAddress)
        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
        .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
        .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, contactModel.CompanyName)
        .build());

        ContentResolver contactAdder = context.getContentResolver();
        try {
            ContentProviderResult[] r =  contactAdder.applyBatch(ContactsContract.AUTHORITY, ops);
            Log.d("DataGenerator", "Content result: " + r.length);
            if (r == null || r.length == 0) {
                Log.d("DataGenerator" , "Insertion is failed");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    public void updateContactsWithCompanyName(Context context, String selectionClause, String[] selectionArgs) {
        ContentResolver cr = context.getContentResolver();

        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, selectionClause, selectionArgs, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        if (cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {

            }
        } else {
            Log.e("DataGenerator", "Failed get contacts.");
        }
    }

    private void deleteAllContacts(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while(cursor.moveToNext()) {
            String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
            cr.delete(uri, null, null);
            Log.d("DataGenerator", "Contact is deleted");
        }
    }

    public void deleteAllContacts(Context context, Subscriber<Integer> subs) {
        Observable<Integer> observable = Observable.create(subscriber -> {
            try {
                deleteAllContacts(context);
                subscriber.onNext(1);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subs);
    }
}
