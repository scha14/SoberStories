package soberstories.app.jsmtech.sobersstories;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCE_APP = "Preference";
    public static final String HAS_SEEN_WALKTHROUGH = "hasSeenWalkThrough";
    private Toolbar mToolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    private int[] tabIcons = {
            R.drawable.view,
            R.drawable.profile,
            R.drawable.search,
            R.drawable.add
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            ReplaceFont.replaceDefaultFont(this, "DEFAULT", "story_1.ttf");
        } catch (NoSuchFieldException e) {
            Log.d("Font not found", "onCreate: "+ e);
        }


        // Shared Preferences - Permanently Save data on the device as long as the app is installed on the device.
        // Lets save  a data such that we can check that user has opened the app for the first time, if he/she has opened it for the first time
        // We will show the user around or give a walkthrough of the app!


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);


        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        // Check if Use is there or not.

        ParseUser user = ParseUser.getCurrentUser();

        if (user==null){ // not logged in
            // redirect to sign up or login screen
            navigatedToLoginScreen();

        }
        else {

            SharedPreferences userPreference = getSharedPreferences(PREFERENCE_APP, MODE_PRIVATE);
            boolean hasSeenWalkthrough = userPreference.getBoolean(HAS_SEEN_WALKTHROUGH, false);

            if(!hasSeenWalkthrough) {
                Intent intent = new Intent(MainActivity.this, AppWalkThrough.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }


            // stay on main
            String userLoggedIn = R.string.user_logged_in + "";
            Toast.makeText(this, userLoggedIn ,Toast.LENGTH_SHORT);

            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager); // ViewPager allows us to slide from one Tab to another


            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            tabLayout.getTabAt(3).setIcon(tabIcons[3]);


        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.upload);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Upload", Snackbar.LENGTH_LONG)
//                        .setAction("Action", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                // H.W!
////                                Intent addStoryIntent = new Intent(MainActivity.this, AddStory.class);
////                                startActivity(addStoryIntent);
//                            }
//                        }).show();
//            3
//        });


    }

    private void navigatedToLoginScreen() {
        Intent loginOrRegister = new Intent(this, LoginOrRegister.class);

        loginOrRegister.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        loginOrRegister.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // prevent back tracking

        startActivity(loginOrRegister);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Stories(), "Stories"); // links
        adapter.addFragment(new Profile(), "Profile");
        adapter.addFragment(new Search(), "Search");
        adapter.addFragment(new AddStory(), "Add Story");
        viewPager.setAdapter(adapter);

    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentsList = new ArrayList<>();
        private final List<String> title = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Depending on which Tab we are this allows us to switch to that position
        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }


        // Using this Method, Custom created, we can add as many fragments as we like to our TabLayout,
        public void addFragment(Fragment f , String t) {
            mFragmentsList.add(f);
            title.add(t);
        }

        // This is to set title to our Tab!
        @Override
        public CharSequence getPageTitle(int position) {
            // return title.get(position);
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            ParseUser.logOut();
            navigatedToLoginScreen();
        }
        if (id == R.id.action_rate_this_app) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()));
            startActivity(intent);
        }

        if (id == R.id.share_this_app) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String PACKAGE_NAME = getPackageName();
            String shareBody = "My events app, please rate and download. https://play.google.com/store/apps/details?id=" + PACKAGE_NAME;
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Event App");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }

            return super.onOptionsItemSelected(item);
    }



    }

