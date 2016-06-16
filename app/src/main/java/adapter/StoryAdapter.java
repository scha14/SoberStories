package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.LikeTracker;
import model.Story;
import soberstories.app.jsmtech.sobersstories.R;
import soberstories.app.jsmtech.sobersstories.ReadStoryInDetail;

/**
 * Created by Sukriti on 6/4/16.
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.RV_ViewHolder> {

    private List<Story> listOfStories;
    private Context mContext;
    String username = new ParseUser().getCurrentUser().getUsername();
    private View itemView;

    public String url;


    public StoryAdapter(Context context, ArrayList<Story> StoryList) { // Context is reference to Activity in adapter class
        this.mContext = context;
        this.listOfStories = StoryList;
    }

    @Override
    public RV_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_story_view, parent, false); // link to xml

        return new RV_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RV_ViewHolder holder, int position) {
        // This method gets called over and over again as we scroll and as position changes.

        final Story story = listOfStories.get(holder.getAdapterPosition());
        url = story.getStoryImageUrl();
        final String description = story.getStoryDescription();
        holder.mStoryDescription.setText(description + "");
        final String storyCaption = story.getStoryCaption();
        holder.mStoryCaption.setText(storyCaption + "");
        holder.mDeleteStory.setVisibility(View.GONE);
        String storyUploadersUsername = story.getUserName();


        holder.mLikeButton.setVisibility(View.GONE);
        // Toast.makeText(mContext, story.getUserName() + " : " + username, Toast.LENGTH_SHORT).show();
        if(!storyUploadersUsername.equals(username) && story.getClicked()) {
            holder.mLikeButton.setVisibility(View.VISIBLE);
        }
        else {
            holder.mLikeButton.setVisibility(View.GONE);
        }



        Typeface fontForHeading = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/headline_1.ttf");
        holder.mStoryCaption.setTypeface(fontForHeading);
        Typeface fontForDesc = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/story_1.ttf");

        holder.mStoryDescription.setTypeface(fontForDesc);


        // Load Image Url into ImageView
        Picasso.with(mContext)
                .load(url)
                .fit()
                .centerCrop()
                .into(holder.mStoryImage);

       // Toast.makeText(mContext, storyUploadersUsername + " : " + username, Toast.LENGTH_SHORT).show();

       /* if(storyUploadersUsername.equals(username)){
            holder.mDeleteStory.setVisibility(View.VISIBLE);
            //holder.mDeleteStory.setEnabled(false);
            //



            holder.mDeleteStory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 1. Delete from listOfBookings Arraylist and Update View with new array list.

                    listOfStories.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());

                    // 2. Delete Booking from the Backend!

                    ParseQuery<Story> query = ParseQuery.getQuery(Story.class);
                    query.whereEqualTo(Story.STORY_USERNAME, ParseUser.getCurrentUser().getUsername());
                    query.whereEqualTo("objectId", story.getId());
                    query.findInBackground(new FindCallback<Story>() {
                        @Override
                        public void done(List<Story> objects, ParseException e) {

                            if (e == null) {
                                for (ParseObject storyToBeDeleted : objects) {
                                    storyToBeDeleted.deleteInBackground();

                                }
                                Toast.makeText(mContext, "Story Deleted", Toast.LENGTH_SHORT).show();
                                //  } else {
                                //      Toast.makeText(mContext, "Error in Finding Events", Toast.LENGTH_SHORT).show();
                                //}
                                //  }
                                //});

                            }
                        }
                    });
                }
            });
        } */

        /*
        * Algorithm for Like Button Functionality
        * 1. We check if this story id & username is present in LikeTracker Class. If we get a result that means image is liked so we set ImageButton Resource to liked and we set Clicked of the story to true
        * 2. We check if the story is clicked or not (Liked Button). If it is we set the image resource accordingly.
        * 3. We handle on Click Listener on likeButton, if already like, unlike it and delete from LikeTracker  Class and if unliked, we like it and save it in LikeTrackerClass
        * */

        // querying the LikeTracker class searcing for this story and this username!
        final ParseQuery<ParseObject> likeQuery = ParseQuery.getQuery("LikeTracker");
        likeQuery.whereEqualTo(LikeTracker.USERNAME, username);
        likeQuery.whereEqualTo(LikeTracker.STORY_ID, story.getId());
        likeQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null) {


                    if(objects.size() != 0) {
                        // Image has been liked.
                        holder.mLikeButton.setImageResource(R.drawable.liked_button);
                        story.setClicked(true);
                    }



                } else {
                    Toast.makeText(mContext, "Error Retriving LIkes", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if(!story.getClicked()) {
            holder.mLikeButton.setImageResource(R.drawable.unliked_button);
            story.setClicked(false);


        } else { // like button has been clicked.

            holder.mLikeButton.setImageResource(R.drawable.liked_button);
            story.setClicked(true);

        }


        // Check if image is Liked or not
        holder.mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!story.getClicked()) {
                    // Image is not clicked before, thus changed to liked and saved it in LikeTracker Class
                    holder.mLikeButton.setImageResource(R.drawable.liked_button);
                    story.setClicked(true);

                    // Now save this like in Backend
                    LikeTracker newLike = new LikeTracker();
                    newLike.setStoryId(story.getId());
                    newLike.setUSERNAME(username);
                    newLike.saveInBackground();

                } else {
                    // Current status : Image is liked and liked data  is saved in Backend.
                    holder.mLikeButton.setImageResource(R.drawable.unliked_button);
                    story.setClicked(false);

                    // Delete this like from Backend

                    ParseQuery<ParseObject> deleteQuery = ParseQuery.getQuery("LikeTracker");
                    deleteQuery.whereEqualTo(LikeTracker.STORY_ID, story.getId());
                    deleteQuery.whereEqualTo(LikeTracker.USERNAME, username);
                    deleteQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(e == null) {

                                for(ParseObject a : objects) {
                                    a.deleteInBackground();
                                }

                            } else {
                                Toast.makeText(mContext, "Error Deleting", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }

            }
        });

        holder.readInDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass this story's data to a new activity so that user can read the description in detail.
                // Pass data from one activity to another
                        Intent intent = new Intent(mContext, ReadStoryInDetail.class);
                        // We want to pass story caption, descripotion
                        intent.putExtra(Story.STORY_CAPTION, storyCaption);
                        intent.putExtra(Story.STORY_DESCRIPTION, story.getStoryDescription());
                        intent.putExtra("ImageUrl", story.getStoryImageUrl());
                        intent.putExtra("VideoUrl", story.getVideoUrl());
                        mContext.startActivity(intent);

            }
        });





        }



    // Size of the listt
    @Override
    public int getItemCount() {
        return listOfStories.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class RV_ViewHolder extends RecyclerView.ViewHolder {

        // link widgets to our view!
        protected TextView mStoryCaption;
        protected TextView mStoryDescription;
        protected ImageView mStoryImage;
        protected ImageButton mDeleteStory;
        protected ImageButton mLikeButton;
        protected Button readInDetail;

        public RV_ViewHolder(View itemView) {
            super(itemView);

            mStoryCaption = (TextView) itemView.findViewById(R.id.story_caption);
            mStoryDescription = (TextView) itemView.findViewById(R.id.story_description);
            mStoryImage = (ImageView) itemView.findViewById(R.id.single_story_image);
            mDeleteStory = (ImageButton) itemView.findViewById(R.id.delete);
            mLikeButton = (ImageButton) itemView.findViewById(R.id.like_button);
            readInDetail = (Button) itemView.findViewById(R.id.more_button);



        }


    }
}
