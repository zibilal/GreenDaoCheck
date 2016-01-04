package com.zibilal.greendaocheck;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zibilal.data.file.FileHelper;
import com.zibilal.data.file.FileHelperException;
import com.zibilal.data.worker.DataGenerator;
import com.zibilal.model.ContactModel;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG=MainActivity.class.getSimpleName();
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION=112;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION=113;
    private String mMessage="";
    @Bind(R.id.text_view)
    TextView mTextView;
    @OnClick(R.id.heavy_lifting_button) void onHeavyLiftingButtonClick() {
        String msg = "start: " + new Date().toString();
        mTextView.setText(msg);
        heavyLiftingFunction();
        msg = "end: " + new Date().toString();
        mTextView.setText(msg);
    }

    @OnClick(R.id.file_save) void onFileSaveClick() {
        String msg = "start: " + new Date().toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 1. Permission check
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                FileHelper.getInstance().write(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Save file completed.");
                        Toast.makeText(MainActivity.this, "Save file is completed.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "Exception is occured: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Toast.makeText(MainActivity.this, "Success: " + integer, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Success : " + integer);
                    }
                }, msg.getBytes());
            } else {
                mMessage = msg;
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
            }

        } else {
            FileHelper.getInstance().write(new Subscriber<Integer>() {
                @Override
                public void onCompleted() {
                    Log.d(TAG, "Save file completed.");
                    Toast.makeText(MainActivity.this, "Save file is completed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(MainActivity.this, "Exception is occured: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                @Override
                public void onNext(Integer integer) {
                    Toast.makeText(MainActivity.this, "Success: " + integer, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Success : " + integer);
                }
            }, msg.getBytes());
        }
    }

    Subscriber<List<ContactModel>> mSubscriber = new Subscriber<List<ContactModel>>() {
        @Override
        public void onCompleted() {
            Toast.makeText(MainActivity.this, "Generator completed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Exception is occured: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<ContactModel> contactModels) {
            Toast.makeText(MainActivity.this, "Generated data size: " + contactModels.size(), Toast.LENGTH_SHORT).show();
        }
    };

    @OnClick(R.id.gen_contacts) public void onGenerateContactsClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheckReadExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionCheckReadContact = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
            int permissionCheckWriteContact = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
            int permissionCheckGetAccounts = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
            if (permissionCheckReadExternalStorage == PackageManager.PERMISSION_GRANTED &&
                    permissionCheckReadContact == PackageManager.PERMISSION_GRANTED &&
                    permissionCheckWriteContact == PackageManager.PERMISSION_GRANTED &&
                    permissionCheckGetAccounts == PackageManager.PERMISSION_GRANTED) {
                DataGenerator.getInstance().generateContactAndSave(MainActivity.this.getApplicationContext(), "contacts_name.txt", mSubscriber);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS},
                        REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
            }
        } else {
            DataGenerator.getInstance().generateContactAndSave(MainActivity.this.getApplicationContext(),"contacts_name.txt", mSubscriber);
        }
    }

    private void readFile() {
        String fileName="contacts_name.txt";
        FileHelper.getInstance().read(new Subscriber<byte[]>() {
            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this, "Read file is finished.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(byte[] bytes) {
                String str = new String(bytes);
                Log.d(TAG, "Str: " + str);
                String[] displayNames = str.split("\r\n");
                for (String s : displayNames) {
                    Log.d(TAG, "S: " + s);
                }
            }
        }, fileName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FileHelper.getInstance().write(new Subscriber<Integer>() {
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "Save file completed.");
                            Toast.makeText(MainActivity.this, "Save file is completed.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(MainActivity.this, "Exception is occured: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Integer integer) {
                            Toast.makeText(MainActivity.this, "Success: " + integer, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Success : " + integer);
                        }
                    }, mMessage.getBytes());
                }
                break;
            case REQUEST_READ_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    DataGenerator.getInstance().generateContactAndSave(MainActivity.this.getApplicationContext(),"contacts_name.txt", mSubscriber);
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show());
    }

    private void heavyLiftingFunction() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
