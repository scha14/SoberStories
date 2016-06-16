package soberstories.app.jsmtech.sobersstories;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseObject;

import model.LikeTracker;
import model.Story;

/**
 * Created by Sukriti on 6/3/16.
 */
public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

    // register classes here

        ParseObject.registerSubclass(Story.class);
        ParseObject.registerSubclass(LikeTracker.class);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("jsm123434231234")
                .clientKey(null)
                .server("http://jsm1408.herokuapp.com/parse/")
                .build()
        );

        try {
            ReplaceFont.replaceDefaultFont(this, "DEFAULT", "helvetica.ttf");
        } catch (NoSuchFieldException e) {
            Log.d("Font not found", "onCreate: "+ e);
        }

    }


}
