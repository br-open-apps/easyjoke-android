package com.divpix.easyjoke.items;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.divpix.easyjoke.R;
import com.divpix.easyjoke.models.Joke;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class JokeItem extends AbstractItem<JokeItem, JokeItem.ViewHolder> {

    private Joke joke;

    private JokeItem(Joke joke) {
        this.joke = joke;
    }

    public static JokeItem newInstance(Joke joke) {
        return new JokeItem(joke);
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.fastadapter_joke_item_id;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.joke_item;
    }

    public Joke getJoke() {
        return joke;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        //set the text for the title
        viewHolder.title.setText(joke.getTitle());
        //set the text for the content or hide
        viewHolder.content.setText(joke.getContent());
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView content;
        protected View view;

        public ViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.joke_title);
            this.content = (TextView) view.findViewById(R.id.joke_content);
            this.view = view;
        }
    }
}
