package com.example.projectkino1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectkino1.data.DBFilms;
import com.example.projectkino1.data.DBViewedFilms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddFilms extends AppCompatActivity {
    private Button btSave,btCancel;
    private EditText FilmN, Comment, Author;
    private int rating;
    private Context context;
    private long MyFilmID;
    private DBFilms db;
    private DBViewedFilms view;
    AutoCompleteTextView Category;
    private ArrayList<String> DefaultCategoryList, NewCategoryList, CategoryList, ViewedCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_films);
        db = new DBFilms(this);
        view = new DBViewedFilms(this);
        FilmN =(EditText)findViewById(R.id.editVFilm);
        Comment =(EditText)findViewById(R.id.editComment);
        Author = (EditText)findViewById(R.id.editAuthor);
        Category = (AutoCompleteTextView) findViewById(R.id.category_list_films);
        btSave=(Button)findViewById(R.id.Save);
        btCancel=(Button)findViewById(R.id.Cancel);

        if(getIntent().hasExtra("Films")){
            ClassFilms vf =(ClassFilms) getIntent().getSerializableExtra("Films");
            FilmN.setText(vf.getName());
            Comment.setText(vf.getComment());
            Author.setText(vf.getAuthor());
            Category.setText(vf.getCategory());
            MyFilmID =vf.getId();
        }
        else
        {
            MyFilmID =-1;
        }
        DefaultCategoryList = new ArrayList<>();
        Collections.addAll(DefaultCategoryList, getResources().getStringArray(R.array.Films));
        NewCategoryList = db.selectCategories();
        CategoryList= joinLists( NewCategoryList, DefaultCategoryList);

        //Create Array Adapter
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, CategoryList);
        //Find TextView control
        //Set the number of characters the user must type before the drop down list is shown
        //Category.setThreshold(0);
        //Set the adapter
        Category.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, CategoryList));
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FilmN.getText().toString().equals("")){
                    FilmN.setHintTextColor(getResources().getColor(R.color.red));
                    FilmN.setHint("Добавьте название фильма");

                }
                else {
                    String new_category = Category.getText().toString();
                    if(new_category.equals("")){
                        new_category = "Все";
                    }
                    ClassFilms vFilm = new ClassFilms(MyFilmID, FilmN.getText().toString(),
                            Comment.getText().toString(), Author.getText().toString(), new_category, -1, 1);
                    if (!NewCategoryList.contains(new_category)) {
                        vFilm.setRang(0);
                    }
                    Intent intent = getIntent();
                    intent.putExtra("Films", vFilm);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
    public void Save(ClassFilms film){

    }
    public static ArrayList<String> joinLists(
            final List<String> listA,
            final List<String> listB) {

        boolean aEmpty = (listA == null) || listA.isEmpty();
        boolean bEmpty = (listB == null) || listB.isEmpty();
        //побитное И!
        if (aEmpty & bEmpty) {
            // оба пустые — отдаем новый пустой список
            return new ArrayList<String>();
        } else if (aEmpty) {
            // один пустой — отдаем копию другого, содержащую все его элементы
            return new ArrayList<String>(listB);
        } else if (bEmpty) {
            return new ArrayList<String>(listA);
        } else {
            // оба непустые — объединяем
            ArrayList<String> result = new ArrayList<String>();
            result.addAll(listA);
            result.removeAll(listB);
            result.addAll(listB);
            return result;
        }
    }

}