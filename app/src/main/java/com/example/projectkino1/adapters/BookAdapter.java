package com.example.projectkino1.adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkino1.AddFilms;
import com.example.projectkino1.ClassFilms;
import com.example.projectkino1.Films;
import com.example.projectkino1.R;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnStateClickListener{
        void onStateClick(ClassFilms film, int position);
    }
    public interface OnAddClickListener{
        void onClick(ClassFilms film);
    }

    private final BookAdapter.OnStateClickListener onClickListener;
    private final BookAdapter.OnAddClickListener onAddClickListener;
    private List<ClassFilms> data;

    private static final int TYPE_HEADER=0;
    private static final int TYPE_ITEM=1;
    private static final int TYPE_ITEM_V = 2;
    private static final int ADD_ACTIVITY = 0;
    private LayoutInflater inflater;
    private Context context;
    public BookAdapter(Context context, List<ClassFilms> data,
                       BookAdapter.OnStateClickListener onClickListener, BookAdapter.OnAddClickListener onAddClickListener ){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.data = data;
        this.onClickListener = onClickListener;
        this.onAddClickListener = onAddClickListener;
    }
    public List<ClassFilms> getData() {
        return data;
    }

    public void setData(List<ClassFilms> data) {
        this.data = data;
    }

    public void delete(int position){
        data.remove(position);

        notifyItemRemoved(position);
    }
    public long isFilmSingle(ClassFilms film){
        int pos = data.indexOf(film);
        if(data.get(pos-1).getRang() == 0 && (pos == data.size() - 1)){
            return data.get(pos - 1).getId();
        }
        else if(data.get(pos-1).getRang() == 0 && data.get(pos+1).getRang() == 0){
            return data.get(pos - 1).getId();
        }
        else{
            return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_HEADER){
            View view=inflater.inflate(R.layout.category_item_layout, parent,false);
            BookAdapter.HeaderHolder holder=new BookAdapter.HeaderHolder(view);
            return holder;
        }
        else if(viewType == TYPE_ITEM ){
            View view=inflater.inflate(R.layout.book_item, parent,false);
            BookAdapter.ItemHolder holder=new BookAdapter.ItemHolder(view);
            return holder;
        }
        else{
            View view=inflater.inflate(R.layout.book_read_item, parent,false);
           BookAdapter.ItemVHolder holder=new BookAdapter.ItemVHolder(view);
            return holder;

        }

    }

    @Override
    public int getItemViewType(int position) {
        ClassFilms film = data.get(position);
        int rang = film.getRang();
        if(rang==0){
            return TYPE_HEADER;
        }
        else {
            if(film.getReview() >= 0){
                return TYPE_ITEM_V;
            }
            else {
                return TYPE_ITEM;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(holder instanceof BookAdapter.HeaderHolder){
            BookAdapter.HeaderHolder headerHolder= (BookAdapter.HeaderHolder) holder;
            ClassFilms film = data.get(position);
            headerHolder.bindHeader(film);

        }
        else if(holder instanceof BookAdapter.ItemHolder){
            BookAdapter.ItemHolder itemHolder= (BookAdapter.ItemHolder) holder;
            ClassFilms header =data.get(position);
            itemHolder.bindBook(header);
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    onClickListener.onStateClick(header, position);
                }
            });
        }
        else{
            BookAdapter.ItemVHolder itemHolder= (BookAdapter.ItemVHolder) holder;
            ClassFilms header =data.get(position);
            itemHolder.bindBook(header);
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    onClickListener.onStateClick(header, position);
                }
            });

        }


    }
    @Override
    public int getItemCount() {

        return data.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView title, comment, author;
        ImageView icon;
        ImageButton check;
        public ItemHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.bookname);
            author = (TextView) itemView.findViewById(R.id.bookauthor);
            comment = (TextView) itemView.findViewById(R.id.bookcomment);
            check = (ImageButton) itemView.findViewById(R.id.is_read);
        }
        public void bindBook(ClassFilms film){
            title.setText(film.getName());
            comment.setText(film.getComment());
            author.setText("Автор: " + film.getAuthor());
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAddClickListener.onClick(film);

                }
            });
        }
    }
    class ItemVHolder extends RecyclerView.ViewHolder{
        TextView title, comment, rating, author;
        ImageButton add;
        public ItemVHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.bookname_r);
            comment = (TextView) itemView.findViewById(R.id.bookcomment_r);
            author = (TextView) itemView.findViewById(R.id.bookauthor_r);
            add = (ImageButton) itemView.findViewById(R.id.delete_book);
            rating = (TextView) itemView.findViewById(R.id.rating_book);
        }
        public void bindBook(ClassFilms film){
            title.setText(film.getName());
            comment.setText(film.getComment());
            author.setText("Автор: " + film.getAuthor());
            rating.setText(String.valueOf(film.getReview()));
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAddClickListener.onClick(film);
                }
            });
        }
    }
    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView title;

        public HeaderHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.CategoryName);

        }
        public void bindHeader(ClassFilms film){
            title.setText(film.getCategory());

        }
    }
}
