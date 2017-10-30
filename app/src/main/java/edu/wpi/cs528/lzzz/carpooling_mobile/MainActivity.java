package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.ConnectionHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpRequestMessage;

public class MainActivity extends AppCompatActivity{

    private Button testButton;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ConnectionHandler connectionHandler = new ConnectionHandler(this);

        testButton = (Button) findViewById(R.id.testConnectionBtn);
        textView = (TextView)  findViewById(R.id.textView);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpRequestMessage request = new HttpRequestMessage();
                request.setMethod("POST");
                request.addParam("name", "morpheus");
                request.addParam("job", "worker");
                request.setUrl("https://reqres.in/api/users");
//                request.setMethod("GET");
//                request.setUrl("https://reqres.in/api/users/2");
                connectionHandler.connectForResponse(request);
            }
        });
    }

}
