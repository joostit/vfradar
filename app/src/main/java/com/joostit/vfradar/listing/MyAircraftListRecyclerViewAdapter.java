package com.joostit.vfradar.listing;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joostit.vfradar.R;
import com.joostit.vfradar.listing.AircraftListFragment.OnListFragmentInteractionListener;
import com.joostit.vfradar.utilities.Numbers;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AircraftListItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAircraftListRecyclerViewAdapter extends RecyclerView.Adapter<MyAircraftListRecyclerViewAdapter.ViewHolder> {

    private final List<AircraftListItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyAircraftListRecyclerViewAdapter(List<AircraftListItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_aircraftlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        AircraftListItem item = mValues.get(position);

        holder.mItem = item;
        holder.mNameView.setText(item.name);
        holder.altitudeView.setText(item.altitude);
        holder.modelView.setText(item.model);
        holder.vrateView.setText(item.vRate);
        holder.relDistanceView.setText(item.relativeDistance);
        holder.relBearingView.setText(item.relativeBearing);
        holder.cnView.setText(item.cn);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    // Item is in holder.mItem
                    mListener.onAircraftSelectedFromList(1324);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView altitudeView;
        public final TextView modelView;
        public final TextView relDistanceView;
        public final TextView relBearingView;
        public final TextView vrateView;
        public final TextView cnView;
        public AircraftListItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.nameView);
            altitudeView = (TextView) view.findViewById(R.id.altitudeView);
            modelView = (TextView) view.findViewById(R.id.modelView);
            vrateView = (TextView) view.findViewById(R.id.vRateView);
            relDistanceView = (TextView) view.findViewById(R.id.relativeDistanceView);
            relBearingView = (TextView) view.findViewById(R.id.relativeBearingView);
            cnView = (TextView) view.findViewById(R.id.competitionNumberView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + altitudeView.getText() + "'";
        }
    }
}
