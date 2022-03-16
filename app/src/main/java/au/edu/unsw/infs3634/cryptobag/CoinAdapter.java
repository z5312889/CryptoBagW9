package au.edu.unsw.infs3634.cryptobag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder> {
    public static final int SORT_COIN_NAME = 1;
    public static final int SORT_COIN_PRICE = 2;
    private ArrayList<Coin> mCoins, mCoinsFiltered;
    private RecyclerViewClickListener mListener;
    // private static final int SORT_COIN_NAME = 1;
    // private static final int SORT_COIN_PRICE = 2;

    // Constructor method for CoinAdapter class
    public CoinAdapter(ArrayList<Coin> coins, RecyclerViewClickListener listener) {
        mCoins = coins;
        mListener = listener;
        mCoinsFiltered = coins;
    }

    //@Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    mCoinsFiltered = mCoins;
                } else {
                    ArrayList<Coin> filteredList = new ArrayList<>();
                    for (Coin coin : mCoins) {
                        if (coin.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(coin);
                        }
                    }
                    mCoinsFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mCoinsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mCoinsFiltered = (ArrayList<Coin>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    // ClickListener interface
    public interface RecyclerViewClickListener {
        void onClick(View view, String coinSymbol);
    }


    // Create a ViewHolder and return it
    @NonNull
    @Override
    public CoinAdapter.CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new CoinViewHolder(v, mListener);
    }

    // Associate data with the view holder for a given position in the RecyclerView
    @Override
    public void onBindViewHolder(@NonNull CoinAdapter.CoinViewHolder holder, int position) {
        Coin coin = mCoins.get(position);

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        holder.name.setText(coin.getName());
        holder.value.setText(formatter.format(Double.valueOf(coin.getPriceUsd())));
        holder.change.setText(String.valueOf(coin.getPercentChange1h()) + " %");
        holder.itemView.setTag(coin.getSymbol());

    }

    // Return the number of data items available for displaying
    @Override
    public int getItemCount() {
        return mCoins.size();
    }

    // Extend the signature of CoinViewHolder to implement a click listener
    public static class CoinViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, value, change;
        private RecyclerViewClickListener mListener;

        // Constructor method for CoinViewHolder class
        public CoinViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mListener = listener;
            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.tvName);
            value = itemView.findViewById(R.id.tvValue);
            change = itemView.findViewById(R.id.tvChange);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, (String) view.getTag());
        }
    }

    public void sort(final int sortMethod) {
        if(mCoinsFiltered.size() > 0) {
            Collections.sort(mCoinsFiltered, new Comparator<Coin>() {
                @Override
                public int compare(Coin o1, Coin o2) {
                    if(sortMethod == SORT_COIN_NAME) {
                        // sort by coin name
                        return o2.getName().compareTo(o1.getName());

                    } else if (sortMethod == SORT_COIN_PRICE){
                        //sort by price
                        return Double.valueOf(o2.getPriceUsd()).compareTo(Double.valueOf(o1.getPriceUsd()));
                    }
                    return o2.getName().compareTo(o1.getName());
                }
            });
        }
        notifyDataSetChanged();
    }

}
