package model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Sukriti on 6/5/16.
 */
@ParseClassName("LikeTracker")
public class LikeTracker extends ParseObject {

    public static String STORY_ID = "StoryId";
    public static String USERNAME = "Username";

    public LikeTracker(){

    }

    public void setStoryId(String storyId){
        put(LikeTracker.STORY_ID, storyId);
    }

    public String getStoryId(){return getString(STORY_ID);}

    public void setUSERNAME(String username){put(LikeTracker.USERNAME, username);}

    public String getUSERNAME(){return getString(USERNAME);} // mapped to a particular user not all

}
