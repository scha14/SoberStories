package model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Sukriti on 6/4/16.
 */

@ParseClassName("Profile")

public class Profile extends ParseObject{


    public static String STORY_USERNAME = "Username";
    public static String YOUR_STORIES = "Stories";


    public Profile(){

    }


    public String getUserName() { return getString(STORY_USERNAME);}

    public void setUsername(String userName) { put(STORY_USERNAME, userName);}

    public String getYourStories() { return getString(YOUR_STORIES);}

    public void setYourStories(String caption) {put(YOUR_STORIES, caption);}

}
