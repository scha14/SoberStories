package model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by Sukriti on 6/3/16.
 */
@ParseClassName("Story")
public class Story extends ParseObject {

    // image/video - caption - description text - comment -- share



    public static String STORY_IMAGE  = "StoryImage";
    public static String STORY_CAPTION = "StoryCaption";
    public static String STORY_DESCRIPTION = "StoryDescription";
    public static String STORY_USERNAME = "Username";
    public static String STORY_INSPIRED = "Inspired"; // IS EVENT FREE WILL BE THE COLUMN IN PARSE, FROM WHERE we store and erquest values
    public static String STORY_VIDEO = "StoryVideo";


    public String storyImageUrl, videoUrl;
    public String id;
    boolean clicked;

    public Story(){

    }


    public String getUserName() { return getString(STORY_USERNAME);}

    public void  setUsername(String userName) { put(STORY_USERNAME, userName);}

    public String getStoryCaption() { return getString(STORY_CAPTION);}

    public void setStoryCaption(String caption) {put(STORY_CAPTION, caption);}

    public ParseFile getStoryVideo() {
        return getParseFile(STORY_VIDEO);
    }

    public void setStoryVideo(ParseFile storyVideo) {
        put(STORY_VIDEO, storyVideo);
    }

    public String getStoryDescription() { return getString(STORY_DESCRIPTION);}

    public void setStoryDescription(String description) {put(STORY_DESCRIPTION, description);}

    public ParseFile getStoryImage() {
        return getParseFile(STORY_IMAGE);
    }

    public void setStoryImage(ParseFile eventImage) {
        put(STORY_IMAGE, eventImage);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoryImageUrl() {
        return storyImageUrl;
    }

    public void setStoryImageUrl(String storyImageUrl){this.storyImageUrl = storyImageUrl;}

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl){this.videoUrl = videoUrl;}


    public int getStoryInspired() {return getInt(STORY_INSPIRED);}

    public void setStoryInspired(int storyInspired) {
        put(STORY_INSPIRED, storyInspired);
    }

    public boolean getClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }



}
