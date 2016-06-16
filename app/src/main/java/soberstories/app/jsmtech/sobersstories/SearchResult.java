package soberstories.app.jsmtech.sobersstories;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import adapter.EndlessRecyclerOnScrollListener;
import adapter.StoryAdapter;
import model.Story;

/**
 * Created by Sukriti on 6/6/16.
 */
public class SearchResult extends AppCompatActivity {

    String user;
    private ArrayList<Story> listOfStories;
    // private TextView userName;
    private RecyclerView recList;
    private ProgressBar progressBar;
    private StoryAdapter mAdapter;
    private int limitRequest = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_stories);

        recList = (RecyclerView) findViewById(R.id.stories_list);
        progressBar = (ProgressBar) findViewById(R.id.stories_progress_bar);

        // Fetch Data From Intent

       // Toast.makeText(SearchResult.this, "Started!", Toast.LENGTH_SHORT).show();
        Bundle data = getIntent().getExtras();
        if(data != null) {
            user = data.getString(Story.STORY_USERNAME);

            final LinearLayoutManager llm = new LinearLayoutManager(SearchResult.this);

            llm.setOrientation(
                    LinearLayoutManager.VERTICAL
            );
            recList.setLayoutManager(llm);
            // recList.setHasFixedSize(true);

            listOfStories = new ArrayList<Story>();
            mAdapter = new StoryAdapter(SearchResult.this, listOfStories);
            recList.setAdapter(mAdapter);

            getStoriesList();


            recList.setOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
                @Override
                public void onLoadMore(int current_page) {
                    loadMoreStories();

                }
            });

        }


    }

    private void loadMoreStories() {

        progressBar.setVisibility(View.VISIBLE);
        ParseQuery<Story> query = ParseQuery.getQuery(Story.class);
        query.whereEqualTo(Story.STORY_USERNAME, user);

        query.setSkip(limitRequest);
        limitRequest += 5;
        query.setLimit(5);

        query.findInBackground(new FindCallback<Story>() {
            @Override
            public void done(List<Story> objects, ParseException e) {
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


                        listOfStories.add(sTemp);

                    }


//
                    mAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(SearchResult.this, "Error!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void getStoriesList() {

      //  Toast.makeText(SearchResult.this, "Reaching getStories", Toast.LENGTH_SHORT).show();


        progressBar.setVisibility(View.VISIBLE);
        // listOfStories = new ArrayList<Story>();
        ParseQuery<Story> query = ParseQuery.getQuery(Story.class);
        query.whereEqualTo(Story.STORY_USERNAME, user);
        query.setLimit(limitRequest);


        query.findInBackground(new FindCallback<Story>() {
            @Override
            public void done(List<Story> objects, ParseException e) {
                progressBar.setVisibility(View.GONE);

                if(e == null)  {

                    for(ParseObject result : objects) {
                    //    Toast.makeText(SearchResult.this,  "found", Toast.LENGTH_LONG).show();

                        ParseFile storyImage = (ParseFile) result.get(Story.STORY_IMAGE);
                        String url = storyImage.getUrl();

                        final Story sTemp = new Story();
                        sTemp.setStoryImageUrl(url);
                        sTemp.setStoryDescription(result.getString(Story.STORY_DESCRIPTION));
                        sTemp.setStoryCaption(result.getString(Story.STORY_CAPTION));
                        sTemp.setId(result.getObjectId());
                        sTemp.setClicked(false);
                        sTemp.setUsername(result.getString(Story.STORY_USERNAME));
                        listOfStories.add(sTemp);

                    }
                    Toast.makeText(SearchResult.this, listOfStories.size() + "", Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();



                } else {
                    Toast.makeText(SearchResult.this, "Error!", Toast.LENGTH_SHORT).show();
                }


            }
        });
       // Toast.makeText(SearchResult.this, user + "", Toast.LENGTH_SHORT).show();
    }

}


