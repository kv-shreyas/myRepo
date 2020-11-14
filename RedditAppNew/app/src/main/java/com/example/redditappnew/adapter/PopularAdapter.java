package com.example.redditappnew.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.redditappnew.R;
import com.example.redditappnew.WebViewActivity;
import com.example.redditappnew.model.Post;
import com.example.redditappnew.model.Feed;

import java.util.List;


public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.CollectionHolder> {

    Context context;
    private List<Post> list;
    private Feed feed;
    RequestOptions requestOptions = new RequestOptions();
    ProgressBar mProgressBar;

    public PopularAdapter(Context context, List<Post> posts,Feed feed) {
        this.context = context;
        this.list = posts;
        this.feed=feed;
    }

    @NonNull
    @Override
    public CollectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_list, parent, false);

        return new CollectionHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull CollectionHolder holder, int position) {

        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);

        Post postItem = list.get(position);

        holder.collectionTitle.setText(postItem.getTitle());
        holder. authorName.setText("Posted by "+postItem.getAuthor());
        holder.date_updated.setText(postItem.getDate_updated());

        if(postItem.getThumbnailURL()!=null) {
            Glide.with(context)
                    .load(postItem.getThumbnailURL())
                    .apply(requestOptions)
                    .into(holder.collectionImage);
        }
        else {
            Glide.with(context)
                    .load(R.drawable.reddit_icon)
                    .apply(requestOptions)
                    .into(holder.collectionImage);
        }
        if(feed!=null && feed.getLogo()!=null) {
            Glide.with(context)
                    .load(feed.getLogo())
                    .apply(requestOptions)
                    .into(holder.profile_icon);
        }
        else {
            Glide.with(context)
                    .load(R.drawable.reddit_logo)
                    .apply(requestOptions)
                    .into(holder.profile_icon);
        }
        //   Toast.makeText(context, postItem.getTitle(), Toast.LENGTH_SHORT).show();

        ////entry




    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class CollectionHolder extends RecyclerView.ViewHolder {

        public ImageView collectionImage,profile_icon;
        public TextView collectionTitle,authorName,date_updated;

        public CollectionHolder(@NonNull View itemView) {
            super(itemView);
            collectionImage = itemView.findViewById(R.id.image);
            collectionTitle = itemView.findViewById(R.id.title);
            authorName = itemView.findViewById(R.id.authorName);
            date_updated = itemView.findViewById(R.id.date_updated);


            ///entry
            profile_icon = itemView.findViewById(R.id.icon);
            //label = itemView.findViewById(R.id.label);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition();

                    if(pos!=RecyclerView.NO_POSITION) {
                        //Log.d(TAG, "onItemClick: Clicked: " + posts.get(position).toString());
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("url", list.get(pos).getPostURL());
                        context.startActivity(intent);
                    }
                }
            });


        }
    }
}
