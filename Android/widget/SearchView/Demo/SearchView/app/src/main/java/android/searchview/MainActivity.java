package android.searchview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchView = findViewById(R.id.searchView);

        searchView.setImageButtonVoiceClickListener(new ISearcher.onImageButtonVoiceClickListener() {
            @Override
            public void onClick(EditText input, ImageView voice, View view) {
                Toast.makeText(MainActivity.this,"语音",Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setonTextViewSearchClickListener(new ISearcher.onTextViewSearchClickListener() {
            @Override
            public void onClick(EditText input, TextView search, View view) {
                Toast.makeText(MainActivity.this,"搜索",Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setImageButtonCancelClickListener();
    }
}
