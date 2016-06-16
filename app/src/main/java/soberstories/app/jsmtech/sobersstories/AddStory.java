package soberstories.app.jsmtech.sobersstories;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import model.Story;

/**
 * Created by Sukriti on 6/4/16.
 */
public class AddStory extends Fragment {


    private static final int MEDIA_TYPE_VIDEO = 2;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 300;
    private View myFragmentView;
    private ImageButton mAddImageButton;
    private ImageButton mAddVideoButton;
    private EditText mStoryCaption;
    private EditText mStoryDescription;
    private ImageView mStoryImage;
    private ImageView mStoryVideo;
    private Button mSubmitButton;

    private boolean isImageAdded = false;
    private boolean isVideoAdded = false;

    private ParseFile storyImageFile;
    private Uri fileUri;
    private ParseFile storyVideoFile;

    public AddStory() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.add_story, container, false);
        mAddImageButton = (ImageButton) myFragmentView.findViewById(R.id.add_image_a);
        mAddVideoButton = (ImageButton) myFragmentView.findViewById(R.id.add_video);
        mStoryCaption = (EditText) myFragmentView.findViewById(R.id.story_caption);
        mStoryDescription = (EditText) myFragmentView.findViewById(R.id.story_description);
        mStoryImage = (ImageView) myFragmentView.findViewById(R.id.story_image);
        mStoryVideo = (ImageView) myFragmentView.findViewById(R.id.story_video);
        mSubmitButton = (Button) myFragmentView.findViewById(R.id.submitStory);


        mAddImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isImageAdded = false;
                addImageFromGalleryOrCamera();
            }
        });

        mAddVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getActivity(), "Button clicked", Toast.LENGTH_SHORT).show();
                isVideoAdded = false;
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);

            }
        });


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard(getActivity());

                String caption = mStoryCaption.getText().toString().trim();
                String description = mStoryDescription.getText().toString().trim();


                if (caption.isEmpty() || description.isEmpty())
                    Toast.makeText(getActivity(), "Empty Fields ", Toast.LENGTH_SHORT).show();
                else {

                    if (isImageAdded) {

                        mSubmitButton.setEnabled(false);
                        // Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_LONG).show();
                        Story newStory = new Story();

                        newStory.setStoryCaption(caption);
                        newStory.setStoryDescription(description);
                        newStory.setStoryImage(storyImageFile);
                        if (isVideoAdded) {
                            newStory.setStoryVideo(storyVideoFile);
                        }
                        newStory.setUsername(ParseUser.getCurrentUser().getUsername());
                        newStory.setStoryInspired(0); // for now
                        newStory.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(getActivity(), "Story Added :D ", Toast.LENGTH_LONG).show();
                                    // submitButton.setEnabled(true);
                                } else {
                                    // submitButton.setEnabled(true);
                                    Toast.makeText(getActivity(), "Story Not Added ", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        mSubmitButton.setEnabled(true);
                        mStoryDescription.getText().clear();
                        mStoryCaption.getText().clear();
                        mStoryImage.setImageResource(android.R.color.transparent);
                        mStoryVideo.setImageResource(android.R.color.transparent);


                    } else

                        Toast.makeText(getActivity(), "Image has not been added", Toast.LENGTH_SHORT).show();


                }

            }
        });

        return myFragmentView;

    }

    private void addImageFromGalleryOrCamera() {
        // Give user option to add image from camera, or picture ggallery.

        final CharSequence[] selectImageOptions = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle("Add Image");
        b.setItems(selectImageOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (which == 0) {// Camera

                    try {

                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "e.jpg");
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        if (takePicture.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivityForResult(takePicture, 100);
                        }

                    } catch (Exception e) {

                    }

                } else if (which == 1) { // Gallery

                    Intent takePictureFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(takePictureFromGallery, 200);


                } else if (which == 2) {
                    // Cancel
                    dialog.dismiss();
                }

            }
        });

        b.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap mBitmap;

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
            //Camera
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "e.jpg");

                mBitmap = decodeSampledBitmapFromFile(f.getAbsolutePath(), 500, 500);
                mStoryImage.setImageBitmap(mBitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream); // Compression from scale of 0 to 100
                byte[] imageData = stream.toByteArray();

                storyImageFile = new ParseFile("e.jpg", imageData);
                storyImageFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), "image Saved", Toast.LENGTH_SHORT).show();
                            isImageAdded = true;
                        }
                    }
                });


            } else if (requestCode == 200) {
                //Gallery

                Uri selectedImageUri = data.getData();
                String tempPath = getPath(selectedImageUri, getActivity());
                BitmapFactory.Options mOption = new BitmapFactory.Options();
                mBitmap = BitmapFactory.decodeFile(tempPath, mOption);
                mStoryImage.setImageBitmap(mBitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] imageData = stream.toByteArray();

                storyImageFile = new ParseFile("e.jpg", imageData);
                storyImageFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), "image Saved", Toast.LENGTH_SHORT).show();
                            isImageAdded = true;
                        }
                    }
                });


            } else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {


                Uri selectedImageUri = data.getData();

                String selectedImagePath = getPathVideo(selectedImageUri);

                if (!selectedImagePath.isEmpty()) {

                    FileInputStream fileInputStream = null;
                    File fileObj = new File(selectedImagePath);

                    String tempPath = getImagePath(selectedImageUri, getActivity());
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    Bitmap mBitmap2 = ThumbnailUtils.createVideoThumbnail(fileObj.getAbsolutePath(),
                            MediaStore.Video.Thumbnails.MINI_KIND);
                     mStoryVideo.setImageBitmap(mBitmap2); // Its a imageview!

//
                    byte[] videoUp = new byte[(int) fileObj.length()];
                    try {
                        //convert file into array of bytes
                        fileInputStream = new FileInputStream(fileObj);
                        fileInputStream.read(videoUp);
                        fileInputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    storyVideoFile = new ParseFile("Vid.mp4", videoUp);
                    storyVideoFile.saveInBackground(new SaveCallback() {

                        public void done(ParseException e) {
                            if (e == null) {


                            } else
                                // e.printStackTrace();
                                Toast.makeText(getActivity(), "Error Uploading Video, Please try again.", Toast.LENGTH_LONG).show();
                            }

                    }, new ProgressCallback() {

                        public void done(Integer percentDone) {
                            // mPercentUpload.setText("Upload : " +percentDone+ "%");
                            // System.out.println("Progress :" + percentDone);

                            // mElasticDownloadView.setProgress(percentDone);

//                             Toast.makeText(getActivity(), "Progress : " + percentDone, Toast.LENGTH_LONG).show();


                            if(percentDone == 100) {
                                isVideoAdded = true;
                                Toast.makeText(getActivity(), "Video is Uploaded!", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Path :(", Toast.LENGTH_LONG).show();
                }



            }
        }
    }

    private String getPathVideo(Uri selectedImageUri) {
        Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    public String getImagePath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    @Deprecated
    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }


    private void hideKeyBoard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}
