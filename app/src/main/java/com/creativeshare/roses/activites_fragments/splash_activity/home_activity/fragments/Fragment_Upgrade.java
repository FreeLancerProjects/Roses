package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.roses.R;
import com.creativeshare.roses.tags.Tags;


public class Fragment_Upgrade extends Fragment {
private WebView mWebView;
    public static Fragment_Upgrade newInstance() {
        return new Fragment_Upgrade();

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upgrade_, container, false);
        mWebView =  view.findViewById(R.id.activity_main_webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(Tags.base_url);
        return view;
    }
}
