package com.alexandremota.easyjoke_android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandremota.easyjoke_android.R;
import com.alexandremota.easyjoke_android.items.JokeItem;
import com.alexandremota.easyjoke_android.models.Joke;
import com.alexandremota.easyjoke_android.services.Api;
import com.alexandremota.easyjoke_android.services.ApiService;
import com.alexandremota.easyjoke_android.services.ListResponse;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {
    private static final String LOG_TAG = CategoryFragment.class.getSimpleName();

    private static final String ARG_CATEGORY_ID = "param1";

    private Long mCategoryId;
    private RecyclerView mRecyclerView;
    private FastItemAdapter<JokeItem> mFastItemAdapter;
    private FooterAdapter<ProgressItem> mFooterAdapter;

    private Api api;
    private ListResponse<Joke> mListResponse;
    private List<JokeItem> mJokeItems = new ArrayList<>();

    public CategoryFragment() {

    }

    public static CategoryFragment newInstance(long categoryId) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCategoryId = getArguments().getLong(ARG_CATEGORY_ID);
            Log.i(LOG_TAG, "Fragment Category Id: " + mCategoryId);

            //create our FastAdapter which will manage everything
            mFastItemAdapter = new FastItemAdapter<>();
            //create our FooterAdapter which will manage the progress items
            mFooterAdapter = new FooterAdapter<>();

            api = new ApiService().getApi();
            api.getJokesFromCategory(mCategoryId, null).enqueue(new Callback<ListResponse<Joke>>() {
                @Override
                public void onResponse(Call<ListResponse<Joke>> call, Response<ListResponse<Joke>> response) {
                    if (response.isSuccessful()) {
                        mListResponse = response.body();
                        for (Joke joke : mListResponse.getData()) {
                            mJokeItems.add(JokeItem.newInstance(joke));
                        }
                        mFastItemAdapter.add(mJokeItems);
                    }
                }

                @Override
                public void onFailure(Call<ListResponse<Joke>> call, Throwable t) {
                    Log.e(LOG_TAG, "Error to list all jokes from category", t);
                }
            });
        }

        //restore selections (this has to be done after the items were added
        mFastItemAdapter.withSavedInstanceState(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFastItemAdapter.withSelectable(true);
        mFastItemAdapter.withPositionBasedStateManagement(false);
        mFastItemAdapter.withSelectable(true);
        mFastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<JokeItem>() {
            @Override
            public boolean onClick(View v, IAdapter<JokeItem> adapter, JokeItem item, int position) {
                // TODO:: Open JokeActivity
                Log.i(LOG_TAG, item.getJoke().getTitle());
                return true;
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mFastItemAdapter);
        mRecyclerView.setAdapter(mFooterAdapter.wrap(mFastItemAdapter));
        mRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mFooterAdapter) {
        @Override
        public void onLoadMore(final int currentPage) {
            mFooterAdapter.clear();
            mFooterAdapter.add(new ProgressItem().withEnabled(false));
            if (mListResponse.getMeta().getPagination().hasNextPage()) {
                api.getJokesFromCategory(mCategoryId, mListResponse.getMeta().getPagination().getNextPage()).enqueue(new Callback<ListResponse<Joke>>() {
                    @Override
                    public void onResponse(Call<ListResponse<Joke>> call, Response<ListResponse<Joke>> response) {
                        if (response.isSuccessful()) {
                            mFooterAdapter.clear();
                            mListResponse = response.body();
                            for (Joke joke : mListResponse.getData()) {
                                mFastItemAdapter.add(mFastItemAdapter.getAdapterItemCount(), JokeItem.newInstance(joke));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ListResponse<Joke>> call, Throwable t) {
                        mFooterAdapter.clear();
                    }
                });
            } else {
                mFooterAdapter.clear();
            }
        }
    };
}
