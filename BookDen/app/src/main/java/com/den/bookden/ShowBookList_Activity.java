package com.den.bookden;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import com.den.bookden.app.AppConfig;
import com.den.bookden.app.AppController;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.den.bookden.app.AppController;

/**
 * Created by Shubhi on 5/9/2016.
 */
public class ShowBookList_Activity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private BooksAdapter mAdapter;
    private Toolbar toolbar;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_DATA = "data";
    private static final String TAG_TITLE = "title";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_PUBLICATION = "publication";
    private static final String TAG_LINK = "link";

    // URL to get contacts JSON
    private static String url = AppConfig.URL_START;
    JSONArray books = null;
    private List<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showbooklist);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String category = extras.getString("category");
            String type = extras.getString("type");
           // Toast.makeText(this, category + " " + type, Toast.LENGTH_LONG).show();
            url = AppConfig.URL_START;
            url += "getBooks.php?type=" + type + "&&category=" + category;
            this.setTitle(category + " " + type);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new BooksAdapter(bookList);


        new GetBooks().execute();

        //prepareMovieData();
        //Toast.makeText(ShowBookList_Activity.this, url, Toast.LENGTH_LONG).show();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Book book = bookList.get(position);
                //Toast.makeText(getApplicationContext(), book.getTitle()+" "+book.getAuthor()+" "+book.getPublication()+ " is selected!", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(ShowBookList_Activity.this,BookDetailActivity.class);
                i.putExtra("title",book.getTitle());
                i.putExtra("author",book.getAuthor());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        mAdapter.notifyDataSetChanged();
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


//    private void prepareMovieData() {
//        Book book = new Book("Mad Max: Fury Road", "Action & Adventure", "2015");
//        bookList.add(book);
//        mAdapter.notifyDataSetChanged();
//    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetBooks extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ShowBookList_Activity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    books = jsonObj.getJSONArray(TAG_DATA);

                    // looping through All Contacts
                    for (int i = 0; i < books.length(); i++) {
                        JSONObject c = books.getJSONObject(i);

                        String title = c.getString(TAG_TITLE);
                        String author = c.getString(TAG_AUTHOR);
                        String publication = c.getString(TAG_PUBLICATION);
                        String link=c.getString(TAG_LINK);
                        Book book = new Book(title, author, publication, link);
                        bookList.add(book);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ShowBookList_Activity.this, "No Books Found", Toast.LENGTH_LONG).show();
                // Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            /**
             * Updating parsed JSON data into ListView
             * */
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(ShowBookList_Activity.this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(mAdapter);        }

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ShowBookList_Activity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ShowBookList_Activity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
