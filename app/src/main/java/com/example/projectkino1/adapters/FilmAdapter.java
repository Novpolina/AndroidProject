package com.example.projectkino1.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkino1.R;
import com.example.projectkino1.ClassFilms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnStateClickListener{
        void onStateClick(ClassFilms film, int position);
    }
    public interface OnAddClickListener{
        void onClick(ClassFilms film);
    }

    private final OnStateClickListener onClickListener;
    private final OnAddClickListener onAddClickListener;
    private List<ClassFilms> data;

    private static final int TYPE_HEADER=0;
    private static final int TYPE_ITEM_V = 2;
    private static final int TYPE_ITEM =1;
    private LayoutInflater inflater;
    private Context context;
    public FilmAdapter(Context context, List<ClassFilms> data,
                       OnStateClickListener onClickListener, OnAddClickListener onAddClickListener){
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
            HeaderHolder holder=new HeaderHolder(view);
            return holder;
        }
        else if(viewType == TYPE_ITEM ){
            View view=inflater.inflate(R.layout.film_item_layout, parent,false);
            ItemHolder holder=new ItemHolder(view);
            return holder;
        }
        else{
            View view=inflater.inflate(R.layout.viewed_layout, parent,false);
            ItemVHolder holder=new ItemVHolder(view);
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
        if(holder instanceof HeaderHolder){
            HeaderHolder headerHolder= (HeaderHolder) holder;
            ClassFilms film = data.get(position);
            headerHolder.bindHeader(film);

        }
        else if(holder instanceof ItemHolder){
            ItemHolder itemHolder= (ItemHolder) holder;
            ClassFilms header =data.get(position);
            itemHolder.bindFilm(header);
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    onClickListener.onStateClick(header, position);
                }
            });
        }
        else{
            ItemVHolder itemHolder= (ItemVHolder) holder;
            ClassFilms header =data.get(position);
            itemHolder.bindFilm(header);
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
        TextView title, comment;
        ImageView icon;
        ImageButton add;
        public ItemHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.filmname);
            comment = (TextView) itemView.findViewById(R.id.filmcomment);
            add = (ImageButton) itemView.findViewById(R.id.is_viewed);
        }
        public void bindFilm(ClassFilms film){
            title.setText(film.getName());
            comment.setText(film.getComment());
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAddClickListener.onClick(film);
                }
            });
        }
    }
    class ItemVHolder extends RecyclerView.ViewHolder{
        TextView title, comment, rating;
        ImageButton add;
        public ItemVHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.filmname_viewed);
            comment = (TextView) itemView.findViewById(R.id.filmcomment_viewed);
            add = (ImageButton) itemView.findViewById(R.id.delete_item);
            rating = (TextView) itemView.findViewById(R.id.rating_view);
        }
        public void bindFilm(ClassFilms film){
            title.setText(film.getName());
            comment.setText(film.getComment());
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

