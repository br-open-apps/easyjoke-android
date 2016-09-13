package com.alexandremota.easyjoke_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alexandremota.easyjoke_android.fragments.CategoryFragment;
import com.alexandremota.easyjoke_android.models.Category;
import com.alexandremota.easyjoke_android.services.Api;
import com.alexandremota.easyjoke_android.services.ApiService;
import com.alexandremota.easyjoke_android.services.ListResponse;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    Toolbar mToolbar;
    private Drawer result;
    private Api api;
    private Drawer.OnDrawerItemClickListener onDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            if (drawerItem != null) {
                Category category = (Category) drawerItem.getTag();
                if (category != null) {
                    getSupportActionBar().setTitle(category.getName());
                }
                openFragment(CategoryFragment.newInstance(drawerItem.getIdentifier()));
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // override toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // create drawer menu
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .addDrawerItems(
                        new SectionDrawerItem().withName(R.string.category_section)
                )
                .withOnDrawerItemClickListener(onDrawerItemClickListener)
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        // get all categories and add in Drawer
        api = new ApiService().getApi();
        api.getCategories().enqueue(new Callback<ListResponse<Category>>() {
            @Override
            public void onResponse(Call<ListResponse<Category>> call, Response<ListResponse<Category>> response) {
                if (response.isSuccessful()) {
                    for (Category category : response.body().getData()) {
                        result.addItem(new PrimaryDrawerItem()
                                .withName(category.getName())
                                .withIdentifier(category.getId())
                                .withTag(category)
                                .withSelectable(true)
                        );
                    }
                }
            }

            @Override
            public void onFailure(Call<ListResponse<Category>> call, Throwable t) {

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
