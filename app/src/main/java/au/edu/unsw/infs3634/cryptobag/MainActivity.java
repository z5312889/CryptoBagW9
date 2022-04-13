package au.edu.unsw.infs3634.cryptobag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
  private static final String TAG = "MainActivity";
  private RecyclerView mRecyclerView;
  private CoinAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private CoinDatabase database;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Get a handle to the RecyclerView
    mRecyclerView = findViewById(R.id.rvList);
    mRecyclerView.setHasFixedSize(true);

    // Instantiate a LinearLayoutManager
    mLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLayoutManager);

    // Implement ClickListener for list items
    CoinAdapter.RecyclerViewListener listener = new CoinAdapter.RecyclerViewListener() {
      @Override
      public void onClick(View view, String coinSymbol) {
        // Launch DetailActivity
        launchDetailActivity(coinSymbol);
      }
    };

    // Create an adapter instance with an empty ArrayList of Coin objects
    mAdapter = new CoinAdapter(new ArrayList<Coin>(), listener);

    database = Room.databaseBuilder(getApplicationContext(), CoinDatabase.class, "coin-database").build();

    // Implement Retrofit to make API call
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.coinlore.net") // Set the base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Create object for the service interface
    CoinService service = retrofit.create(CoinService.class);
    Call<CoinLoreResponse> responseCall = service.getResponse();
    responseCall.enqueue(new Callback<CoinLoreResponse>() {
      @Override
      public void onResponse(Call<CoinLoreResponse> call, Response<CoinLoreResponse> response) {
        Log.d(TAG, "API call successful!");
        List<Coin> coins = response.body().getData();


        Executors.newSingleThreadExecutor().execute(new Runnable() {
          @Override
          public void run() {
            database.coinDao().deleteAll();
            database.coinDao().insertCoins(coins.toArray(new Coin[0]));
          }
        });

        // Supply data to the adapter to be displayed
        mAdapter.setData((ArrayList)coins);
        mAdapter.sort(CoinAdapter.SORT_METHOD_NAME);
        // Connect the adapter with the RecyclerView
        mRecyclerView.setAdapter(mAdapter);
      }

      @Override
      public void onFailure(Call<CoinLoreResponse> call, Throwable t) {
        Log.d(TAG, "API call failure.");
      }
    });
  }

  @Override
  // Instantiate the menu
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);
    SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        mAdapter .getFilter().filter(query);
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return false;
      }
    });
    return true;
  }

  @Override
  // React to user interaction with the menu
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.sortName:
        mAdapter.sort(CoinAdapter.SORT_METHOD_NAME);
        return true;
      case R.id.sortValue:
        mAdapter.sort(CoinAdapter.SORT_METHOD_VALUE);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  // Called when user taps on a row on the RecyclerView
  private void launchDetailActivity(String message){
    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
    intent.putExtra(DetailActivity.INTENT_MESSAGE, message);
    startActivity(intent);
  }

}