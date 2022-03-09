package au.edu.unsw.infs3634.cryptobag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder> {
    //public class CoinAdapter extends AppCompatActivity {
    private ArrayList<Coin> mCoins;
    private RecyclerViewClickListener mListener;

    public CoinAdapter(ArrayList<Coin> coins, RecyclerViewClickListener listener) {
        mCoins = coins;
        mListener = listener;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, String id);
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext().inflate(R.layout.coin_list, parest, false));
        return new CoinViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinAdapter.CoinViewHolder holder, int position) {
        Coin coin = mCoins.get(position);
        holder.coin.setText(coin.getName());
        holder.value.setText(String.valueOf(coin.getPriceUsd()));

        holder.change.setText(String.valueOf(coin.getPercentChange24h()) + "%");
        holder.itemView.setTag(coin.getName());
    }

    @Override
    public int getItemCount() {
        return mCoins.size();
    }

    public class CoinViewHolder extends RecyclerView.ViewHolder implements View.onClickListener {
        public TextView coin, value, change;
        private RecyclerViewClickListener bListener;

        public CoinViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            this.bListener = listener;
            itemView.setOnClickListener(this);
            coin = itemView.findViewById(R.id.tvValue);
            value = itemView.findViewById(R.id.tvValue);
            change = itemView.findViewById(R.id.tvChange);
        }

        @Override
        public void onClick(View v) {bListener.onClick(v.(String)v.getTag());}

//    @Override
        //   protected void onCreate(Bundle savedInstanceState) {
        //       super.onCreate(savedInstanceState);
        //       setContentView(R.layout.activity_coin_adapter);
        //   }
//}

//public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder> {



    }