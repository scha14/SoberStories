package adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;


public abstract class EndlessRecyclerOnScrollListenserStaggered extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class
            .getSimpleName();

    private boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    public EndlessRecyclerOnScrollListenserStaggered(
            StaggeredGridLayoutManager staggeredGridLayoutManager) {
        this.mStaggeredGridLayoutManager = staggeredGridLayoutManager;
    }



    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        visibleItemCount = mStaggeredGridLayoutManager.getChildCount();
        totalItemCount = mStaggeredGridLayoutManager.getItemCount();
        int[] firstVisibleItems = null;
        firstVisibleItems = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
        if(firstVisibleItems != null && firstVisibleItems.length > 0) {
            pastVisibleItems = firstVisibleItems[0];
        }

        if (loading) {
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                loading = false;
                Log.d("tag", "LOAD NEXT ITEM");
                current_page++;

                onLoadMore(current_page);
                loading = true;

            }
        }    }

    public abstract void onLoadMore(int current_page);
}



