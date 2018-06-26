package mychevroletconnect.com.chevroletapp.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;



/**
 * @author pocholomia
 * @since 25/10/2016
 */
class MainTabAdapter extends FragmentStatePagerAdapter {

    private static final String[] TITLES = {"HOME", "CALENDAR"};

    public MainTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
              //   return new HomeFragment();
            case 1:
               // return new ScheduleFragment();


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
