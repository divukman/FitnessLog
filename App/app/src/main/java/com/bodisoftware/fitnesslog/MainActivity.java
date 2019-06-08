package com.bodisoftware.fitnesslog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.bodisoftware.fitnesslog.database.Bootstrap;
import com.bodisoftware.fitnesslog.ui.tabs.PagerAdapter;


public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Sessions"));
        tabLayout.addTab(tabLayout.newTab().setText("Routines"));
        tabLayout.addTab(tabLayout.newTab().setText("Excercises"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        bootstrapDatabase();

        final Intent intent = getIntent();
        if (intent.hasExtra(Constants.CURRENT_PAGE)) {
            final int tab = intent.getIntExtra(Constants.CURRENT_PAGE, -1);
            if (tab != -1) {
                mViewPager.setCurrentItem(tab);
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.sync).setEnabled(false);
        menu.findItem(R.id.export).setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about) {
            new MaterialDialog.Builder(this)
                    .iconRes(R.mipmap.ic_launcher)
                    .title(R.string.about_title)
                    .content(R.string.about_text, true)
                    .positiveText(R.string.about_btn_close)
                    .negativeText(R.string.about_btn_donate)
                    .neutralText(R.string.about_btn_web)
                    .btnStackedGravity(GravityEnum.CENTER)
                    .stackingBehavior(StackingBehavior.ALWAYS)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (which.ordinal() == 0) {
                                // Toast.makeText(getApplicationContext(), "Close", Toast.LENGTH_SHORT).show();
                            } else if (which.ordinal() == 2) {
                                Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://paypal.me/divukman"));
                                startActivity(browserIntent);
                            } else if (which.ordinal() == 1) {
                                Toast.makeText(getApplicationContext(), "Visit Web", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void bootstrapDatabase() {
        final boolean firstRun = Bootstrap.isFirstTimeRunning(getApplicationContext());
        if (firstRun) {
            Bootstrap.bootstrapDatabase(getApplicationContext());
        }
    }

    public int getCurrentTabIndex() {
       return mViewPager.getCurrentItem();
    }
}
