package com.den.bookden;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.den.bookden.app.*;
import com.den.bookden.helper.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private BooksAdapter mAdapter;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private TextView txtName;
   // private TextView txtEmail;
    //private Button btnLogout;
   private static final String TAG_DATA = "detail";
    private static final String TAG_TITLE = "title";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_PUBLICATION="publication";
    private static final String TAG_LINK = "link";
    // URL to get contacts JSON
    private static String url = AppConfig.URL_START;
    JSONArray books = null;
    private List<Book> bookList = new ArrayList<>();

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new BooksAdapter(bookList);


        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        onNavigationDrawerItemSelected(0);

        txtName = (TextView) findViewById(R.id.name);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");
        txtName.setText("Welcome " + name + " to your bookden account");
        url = AppConfig.URL_START;
        url += "userbook.php?email=" + email;
        new GetBooks().execute();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ShowBookList_Activity.ClickListener()
        {
            @Override
            public void onClick(View view, int position) {
                Book book = bookList.get(position);
                Toast.makeText(getApplicationContext(), book.getTitle()+" "+book.getAuthor()+" "+ book.getPublication()+" "+book.getLink()+ " is selected!", Toast.LENGTH_SHORT).show();
                Intent addclass = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.URL_START+book.getLink()));
                startActivity(addclass);
//                Intent i=new Intent(Main.this,BookDetailActivity.class);
//                i.putExtra("title",book.getTitle());
//                i.putExtra("author",book.getAuthor());
//                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        if (pDialog.isShowing())
            pDialog.dismiss();

        mAdapter.notifyDataSetChanged();
    }
    private class GetBooks extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Main.this);
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
                        String publication=c.getString(TAG_PUBLICATION);
                        String link = c.getString(TAG_LINK);
                        //Toast.makeText(Main.this,link,Toast.LENGTH_LONG).show();
                        Book book = new Book(title, author,publication, link);
                        bookList.add(book);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(Main.this, "No Books Found", Toast.LENGTH_LONG).show();
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
            recyclerView.addItemDecoration(new DividerItemDecoration(Main.this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(mAdapter);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logoutUser();

        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(Main.this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        onNavigationDrawerItemSelected(position);
    }



    public void onNavigationDrawerItemSelected(int position) {
        switch(position) {
            case 0:
                break;
            case 1:
                Intent i=new Intent(this,ShowBookList_Activity.class);
                i.putExtra("category","romance");
                i.putExtra("type","fiction");
                startActivity(i);
                break;
            case 2:
                Intent thriller=new Intent(this,ShowBookList_Activity.class);
                thriller.putExtra("category","thriller");
                thriller.putExtra("type","fiction");
                startActivity(thriller);
                break;
            case 3:
                Intent mystery=new Intent(this,ShowBookList_Activity.class);
                mystery.putExtra("category","mystery");
                mystery.putExtra("type","fiction");
                startActivity(mystery);
                break;
            case 4:
                Intent short_stories=new Intent(this,ShowBookList_Activity.class);
                short_stories.putExtra("category","short_stories");
                short_stories.putExtra("type","fiction");
                startActivity(short_stories);
                break;
            case 5:
                Intent fantasy=new Intent(this,ShowBookList_Activity.class);
                fantasy.putExtra("category","fantasy");
                fantasy.putExtra("type","fiction");
                startActivity(fantasy);
                break;
            case 6:
                Intent myth=new Intent(this,ShowBookList_Activity.class);
                myth.putExtra("category","myths_legends_and_sagas");
                myth.putExtra("type","fiction");
                startActivity(myth);
                break;
            case 7:
                Intent biography=new Intent(this,ShowBookList_Activity.class);
                biography.putExtra("category","biography");
                biography.putExtra("type","non-fiction");
                startActivity(biography);
                break;
            case 8:
                Intent auto=new Intent(this,ShowBookList_Activity.class);
                auto.putExtra("category","autobiography");
                auto.putExtra("type","non-fiction");
                startActivity(auto);
                break;
            case 9:
                Intent poem=new Intent(this,ShowBookList_Activity.class);
                poem.putExtra("category","Poems");
                poem.putExtra("type","non-fiction");
                startActivity(poem);
                break;

        }
    }
}
