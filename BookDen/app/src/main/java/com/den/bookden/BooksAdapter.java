package com.den.bookden;

/**
 * Created by Shubhi on 5/10/2016.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

    private List<Book> booksList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, author, publication,link;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            author = (TextView) view.findViewById(R.id.publication);
            publication = (TextView) view.findViewById(R.id.author);
            link=(TextView)view.findViewById(R.id.link);
        }
    }


    public BooksAdapter(List<Book> booksList) {
        this.booksList = booksList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Book book = booksList.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.publication.setText(book.getPublication());
        holder.link.setText(book.getLink());
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }
}