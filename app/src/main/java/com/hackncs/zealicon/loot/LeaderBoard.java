package com.hackncs.zealicon.loot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderBoard extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<User> users;
    ArrayList<String> usernames, coins;
    ArrayList<Integer> avatarIDs;
    View view;
    RequestQueue requestQueue;

    public LeaderBoard() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_leader_board, container, false);
        mRecyclerView = view.findViewById(R.id.my_recycler_view);

        users = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new CustomRecycleAdapter(getContext(), getActivity(), users,"leaderBoard");

        mRecyclerView.setAdapter(mAdapter);
        BottomNavigationView navigationView=getActivity().findViewById(R.id.bottom_nav);
        navigationView.getMenu().getItem(2).setChecked(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        usernames = new ArrayList<>();
        coins = new ArrayList<>();
        avatarIDs = new ArrayList<>();
        Log.i("times","times");
        StringRequest leaders = new StringRequest(Request.Method.GET, Endpoints.leaders,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray;
                        try {
                            jsonArray= new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.d("JSON"+i, jsonObject.toString());
                                User user=new User();
                                user.setUsername(jsonObject.getString("username"));
                                user.setScore(Integer.valueOf(jsonObject.getString("score")));
                                user.setAvatarID(jsonObject.getInt("avatar_id"));
                                users.add(user);
                                mAdapter.notifyDataSetChanged();
                                usernames.add(i, jsonObject.getString("username"));
                                coins.add(i, jsonObject.getString("score"));
                                avatarIDs.add(i, jsonObject.getInt("avatar_id"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-auth",Endpoints.apikey);
                return params;
            }
        };
        if(requestQueue==null) {
            requestQueue = Volley.newRequestQueue(getActivity());
        }
        requestQueue.add(leaders);

    }
}
