package au.edu.unsw.infs3634.cryptobag;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CoinService {
    @GET("api/tickers")
    Call<CoinLoreResponse> getResponse();
}
