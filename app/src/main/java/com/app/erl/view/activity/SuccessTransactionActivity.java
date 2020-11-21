package com.app.erl.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erl.R;
import com.app.erl.databinding.ActivitySuccessTransactionBinding;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.viewModel.ManageOrderViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.StringHelper;
import com.telr.mobile.sdk.activty.WebviewActivity;
import com.telr.mobile.sdk.entity.response.status.StatusResponse;

public class SuccessTransactionActivity extends BaseActivity {
    private TextView mTextView;
    private ActivitySuccessTransactionBinding binding;
    private ManageOrderViewModel manageOrderViewModel;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_success_transaction);
        mContext = this;
        manageOrderViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageOrderViewModel.class);
        manageOrderViewModel.createView(this);
        manageOrderViewModel.mBaseResponse()
                .observe(this, mBaseResponse());

        binding.txtHome.setOnClickListener(v -> moveActivity(mContext, DashBoardActivity.class, true, true, null));

        Intent intent = getIntent();
        StatusResponse status = (StatusResponse) intent.getParcelableExtra(WebviewActivity.PAYMENT_RESPONSE);
        if (status != null && !StringHelper.isEmpty(status.getTrace())) {
            binding.txtHome.setClickable(false);
            binding.txtHome.setEnabled(false);
            manageOrderViewModel.storePaymentInfoRequest(AppConstant.ORDER_ID, 1, status.getTrace());
        }

//        TextView textView = (TextView) findViewById(R.id.text_payment_result);
//        textView.setText(textView.getText() + " : " + status.getTrace());
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Intent intent = getIntent();
//        StatusResponse status = (StatusResponse) intent.getParcelableExtra(WebviewActivity.PAYMENT_RESPONSE);
////        TextView textView = (TextView) findViewById(R.id.text_payment_result);
////        textView.setText(textView.getText() + " : " + status.getTrace());
//
//        if (status.getAuth() != null) {
//            status.getAuth().getStatus();   // Authorisation status. A indicates an authorised transaction. H also indicates an authorised transaction, but where the transaction has been placed on hold. Any other value indicates that the request could not be processed.
//            status.getAuth().getAvs();      /* Result of the AVS check:
//                                            Y = AVS matched OK
//                                            P = Partial match (for example, post-code only)
//                                            N = AVS not matched
//                                            X = AVS not checked
//                                            E = Error, unable to check AVS */
//            status.getAuth().getCode();     // If the transaction was authorised, this contains the authorisation code from the card issuer. Otherwise it contains a code indicating why the transaction could not be processed.
//            status.getAuth().getMessage();  // The authorisation or processing error message.
//            status.getAuth().getCa_valid();
//            status.getAuth().getCardcode(); // Code to indicate the card type used in the transaction. See the code list at the end of the document for a list of card codes.
//            status.getAuth().getCardlast4();// The last 4 digits of the card number used in the transaction. This is supplied for all payment types (including the Hosted Payment Page method) except for PayPal.
//            status.getAuth().getCvv();      /* Result of the CVV check:
//                                           Y = CVV matched OK
//                                           N = CVV not matched
//                                           X = CVV not checked
//                                           E = Error, unable to check CVV */
//            status.getAuth().getTranref(); //The payment gateway transaction reference allocated to this request.
//            Log.d("hany", status.getAuth().getTranref());
//            status.getAuth().getCardfirst6(); // The first 6 digits of the card number used in the transaction, only for version 2 is submitted in Tran -> Version
//
//            setTransactionDetails(status.getAuth().getTranref(), status.getAuth().getCardlast4());
//        }
//    }
//
//    private void setTransactionDetails(String ref, String last4) {
//        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("telr", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("ref", ref);
//        editor.putString("last4", last4);
//        editor.commit();
//    }

//    public void closeWindow(View view) {
//        this.finish();
//    }

    public Observer mBaseResponse() {
        return (Observer<BaseResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    binding.txtHome.setClickable(true);
                    binding.txtHome.setEnabled(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public void onBackPressed() {

    }
}
