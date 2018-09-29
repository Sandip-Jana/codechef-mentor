package com.hackathon.codechefapp.activities.nav.leaderboard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.activities.SearchUser.CodechefUser;
import com.hackathon.codechefapp.adapter.LeaderBoardAdapter;
import com.hackathon.codechefapp.client.RetrofitClient;
import com.hackathon.codechefapp.constants.Constants;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.listener.OnItemClickListener;
import com.hackathon.codechefapp.model.alibaba.myRelationshipApi.RelationResponse;
import com.hackathon.codechefapp.model.alibaba.myRelationshipApi.UserRelations;
import com.hackathon.codechefapp.model.leaderboard.LeaderBoardResponse;
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

public class LeaderBoard extends AppCompatActivity implements OnItemClickListener {

    private ProgressBar progressBar;

    private TextView rankFirstTxt;
    private TextView rankSecondTxt;
    private TextView rankThirdTxt;

    private RecyclerView leaderboardRecyclerView;
    private LeaderBoardAdapter leaderBoardAdapterAdapter;

    private List<LeaderBoardResponse> ranksExcludingTopThree;

    private SharedPreferenceUtils prefs;

    private HashMap<String, String> hashRelations = new HashMap<>();

    private HashMap<String, String> hashRoomId = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        progressBar = findViewById(R.id.leaderboardProgressBar);

        rankFirstTxt = findViewById(R.id.rankFirstTxt);
        rankSecondTxt = findViewById(R.id.rankSecondTxt);
        rankThirdTxt = findViewById(R.id.rankThirdTxt);

        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        leaderboardRecyclerView.setHasFixedSize(false);

        prefs = SharedPreferenceUtils.getInstance(getApplicationContext());

        if (prefs.contains(PreferenceConstants.LOGGED_IN_USER_NAME))
            fetchUserRelationShips();

        fecthLeaderBoard();
    }

    private void fecthLeaderBoard() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new RetrofitClient().getAlibabaCookiesApi(this);
        IChef ichef = retrofit.create(IChef.class);
        Call<List<LeaderBoardResponse>> getLeaderBoard = ichef.getLeaderBoard(Constants.USERS_COUNT);
        getLeaderBoard.enqueue(new Callback<List<LeaderBoardResponse>>() {
            @Override
            public void onResponse(Call<List<LeaderBoardResponse>> call, Response<List<LeaderBoardResponse>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    parseResponseToUI(response.body());
                } else {
                    DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.database_error));
                }
            }

            @Override
            public void onFailure(Call<List<LeaderBoardResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.error_message));
            }
        });

    }

    private void parseResponseToUI(List<LeaderBoardResponse> responseList) {
        int count = responseList.size();

        ranksExcludingTopThree = new ArrayList<>();

        for (int i = 0; i < responseList.size(); i++) {
            if (i <= 2) {
                switch (i) {
                    case 0:
                        rankFirstTxt.setText(responseList.get(i).getCurrentUser() + " " + responseList.get(i).getScore());
                        break;
                    case 1:
                        rankSecondTxt.setText(responseList.get(i).getCurrentUser() + " " + responseList.get(i).getScore());
                        break;
                    case 2:
                        rankThirdTxt.setText(responseList.get(i).getCurrentUser() + " " + responseList.get(i).getScore());
                        break;
                }
            } else {
                ranksExcludingTopThree.add(responseList.get(i));
            }
        }

        leaderBoardAdapterAdapter = new LeaderBoardAdapter(ranksExcludingTopThree);
        leaderBoardAdapterAdapter.setItemClickListener(this);
        leaderboardRecyclerView.setAdapter(leaderBoardAdapterAdapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        if (ranksExcludingTopThree != null && ranksExcludingTopThree.size() > position) {
            if (hashRelations.containsKey(ranksExcludingTopThree.get(position).getCurrentUser()))
                startActivityCodechefUser(ranksExcludingTopThree.get(position).getCurrentUser(), hashRelations.get(ranksExcludingTopThree.get(position).getCurrentUser()), hashRoomId.get(ranksExcludingTopThree.get(position).getCurrentUser()));
            else
                startActivityCodechefUser(ranksExcludingTopThree.get(position).getCurrentUser());
        }
    }

    private void startActivityCodechefUser(String userName, String relationStatus, String roomId) {
        Intent intent = new Intent(LeaderBoard.this, CodechefUser.class);
        intent.putExtra(Constants.username, userName);
        intent.putExtra(Constants.RELATION, relationStatus);
        intent.putExtra(Constants.ROOM_ID, roomId);

        startActivity(intent);
    }

    private void startActivityCodechefUser(String userName) {
        Intent intent = new Intent(LeaderBoard.this, CodechefUser.class);
        intent.putExtra(Constants.username, userName);
        startActivity(intent);
    }

    private void fetchUserRelationShips() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new RetrofitClient().getAlibabaCookiesApi(this);

        IChef ichef = retrofit.create(IChef.class);

        Call<UserRelations> fetchRelations = ichef.getUserRelations();

        fetchRelations.enqueue(new Callback<UserRelations>() {
            @Override
            public void onResponse(Call<UserRelations> call, Response<UserRelations> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response != null && response.body() != null) {
                    parseUserRelationsResponse(response.body());
                } else {
                    DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.database_error));
                }

            }

            @Override
            public void onFailure(Call<UserRelations> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
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

}
