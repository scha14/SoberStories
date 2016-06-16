package soberstories.app.jsmtech.sobersstories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Sukriti on 6/4/16.
 */
public class Stories extends Fragment {
    private View myFragmentView;

    ArrayList<Story> listOfStories;
    private RecyclerView recList;
    private ProgressBar progressBar;
    private StoryAdapter mAdapter;
    private int limitRequest = 15;

    public Stories() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.view_stories, container, false);

        recList = (RecyclerView) myFragmentView.findViewById(R.id.stories_list);
        progressBar = (ProgressBar) myFragmentView.findViewById(R.id.stories_progress_bar);


        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        llm.setOrientation(
                LinearLayoutManager.VERTICAL
        );
        recList.setLayoutManager(llm);
        recList.setHasFixedSize(true);


        recList.setOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onLoadMore(int current_page) {


                Toast.makeText(getActivity(), "Loading More Stories ", Toast.LENGTH_SHORT).show();
                loadMoreStories();

            }
        });



        getStoriesList();


        return myFragmentView;
    }

    private void loadMoreStories() {

        progressBar.setVisibility(View.VISIBLE);
        ParseQuery<Story> query = ParseQuery.getQuery(Story.class);

        query.setSkip(limitRequest);
        limitRequest += 15;
        query.setLimit(15);

        query.findInBackground(new FindCallback<Story>() {
            @Override
            public void done(List<Story> objects, ParseException e) {
                progressBar.setVisibility(View.GONE);

                if(e == null)  {

                    for(ParseObject result : objects) {
                        String vurl = "";
                        ParseFile storyImage = (ParseFile) result.get(Story.STORY_IMAGE);
                        String url = storyImage.getUrl();
                        if(result.get(Story.STORY_VIDEO)!=null) {
                            ParseFile storyVideo = (ParseFile) result.get(Story.STORY_VIDEO);
                            vurl = storyVideo.getUrl();
                        }
                        final Story sTemp = new Story();
                        sTemp.setStoryImageUrl(url);
                        sTemp.setVideoUrl(vurl);
                        sTemp.setStoryDescription(result.getString(Story.STORY_DESCRIPTION));
                        sTemp.setStoryCaption(result.getString(Story.STORY_CAPTION));
                        sTemp.setId(result.getObjectId());
                        sTemp.setClicked(false);

                        sTemp.setUsername(result.getString(Story.STORY_USERNAME));


                        listOfStories.add(sTemp);

                    }


                        mAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void getStoriesList() {

        progressBar.setVisibility(View.VISIBLE);
        listOfStories = new ArrayList<Story>();
        ParseQuery<Story> query = ParseQuery.getQuery(Story.class);
        query.setLimit(limitRequest);

        query.findInBackground(new FindCallback<Story>() {
            @Override
            public void done(List<Story> objects, ParseException e) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar

                if(e == null)  {

                    for(ParseObject result : objects) {

                        String vurl = "";
                        ParseFile storyImage = (ParseFile) result.get(Story.STORY_IMAGE);
                        String url = storyImage.getUrl();

                        if (result.get(Story.STORY_VIDEO) != null ) {
                            ParseFile storyVideo = (ParseFile) result.get(Story.STORY_VIDEO);
                            vurl = storyVideo.getUrl();
                        }


                        final Story sTemp = new Story();
                        sTemp.setStoryImageUrl(url);
                        sTemp.setVideoUrl(vurl);
                        sTemp.setStoryDescription(result.getString(Story.STORY_DESCRIPTION));
                        sTemp.setStoryCaption(result.getString(Story.STORY_CAPTION));
                        sTemp.setId(result.getObjectId());
                         sTemp.setClicked(false);

                        sTemp.setUsername(result.getString(Story.STORY_USERNAME));


                        listOfStories.add(sTemp);

                    }


                    mAdapter = new StoryAdapter(getActivity(), listOfStories);
                    recList.setAdapter(mAdapter);

                } else {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }


            }
        });



    }

}


