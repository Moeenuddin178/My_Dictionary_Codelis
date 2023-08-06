package com.my.dictionary.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.my.dictionary.ActivityDetails;
import com.my.dictionary.ActivityMain;
import com.my.dictionary.R;
import com.my.dictionary.adapter.ItemListBaseAdapter;
import com.my.dictionary.data.DatabaseManager;
import com.my.dictionary.data.GlobalVariable;
import com.my.dictionary.model.Word;

import java.util.ArrayList;
import java.util.List;

public class FragmentTranslate extends Fragment {

    private View view;
    private AppCompatRadioButton radio_eng_ind, radio_ind_eng;
    private ListView listview;
    private LinearLayout lyt_not_found;
    private String table;
    private DatabaseManager db;
    private GlobalVariable global;

    private SearchView searchView = null;
    private List<Word> item_data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_translate, null);

        // activate fragment menu
        setHasOptionsMenu(true);

        db = ((ActivityMain) getActivity()).getDbManager();
        global = ((ActivityMain) getActivity()).getGlobalVariable();

        initComponent();
        initAction();

        return view;
    }

    private void initComponent() {
        (getActivity().findViewById(R.id.search_view)).setVisibility(View.VISIBLE);
        searchView = getActivity().findViewById(R.id.search_view);
        searchView.setQuery("", false);
        searchView.setQueryHint("Search word...");
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    fillList(s);
                } catch (Exception e) {
                }
                return true;
            }
        });
        searchView.onActionViewExpanded();

        listview = (ListView) view.findViewById(R.id.listView1);
        radio_eng_ind = (AppCompatRadioButton) view.findViewById(R.id.radio_eng_ind);
        radio_ind_eng = (AppCompatRadioButton) view.findViewById(R.id.radio_ind_eng);
        lyt_not_found = (LinearLayout) view.findViewById(R.id.lyt_not_found);

        table = global.getStringPref(global.S_KEY_TABLE, db.getTABLE2_NAME());
        toogleRadioButton();
        validateItemCount();
    }


    private void initAction() {
        radio_eng_ind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.setStringPref(global.S_KEY_TABLE, db.getTABLE2_NAME());
                toogleRadioButton();
                fillList(searchView.getQuery().toString());
            }
        });
        radio_ind_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.setStringPref(global.S_KEY_TABLE, db.getTABLE1_NAME());
                toogleRadioButton();
                fillList(searchView.getQuery().toString());
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                showInterstitial();
                Intent i = new Intent(getActivity().getApplicationContext(), ActivityDetails.class);
                i.putExtra("object_word", item_data.get(arg2));
                startActivity(i);
            }
        });
    }


    private void fillList(String keyword) {
        if (keyword.equals("")) {
            item_data = new ArrayList<>();
        } else {
            if (keyword.toCharArray().length < 2) {
                item_data = db.getAllRow(keyword, table, true);
            } else {
                item_data = db.getAllRow(keyword, table, true);
            }
        }
        listview.setAdapter(new ItemListBaseAdapter(getActivity().getApplicationContext(), item_data));
        validateItemCount();
    }


    public void toogleRadioButton() {
        table = global.getStringPref(global.S_KEY_TABLE, db.getTABLE2_NAME());
        if (table.equals(db.getTABLE2_NAME())) {
            radio_eng_ind.setChecked(true);
            radio_ind_eng.setChecked(false);
        } else {
            radio_eng_ind.setChecked(false);
            radio_ind_eng.setChecked(true);
        }
    }

    private void validateItemCount() {
        if (item_data.size() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
    }

    private void showInterstitial() {
        try {
            ((ActivityMain) getActivity()).showInterstitial();
        } catch (Exception e) {
        }
    }

}
