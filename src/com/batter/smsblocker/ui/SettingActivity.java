package com.batter.smsblocker.ui;

import com.batter.smsblocker.R;
import com.batter.smsblocker.R.id;
import com.batter.smsblocker.R.layout;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SettingActivity extends SlidingFragmentActivity {

    private Fragment mOverViewFragment;
    private Fragment mHistoryFragment;
    private Fragment mMenuFragment;
    private Fragment mBlocklistFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame);

        mOverViewFragment = new OverViewFragment();
        mBlocklistFragment = new BlockListFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mBlocklistFragment).commit();

        mMenuFragment = new MenuFragment();
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, mMenuFragment).commit();

        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }
}
