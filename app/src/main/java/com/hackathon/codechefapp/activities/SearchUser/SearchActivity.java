package com.hackathon.codechefapp.activities.SearchUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.adapter.SearchAdapter;
import com.hackathon.codechefapp.client.RetrofitClient;
import com.hackathon.codechefapp.constants.Constants;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.listener.OnItemClickListener;
import com.hackathon.codechefapp.model.alibaba.myRelationshipApi.RelationResponse;
import com.hackathon.codechefapp.model.alibaba.myRelationshipApi.UserRelations;
import com.hackathon.codechefapp.model.search.Content;
import com.hackathon.codechefapp.model.search.Search;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;
import com.hackathon.codechefapp.retrofitmapping.IChef;
import com.hackathon.codechefapp.utils.DisplayToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity implements OnItemClickListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private TextInputLayout searchLayout;
    private EditText searchTxt;
    private TextView searchBtn;

    private ProgressBar progressBarSearch;

    private RecyclerView searchRecyclerView;
    private SearchAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Content> searchData;

    private SharedPreferenceUtils prefs;

    private HashMap<String, String> hashRelations = new HashMap<>();

    private HashMap<String, String> hashRoomId = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initToolbar();

        searchLayout = findViewById(R.id.searchLayout);
        searchTxt = findViewById(R.id.searchTxt);
        searchBtn = findViewById(R.id.searchBtn);
        progressBarSearch = findViewById(R.id.progressBarSearch);
        searchRecyclerView = findViewById(R.id.searchRecyclerView);

        searchRecyclerView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(this);
        searchRecyclerView.setLayoutManager(layoutManager);

        prefs = SharedPreferenceUtils.getInstance(getApplicationContext());

        if (prefs.contains(PreferenceConstants.LOGGED_IN_USER_NAME))
            fetchUserRelationShips();
        else
            DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), "Could not fetch your relations");

        addListeners();
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addListeners() {

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchTxt.getText().toString().trim().length() > 0) {
                    hideKeyboard();
                    searchCodechefUsername(searchTxt.getText().toString().trim());
                } else {
                    DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.valid_username));
                }
            }
        });

    }

    private void searchCodechefUsername(String username) {
        progressBarSearch.setVisibility(View.VISIBLE);

        Retrofit retrofitClient = new RetrofitClient().get(this);

        final IChef iChef = retrofitClient.create(IChef.class);

        HashMap<String, String> queryData = new HashMap<>();
        queryData.put(Constants.fields, Constants.username);
        queryData.put(Constants.limit, Constants.LIMIT_VALUE);
        queryData.put(Constants.offset, Constants.OFFSET_VALUE);
        queryData.put(Constants.search, username);

        String authToken = "Bearer " + prefs.getStringValue(PreferenceConstants.ACCESS_TOKEN, "");

        Call<Search> searchApi = iChef.searchUsersByName(authToken, queryData);

        searchApi.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {

                progressBarSearch.setVisibility(View.INVISIBLE);

                if (response.isSuccessful()) {
                    parseSearchResults(response);
                } else {
                    DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), "Invalid Search");
                }

            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                progressBarSearch.setVisibility(View.INVISIBLE);

                DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.no_internet));
            }
        });
    }

    private void parseSearchResults(Response<Search> response) {
        if (response == null || response.body() == null || response.body().getResult() == null || response.body().getResult().getData() == null ||
                response.body().getResult().getData().getContent() == null) {
            DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.no_user_found));
            return;
        }

        searchData = (ArrayList<Content>) response.body().getResult().getData().getContent();

        ArrayList<Content> temp = new ArrayList<>(searchData);

        for (Content content : temp) {
            if (content.getFullname() == null) {
                searchData.remove(content);
            }
        }

        if (searchData.size() > 0) {
            adapter = new SearchAdapter(searchData);
            adapter.setItemClickListener(this);
            searchRecyclerView.setAdapter(adapter);
        } else {
            DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.no_data));
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (searchData != null && searchData.size() > position) {
            if (hashRelations.containsKey(searchData.get(position).getUsername()))
                startActivityCodechefUser(searchData.get(position).getUsername(), hashRelations.get(searchData.get(position).getUsername()) , hashRoomId.get(searchData.get(position).getUsername()));
            else
                startActivityCodechefUser(searchData.get(position).getUsername());
        }
    }

    private void startActivityCodechefUser(String userName) {
        Intent intent = new Intent(SearchActivity.this, CodechefUser.class);
        intent.putExtra(Constants.username, userName);
        startActivity(intent);
    }

    private void startActivityCodechefUser(String userName, String relationStatus , String roomId) {
        Intent intent = new Intent(SearchActivity.this, CodechefUser.class);
        intent.putExtra(Constants.username, userName);
        intent.putExtra(Constants.RELATION, relationStatus);
        intent.putExtra(Constants.ROOM_ID , roomId);

        startActivity(intent);
    }

    private void fetchUserRelationShips() {
        progressBarSearch.setVisibility(View.VISIBLE);

        Retrofit retrofit = new RetrofitClient().getAlibabaCookiesApi(this);

        IChef ichef = retrofit.create(IChef.class);

        Call<UserRelations> fetchRelations = ichef.getUserRelations();

        fetchRelations.enqueue(new Callback<UserRelations>() {
            @Override
            public void onResponse(Call<UserRelations> call, Response<UserRelations> response) {
                progressBarSearch.setVisibility(View.INVISIBLE);

                if (response.isSuccessful() && response != null && response.body() != null) {
                    parseUserRelationsResponse(response.body());
                } else {
                    DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.database_error));
                }

            }

            @Override
            public void onFailure(Call<UserRelations> call, Throwable t) {
                progressBarSearch.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void parseUserRelationsResponse(UserRelations response) {
        if (response.getStatus().toLowerCase().trim().equalsIgnoreCase("success")) {
            List<RelationResponse> relationlist = response.getResponse();
            for (int i = 0; i < relationlist.size(); i++) {
                hashRelations.put(relationlist.get(i).getUsername(), relationlist.get(i).getStatus() + Constants.DELIMETER + relationlist.get(i).getRelationship());
                hashRoomId.put(relationlist.get(i).getUsername(), relationlist.get(i).getRoom_id());
            }
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (prefs.contains(PreferenceConstants.LOGGED_IN_USER_NAME))
            fetchUserRelationShips();
        else
            DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), "Could not fetch your relations");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
