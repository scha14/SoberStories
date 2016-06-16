package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.Story;
import soberstories.app.jsmtech.sobersstories.R;
import soberstories.app.jsmtech.sobersstories.SearchResult;

/**
 * Created by Sukriti on 6/8/16.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.RV_ViewHolder> {

    private List<String> listOfUsers;
    private Context mContext;
    View itemView;

    public String url;


    public UserListAdapter(Context context, ArrayList<String> listOfUsers) {
        this.mContext = context;
        this.listOfUsers = listOfUsers;
    }

    @Override
    public RV_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_view, parent, false); // link to xml
        return new RV_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RV_ViewHolder holder, final int position) {
        final String username = listOfUsers.get(holder.getAdapterPosition());
        holder.mUsername.setText(username);


        holder.mUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchResult.class);
                intent.putExtra(Story.STORY_USERNAME, username);
                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return listOfUsers.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class RV_ViewHolder extends RecyclerView.ViewHolder {

        protected TextView mUsername;

        public RV_ViewHolder(View itemView) {
            super(itemView);

            mUsername = (TextView) itemView.findViewById(R.id.username_text);

        }


    }
}
