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
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.projectkino1.data.DBBooks;
import com.example.projectkino1.data.DBFilms;
import com.example.projectkino1.data.DBReadBooks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddRead extends AppCompatActivity {

    private Button btSave,btCancel;
    private EditText FilmN, Comment, Author;
    private int rating;
    private Context context;
    private long MyFilmID;
    private DBBooks db;
    private DBReadBooks read;
    AutoCompleteTextView Category;
    private RatingBar ratingBar;
    final int CANCEL = 2, ADD_TO_READ = 4, ADD_TO_VIEWED_NEED_DELETE = 4;
    private ArrayList<String> DefaultCategoryList, NewCategoryList, CategoryList, AddCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_read);
        db = new DBBooks(this);
        read = new DBReadBooks(this);
        FilmN =(EditText)findViewById(R.id.editRBook);
        Comment =(EditText)findViewById(R.id.editComment_rbooks);
        Author = (EditText)findViewById(R.id.editAuthor_rbook);
        Category = (AutoCompleteTextView) findViewById(R.id.category_list_rbooks);
        ratingBar = (RatingBar) findViewById(R.id.ratingBarrBooks);
        btSave=(Button)findViewById(R.id.Save_rook);
        btCancel=(Button)findViewById(R.id.Cancel_rbook);

        if(getIntent().hasExtra("RBooks")){
            ClassFilms vf =(ClassFilms) getIntent().getSerializableExtra("RBooks");
            FilmN.setText(vf.getName());
            Comment.setText(vf.getComment());
            Author.setText(vf.getAuthor());
            if(vf.getReview() != -1){
                ratingBar.setRating(vf.getReview());
            }
            else {
                ratingBar.setRating(0);
            }
            Category.setText(vf.getCategory());
            MyFilmID =vf.getId();
        }
        else
        {
            MyFilmID =-1;
        }
        DefaultCategoryList = new ArrayList<String>();
        Collections.addAll(DefaultCategoryList, getResources().getStringArray(R.array.Books));
        NewCategoryList = db.selectCategories();
        AddCategoryList = read.selectCategories();
        CategoryList= joinLists(NewCategoryList, DefaultCategoryList);
        CategoryList = joinLists(AddCategoryList, CategoryList);

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
                    FilmN.setHint("Добавьте название книги");

                }
                else {
                    String new_category = Category.getText().toString();
                    if(new_category.equals("")){
                        new_category = "Все";
                    }
                    ClassFilms vFilm = new ClassFilms(MyFilmID, FilmN.getText().toString(),
                            Comment.getText().toString(), Author.getText().toString(),
                            new_category, (int) ratingBar.getRating(), 1);
                    //Toast.makeText(getApplicationContext(), Category.getText().toString(), Toast.LENGTH_LONG).show();
                    if (!AddCategoryList.contains(new_category)) {
                        vFilm.setRang(0);
                        Intent intent = getIntent();
                        intent.putExtra("RBooks", vFilm);
                        setResult(ADD_TO_READ, intent);
                        finish();
                        //cdb.insert("", "", "", new_category, 1, 0);
                    } else {
                        Intent intent = getIntent();
                        intent.putExtra("RBooks", vFilm);
                        setResult(ADD_TO_READ, intent);
                        finish();
                    }
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(CANCEL);
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