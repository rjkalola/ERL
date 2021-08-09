package com.app.erl.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erl.R;
import com.app.erl.databinding.ActivityContactUsBinding;
import com.app.erl.util.AppConstant;
import com.app.utilities.utils.StringHelper;

public class ContactUsActivity extends BaseActivity implements View.OnClickListener {
    private ActivityContactUsBinding binding;
    private Context mContext;
    private String mobileNumber, email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        mContext = this;

        binding.routCall.setOnClickListener(this);
        binding.routWhatsApp.setOnClickListener(this);
        binding.routEmail.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null) {
            mobileNumber = getIntent().getStringExtra(AppConstant.IntentKey.PHONE);
            email = getIntent().getStringExtra(AppConstant.IntentKey.EMAIL);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.routCall:
                if (!StringHelper.isEmpty(mobileNumber)) {
                    String phoneNumber = "tel:" + mobileNumber;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(phoneNumber));
                    startActivity(intent);
                }
                break;
            case R.id.routWhatsApp:
                if (!StringHelper.isEmpty(mobileNumber))
                    openWhatsApp(mobileNumber);
                break;
            case R.id.routEmail:
                if (!StringHelper.isEmpty(email)) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", email, null));
                    startActivity(Intent.createChooser(emailIntent, null));
                }
                break;
            case R.id.imgBack:
                finish();
                break;
        }
    }

    public void openWhatsApp(String mobileNumber) {
//        PackageManager pm = getPackageManager();
       /* try {
//            String toNumber = "xxxxxxxxxx"; // Replace with mobile phone number without +Sign or leading zeros, but with country code.
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + "" + mobileNumber + "?body=" + ""));
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "WhatsApp not Installed", Toast.LENGTH_LONG).show();

        }*/

        try {
            mobileNumber = mobileNumber.replace(" ", "").replace("+", "");
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(mobileNumber) + "@s.whatsapp.net");
            startActivity(sendIntent);
        } catch (Exception e) {

        }
    }
}
