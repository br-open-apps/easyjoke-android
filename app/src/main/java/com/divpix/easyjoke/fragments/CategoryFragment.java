package com.divpix.easyjoke.fragments;

import android.content.Intent;
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

import com.divpix.easyjoke.R;
import com.divpix.easyjoke.activities.JokeActivity;
import com.divpix.easyjoke.items.JokeItem;
import com.divpix.easyjoke.models.Category;
import com.divpix.easyjoke.models.Joke;
import com.divpix.easyjoke.services.Api;
import com.divpix.easyjoke.services.ApiService;
import com.divpix.easyjoke.services.ListResponse;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {

    private static final String LOG_TAG = CategoryFragment.class.getSimpleName();
    private static final String ARG_CATEGORY = "category";

    private Category mCategory;
    private RecyclerView mRecyclerView;
    private FastItemAdapter<JokeItem> mFastItemAdapter;
    private FooterAdapter<ProgressItem> mFooterAdapter;

    private Api api;
    private ListResponse<Joke> mListResponse;

    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mFooterAdapter) {
        @Override
        public void onLoadMore(final int currentPage) {
            mFooterAdapter.clear();
            if (mListResponse != null) {
                if (mListResponse.getMeta().getPagination().hasNextPage()) {
                    mFooterAdapter.add(new ProgressItem().withEnabled(false));
                    api.getJokesFromCategory(mCategory.getId(), mListResponse.getMeta().getPagination().getNextPage()).enqueue(new Callback<ListResponse<Joke>>() {
                        @Override
                        public void onResponse(Call<ListResponse<Joke>> call, Response<ListResponse<Joke>> response) {
                            if (!response.isSuccessful()) {
                                mFooterAdapter.clear();
                            } else {
                                mListResponse = response.body();
                                for (Joke joke : mListResponse.getData()) {
                                    mFastItemAdapter.add(mFastItemAdapter.getAdapterItemCount(), JokeItem.newInstance(joke));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ListResponse<Joke>> call, Throwable t) {
                            Log.e(LOG_TAG, "Error to load all jokes from category: " + mCategory.getId(), t);
                            mFooterAdapter.clear();
                        }
                    });
                }
            }
        }
    };

    public CategoryFragment() {
    }

    public static CategoryFragment newInstance(Category category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CATEGORY, category);
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
            mCategory = getArguments().getParcelable(ARG_CATEGORY);
        }

        api = new ApiService().getApi();

        //create our FastAdapter which will manage everything
        mFastItemAdapter = new FastItemAdapter<>();
        //create our FooterAdapter which will manage the progress items
        mFooterAdapter = new FooterAdapter<>();
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
                Intent intent = new Intent(getActivity(), JokeActivity.class);
                intent.putExtra(JokeActivity.JOKE, item.getJoke());
                intent.putExtra(JokeActivity.JOKE_CATEGORY, mCategory);
                startActivity(intent);
                return true;
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mFooterAdapter.wrap(mFastItemAdapter));
        mRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

        if (mCategory != null) {
            api.getJokesFromCategory(mCategory.getId(), null).enqueue(new Callback<ListResponse<Joke>>() {
                @Override
                public void onResponse(Call<ListResponse<Joke>> call, Response<ListResponse<Joke>> response) {
                    if (response.isSuccessful()) {
                        mListResponse = response.body();
                        for (Joke joke : mListResponse.getData()) {
                            mFastItemAdapter.add(mFastItemAdapter.getAdapterItemCount(), JokeItem.newInstance(joke));
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListResponse<Joke>> call, Throwable t) {
                    Log.e(LOG_TAG, "Error to list all jokes from category", t);
                }
            });
        }

    }
}
