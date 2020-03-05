package com.creativeshare.roses.activites_fragments.home_activity.fragments.fragmnet_more;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.share.Common;
import com.creativeshare.roses.tags.Tags;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Edit_profile extends Fragment {
    private HomeActivity homeActivity;
    private Preferences preferences;
    private String current_lang;
    private UserModel userModel;
    private CircleImageView im_profile;
    private CountryCodePicker countryCodePicker;
    private EditText edt_name, edt_phone, edt_pass;
    private Button bt_save;
    private ImageView back;
    private final int IMG1 = 1;
    private Uri uri = null;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;

    public static Fragment_Edit_profile newInstance() {
        Fragment_Edit_profile fragment_edit_profile = new Fragment_Edit_profile();

        return fragment_edit_profile;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initview(view);
        updateprofile();
        return view;
    }

    private void updateprofile() {
        if (userModel != null) {

            Picasso.with(homeActivity).load(Uri.parse(Tags.IMAGE_URL + userModel.getLogo())).fit().placeholder(
                    R.drawable.logo
            ).into(im_profile);
            edt_name.setText(userModel.getName());
            edt_phone.setText(userModel.getPhone());
            countryCodePicker.setCountryForPhoneCode(Integer.parseInt(userModel.getPhone_code()));
        }
    }

    private void initview(View view) {
        Log.e("lll", "llllmnn");
        homeActivity = (HomeActivity) getActivity();
        Paper.init(homeActivity);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        im_profile = view.findViewById(R.id.image);
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        back = view.findViewById(R.id.arrow);

        // edt_location = view.findViewById(R.id.edt_loc);
        // edt_commercial = view.findViewById(R.id.edt_commercial);
        countryCodePicker = view.findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(edt_phone);
        edt_pass = view.findViewById(R.id.edt_password);
        bt_save = view.findViewById(R.id.bt_save);
        if (current_lang.equals("en")) {

            back.setRotation(180);
        }
        im_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check_ReadPermission(IMG1);

            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkdata();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.Back();
            }
        });
    }

    private void Check_ReadPermission(int img_req) {
        if (ContextCompat.checkSelfPermission(homeActivity, read_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(homeActivity, new String[]{read_permission}, img_req);
        } else {
            select_photo(img_req);
        }
    }

    private void select_photo(int img1) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            if (img1 == 2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            if (img1 == 2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }

        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent, img1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG1 && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.getData();
            Picasso.with(homeActivity).load(uri).fit().into(im_profile);

            // UpdateImage(uri);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG1) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    select_photo(IMG1);
                } else {
                    Toast.makeText(homeActivity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void checkdata() {
        String name = edt_name.getText().toString();
        String phone = edt_phone.getText().toString();
        String phonecode = countryCodePicker.getSelectedCountryCode();
        //String city = edt_location.getText().toString();

        //String coomericial = edt_commercial.getText().toString();
        String pass = edt_pass.getText().toString();
        if (name.isEmpty() || phone.isEmpty() || !countryCodePicker.isValidFullNumber() || pass.isEmpty() || pass.length() < 6) {
            if (name.isEmpty()) {
                edt_name.setError(getResources().getString(R.string.field_req));
            }

            if (phone.isEmpty()) {
                edt_phone.setError(getResources().getString(R.string.field_req));

            }
            if (!countryCodePicker.isValidFullNumber()) {
                edt_phone.setError(getResources().getString(R.string.inc_phone));
            }


            if (pass.isEmpty()) {
                edt_pass.setError(getResources().getString(R.string.field_req));
            }
            if (pass.length() < 6) {
                edt_pass.setError(getResources().getString(R.string.inc_pass));
            }
        } else {
            updateprofile(name, phone, phonecode, pass, uri);
        }

    }

    private void updateprofile(String name, String phone, String phonecode, String pass, Uri uri) {
        final Dialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        if (uri == null && userModel.getLogo() != null) {
            uri = Uri.parse(Tags.IMAGE_URL + userModel.getLogo());

        }


        // Log.e("data",name+" "+phone+" "+phonecode+" "+pass+" "+uri.toString());
        if (uri != null) {
            try {

                RequestBody token_part = Common.getRequestBodyText(userModel.getId() + "");
                RequestBody name_part = Common.getRequestBodyText(name);
                RequestBody phone_part = Common.getRequestBodyText(phone);
                RequestBody phone_code_part = Common.getRequestBodyText(phonecode.replace("+", "00"));
                RequestBody pass_part = Common.getRequestBodyText(pass);
                MultipartBody.Part avatar_part = Common.getMultiPart(homeActivity, uri, "logo");
                Api.getService(Tags.base_url)
                        .udateprofile(token_part, name_part, phone_part, phone_code_part, pass_part, avatar_part)
                        .enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                                dialog.dismiss();

                                if (response.isSuccessful()) {

                                    if (response.body() != null) {
                                        Toast.makeText(homeActivity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                                        userModel = response.body();
                                        preferences.create_update_userdata(homeActivity, userModel);
                                        updateprofile();

                                    }

                                } else {
                                    Common.CreateSignAlertDialog(homeActivity, getString(R.string.something));
                                }

                            }

                            @Override
                            public void onFailure(Call<UserModel> call, Throwable t) {
                                try {
                                    dialog.dismiss();
                                    Log.e("Error", t.getMessage());
                                    Toast.makeText(homeActivity, R.string.failed, Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                }
                            }
                        });
            } catch (
                    Exception e) {
            }
        } else {
            Api.getService(Tags.base_url).updateprofile(userModel.getId() + "", name, phone, phonecode.replace("+", "00"), pass).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(homeActivity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                        userModel = response.body();
                        preferences.create_update_userdata(homeActivity, userModel);
                        updateprofile();

                        // Common.CreateSignAlertDialog(homeActivity, getResources().getString(R.string.suc));

                        // edt_pass.setText("");
                        updateprofile();
                    } else {

                        try {
                            Toast.makeText(homeActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }

                }
            });
        }
    }


}
