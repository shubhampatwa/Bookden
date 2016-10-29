package com.den.bookden;

import android.app.ProgressDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.den.bookden.app.AppConfig;
import com.den.bookden.app.AppController;
import com.den.bookden.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shubhi on 5/13/2016.
 */
public class BookDetailActivity extends AppCompatActivity {
    private static final String TAG = BookDetailActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private BooksAdapter mAdapter;
    private Toolbar toolbar;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ID = "id";
    private static final String TAG_DETAIL = "detail";
    private static final String TAG_TITLE = "title";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_PUBLICATION = "publication";
    private static final String TAG_PAGES = "pages";
    private static final String TAG_TYPE = "type";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_E_COMMERCE = "e_commerce_link";
    private static final String TAG_GOODREAD = "goodread";
    private static final String TAG_LANGUAGE = "language";
    private static final String TAG_SAMPLE = "sample";
    private static final String TAG_COVER = "cover";
    private TextView txtTitle, txtAuthor, txtPublication, txtPages, txtType, txtCategory, txtEcommerce, txtLanguage;
    private String title1, author1, id, publication, type, category, e_commerce, language,goodread ,sample = "a.pdf", cover;
    private Button samplebook, share,e_commerce_link,goodread_link;
    private int pages;
    private SQLiteHandler db;

    // URL to get contacts JSON
    private static String url = AppConfig.URL_START;
    JSONArray books = null;
    private List<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtTitle = (TextView) findViewById(R.id.title);
        txtAuthor = (TextView) findViewById(R.id.author);
        txtPublication = (TextView) findViewById(R.id.publication);
        txtPages = (TextView) findViewById(R.id.pages);
        txtType = (TextView) findViewById(R.id.type);
        txtCategory = (TextView) findViewById(R.id.category);
        txtLanguage = (TextView) findViewById(R.id.language);
        e_commerce_link = (Button) findViewById(R.id.e_commerce);
        goodread_link=(Button)findViewById(R.id.goodread);
        samplebook = (Button) findViewById(R.id.sample);
        share = (Button) findViewById(R.id.share);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("title");
            String author = extras.getString("author");
            // Toast.makeText(this, category + " " + type, Toast.LENGTH_LONG).show();
            url = AppConfig.URL_START + "getBooks.php";
            sendrequest(title, author);
//           try {
//
//            url += URLEncoder.encode("title=" + title + "&&author=" + author,"UTF-8");
//            Log.e(TAG,url);
//            Toast.makeText(BookDetailActivity.this,url,Toast.LENGTH_SHORT).show();
//           }
//           catch (Exception e)
//           {
//
//           }
            //this.setTitle(category + " " + type);
        }
        //new GetDetail().execute();
//txtTitle.setText("Title :"+title);
        e_commerce_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addclass = new Intent(Intent.ACTION_VIEW, Uri.parse(e_commerce));
                startActivity(addclass);
            }
        });
        goodread_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addclass = new Intent(Intent.ACTION_VIEW, Uri.parse(goodread));
                startActivity(addclass);
            }
        });
        samplebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addclass = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.URL_START+sample));
                startActivity(addclass);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = new SQLiteHandler(getApplicationContext());

                HashMap<String, String> user = db.getUserDetails();
                String email = user.get("email");
                url = AppConfig.URL_START+"userbook.php?email=" + email + "&&id=" + id;
                new GetBooks().execute();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "get the lattest books from here!!";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Bookden");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });


    }

    private void sendrequest(final String title, final String author) {
        // Tag used to cancel the request
        String tag_string_req = "req_data";

//        pDialog.setMessage("Logging in ...");
        //      showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    //Log.e(TAG, "onResponse: ", );
                    books = jObj.getJSONArray(TAG_DETAIL);
                    //  looping through All Contacts
                    for (int i = 0; i < books.length(); i++) {
                        JSONObject c = books.getJSONObject(i);
//                         title1 = c.getString(TAG_TITLE);
//                         author1 = c.getString(TAG_AUTHOR);
                        id = c.getString(TAG_ID);
                        publication = c.getString(TAG_PUBLICATION);
                        pages = c.getInt(TAG_PAGES);
                        type = c.getString(TAG_TYPE);
                        category = c.getString(TAG_CATEGORY);
                        e_commerce = c.getString(TAG_E_COMMERCE);
                        goodread=c.getString(TAG_GOODREAD);
                        language = c.getString(TAG_LANGUAGE);
                        sample = c.getString(TAG_SAMPLE);
                        cover = c.getString(TAG_COVER);
                        txtTitle.setText("Title :" + title);
                        txtAuthor.setText("Author :" + author);
                        txtPublication.setText("Publication :" + publication);
                        txtPages.setText("Pages :" + pages);
                        txtType.setText("Type :" + type);
                        txtCategory.setText("Category :" + category);
                        txtLanguage.setText("Language :" + language);
                        e_commerce_link.setText("E_commerce"+e_commerce);
                        goodread_link.setText("GoodRead"+goodread);
                       // txtEcommerce.setText("Link :" + e_commerce);
                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
//                Toast.makeText(MainActivity.this,
//                        error.getMessage(), Toast.LENGTH_LONG).show();
                //            hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", title);
                params.put("author", author);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private class GetBooks extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(BookDetailActivity.this);
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


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();


        }
    }
}
//    private void showDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }
//
//    private void hideDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }
//    private class GetDetail extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Showing progress dialog
//            pDialog = new ProgressDialog(BookDetailActivity.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
////            pDialog.show();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            // Creating service handler class instance
//            ServiceHandler sh = new ServiceHandler();
//
//            // Making a request to url and getting response
//            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
//
//            Log.d("Response: ", "> " + jsonStr);
//
//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//                    // Getting JSON Array node
//                    books = jsonObj.getJSONArray(TAG_DETAIL);
//                    // looping through All Contacts
//                    for (int i = 0; i < books.length(); i++) {
//                        JSONObject c = books.getJSONObject(i);
//                         title = c.getString(TAG_TITLE);
//                         author = c.getString(TAG_AUTHOR);
//                         publication = c.getString(TAG_PUBLICATION);
//                         pages=c.getInt(TAG_PAGES);
//                         type=c.getString(TAG_TYPE);
//                         category=c.getString(TAG_CATEGORY);
//                         e_commerce=c.getString(TAG_E_COMMERCE);
//                        language=c.getString(TAG_LANGUAGE);
//                         sample=c.getString(TAG_SAMPLE);
//                         cover=c.getString(TAG_COVER);
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Toast.makeText(BookDetailActivity.this, "No Books Found", Toast.LENGTH_LONG).show();
//                // Log.e("ServiceHandler", "Couldn't get any data from the url");
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            // Dismiss the progress dialog
//            if (pDialog.isShowing())
//                pDialog.dismiss();
//            txtTitle.setText("Title :"+title);
//            txtAuthor.setText("Author :"+author);
//            txtPublication.setText("Publication :"+publication);
//            txtPages.setText("Pages :"+pages);
//            txtType.setText("Title :"+type);
//            txtCategory.setText("Title :"+category);
//            txtLanguage.setText("Title :"+language);
//            txtEcommerce.setText("Link :"+e_commerce);
//            /**
//             * Updating parsed JSON data into ListView
//             * */
////            ListAdapter adapter = new SimpleAdapter(
////                    ShowBookList_Activity.this, bookList,
////                    R.layout.list_item, new String[] { TAG_TITLE, TAG_AUTHOR,
////                    TAG_PUBLICATION }, new int[] { R.id.title,
////                    R.id.author, R.id.publication });
////
////            setListAdapter(adapter);
//        }
//
//    }

