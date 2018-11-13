package com.example.android.lendabook;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/*Activity that launches ViewPager for the two tabs*/

public class UserAccountActivity extends AppCompatActivity {

    //number of tabs in user's account
    private int NUM_TABS = 2;

    private PagerAdapter adapter;
    private ViewPager pager;
    //String [] names = getResources().getStringArray(R.array.tab_names);
    String [] tab_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tab_names = getResources().getStringArray(R.array.tab_names);
        adapter = new PagerAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.pager);

        pager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));

    }

    public void launchThis(Activity from){
        Intent intent = new Intent(from, UserAccountActivity.class);
        startActivity(intent);
    }



    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_TABS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Log.d("MyBooks", "retunred my books");
                    MyBooksFragment booksFragment = new MyBooksFragment();
                    booksFragment.setParentActivity(UserAccountActivity.this);
                    return booksFragment;
                case 1:
                    BorrowedBooksFragment borrowedBooksFragment = new BorrowedBooksFragment();
                    borrowedBooksFragment.setParentActivity(UserAccountActivity.this);
                    return borrowedBooksFragment;
                    default:
                        return null;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       if(id == R.id.action_create_group){
            Intent intent = new Intent(UserAccountActivity.this, CreateGroupActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_join_group){
            Intent intent = new Intent(UserAccountActivity.this, JoinGroupActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_search_book){
            Intent intent = new Intent(UserAccountActivity.this, SearchBookActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
