package com.example.projectkino1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectkino1.adapters.FilmAdapter;
import com.example.projectkino1.data.DBFilms;
import com.example.projectkino1.data.DBViewedFilms;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ViewedFilms extends Fragment {

    private FloatingActionButton add_qt;
    private FloatingActionButton add_vw;
    int ADD_ACTIVITY = 0;
    int UPDATE_ACTIVITY = 1;
    int SINGLE_FILM = 2;
    final int CANCEL = 2, ADD_TO_VIEWED = 3, ADD_TO_VIEWED_NEED_DELETE = 4;
    long is, is_add, is_delete;
    private TextView tw;
    private DBViewedFilms dbVFilms;
    List<ClassFilms> list_films, prob;
    private RecyclerView mRecyclerView;
    private FilmAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ImageButton del;

    public ViewedFilms() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // менеджер компоновки, который позволяет получать доступ к layout с наших ресурсов
        View view = inflater.inflate(R.layout.fragment_viewed_films, container, false);
        dbVFilms = new DBViewedFilms(getContext());
        getParentFragmentManager().setFragmentResultListener("VFilm", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                ClassFilms result = (ClassFilms) bundle.getSerializable("VFilm");
                int rating = 0;
                if(result.getRang() == 0){
                    dbVFilms.insert("Category", "", "", result.getCategory(), -1, 0);
                    result.setRang(1);
                }
                if(result.getReview() != -1){
                    rating = result.getReview();
                }
                dbVFilms.insert(result.getName(), result.getAuthor(), result.getComment(),
                        result.getCategory(), rating, result.getRang() );
                updateList();

            }
        });
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
        add_vw = (FloatingActionButton) view.findViewById(R.id.adding_viewed);
        add_vw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddViewed.class);
                startActivityForResult(i, ADD_ACTIVITY);
                updateList();

            }
        });
        dbVFilms = new DBViewedFilms(getContext());
        list_films = dbVFilms.selectAll();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_viewed);

        // если вы уверены, что изменения в контенте не изменят размер лайота, то передаём параметр true - это увеличивает производительность
        mRecyclerView.setHasFixedSize(true);

        // используем LinearLayoutManager для отображения как ListView
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        FilmAdapter.OnStateClickListener stateClickListener = new FilmAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(ClassFilms film, int position) {
                Intent i = new Intent(getActivity(), AddViewed.class);
                is = mAdapter.isFilmSingle(film);
                i.putExtra("VFilms", film);
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
                is_delete = mAdapter.isFilmSingle(film);
                if(is_delete != -1){
                    dbVFilms.delete(is_delete);
                }
                dbVFilms.delete(film.getId());
                updateList();
            }
        };
        mAdapter = new FilmAdapter(getActivity(), list_films, stateClickListener, addClickListener);
        mRecyclerView.setAdapter(mAdapter);





        return view;
    }

    private void updateList () {
        mAdapter.setData(dbVFilms.selectAll());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if(resultCode == ADD_TO_VIEWED){
            ClassFilms md = (ClassFilms) data.getExtras().getSerializable("VFilms");
            if(md.getRang() == 0){
                dbVFilms.insert("Category", "", "", md.getCategory(), -1, 0);
                md.setRang(1);
            }


            if (requestCode == UPDATE_ACTIVITY){
                dbVFilms.update(md);

            }
            if ((requestCode == SINGLE_FILM)){
                dbVFilms.delete(is);
                dbVFilms.update(md);
            }


            else{
                dbVFilms.insert(md.getName(), md.getAuthor(), md.getComment(), md.getCategory(), md.getReview(), 1);
            }
            updateList();



        }
    }

}