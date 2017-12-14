package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.ISearchStatus;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.SearchHandler;

public class SearchActivity extends AppCompatActivity {

    private EditText searchTargetAddressText;
    private EditText searchDateText;

    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        searchTargetAddressText = (EditText) findViewById(R.id.to_address);
        searchDateText = (EditText) findViewById(R.id.search_day);
        searchBtn = (Button) findViewById(R.id.search_button_filter) ;

//        final String targetAddress = searchTargetAddressText.getText().toString();
//        String searchDateString = searchDateText.getText().toString();
//        long searchMilliseconds =

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("+============", "searchbutton clicked.");
                ISearchStatus searchStatus = new ISearchStatus() {
                    @Override
                    public void onSearchComplete() {
                        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                };
                final String targetAddress = searchTargetAddressText.getText().toString();
                SearchHandler.performSearch(targetAddress, searchStatus);
            }
        });
    }



}
