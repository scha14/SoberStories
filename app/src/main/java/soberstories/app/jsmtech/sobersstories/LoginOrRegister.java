package soberstories.app.jsmtech.sobersstories;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Sukriti on 6/3/16.
 */
public class LoginOrRegister extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // link to xml
        setContentView(R.layout.login_or_register);


        // accept username and password
        Button loginButton = (Button) findViewById(R.id.login_button);
        Button signUp = (Button) findViewById(R.id.signup_select);


        Typeface fontForButton = Typeface.createFromAsset(getAssets(), "fonts/headline_1.ttf");
        loginButton.setTypeface(fontForButton);
        signUp.setTypeface(fontForButton);

        signUp.setOnClickListener(new View.OnClickListener() {
             @Override 
            public void onClick(View v) {
                startActivity(new Intent(LoginOrRegister.this, SignUpActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginOrRegister.this, Login.class));


            }
        });


    }
}


