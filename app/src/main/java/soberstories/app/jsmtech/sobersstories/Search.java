package soberstories.app.jsmtech.sobersstories;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import adapter.UserListAdapter;

/**
 * Created by Sukriti on 6/4/16.
 */
public class Search extends Fragment {

    private View myFragmentView;

    private EditText mEnteredUsername;
    private ImageButton mSearchUsername;
    private RecyclerView mRecList;
    ArrayList<String> listOfUsers;
    private UserListAdapter mAdapter;
    private int searchLimit = 25;


    public Search() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.search_user_stories, container, false);

        mEnteredUsername = (EditText) myFragmentView.findViewById(R.id.search_username_text);
        mSearchUsername = (ImageButton) myFragmentView.findViewById(R.id.search_username_button);
        mRecList = (RecyclerView) myFragmentView.findViewById(R.id.search_user_recycler_view);

        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        llm.setOrientation(
                LinearLayoutManager.VERTICAL
        );
        mRecList.setLayoutManager(llm);
        mRecList.setHasFixedSize(true);
        listOfUsers = new ArrayList<String>();
        mAdapter = new UserListAdapter(getActivity(), listOfUsers);
        mRecList.setAdapter(mAdapter);

        mSearchUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listOfUsers = new ArrayList<String>(); // Clearing the existing list Of users!

                hideKeyBoard(getActivity());

                final ProgressDialog progressBar = new ProgressDialog(getActivity());
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.setCancelable(false);
                progressBar.show();
                mSearchUsername.setEnabled(false);

                listOfUsers.clear();
                mAdapter.notifyDataSetChanged();
                String enteredUsername = mEnteredUsername.getText().toString().trim().toLowerCase();

                if(enteredUsername.isEmpty()) {
                    Toast.makeText(getActivity(), "No Username Entered", Toast.LENGTH_SHORT).show();
                } else {

                    ParseQuery<ParseUser> mQuery = ParseUser.getQuery();
                    mQuery.whereStartsWith("username", enteredUsername);
                    mQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {

                            // This is the point where we have results form querying.
                            progressBar.dismiss();
                            mSearchUsername.setEnabled(true);


                            if(e == null) { // No error

                                if(objects.size() == 0) {
                                    // No User Found
                                    Toast.makeText(getActivity(), "No User Found", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Found Users!
                                    for(ParseUser singleUser : objects) {
                                        listOfUsers.add(singleUser.getUsername());
                                    }

//                                    mAdapter = new UserListAdapter(getActivity(), listOfUsers);
//                                    mRecList.setAdapter(mAdapter);

                                    mAdapter.notifyDataSetChanged();

                                }


                            } else {
                                Toast.makeText(getActivity(), "Error Fetching Users list", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }

            }
        });




        return myFragmentView;
    }

    private void hideKeyBoard(Activity activity) {

            InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
