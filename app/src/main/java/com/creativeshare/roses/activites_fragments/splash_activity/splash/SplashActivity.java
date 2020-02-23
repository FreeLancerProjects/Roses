package com.creativeshare.roses.activites_fragments.splash_activity.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.roses.activites_fragments.sign_in_sign_up_activity.activity.Login_Activity;
import com.creativeshare.roses.language.Language;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.tags.Tags;

public class SplashActivity extends AppCompatActivity {

    private ConstraintLayout cons_spalash;
    private Preferences preferences;
    private String session;
    private Animation animation;

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(Language.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        preferences=Preferences.getInstance();
        session=preferences.getSession(this);
        cons_spalash =findViewById(R.id.cons);

        animation= AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade);

        cons_spalash.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(session.equals(Tags.session_login)){
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
               startActivity(intent);
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, Login_Activity.class);
                    startActivity(intent);
                }
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
