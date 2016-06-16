package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.Story;
import soberstories.app.jsmtech.sobersstories.R;

/**
 * Created by Sukriti on 6/4/16.
 */
public class UserStoriesAdapter extends RecyclerView.Adapter<UserStoriesAdapter.RV_ViewHolder> {

    private List<Story> listOfStories;
    private Context mContext;
    String username = new ParseUser().getCurrentUser().getUsername();
    View itemView;

    public String url;


    public UserStoriesAdapter(Context context, ArrayList<Story> StoryList) {
        this.mContext = context;
        this.listOfStories = StoryList;
    }

    @Override
    public RV_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_single_story_view, parent, false); // link to xml
        return new RV_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RV_ViewHolder holder,final int position) {
        final Story story = listOfStories.get(holder.getAdapterPosition());
        url = story.getStoryImageUrl();
        final String description = story.getStoryDescription();
        final String storyCaption = story.getStoryCaption();
        holder.mDeleteStory.setVisibility(View.GONE);

        String storyUploadersUsername = story.getUserName();
        Picasso.with(mContext)
                .load(url)
                .fit()
                .centerCrop()
                .into(holder.mStoryImage);

        Toast.makeText(mContext, storyUploadersUsername + " : " + username, Toast.LENGTH_SHORT).show();

        if(storyUploadersUsername.equals(username)){
            holder.mDeleteStory.setVisibility(View.VISIBLE);


            holder.mDeleteStory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 1. Delete from listOfBookings Arraylist and Update View with new array list.

                    listOfStories.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());

                    // 2. Delete Booking from the Backend

                    ParseQuery<Story> query = ParseQuery.getQuery(Story.class);
                    query.whereEqualTo(Story.STORY_USERNAME, ParseUser.getCurrentUser().getUsername()); // Need to check the query it could be wrong.
                    query.whereEqualTo("objectId", story.getId());
                    query.findInBackground(new FindCallback<Story>() {
                        @Override
                        public void done(List<Story> objects, ParseException e) {

                            if (e == null) {
                                for (ParseObject storyToBeDeleted : objects) {
                                    storyToBeDeleted.deleteInBackground();
//
                                }
                                Toast.makeText(mContext, "Story Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return listOfStories.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class RV_ViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mStoryImage;
        protected ImageButton mDeleteStory;

        public RV_ViewHolder(View itemView) {
            super(itemView);

            mStoryImage = (ImageView) itemView.findViewById(R.id.single_story_image);
            mDeleteStory = (ImageButton) itemView.findViewById(R.id.delete);

        }


    }
}
