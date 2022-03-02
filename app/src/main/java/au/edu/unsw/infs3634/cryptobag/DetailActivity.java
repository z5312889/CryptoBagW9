package au.edu.unsw.infs3634.cryptobag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    public static final String INTENT_MESSAGE = "au.edu.unsw infs3634.covidtracker.intent_message";
    private TextView tCoin, tvSymbol, tValueUSD, tvChange1h, tvChange24h, tvChange7d, tvMarketCap, tvVolume;
    private Button bSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Detail Activity");

        tCoin = findViewById(R.id.tCoin);
        tvSymbol = findViewById(R.id.tvSymbol);
        tValueUSD = findViewById(R.id.tValueUSD);
        tvChange1h = findViewById(R.id.tvChange1h);
        tvChange24h = findViewById(R.id.tvChange24h);
        tvChange7d = findViewById(R.id.tvChange7d);
        tvMarketCap = findViewById(R.id.tvMarketCap);
        tvVolume = findViewById(R.id.tvVolume);
        bSearch = findViewById(R.id.btSearch);

        Intent intent = getIntent();

        String id = intent.getStringExtra(INTENT_MESSAGE);

        Coin coin = Coin.getCoin(id);

        if (coin != null) {
            setTitle(coin.getName());
            tCoin.setText(coin.getName());
            tvSymbol.setText(String.valueOf(coin.getSymbol()));
            tValueUSD.setText(String.valueOf(coin.getPriceUsd()));
            tvChange1h.setText(String.valueOf(coin.getPercentChange1h()));
            tvChange24h.setText(String.valueOf(coin.getPercentChange24h()));
            tvChange7d.setText(String.valueOf(coin.getPercentChange7d()));
            tvMarketCap.setText(String.valueOf(coin.getMarketCapUsd()));
            tvVolume.setText(String.valueOf(coin.getVolume24()));
            bSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + coin.getName()));
                    startActivity(intent);
                }
            });


        }
    }
}