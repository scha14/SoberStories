package soberstories.app.jsmtech.sobersstories;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Sukriti on 6/3/16.
 */


public class Login extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // link to xml
        setContentView(R.layout.login_screen);


        // accept username and password
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        Button loginButton = (Button) findViewById(R.id.login_button);
        // Button signUp = (Button) findViewById(R.id.signUp_select);

        // Toast.makeText(this, R.string.enter_values ,Toast.LENGTH_SHORT);


//        signUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(Login.this, SignUpActivity.class));
//
//            }
//        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               hideKeyBoard(Login.this);

                String usernameValue = username.getText().toString().trim();
                String passwordValue = password.getText().toString().trim();

                if(usernameValue.equals("") || passwordValue.isEmpty()) {
                   // Toast.makeText(Login.this, R.string.enter_values ,Toast.LENGTH_SHORT);
                    Snackbar sB = Snackbar.make(coordinatorLayout, R.string.enter_values, Snackbar.LENGTH_SHORT);
                    sB.show();


                }
                else {

                    ParseUser.logInInBackground(usernameValue, passwordValue, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (e == null) {
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                // If we dont add flags now, then from MainActivity Pressing back would bring us back here
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            } else { // Invalid Credentials

                                Toast.makeText(Login.this, "Invalid Request! " + e.getMessage(), Toast.LENGTH_SHORT).show();


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
