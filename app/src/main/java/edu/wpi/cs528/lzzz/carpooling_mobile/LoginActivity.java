package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.CarpoolsHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.IConnectionStatus;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.LogInHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.User;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonUtils;

public class LoginActivity extends AppCompatActivity{
    private final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private LogInHandler logInHandler;
    private ProgressDialog progressDialog;
    private Gson gson = new Gson();
    private User user;

    @Bind(R.id.input_username)
    EditText _usernameText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
//                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (!validate(username, password)) {
            return;
        }
        _loginButton.setEnabled(false);

        progressDialog = CommonUtils.createProgressDialog(this, "Authenticating...");
        progressDialog.show();


        username = "qqqqq";
        password = "qqqqq";
        user = new User(username, password);

        logInHandler = new LogInHandler(new IConnectionStatus() {
            @Override
            public void onComplete(Boolean isSuccess, String additionalInfos) {
                progressDialog.dismiss();
                if (isSuccess){
                    onLoginSuccess();
                }else{
                    onLoginFailed(additionalInfos);
                }
            }
        });

        String userJson = gson.toJson(user);
        logInHandler.connectForResponse(CommonUtils.createHttpPOSTRequestMessage(userJson, CommonConstants.login));
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        AppContainer.getInstance().setActiveUser(this.user);
        CarpoolsHandler carpoolsHandler = new CarpoolsHandler(new IConnectionStatus() {
            @Override
            public void onComplete(Boolean isSuccess, String additionalInfos) {
                progressDialog.dismiss();
                if (isSuccess){
                    onGetAllCarpoolsSuccess();
                }else{
                    onGetAllCarpoolFailed(additionalInfos);
                }
            }
        });
        if(AppContainer.getInstance().isLogIn()){
            carpoolsHandler.connectForResponse(CommonUtils.createHttpGETRequestMessageWithToken(CommonConstants.getAllCarpools));
            progressDialog = CommonUtils.createProgressDialog(this, "Loading...");
            progressDialog.show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                _usernameText.setText(data.getStringExtra("name"));
                _passwordText.setText(data.getStringExtra("password"));
            }
        }
    }

    private void onGetAllCarpoolsSuccess(){
        finish();
    }

    public void onLoginFailed(String reason) {
        Toast.makeText(getBaseContext(), reason, Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    private void onGetAllCarpoolFailed(String error){
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    public boolean validate(String username, String password) {
        boolean valid = true;
        if (username.isEmpty()) {
            _usernameText.setError("username can not be null");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
