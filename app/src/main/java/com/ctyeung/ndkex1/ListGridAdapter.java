package com.ctyeung.ndkex1;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ctyeung.ndkex1.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

public class ListGridAdapter extends RecyclerView.Adapter<ListGridAdapter.NumberViewHolder>
{
    private static final String TAG = ListGridAdapter.class.getSimpleName();

    private int mViewHolderCount;
    private JSONArray mCircleList;

    public interface SVGLoadListener
    {
        void onLoadSVGComplete(boolean success);
    }

    public ListGridAdapter(JSONArray circles)
    {
        mViewHolderCount = 0;
        this.mCircleList = circles;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                               int viewType)
    {
        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        mViewHolderCount++;
        return viewHolder;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(NumberViewHolder holder,
                                 int position)
    {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }


    @Override
    public int getItemCount()
    {
        return this.mCircleList.length();
    }

    /**
     * Cache of the children views for a list item.
     */
    class NumberViewHolder extends RecyclerView.ViewHolder
    {
        TextView viewHolderName;

        public NumberViewHolder(View itemView)
        {
            super(itemView);
            viewHolderName = (TextView) itemView.findViewById(R.id.txt_circle_info);
        }

        /**
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         * @param position Position of the item in the list
         */
        void bind(int position)
        {
            try {
                JSONObject json = mCircleList.getJSONObject(position);
                viewHolderName.setText(json.toString());
            }
            catch (Exception ex)
            {
                viewHolderName.setText("failed to load");
            }
        }
    }
}
