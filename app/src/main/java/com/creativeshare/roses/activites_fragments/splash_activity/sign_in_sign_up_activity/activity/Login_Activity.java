package com.creativeshare.roses.activites_fragments.splash_activity.sign_in_sign_up_activity.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.activites_fragments.splash_activity.sign_in_sign_up_activity.fragments.FragmentNewPassword;
import com.creativeshare.roses.activites_fragments.splash_activity.sign_in_sign_up_activity.fragments.Fragment_Language;
import com.creativeshare.roses.activites_fragments.splash_activity.sign_in_sign_up_activity.fragments.Fragment_Login;
import com.creativeshare.roses.activites_fragments.splash_activity.sign_in_sign_up_activity.fragments.Fragment_Signup;
import com.creativeshare.roses.activites_fragments.splash_activity.sign_in_sign_up_activity.fragments.Fragment_Upgrade;
import com.creativeshare.roses.activites_fragments.splash_activity.sign_in_sign_up_activity.fragments.FragmentForgotpassword;
import com.creativeshare.roses.language.Language;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.share.Common;
import com.creativeshare.roses.tags.Tags;

import io.paperdb.Paper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login_Activity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment_Login fragmentLogin;
    private Fragment_Signup fragmentSignup;
    private Fragment_Upgrade fragment_upgrade;

    private Fragment_Language fragment_language;

    private int fragment_counter = 0;
    private Preferences preferences;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String id;
    private String vercode;
    private FirebaseAuth mAuth;
    private Dialog dialog;
    private EditText verificationCodeEditText;
    private ProgressDialog dialo;
    private UserModel userModel;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private Runnable mUpdateResults;
    private FragmentForgotpassword fragment_forgotpass;
    private FragmentNewPassword fragmentnewpass;
private int type;

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(Language.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = Preferences.getInstance();
        Paper.init(this);
        CreateSignAlertDialog();
        authn();
        if (savedInstanceState == null) {
            fragmentManager = this.getSupportFragmentManager();

            if (preferences.isLanguageSelected(this))
            {
                if (preferences.getSession(this).equals(Tags.session_login))
                {
                    NavigateToHomeActivity();
                }else
                    {
                        DisplayFragmentLogin();

                    }

            }else
                {
                   DisplayFragmentLanguage();
                }

        }

        getDataFromIntent();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("sign_up"))
        {
            boolean isSign_in = intent.getBooleanExtra("sign_up",true);
            if (!isSign_in)
            {
                DisplayFragmentSignUp();

            }
        }
    }

    public void CreateSignAlertDialog() {
        dialog = new Dialog(this, R.style.CustomAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_login);
        LinearLayout ll = dialog.findViewById(R.id.ll);
        verificationCodeEditText=dialog.findViewById(R.id.edt_ver);
        ll.setBackgroundResource(R.drawable.custom_bg_login);
        Button confirm=dialog.findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vercode=verificationCodeEditText.getText().toString();
                if(TextUtils.isEmpty(vercode)){
                    verificationCodeEditText.setError(getResources().getString(R.string.field_req));
                }
                else {
                    Log.e("code",vercode);
                    verfiycode(vercode);

                }
            }
        });
    }
    private void authn() {

        mAuth= FirebaseAuth.getInstance();
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                //  super.onCodeSent(s, forceResendingToken);
                id=s;
                mResendToken=forceResendingToken;
                Log.e("authid",id);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) { ;
//phoneAuthCredential.getProvider();

                if(phoneAuthCredential.getSmsCode()!=null){
                    verificationCodeEditText.setText(phoneAuthCredential.getSmsCode());
                    siginwithcredental(phoneAuthCredential);}
                else {
                    siginwithcredental(phoneAuthCredential);
                }



            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("llll",e.getMessage());
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                //   super.onCodeAutoRetrievalTimeOut(s);
                Log.e("data",s);
                //   mUpdateResults.run();


            }
        };

    }
    private void verfiycode(String code) {

        if(id!=null){
            PhoneAuthCredential credential=PhoneAuthProvider.getCredential(id,code);

            siginwithcredental(credential);}
    }

    private void siginwithcredental(PhoneAuthCredential credential) {
        dialo = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialo.setProgressStyle(R.style.MyAlertDialogStyle);
        dialog.show();
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialo.dismiss();
                dialog.dismiss();
                if(task.isSuccessful()){

                    // activity.NavigateToHomeActivity();
                    //  mAuth.signOut();

                    mAuth.signOut();
                    if(type==0){
                    NavigateToHomeActivity();}
                    else {
                        DisplayFragmentNewPass(userModel);
                    }
                }

            }
        });
    }

    public void sendverficationcode(String phone, String phone_code, UserModel userModel,int type) {
        dialog.show();
        this.userModel=userModel;
        this.type=type;
        Log.e("kkk",phone_code+phone);
        mUpdateResults = new Runnable() {
            public void run() {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_code+phone,10, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,  mCallbacks);
            }
        };
        mUpdateResults.run();

    }
    private void DisplayFragmentLanguage()
    {

        fragment_language = Fragment_Language.newInstance();

        if (fragment_language.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_language).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_language, "fragment_language").addToBackStack("fragment_language").commit();
        }
    }

    public void DisplayFragmentLogin()
    {

        fragment_counter += 1;
            fragmentLogin = Fragment_Login.newInstance();

        if (fragmentLogin.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentLogin).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentLogin, "fragmentLogin").addToBackStack("fragmentLogin").commit();
        }
    }
    public void DisplayFragmentForgotPass()
    {

        fragment_counter += 1;
        if (fragment_forgotpass == null) {
            fragment_forgotpass = FragmentForgotpassword.newInstance();
        }
        if (fragment_forgotpass.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_forgotpass).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_forgotpass, "fragment_forgotpass").addToBackStack("fragment_forgotpass").commit();
        }
    }
    public void DisplayFragmentNewPass(UserModel userModel)
    {

        fragment_counter += 1;
        if (fragmentnewpass == null) {
            fragmentnewpass = FragmentNewPassword.newInstance(userModel);
        }
        if (fragmentnewpass.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentnewpass).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentnewpass, "fragmentnewpass").addToBackStack("fragmentnewpass").commit();
        }
    }
    public void DisplayFragmentSignUp()
    {

        fragment_counter += 1;

        if (fragmentSignup == null) {
            fragmentSignup = Fragment_Signup.newInstance();
        }
        if (fragmentSignup.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentSignup).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentSignup, "fragmentSignup").addToBackStack("fragmentSignup").commit();
        }
    }
    public void DisplayFragmentUpgrade()
    {

        fragment_counter += 1;

        if (fragment_upgrade == null) {
            fragment_upgrade = Fragment_Upgrade.newInstance();
        }
        if (fragment_upgrade.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_upgrade).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_upgrade, "fragment_upgrade").addToBackStack("fragment_upgrade").commit();
        }
    }

    public void NavigateToHomeActivity()
    {
        preferences = Preferences.getInstance();
        preferences.create_update_userdata(this,userModel);
        Intent intent = new Intent(Login_Activity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }



    public void Back() {
        if (fragment_counter == 1) {
            finish();
        } else {
            fragment_counter -= 1;
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        Back();
    }


    public void RefreshActivity(String selected_language) {
        Log.e("lang",selected_language);
        Paper.book().write("lang",selected_language);
        preferences.create_update_language(this,selected_language);
        preferences.setIsLanguageSelected(this);
        Language.setNewLocale(this,selected_language);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
