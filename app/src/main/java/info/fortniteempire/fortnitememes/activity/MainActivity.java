package info.fortniteempire.fortnitememes.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import info.fortniteempire.fortnitememes.adapter.GalleryAdapter;
import info.fortniteempire.fortnitememes.app.AppController;
import info.fortniteempire.fortnitememes.model.Image;
import info.fortniteempire.fortnitememes.R;

public class MainActivity extends AppCompatActivity {


    public DrawerLayout mDrawerLayout;
    public NavigationView mNavigationView;
    private String TAG = MainActivity.class.getSimpleName();
    private static final String endpoint = "https://gist.githubusercontent.com/DaFaack/cea41308cf664d1e5bfc4b5ee331a2ea/raw/Memes";
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    public static int advertisingcounter = 0;
    public TextView internet;
    public Button tryagain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        internet = findViewById(R.id.internetTV);
        tryagain = findViewById(R.id.tryagain);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fetchImages();
        navigationView();
        firstRunDialog();
    }

    private void fetchImages() {

        pDialog.setMessage("Loading Images...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(endpoint,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();

                        images.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Image image = new Image();

                                JSONObject url = object.getJSONObject("url");
                                //image.setSmall(url.getString("small"));
                                image.setMedium(url.getString("image"));

                                images.add(image);

                            } catch (JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }


    public void navigationView() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);


        mNavigationView.setItemIconTintList(null);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                String url;

                url = "sss";

                Boolean link;
                link = true;

                switch (menuItem.getItemId()) {


                    case R.id.instagram:
                        url = "https://www.instagram.com/thefortniteempire";
                        break;
                    case R.id.ninjasoundboard:
                        url = "https://play.google.com/store/apps/details?id=com.pentasounds.soundboardninja";
                        break;
                    case R.id.mythsoundboard:
                        url = "https://play.google.com/store/apps/details?id=com.pentasounds.soundboardtsmmyth";
                        break;
                    case R.id.rate:
                        url = "https://play.google.com/store/apps/details?id=info.fortniteempire.fortnitememes";
                        break;
                    case R.id.challenges:
                        url = "https://play.google.com/store/apps/details?id=com.pentasounds.randomchallenges";
                        break;
                    case R.id.chestmap:
                        url = "https://play.google.com/store/apps/details?id=com.pentasounds.fortnitechestmap";
                        break;
                    case R.id.soundboard:
                        url = "https://play.google.com/store/apps/details?id=com.pentasounds.fortnitesoundboard";
                        break;
                    case R.id.gtp:
                        url = "https://play.google.com/store/apps/details?id=com.pentasounds.fortniteguessthewordquiz";
                        break;


                    case R.id.teilen:
                        link = false;
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "✶" + R.string.app_name + "✶");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "✶Go and check out the" + " \"" + getText(R.string.app_name) + "\" " + "App ✶\n\n " + getText(R.string.link_zur_app));
                        startActivity(Intent.createChooser(shareIntent, "Share via"));
                        break;
                    case R.id.email:
                        link = false;
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "fortniteempire.business@gmail.com", null));
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n\n\n\n\n[Packagename:info.fortniteempire.fortnitememes ---Don't delete this information---]");
                        startActivity(Intent.createChooser(emailIntent, "Send E-Mail..."));
                        break;


                }
                if (link) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    onPause();
                    startActivity(intent);
                }


                return false;
            }

        });


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }


    public void firstRunDialog() {





        SharedPreferences prefs = getSharedPreferences("werte", 0);
        boolean b = prefs.getBoolean("firstrun", true);


        if(b){


                if (AppStatus.getInstance(this).isOnline()) {

                   // Toast.makeText(MainActivity.this, "Internet An", Toast.LENGTH_SHORT).show();

                }else{

                    //Toast.makeText(MainActivity.this, "Internet Aus", Toast.LENGTH_LONG).show();

                    internet.setVisibility(View.VISIBLE);
                    tryagain.setVisibility(View.VISIBLE);
                    tryagain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                                internet.setVisibility(View.INVISIBLE);
                                tryagain.setVisibility(View.INVISIBLE);
                                fetchImages();

                            } else {

                                Toast.makeText(MainActivity.this, "Please turn on your Internet", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
            }


        }else{

           // Toast.makeText(MainActivity.this, "Definitiv nicht der erste Start", Toast.LENGTH_LONG).show();
        }

            SharedPreferences sh = getSharedPreferences("werte", 0);
            SharedPreferences.Editor editor = sh.edit();
            editor.putBoolean("firstrun", false);
            editor.commit();


        }

    }

