package com.prathigram.shopbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class AboutUs extends AppCompatActivity {
    //Initialize variable
    DrawerLayout drawerLayout;
    LinearLayout contentView;
    static final float END_SCALE = 0.6f;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about_us);
            //assign variable
            drawerLayout = findViewById(R.id.drawer_layout);
            contentView =(LinearLayout) findViewById(R.id.content);
        }
        public void ClickMenu(View view){
            //Open Drawer
            MainActivity.openDrawer(drawerLayout);
            animateNavigationDrawer();
        }
    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        drawerLayout.setScrimColor(Color.RED);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

        public void ClickLogo(View view){
            //close drawer
            MainActivity.closeDrawer(drawerLayout);
        }

        public void ClickHome(View view){
            //Redirect Activity to home
            MainActivity.redirectActivity(this, MainActivity.class);
        }

        public void ClickDashboard(View view){
            //Redirect to Dashboard
            MainActivity.redirectActivity(this, Retailers.class);
        }

        public void ClickAboutUs(View view){
            //Recreate View
            recreate();
        }

        public void ClickLogout(View view){
            MainActivity.logout(this);
        }

        @Override
        protected void onPause() {
            super.onPause();
            //Close Drawer
            MainActivity.closeDrawer(drawerLayout);

        }
    }