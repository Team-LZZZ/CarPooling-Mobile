package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpRequestMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.ConnectionHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.SignUpHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.User;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonExceptionMessages;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_mobile)
    EditText _mobileText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        final SignUpHandler signUpHandler = new SignUpHandler(this);


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(signUpHandler);
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup(SignUpHandler signUpHandler) {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
//        Bitmap defaultPhoto = BitmapFactory.decodeResource(this.getResources(),R.drawable.wpi);


        User user = new User();
             user.setUsername(name);
             user.setEmail(email);
             user.setPhone(mobile);
             user.setPassword(password);
             user.setPhoto("null");

        Gson gson = new Gson();
        String userJson = gson.toJson(user);

        HttpRequestMessage request = new HttpRequestMessage();
                request.setMethod("POST");
                request.setBody(userJson);
                request.setUrl(CommonConstants.BASE_URL + "reg");
                signUpHandler.connectForResponse(request);
        Log.i(CommonConstants.LogPrefix, userJson);


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        if (CommonConstants.isSignUp)
                            onSignupSuccess();
                        else
                            onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 2000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), CommonExceptionMessages.REGISTER_FAILURE, Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

//        String name = _nameText.getText().toString();
//        String email = _emailText.getText().toString();
//        String mobile = _mobileText.getText().toString();
//        String password = _passwordText.getText().toString();
//
//        if (name.isEmpty() || name.length() < 3) {
//            _nameText.setError("at least 3 characters");
//            valid = false;
//        } else {
//            _nameText.setError(null);
//        }
//
//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _emailText.setError("enter a valid email address");
//            valid = false;
//        } else {
//            _emailText.setError(null);
//        }
//
//        if (mobile.isEmpty() || mobile.length()!=10) {
//            _mobileText.setError("Enter Valid Mobile Number");
//            valid = false;
//        } else {
//            _mobileText.setError(null);
//        }
//
//        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//            _passwordText.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//            _passwordText.setError(null);
//        }
        
        return valid;
    }
}