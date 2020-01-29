package com.creativeshare.roses.activites_fragments.splash_activity.sign_in_sign_up_activity.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.activites_fragments.splash_activity.sign_in_sign_up_activity.activity.Login_Activity;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.share.Common;
import com.creativeshare.roses.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentNewPassword extends Fragment {
    private Button btn_login;

    private EditText edt_password;
    final static private String Tag = "user_model";

    private String current_language;
    private HomeActivity homeActivity;
    private Login_Activity activity;
private UserModel userModel;
    public static FragmentNewPassword newInstance(UserModel userModel) {
        FragmentNewPassword fragmentNewPassword=new FragmentNewPassword();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Tag, userModel);

        fragmentNewPassword.setArguments(bundle);
        return fragmentNewPassword;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newpassword, container, false);
        initView(view);
        return view;
    }

    private void initView(final View view) {
        userModel = (UserModel) getArguments().getSerializable(Tag);

        activity = (Login_Activity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        btn_login = view.findViewById(R.id.btn_login);

        edt_password = view.findViewById(R.id.edt_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

    }

    private void checkData() {
        String m_password = edt_password.getText().toString().trim();

        if (
                !TextUtils.isEmpty(m_password)
        ) {
            edt_password.setError(null);
            Common.CloseKeyBoard(activity, edt_password);

            newpass(m_password);

        } else {


            if (TextUtils.isEmpty(m_password)) {
                edt_password.setError(getString(R.string.field_req));
            } else {
                edt_password.setError(null);

            }


        }
    }


    private void newpass(String m_password) {
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .NewPass(m_password,userModel.getId()+"")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            Log.e("fddddd",response.code()+"");

                            activity.DisplayFragmentLogin();

                        }  else {
                           // Common.CreateSignAlertDialog(activity, getString(R.string.inc_phone_pas));

                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }


}
