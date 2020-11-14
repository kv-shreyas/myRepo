package com.example.redditappnew.api;


import com.example.redditappnew.model.Feed;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Service {

    String BASE_URL = "https://www.reddit.com/r/";

    //Non-static feed name
    @GET("{feed_name}/.rss")
    Observable<Feed> getFeed(@Path("feed_name") String feed_name);
    @GET("popular/{filter_name}/.rss")
    Observable<Feed> getFilteredFeed(@Path("filter_name") String feed_name);

}
