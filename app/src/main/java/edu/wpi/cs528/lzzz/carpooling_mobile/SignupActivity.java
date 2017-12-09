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
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.IConnectionStatus;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.SignUpHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.User;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonUtils;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private SignUpHandler signUpHandler;
    private Gson gson = new Gson();
    private ProgressDialog progressDialog;

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

        _nameText.setText("qqqqqq");
        _emailText.setText("qqqqqq@test.com");
        _mobileText.setText("1111111111");
        _passwordText.setText("qqqqqq");

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
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

    public void signUp() {
        Log.d(TAG, "Signup");

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();

        if (!validate(name, email, mobile, password)) {
            return;
        }

        _signupButton.setEnabled(false);

        progressDialog = CommonUtils.createProgressDialog(this, "Creating Account...");
        progressDialog.show();
//        Bitmap defaultPhoto = BitmapFactory.decodeResource(this.getResources(),R.drawable.wpi);

        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setPhone(mobile);
        user.setPassword(password);
        user.setPhoto("null");
        String userJson = gson.toJson(user);

        signUpHandler = new SignUpHandler(new IConnectionStatus() {
            @Override
            public void onComplete(Boolean isSuccess, String additionalInfos) {
                progressDialog.dismiss();
                if (isSuccess){
                    onSignupSuccess();
                }else{
                    onSignupFailed(additionalInfos);
                }
            }
        });
        signUpHandler.connectForResponse(CommonUtils.createHttpPOSTRequestMessage(userJson, CommonConstants.registration));
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);

        String name = _nameText.getText().toString();
        String password = _passwordText.getText().toString();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("name", name);
        returnIntent.putExtra("password", password);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void onSignupFailed(String reason) {
        Toast.makeText(getBaseContext(), reason, Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate(String name, String email, String mobile, String password) {
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