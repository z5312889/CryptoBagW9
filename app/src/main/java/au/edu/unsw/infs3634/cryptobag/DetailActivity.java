package au.edu.unsw.infs3634.cryptobag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Detail Activity");
        Button button = findViewById(R.id.btnPlayVideo);

        //make a text view and find the view by ID
        TextView detailMsg = findViewById(R.id.detailMsg);

        //get the message from main activity using getStringExtra
        final String getMessage = getIntent().getStringExtra("message");

        //set the text of the text view to be the message we got from main activity
        detailMsg.setText(getMessage);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
                startActivity(intent);
            }
        });
    }
}