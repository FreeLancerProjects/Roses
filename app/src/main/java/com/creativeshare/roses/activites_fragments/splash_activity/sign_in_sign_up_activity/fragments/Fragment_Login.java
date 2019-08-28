package com.creativeshare.roses.activites_fragments.splash_activity.sign_in_sign_up_activity.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.sign_in_sign_up_activity.activity.Login_Activity;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.share.Common;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Login extends Fragment {
    private Button btn_login;
    private TextView tv_skip, tv_sign_up;
    private EditText edt_username, edt_password;
    private Login_Activity activity;
    private Preferences preferences;

    public static Fragment_Login newInstance() {

        return new Fragment_Login();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        return view;
    }

    private void initView(final View view) {
        activity = (Login_Activity) getActivity();
        btn_login = view.findViewById(R.id.btn_login);
        tv_skip = view.findViewById(R.id.tv_skip);
        tv_sign_up = view.findViewById(R.id.tv_sign_up);
        edt_username = view.findViewById(R.id.edt_email);
        edt_password = view.findViewById(R.id.edt_password);

        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentSignUp();
            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent intent = new Intent(activity, HomeActivity.class);
                //startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  checkData();
            }
        });
    }

  /*  private void checkData() {
        String m_username = edt_username.getText().toString().trim();
        String m_password = edt_password.getText().toString().trim();

        if (!TextUtils.isEmpty(m_username) &&
                !TextUtils.isEmpty(m_password)
        ) {
            edt_username.setError(null);
            edt_password.setError(null);
            Common.CloseKeyBoard(activity, edt_username);

            Login(m_username, m_password);

        } else {


            if (TextUtils.isEmpty(m_username)) {
            //    edt_username.setError(getString(R.string.field_req));
            } else {
                edt_username.setError(null);

            }


            if (TextUtils.isEmpty(m_password)) {
              //  edt_password.setError(getString(R.string.field_req));
            } else {
                edt_password.setError(null);

            }
        }
    }


    private void Login(String m_username, String m_password) {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService()
                .SignIn( m_username, m_password)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null) {

                            preferences = Preferences.getInstance();
                            preferences.create_update_userdata(activity,response.body());
                            activity.NavigateToHomeActivity();
                        } else if (response.code() == 404) {
                            Common.CreateSignAlertDialog(activity,getString(R.string.inc_em_pas));
                        } else {
                            Common.CreateSignAlertDialog(activity,getString(R.string.inc_em_pas));

                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }


*/
}
