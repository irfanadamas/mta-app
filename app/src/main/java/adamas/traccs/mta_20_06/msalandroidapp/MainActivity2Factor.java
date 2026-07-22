// Copyright (c) Microsoft Corporation.
// All rights reserved.
//
// This code is licensed under the MIT License.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files(the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions :
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package adamas.traccs.mta_20_06.msalandroidapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import adamas.traccs.mta_20_06.MainActivity;
import adamas.traccs.mta_20_06.R;

public class MainActivity2Factor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnFragmentInteractionListener{

    enum AppFragment {
        SingleAccount,
        MultipleAccount,
        B2C
    }

    private AppFragment mCurrentFragment;

    private ConstraintLayout mContentMain;
    private boolean menu_displayed=false;
    private boolean dashboard_displayed=false;
    private void setupActionBar() {

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(false);

            try {
                String Title = actionBar.getTitle().toString();
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                actionBar.setCustomView(getLayoutInflater().inflate(R.layout.action_bar_home2, null),
                        new ActionBar.LayoutParams(
                                ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT,
                                Gravity.CENTER
                        )
                );
                TextView textviewTitle = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_textview);
                textviewTitle.setGravity(Gravity.CENTER);
                textviewTitle.setText(Title);
                final View imageMenu=actionBar.getCustomView().findViewById(R.id.imageMenu_view);
                // imageMenu.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(menu_displayed==false) {
                            menu_displayed=true;
                            set_main_menu(v.getContext());
                        }
                    }
                });

                TextView txtExit= actionBar.getCustomView().findViewById(R.id.txtExit);
                // txtExit.setVisibility(View.GONE);
                //  actionBar.setDisplayHomeAsUpEnabled(true);
                // imageMenu.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                txtExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onBackPressed();
                       if(1==1) return;
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(MainActivity2Factor.this).inflate(R.layout.rl_exit_confirmation, viewGroup, false);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2Factor.this);
                        builder.setView(dialogView);
                        final AlertDialog alertDialog = builder.create();
                        Button dialogButtonNo =  dialogView.findViewById(R.id.btnNo);
                        // if button is clicked, close the custom dialog
                        dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        Button dialogButtonYes =  dialogView.findViewById(R.id.btnYes);
                        // if button is clicked, close the custom dialog
                        dialogButtonYes.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View v) {
                                try{
                                    finish();
                                  //  new MainActivity.MyAsyncClass_RemoveSession().execute(UserName);
                                }catch(Exception ex){}
                            }
                        });
                        alertDialog.show();

                    }
                });

                textviewTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  onBackPressed();
                    }
                });


            } catch (Exception ex) { }
        }
    }
    public void set_main_menu(Context view) {
        final Dialog dialog = new Dialog(view, R.style.CustomDialog);
        dialog.setContentView(R.layout.main_dairy_menu);
        //dialog.setTitle("My Dashboard");
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                menu_displayed=false;
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.TOP | Gravity.RIGHT;
        dialog.getWindow().setAttributes(lp);

        Window window = dialog.getWindow();
        if (window != null) {
            window.getAttributes().x = 0;//(int) TextDate.getTop();
            window.getAttributes().y = 0;
            window.setBackgroundDrawableResource(R.color.transparent);

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;


            params.horizontalMargin = 0;
            params.verticalMargin = 0;
            // params.gravity = Gravity.RIGHT;
            params.gravity= Gravity.TOP | Gravity.RIGHT;
            params.dimAmount = 0;
            params.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(params);
        }


        ImageView ImageExit=(ImageView) dialog.findViewById(R.id.ImageExit);
        ImageExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
                menu_displayed=false;
            }
        });

        dialog.show();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2factor);

        mContentMain = findViewById(R.id.content_main);

      //  Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        setupActionBar();
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);
//
//        //Set default fragment
//        navigationView.setCheckedItem(R.id.nav_single_account);
        setCurrentFragment(AppFragment.SingleAccount);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) { }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) { }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_single_account) {
                    setCurrentFragment(AppFragment.SingleAccount);
                }

                if (id == R.id.nav_multiple_account) {
                    setCurrentFragment(AppFragment.MultipleAccount);
                }

                if (id == R.id.nav_b2c) {
                    setCurrentFragment(AppFragment.B2C);
                }

                drawer.removeDrawerListener(this);
            }

            @Override
            public void onDrawerStateChanged(int newState) { }
        });

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setCurrentFragment(final AppFragment newFragment){
        if (newFragment == mCurrentFragment) {
            return;
        }

        mCurrentFragment = newFragment;
        setHeaderString(mCurrentFragment);
        displayFragment(mCurrentFragment);
    }

    private void setHeaderString(final AppFragment fragment){
        switch (fragment) {
            case SingleAccount:
                getSupportActionBar().setTitle("Single Account Mode");
                return;

            case MultipleAccount:
                getSupportActionBar().setTitle("Multiple Account Mode");
                return;

            case B2C:
                getSupportActionBar().setTitle("B2C Mode");
                return;
        }
    }

    private void displayFragment(final AppFragment fragment){
        switch (fragment) {
            case SingleAccount:
                attachFragment(new SingleAccountModeFragment());
                return;

            case MultipleAccount:
                attachFragment(new MultipleAccountModeFragment());
                return;

            case B2C:
                attachFragment(new B2CModeFragment());
                return;
        }
    }

    private void attachFragment(final Fragment fragment) {

        //  .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        getSupportFragmentManager()
                .beginTransaction()
                .replace(mContentMain.getId(),fragment)
                .commit();
    }
}
