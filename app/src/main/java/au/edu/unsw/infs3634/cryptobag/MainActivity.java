package au.edu.unsw.infs3634.cryptobag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
  private String message;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Button button = findViewById(R.id.btnLaunchDetailActivity);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        launchDetailActivity("Stellar");
      }
    });
  }
       private void launchDetailActivity(String message) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

        intent.putExtra(DetailActivity.INTENT_MESSAGE, message);
        //starting the intent
        startActivity(intent);
      }
}
