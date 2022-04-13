package au.edu.unsw.infs3634.cryptobag;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CoinDao {

    @Query("SELECT * FROM Coin")
    List<Coin> getCoins();

    @Query("SELECT * FROM Coin WHERE symbol == :coinSymbol")
    Coin getCoin(String coinSymbol);

    @Query("DELETE FROM Coin")
    void deleteAll();

  //  @Delete
  //  void deleteCoins(Coin... coins);

    @Insert
    void insertCoins(Coin... coins);
}
