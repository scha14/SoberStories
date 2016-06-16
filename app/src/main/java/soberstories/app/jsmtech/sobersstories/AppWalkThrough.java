package soberstories.app.jsmtech.sobersstories;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sukriti on 6/12/16.
 */
public class AppWalkThrough extends AppCompatActivity {

    private Button mHasSeenWalkThroughButton;
    private ImageButton mNextButton;
    private TextView mDescription;
    private ImageView mFragmentName;
    private int count = 1;

    // String
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_walk_through);

        mHasSeenWalkThroughButton = (Button) findViewById(R.id.has_see_walkthrough_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mDescription = (TextView) findViewById(R.id.description);
        mFragmentName = (ImageView) findViewById(R.id.fragment_name);


        mHasSeenWalkThroughButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(MainActivity.PREFERENCE_APP, MODE_PRIVATE).edit();
                editor.putBoolean(MainActivity.HAS_SEEN_WALKTHROUGH, true);
                editor.commit();


                Intent intent = new Intent(AppWalkThrough.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count ++;

                if(count==2){
                    mDescription.setText(R.string.profile_description);
                    mFragmentName.setImageResource(R.drawable.profile);
                }
                else if (count==3){
                    mDescription.setText(R.string.search_description);
                    mFragmentName.setImageResource(R.drawable.search);
                }

                else if(count ==4){
                    mDescription.setText(R.string.add_description);
                    mFragmentName.setImageResource(R.drawable.add);
                    mHasSeenWalkThroughButton.setVisibility(View.VISIBLE);
                    mNextButton.setVisibility(View.GONE);
                }


            }
        });







    }
}
