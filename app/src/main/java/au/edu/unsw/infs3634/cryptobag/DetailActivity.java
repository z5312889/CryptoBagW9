package au.edu.unsw.infs3634.cryptobag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    public static final String INTENT_MESSAGE = "intent_message";
    private static final String TAG = "DetailActivity";
    private TextView mName;
    private TextView mSymbol;
    private TextView mValue;
    private TextView mChange1h;
    private TextView mChange24h;
    private TextView mChange7d;
    private TextView mMarketcap;
    private TextView mVolume;
    private ImageView mSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get handle for view elements
        mName = findViewById(R.id.tvName);
        mSymbol = findViewById(R.id.tvSymbol);
        mValue = findViewById(R.id.tvValueField);
        mChange1h = findViewById(R.id.tvChange1hField);
        mChange24h = findViewById(R.id.tvChange24hField);
        mChange7d = findViewById(R.id.tvChange7dField);
        mMarketcap = findViewById(R.id.tvMarketcapField);
        mVolume = findViewById(R.id.tvVolumeField);
        mSearch = findViewById(R.id.ivSearch);

        // Get the intent that started this activity
        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_MESSAGE)) {
            String coinSymbol = intent.getStringExtra(INTENT_MESSAGE);
            Log.d(TAG, "INTENT_MESSAGE = " + coinSymbol);
            Coin coin = Coin.findCoin(coinSymbol);

            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.coinlore.net").addConverterFactory(GsonConverterFactory.create()).build();
            CoinService service = retrofit.create(CoinService.class);
            Call<CoinLoreResponse> responseCall = service.getResponse();
            responseCall.enqueue(new Callback<CoinLoreResponse>() {
                @Override
                public void onResponse(Call<CoinLoreResponse> call, Response<CoinLoreResponse> response) {
                    List<Coin> coins = response.body().getData();
                    for (Coin coin : coins) {
                        if (coinSymbol.equals(coin.getName())) {

                            if (coin != null) {
                                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                setTitle(coin.getName());
                                mName.setText(coin.getName());
                                mSymbol.setText(coin.getSymbol());
                                mValue.setText(formatter.format(Double.valueOf(coin.getPriceUsd())));
                                mChange1h.setText(coin.getPercentChange1h() + " %");
                                mChange24h.setText(coin.getPercentChange24h() + " %");
                                mChange7d.setText(coin.getPercentChange7d() + " %");
                                mMarketcap.setText(formatter.format(Double.valueOf(coin.getMarketCapUsd())));
                                mVolume.setText(formatter.format(coin.getVolume24()));
                                mSearch.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + coin.getName()));
                                        startActivity(intent);
                                    }
                                    //  searchCoin(coin.getName());
                                });
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<CoinLoreResponse> call, Throwable t) {

                    //  private void searchCoin(String name) {
                    //      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + coin.getName()));
                    //     startActivity(intent);
                }
            });
        }

    }
}