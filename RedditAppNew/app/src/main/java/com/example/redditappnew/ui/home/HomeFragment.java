package com.example.redditappnew.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditappnew.R;

import com.example.redditappnew.adapter.PopularAdapter;
import com.example.redditappnew.api.Client;
import com.example.redditappnew.api.Service;
import com.example.redditappnew.model.ExtractXML;
import com.example.redditappnew.model.Post;
import com.example.redditappnew.model.Feed;
import com.example.redditappnew.model.entry.Entry;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    Service apiInterface;
    PopularAdapter popularAdapter;
    private EditText searchEt;
    private static final String TAG = "HomeFragment";
    private static final String[] paths = {"Hot", "New", "Top", "Rising"};
    private ProgressBar progressBar;
    private TextView loadingText;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycler_view_collection);
        apiInterface = Client.getClient().create(Service.class);
        searchEt = root.findViewById(R.id.search);

        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    setSearchedResult(String.valueOf(searchEt.getText()));
                    return true;
                }
                return false;
            }
        });

        progressBar = (ProgressBar) root.findViewById(R.id.webviewLoadingProgressBar);
        loadingText = (TextView) root.findViewById(R.id.progressText);
        Log.d(TAG, "onCreate: Started.");
        progressBar.setVisibility(View.VISIBLE);
        setSearchedResult("popular");
        getSpinner(root);

        return root;
    }


    private void getSpinner(View root) {
        Spinner spinner = (Spinner) root.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }


    @SuppressLint("CheckResult")
    private void setSearchedResult(String feed_name) {
//        progressBar.setVisibility(View.VISIBLE);
        Observable<Feed> observable = apiInterface.getFeed(feed_name);
        observable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError);
    }

    @SuppressLint("CheckResult")
    private void setFiteredResult(String feed_name) {
//        progressBar.setVisibility(View.VISIBLE);
        Observable<Feed> observable = apiInterface.getFilteredFeed(feed_name);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError);
    }

    private void handleError(Throwable throwable) {
        Log.d("THROWERROR", throwable.getMessage());
//        Toast.makeText(getContext(), "Something went wrong ,please try again.", Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Type ", Toast.LENGTH_SHORT).show();
    }

    private void handleResults(Feed collectionsModel) {
        if (collectionsModel != null) {

            List<Entry> entrys = collectionsModel.getEntrys();
            Log.d(TAG, "onResponse: entrys: " + entrys);
            Log.d(TAG, "onResponse: author: " + entrys.get(0).getAuthor().getName());
            Log.d(TAG, "onResponse: updated: " + entrys.get(0).getUpdated());
            Log.d(TAG, "onResponse: title: " + entrys.get(0).getTitle());
            ArrayList<Post> posts = new ArrayList<Post>();
            for (int i = 0; i < entrys.size(); i++) {
                ExtractXML extractXML1 = new ExtractXML(entrys.get(i).getContent(), "<a href=");
                List<String> postContent = extractXML1.start();

                ExtractXML extractXML2 = new ExtractXML(entrys.get(i).getContent(), "<img src=");
                try {
                    postContent.add(extractXML2.start().get(0));
                } catch (NullPointerException e) {
                    postContent.add(null);
                    Log.e(TAG, "onResponse: NullPointerException(thumbnail):" + e.getMessage());
                } catch (IndexOutOfBoundsException e) {
                    postContent.add(null);
                    Log.e(TAG, "onResponse: IndexOutOfBoundsException(thumbnail):" + e.getMessage());
                }
                int lastPosition = postContent.size() - 1;

                try {
                    posts.add(new Post(
                            entrys.get(i).getTitle(),
                            entrys.get(i).getAuthor().getName(),
                            entrys.get(i).getUpdated(),
                            postContent.get(0),
                            postContent.get(lastPosition)
                    ));
                } catch (NullPointerException e) {
                    posts.add(new Post(
                            entrys.get(i).getTitle(),
                            "None",
                            entrys.get(i).getUpdated(),
                            postContent.get(0),
                            postContent.get(lastPosition)
                    ));
                    Log.e(TAG, "onResponse: NullPointerException: " + e.getMessage());
                }

                if (posts != null) {
                    Log.d("TAGG", posts + "  collectionsModelsModelList");
                    setRecyclerView(posts, collectionsModel);
                } else {
                    Log.d("TAGG", "NOT IN RECYCLER");
                }
            }
        } else {
            Toast.makeText(getContext(), "NO DATA", Toast.LENGTH_SHORT).show();
        }

    }


    private void setRecyclerView(List<Post> restaurantFullModelList, Feed feed) {
        popularAdapter = new PopularAdapter(getContext(), restaurantFullModelList, feed);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//      LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(popularAdapter);
        popularAdapter.notifyDataSetChanged();

        progressBar.setVisibility(View.GONE);
        loadingText.setText("");
        Log.d("TAGG", "SET RECYCLER");
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 0:
                progressBar.setVisibility(View.VISIBLE);
                setFiteredResult("hot");
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                progressBar.setVisibility(View.VISIBLE);
                setFiteredResult("new");
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                progressBar.setVisibility(View.VISIBLE);
                setFiteredResult("top");
                // Whatever you want to happen when the thrid item gets selected
                break;
            case 3:
                progressBar.setVisibility(View.VISIBLE);
                setFiteredResult("rising");
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}