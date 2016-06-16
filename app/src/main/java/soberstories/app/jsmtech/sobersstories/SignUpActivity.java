package soberstories.app.jsmtech.sobersstories;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Sukriti on 6/3/16.
 */
public class SignUpActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_up_screen);

        final EditText mUsername = (EditText) findViewById(R.id.username);
        final EditText mPassword = (EditText) findViewById(R.id.password);
        final EditText mEmail = (EditText) findViewById(R.id.email);
        Button mSignUpButton = (Button) findViewById(R.id.sign_up_button);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard(SignUpActivity.this);

                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, R.string.enter_values, Toast.LENGTH_SHORT).show();
                } else {

                    ParseUser newUser = new ParseUser();
                    newUser.setEmail(email);
                    newUser.setPassword(password);
                    newUser.setUsername(username);

                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {

                                Toast.makeText(SignUpActivity.this, "Invalid Request : " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                }

            }
        });


    }


    private void hideKeyBoard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
