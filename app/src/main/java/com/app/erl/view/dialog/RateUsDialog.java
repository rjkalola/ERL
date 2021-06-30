package com.app.erl.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.app.erl.R;
import com.app.erl.callback.OnSubmitRateListener;
import com.app.erl.callback.SelectTimeListener;
import com.app.erl.databinding.DialogRateUsBinding;
import com.app.erl.databinding.DialogSelectHourMinuteBinding;
import com.app.utilities.utils.StringHelper;
import com.app.utilities.utils.ToastHelper;

import java.util.Calendar;
import java.util.Locale;

public class RateUsDialog extends DialogFragment {
    private DialogRateUsBinding binding;
    private static Context context;
    private AlertDialog dialog;
    private static OnSubmitRateListener listener;
    private static int orderId;

    public RateUsDialog() {
    }

    public static RateUsDialog newInstance(Context mContext, int oId, OnSubmitRateListener l) {
        RateUsDialog frag = new RateUsDialog();
        context = mContext;
        listener = l;
        orderId = oId;
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFragmentStyle);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context, R.style.MyDialogFragmentStyle);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_rate_us, null);
        binding = DataBindingUtil.bind(view);

        binding.txtSubmit.setOnClickListener(v -> {
            if (binding.ratingBar.getRating() > 0) {
                if (listener != null)
                    listener.onSubmitRate(orderId, binding.ratingBar.getRating(), binding.edtFeedback.getText().toString().trim());
                dismiss();
            } else {
                ToastHelper.normal(context, context.getString(R.string.error_empty_rating), Toast.LENGTH_SHORT, false);
            }
        });

        ad.setView(view);
        dialog = ad.create();
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
