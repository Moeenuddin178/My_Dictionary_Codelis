package com.my.dictionary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.my.dictionary.ActivityDetails;
import com.my.dictionary.ActivityMain;
import com.my.dictionary.R;
import com.my.dictionary.adapter.ItemListBaseAdapter;
import com.my.dictionary.data.DatabaseManager;
import com.my.dictionary.data.GlobalVariable;
import com.my.dictionary.model.Word;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentFavorites extends Fragment {

    private View view;
    private ListView listview;
    private LinearLayout lyt_not_found;
    private DatabaseManager db;
    private GlobalVariable global;
    private ProgressBar progress;
    private List<Word> item_data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorites, null);

        db = ((ActivityMain) getActivity()).getDbManager();
        global = ((ActivityMain) getActivity()).getGlobalVariable();

        initComponent();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                showInterstitial();
                Intent i = new Intent(getActivity().getApplicationContext(), ActivityDetails.class);
                i.putExtra("object_word", item_data.get(arg2));
                startActivity(i);
            }
        });

        return view;
    }

    private void initComponent() {
        (getActivity().findViewById(R.id.search_view)).setVisibility(View.GONE);
        listview = (ListView) view.findViewById(R.id.listView1);
        progress = (ProgressBar) view.findViewById(R.id.progressBar1);
        lyt_not_found = (LinearLayout) view.findViewById(R.id.lyt_not_found);
        progress.setVisibility(View.GONE);
    }

    private void validateItemCount() {
        if (item_data.size() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        item_data = db.getFavoritesRow();
        listview.setAdapter(new ItemListBaseAdapter(getActivity(), item_data));
        global.setIntPref(global.I_KEY_FAV_COUNTE, item_data.size());
        validateItemCount();
    }

    private void showInterstitial() {
        try {
            ((ActivityMain) getActivity()).showInterstitial();
        } catch (Exception e) {
        }
    }

}
