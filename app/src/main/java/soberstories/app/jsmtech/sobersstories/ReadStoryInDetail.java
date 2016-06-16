package soberstories.app.jsmtech.sobersstories;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import model.Story;

/**
 * Created by Sukriti on 6/6/16.
 */
public class ReadStoryInDetail extends AppCompatActivity {

    String caption, description, imageUrl;
    String videoUrl = "";

    private ImageView mStoryImageIV;
    private TextView mCaptionTV, mDescriptionTV;
    private Button mShareButton;
    private VideoView mVideoView;

    private MediaController ctlr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_story_in_detail);

        mStoryImageIV = (ImageView) findViewById(R.id.story_image);
        mVideoView = (VideoView) findViewById(R.id.story_video);
        mCaptionTV = (TextView) findViewById(R.id.caption_text);
        mDescriptionTV = (TextView) findViewById(R.id.description_text);
        mShareButton = (Button) findViewById(R.id.share);

        // Fetch Data From Intent

        Bundle data = getIntent().getExtras();
        if (data != null) {
            caption = data.getString(Story.STORY_CAPTION);
            description = data.getString(Story.STORY_DESCRIPTION);
            imageUrl = data.getString("ImageUrl", "No Url Found!");
            videoUrl = data.getString("VideoUrl", "");

            if (!videoUrl.isEmpty()) {
                mStoryImageIV.setVisibility(View.GONE);

                // mVideoView.setBackgroundColor(Color.WHITE);
                // mVideoView.setVideoPath(videoUrl);
                Uri uri = Uri.parse(videoUrl);//
                mVideoView.setVideoURI(uri);
//
                ctlr = new MediaController(this);
                ctlr.setMediaPlayer(mVideoView);
                ctlr.setAnchorView(mVideoView);

                mVideoView.setMediaController(ctlr);
                mVideoView.requestFocus();

                mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // mVideoView.setBackgroundColor(Color.TRANSPARENT);
                        // mVideoView.setZOrderOnTop(true);
                        mVideoView.start();
                        Toast.makeText(ReadStoryInDetail.this, videoUrl + ": 4", Toast.LENGTH_SHORT).show();
                    }
                });

            }



        }


        Typeface fontCaption = Typeface.createFromAsset(getAssets(), "fonts/headline_1.ttf");
        mCaptionTV.setTypeface(fontCaption);
        mCaptionTV.setText(caption);


        // Toast.makeText(this, description, Toast.LENGTH_SHORT).show();
        Typeface secondFont = Typeface.createFromAsset(getAssets(), "fonts/story_1.ttf");
        mDescriptionTV.setTypeface(secondFont);
        mDescriptionTV.setText(description);


        Picasso.with(this)
                .load(imageUrl)
                .fit()
                .centerCrop()
                .error(R.drawable.proceed_button)
                .into(mStoryImageIV);

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Share Caption + Description with others!
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = caption + " - " + description;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sobers Stories");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share With Friends"));

            }
        });



    }
}
