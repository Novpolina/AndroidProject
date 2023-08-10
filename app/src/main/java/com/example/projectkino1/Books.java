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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectkino1.adapters.BookAdapter;
import com.example.projectkino1.adapters.FilmAdapter;
import com.example.projectkino1.data.DBBooks;
import com.example.projectkino1.data.DBFilms;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Books extends Fragment {
    interface OnFragmentSendDataListener {
        void onSendData(ClassFilms data);
    }

    private Films.OnFragmentSendDataListener fragmentSendDataListener;

    private FloatingActionButton add_vw;
    int ADD_ACTIVITY = 0;
    int UPDATE_ACTIVITY = 1;
    int SINGLE_BOOK = 2, ADD_TO_READ = 4;
    long is, is_add;
    private TextView tw;
    private DBBooks dbBooks;
    List<ClassFilms> list_films, prob;
    private RecyclerView mRecyclerView;
    private BookAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ImageButton del;



    public Books() {
        // Required empty public constructor
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentSendDataListener = (Films.OnFragmentSendDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
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
        add_vw = (FloatingActionButton) view.findViewById(R.id.add_want_book);
        add_vw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddBooks.class);
                startActivityForResult(i, ADD_ACTIVITY);
                updateList();

            }
        });
        dbBooks = new DBBooks(getContext());
        tw = (TextView) view.findViewById(R.id.textView5);
        list_films = dbBooks.selectAll();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_of_books);

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
                i.putExtra("Books", film);
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
                Intent i = new Intent(getActivity(), AddRead.class); //поменять
                is_add = mAdapter.isFilmSingle(film);
                i.putExtra("RBooks", film);
                if(is_add != -1){
                    startActivityForResult(i, SINGLE_BOOK);
                }
                else {
                    startActivityForResult(i, UPDATE_ACTIVITY);
                }
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
        if (resultCode == Activity.RESULT_OK) {
            ClassFilms md = (ClassFilms) data.getExtras().getSerializable("Books");
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
                dbBooks.insert(md.getName(), md.getAuthor(), md.getComment(), md.getCategory(), -1, 1);
            }
            updateList();
        }
        else if(resultCode == ADD_TO_READ){
            ClassFilms md = (ClassFilms) data.getExtras().getSerializable("RBooks");
            Bundle result = new Bundle();
            result.putSerializable("RBook", md);
            getParentFragmentManager().setFragmentResult("RBook", result);
            if(requestCode == SINGLE_BOOK){
                dbBooks.delete(is_add);
                dbBooks.delete(md.getId());
            }
            else if (requestCode == UPDATE_ACTIVITY){
                dbBooks.delete(md.getId());
            }
            updateList();

        }
    }
}