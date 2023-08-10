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

import com.example.projectkino1.adapters.BookAdapter;
import com.example.projectkino1.data.DBBooks;
import com.example.projectkino1.data.DBReadBooks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ReadBooks extends Fragment {

    private FloatingActionButton add_vw;
    int ADD_ACTIVITY = 0;
    int UPDATE_ACTIVITY = 1;
    int SINGLE_BOOK = 2, ADD_TO_READ = 4;
    long is, is_delete;
    private TextView tw;
    private DBReadBooks dbBooks;
    List<ClassFilms> list_films, prob;
    private RecyclerView mRecyclerView;
    private BookAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ImageButton del;


    public ReadBooks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_read_books, container, false);

        getParentFragmentManager().setFragmentResultListener("RBook", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                ClassFilms result = (ClassFilms) bundle.getSerializable("RBook");
                int rating = 0;
                if(result.getRang() == 0){
                    dbBooks.insert("Category", "", "", result.getCategory(), -1, 0);
                    result.setRang(1);
                }
                if(result.getReview() > 0){
                    rating = result.getReview();
                }
                dbBooks.insert(result.getName(), result.getAuthor(), result.getComment(),
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
        add_vw = (FloatingActionButton) view.findViewById(R.id.add_viewed_book);
        add_vw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddRead.class);
                startActivityForResult(i, ADD_ACTIVITY);
                updateList();

            }
        });
        dbBooks = new DBReadBooks(getContext());
        list_films = dbBooks.selectAll();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_of_rbooks);

        // если вы уверены, что изменения в контенте не изменят размер лайота, то передаём параметр true - это увеличивает производительность
        mRecyclerView.setHasFixedSize(true);

        // используем LinearLayoutManager для отображения как ListView
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        BookAdapter.OnStateClickListener stateClickListener = new BookAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(ClassFilms film, int position) {
                Intent i = new Intent(getActivity(), AddBooks.class);
                is = mAdapter.isFilmSingle(film);
                i.putExtra("RBooks", film);
                if(is != -1){
                    startActivityForResult(i, SINGLE_BOOK);
                }
                else {
                    startActivityForResult(i, UPDATE_ACTIVITY);
                }
                updateList();

            }
        };
        BookAdapter.OnAddClickListener addClickListener = new BookAdapter.OnAddClickListener() {
            @Override
            public void onClick(ClassFilms film) {
                is_delete = mAdapter.isFilmSingle(film);
                if(is_delete != -1){
                    dbBooks.delete(is_delete);
                }
                dbBooks.delete(film.getId());
                updateList();


            }
        };
        mAdapter = new BookAdapter(getActivity(), list_films, stateClickListener, addClickListener);
        mRecyclerView.setAdapter(mAdapter);





        return view;
    }

    private void updateList () {
        mAdapter.setData(dbBooks.selectAll());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if (resultCode == ADD_TO_READ) {
            ClassFilms md = (ClassFilms) data.getExtras().getSerializable("RBooks");
            if(md.getRang() == 0){
                dbBooks.insert("Category", "", "", md.getCategory(), -1, 0);
            }


            if (requestCode == UPDATE_ACTIVITY){
                dbBooks.update(md);

            }
            if ((requestCode == SINGLE_BOOK)){
                dbBooks.delete(is);
                dbBooks.update(md);
            }

            else{
                dbBooks.insert(md.getName(), md.getAuthor(), md.getComment(), md.getCategory(), md.getReview(), 1);
            }
            updateList();
        }
    }
}