package com.zibilal.data.file;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.zibilal.greendaocheck.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Bilal on 1/4/2016.
 */
public class FileHelper {

    public static final int WRITE_SUCCESS=101;
    public static final int READ_SUCCESS=102;

    private String mDirectory;
    private Context mContext;

    private static FileHelper _instance;

    private FileHelper(Context context) {
        mContext = context;
        mDirectory = mContext.getString(R.string.app_name);
    }

    public static void initInstance(Context context) {
        _instance = new FileHelper(context);
    }

    public static FileHelper getInstance() {
        if (_instance == null) throw new IllegalStateException("Please call initInstance first.");

        return _instance;
    }

    public  void read(Subscriber<byte[]> subs, final String fileName) {
        Observable<byte[]> observable = Observable.create(subscriber -> {
            try {
                byte[] result = read(fileName);
                subscriber.onNext(result);
                subscriber.onCompleted();
            } catch (FileHelperException e) {
                subscriber.onError(e);
            }
        });
        observable.observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(subs);
    }

    public void write(Subscriber<Integer> subs, byte[] bytes) {
        Observable<Integer> observable = Observable.create(subscriber -> {
            try {
                write(bytes);
                subscriber.onNext(WRITE_SUCCESS);
                subscriber.onCompleted();
            } catch (FileHelperException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subs);
    }

    public byte[] read(String fileName) throws FileHelperException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File file;
            BufferedInputStream inputStream = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mDirectory);
                if (!file.exists())
                    file.mkdir();
                inputStream = new BufferedInputStream(new FileInputStream(new File(file, fileName)));
                byte[] buffer = new byte[512];
                int len = inputStream.read(buffer);

                while(len > 0) {
                    Log.d("FileHelper", new String(buffer));
                    baos.write(buffer, 0, len);
                    len = inputStream.read(buffer, 0, len);
                }

                return baos.toByteArray();
            } catch (Exception e) {
                throw new FileHelperException(e.getMessage());
            } finally {
                try {
                    if (inputStream!=null)
                        inputStream.close();
                    baos.close();
                } catch (IOException e) {

                }
            }

        } else {
            throw new FileHelperException("External storage state is not mounted.");
        }
    }

    public void write(byte[] bytes) throws FileHelperException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file;
            BufferedOutputStream outputStream=null;
            try {
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mDirectory);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File writeFile = new File(file, "bc_text.txt");
                outputStream = new BufferedOutputStream(new FileOutputStream(writeFile));
                outputStream.write(bytes);
                outputStream.flush();
            } catch(Exception e) {
                throw new FileHelperException(e.getMessage());
            } finally {
                try {
                    if (outputStream != null)
                        outputStream.close();
                } catch (IOException e) {

                }
            }
        } else {
            throw new FileHelperException("External storage state is not mounted.");
        }
    }
}
