package com.example.projectkino1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectkino1.adapters.FilmAdapter;
import com.example.projectkino1.data.DBFilms;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Films extends Fragment {
    interface OnFragmentSendDataListener {
        void onSendData(ClassFilms data);
    }

    private OnFragmentSendDataListener fragmentSendDataListener;
    private FloatingActionButton add_vw;
    int ADD_ACTIVITY = 0;
    int UPDATE_ACTIVITY = 1;
    int SINGLE_FILM = 2;
    final int CANCEL = 2, ADD_TO_VIEWED = 3, ADD_TO_VIEWED_NEED_DELETE = 4;
    long is, is_add;
    private TextView tw;
    private DBFilms dbFilms;
    List<ClassFilms> list_films, prob;
    private RecyclerView mRecyclerView;
    private FilmAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ImageButton del;



    public Films() {
        // Required empty public constructor
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentSendDataListener = (OnFragmentSendDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_films, container, false);
        prob = new ArrayList<ClassFilms>();
        String name = "второй";
        for(int i = 0; i< 5;i++){
            if(i == 0){
                name = "первый";
            }
            ClassFilms f = new ClassFilms(i, name, "com", "at", "cat", 1, 1);
            prob.add(f);
        }

        // Inflate the layout for this fragment
        add_vw = (FloatingActionButton) view.findViewById(R.id.add_want);
        add_vw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddFilms.class);
                startActivityForResult(i, ADD_ACTIVITY);
                updateList();

            }
        });
        dbFilms = new DBFilms(getContext());
        tw = (TextView) view.findViewById(R.id.textView25);
        list_films = dbFilms.selectAll();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_of_films);

        // если вы уверены, что изменения в контенте не изменят размер лайота, то передаём параметр true - это увеличивает производительность
        mRecyclerView.setHasFixedSize(true);

        // используем LinearLayoutManager для отображения как ListView
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        FilmAdapter.OnStateClickListener stateClickListener = new FilmAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(ClassFilms film, int position) {
                Intent i = new Intent(getActivity(), AddFilms.class);
                is = mAdapter.isFilmSingle(film);
                i.putExtra("Films", film);
                if(is != -1){
                    startActivityForResult(i, SINGLE_FILM);
                }
                else {
                    startActivityForResult(i, UPDATE_ACTIVITY);
                }
                updateList();

            }
        };
        FilmAdapter.OnAddClickListener addClickListener = new FilmAdapter.OnAddClickListener() {
            @Override
            public void onClick(ClassFilms film) {
                Intent i = new Intent(getActivity(), AddViewed.class);
                is_add = mAdapter.isFilmSingle(film);
                i.putExtra("VFilms", film);
                if(is_add != -1){
                    startActivityForResult(i, SINGLE_FILM);
                }
                else {
                    startActivityForResult(i, UPDATE_ACTIVITY);
                }
                updateList();
            }
        };
        mAdapter = new FilmAdapter(getActivity(), list_films, stateClickListener, addClickListener);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void updateList () {
        mAdapter.setData(dbFilms.selectAll());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if (resultCode == Activity.RESULT_OK) {
            ClassFilms md = (ClassFilms) data.getExtras().getSerializable("Films");
            if(md.getRang() == 0){
                dbFilms.insert("Category", "", "", md.getCategory(), -1, 0);
                md.setRang(1);
            }


            if (requestCode == UPDATE_ACTIVITY){
                dbFilms.update(md);

            }
            if ((requestCode == SINGLE_FILM)){
                dbFilms.delete(is);
                dbFilms.update(md);
            }


            else{
                dbFilms.insert(md.getName(), md.getAuthor(), md.getComment(), md.getCategory(), -1, 1);
            }
            updateList();
        }
        else if(resultCode == ADD_TO_VIEWED){
            ClassFilms md = (ClassFilms) data.getExtras().getSerializable("VFilms");
            Bundle result = new Bundle();
            result.putSerializable("VFilm", md);
            getParentFragmentManager().setFragmentResult("VFilm", result);
            if(requestCode == SINGLE_FILM){
                dbFilms.delete(is_add);
                dbFilms.delete(md.getId());
            }
            else if (requestCode == UPDATE_ACTIVITY){
                dbFilms.delete(md.getId());
            }
            updateList();



        }
    }



}