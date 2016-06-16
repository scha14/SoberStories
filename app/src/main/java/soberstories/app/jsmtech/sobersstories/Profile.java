package soberstories.app.jsmtech.sobersstories;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import adapter.EndlessRecyclerOnScrollListenserStaggered;
import adapter.UserStoriesAdapter;
import model.Story;


/**
 * Created by Sukriti on 6/4/16.
 */
public class Profile extends Fragment {
    private View myFragmentView;
    private ArrayList<Story> listOfYourStories;
    private TextView userName;
    private RecyclerView recList;
    private ProgressBar progressBar;
    private UserStoriesAdapter mAdapter;
    private int limitRequest = 5;


    public Profile(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myFragmentView = inflater.inflate(R.layout.your_profile, container, false);
        userName = (TextView) myFragmentView.findViewById(R.id.username) ;
        recList = (RecyclerView) myFragmentView.findViewById(R.id.story_list);
        progressBar = (ProgressBar) myFragmentView.findViewById(R.id.story_progress_bar);

        userName.setText(ParseUser.getCurrentUser().getUsername() + "");
        Typeface fontCaption = Typeface.createFromAsset(myFragmentView.getContext().getAssets(), "fonts/headline_1.ttf");
        userName.setTypeface(fontCaption);


//        // RecyclerView can be use to list items vertically or horizontally. Here we need to to scroll vertically
//        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
//
//        llm.setOrientation(
//                LinearLayoutManager.VERTICAL
//        );


        final StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recList.setLayoutManager(sglm);
        recList.setHasFixedSize(true); // performance is better!

        recList.setOnScrollListener(new EndlessRecyclerOnScrollListenserStaggered(sglm) {
            @Override
            public void onLoadMore(int current_page) {

                // We have reached the end of list of stories, so we can now load more if there are more.

                Toast.makeText(getActivity(), "Loading More Stories ", Toast.LENGTH_SHORT).show();
                loadMoreStories();

            }
        });



        getStoryList();


        return myFragmentView;
    }

    private void loadMoreStories() {

        progressBar.setVisibility(View.VISIBLE);
        ParseQuery<Story> query = ParseQuery.getQuery(Story.class); // We want to query the Events Class in Parse Dashboard!
        query.whereEqualTo(Story.STORY_USERNAME, ParseUser.getCurrentUser().getUsername());
        query.setSkip(limitRequest);
        limitRequest += 5;
        query.setLimit(5);

        query.findInBackground(new FindCallback<Story>() {
            @Override
            public void done(List<Story> objects, ParseException e) { // We are getting list of stories from this query
                progressBar.setVisibility(View.GONE); // Hide ProgressBar

                if(e == null)  {

                    for(ParseObject result : objects) {

                        ParseFile storyImage = (ParseFile) result.get(Story.STORY_IMAGE);
                        String url = storyImage.getUrl();

                        final Story sTemp = new Story();
                        sTemp.setStoryImageUrl(url);
                        sTemp.setStoryDescription(result.getString(Story.STORY_DESCRIPTION));
                        sTemp.setStoryCaption(result.getString(Story.STORY_CAPTION));
                        sTemp.setId(result.getObjectId());
                        sTemp.setClicked(false);

                        sTemp.setUsername(result.getString(Story.STORY_USERNAME));


                        listOfYourStories.add(sTemp);

                    }


//                    mAdapter = new StoryAdapter(getActivity(), listOfStories); //
//                    recList.setAdapter(mAdapter); // Link, we set dapter to RecyclerView here

                    mAdapter.notifyDataSetChanged(); // We are telling adapter that we have added new data to the list - update the list.


                } else {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    private void getStoryList() {

        progressBar.setVisibility(View.VISIBLE);
        listOfYourStories = new ArrayList<Story>(); // Initializing ArrayList

        ParseQuery<Story> query = ParseQuery.getQuery(Story.class);
        query.whereEqualTo(Story.STORY_USERNAME, ParseUser.getCurrentUser().getUsername());


        query.findInBackground(new FindCallback<Story>() {
            @Override
            public void done(List<Story> objects, ParseException e) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar

                if(e == null)  { // Succesfull querying
                    for(ParseObject result : objects) {
                        Story newStory = new Story();

                        ParseFile storyImage = (ParseFile) result.get(Story.STORY_IMAGE);
                        String url = storyImage.getUrl();

                        newStory.setStoryCaption(result.getString(Story.STORY_CAPTION));
                        newStory.setStoryDescription(result.getString(Story.STORY_DESCRIPTION));
                        newStory.setStoryImageUrl(url);
                        newStory.setUsername(ParseUser.getCurrentUser().getUsername());
                        newStory.setStoryInspired(0); // for now

                        listOfYourStories.add(newStory);
                    }

                    mAdapter = new UserStoriesAdapter(getActivity(), listOfYourStories);
                    recList.setAdapter(mAdapter);
                } else {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }
