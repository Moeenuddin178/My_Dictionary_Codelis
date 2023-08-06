package com.my.dictionary.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.my.dictionary.ActivityMain;
import com.my.dictionary.R;
import com.my.dictionary.data.DatabaseUserManager;
import com.my.dictionary.data.GlobalVariable;
import com.my.dictionary.model.Word;

public class FragmentAddNew extends Fragment {

    private View view;
    private AppCompatRadioButton radio_eng_ind, radio_ind_eng;
    private DatabaseUserManager db_user;
    private String table;
    private GlobalVariable global;
    private Button button_clear, button_save;
    private EditText editText_word, editText_result;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_new, container, false);

        db_user = new DatabaseUserManager(getActivity());
        global = ((ActivityMain) getActivity()).getGlobalVariable();

        initComponent();
        initAction();

        return view;
    }

    private void initComponent() {
        (getActivity().findViewById(R.id.search_view)).setVisibility(View.GONE);
        radio_eng_ind = (AppCompatRadioButton) view.findViewById(R.id.radio_eng_ind);
        radio_ind_eng = (AppCompatRadioButton) view.findViewById(R.id.radio_ind_eng);

        button_clear = (Button) view.findViewById(R.id.button_clear);
        button_save = (Button) view.findViewById(R.id.button_save);
        editText_word = (EditText) view.findViewById(R.id.editText_word);
        editText_result = (EditText) view.findViewById(R.id.editText_result);

        table = global.getStringPref(global.S_KEY_TABLE, db_user.getTABLE2_NAME());
        toogleRadioButton();
    }

    private void initAction() {
        radio_eng_ind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.setStringPref(global.S_KEY_TABLE, db_user.getTABLE2_NAME());
                toogleRadioButton();
            }
        });
        radio_ind_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.setStringPref(global.S_KEY_TABLE, db_user.getTABLE1_NAME());
                toogleRadioButton();
            }
        });


        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetField();
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText_result.getText().toString().equals("") && !editText_word.getText().toString().equals("")) {
                    Word this_word = new Word();
                    this_word.setWord(editText_word.getText().toString());
                    this_word.setResult(editText_result.getText().toString());
                    this_word.setEdited(global.generateCurrentDate(2));
                    this_word.setFavorites("-");
                    db_user.insertNewRecord(global.getStringPref(global.S_KEY_TABLE, db_user.getTABLE2_NAME()), this_word);
                    Snackbar.make(view, "New word '" + editText_word.getText().toString() + "' successfully added", Snackbar.LENGTH_LONG).show();
                    resetField();
                    //((ActivityMain)getActivity()).openDrawer();
                } else {
                    Snackbar.make(view, "Please fill first", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resetField() {
        editText_word.setText("");
        editText_result.setText("");
    }

    public void toogleRadioButton() {
        table = global.getStringPref(global.S_KEY_TABLE, db_user.getTABLE2_NAME());
        if (table.equals(db_user.getTABLE2_NAME())) {
            radio_eng_ind.setChecked(true);
            radio_ind_eng.setChecked(false);
        } else {
            radio_eng_ind.setChecked(false);
            radio_ind_eng.setChecked(true);
        }
    }
}
