package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.share.Common;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_More extends Fragment {
    private LinearLayout ll_lang, ll_terms, ll_about, ll_contact, ll_register_as_a_company, ll_logout,ll_bank, ll_edit_profile;
    private HomeActivity homeActivity;
    private Preferences preferences;
    private UserModel userModel;
    private String current_language;
    private String [] language_array;
    public static Fragment_More newInstance() {
        Fragment_More fragment = new Fragment_More();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        initview(view);
        return view;
    }

    private void initview(View view) {
        homeActivity = (HomeActivity) getActivity();
        Paper.init(homeActivity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        language_array = new String[]{"English","العربية"};

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        ll_lang = view.findViewById(R.id.ll_lang);
        ll_terms = view.findViewById(R.id.ll_terms);
        ll_about = view.findViewById(R.id.ll_about);
        ll_contact=view.findViewById(R.id.ll_contact);
        ll_register_as_a_company = view.findViewById(R.id.ll_register_as_a_company);
        ll_logout = view.findViewById(R.id.ll_logout);
        ll_edit_profile =view.findViewById(R.id.ll_edit_profile);
ll_bank=view.findViewById(R.id.ll_bank);
        ll_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayFragmentTerms_Condition();
            }
        });
        ll_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayFragmentabout();
            }
        });
        ll_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayFragmentContactUS();
            }
        });
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel == null) {
                    Common.CreateUserNotSignInAlertDialog(homeActivity);
                } else {
                    homeActivity.Logout();
                }
            }
        });
        ll_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateLanguageDialog();
            }
        });
        ll_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayFragmentBankAccount();
            }
        });
        ll_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userModel!=null){
                Log.e("llll","edit");
                homeActivity.DisplayFragmentEditprofile();}
                else {
                    Common.CreateUserNotSignInAlertDialog(homeActivity);
                }
            }
        });

        ll_register_as_a_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayFragmentUgrade();
            }
        });
    }
    private void CreateLanguageDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(homeActivity)
                .setCancelable(true)
                .create();

        View view  = LayoutInflater.from(homeActivity).inflate(R.layout.dialog_language,null);
        Button btn_select = view.findViewById(R.id.btn_select);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        final NumberPicker numberPicker = view.findViewById(R.id.numberPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(language_array.length-1);
        numberPicker.setDisplayedValues(language_array);
        numberPicker.setWrapSelectorWheel(false);
        if (current_language.equals("ar"))
        {
            numberPicker.setValue(2);

        }else if (current_language.equals("en"))
        {
            numberPicker.setValue(0);

        }else
        {
            numberPicker.setValue(1);
        }
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int pos = numberPicker.getValue();
                if (pos == 0)
                {
                    homeActivity.RefreshActivity("en");
                }else if (pos ==1)
                {
                    homeActivity.RefreshActivity("ar");

                }


            }
        });




        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

      //  dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setView(view);
        dialog.show();
    }

}
