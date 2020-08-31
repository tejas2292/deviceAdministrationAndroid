package com.example.deviceadmin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private SubscriptionManager mSubscriptionManager;

    public static boolean isMultiSimEnabled = false;
    public static String defaultSimName;

    public static List<SubscriptionInfo> subInfoList;
    public static ArrayList<String> Numbers;


    DevicePolicyManager devicePolicyManager;
    ComponentName demoDeviceAdmin;
    Button b1, b2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = findViewById(R.id.click);
        b2 = findViewById(R.id.clickTo);

        context = this;
        Numbers = new ArrayList<String>();
        mSubscriptionManager = SubscriptionManager.from(context);
        GetCarriorsInformation();

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        demoDeviceAdmin = new ComponentName(this, DemoDeviceAdmin.class);
        Log.e("DeviceAdminActive==", "" + demoDeviceAdmin);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);// adds new device administrator
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, demoDeviceAdmin);//ComponentName of the administrator component.
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "Disable app");//dditional explanation
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicePolicyManager.removeActiveAdmin(demoDeviceAdmin);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void GetCarriorsInformation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        subInfoList = mSubscriptionManager.getActiveSubscriptionInfoList();
        if (subInfoList.size() > 1) {
            isMultiSimEnabled = true;
        }
        for (SubscriptionInfo subscriptionInfo : subInfoList) {
            Numbers.add(subscriptionInfo.getNumber());
            Toast.makeText(context, ""+Numbers, Toast.LENGTH_SHORT).show();
        }
    }

}
