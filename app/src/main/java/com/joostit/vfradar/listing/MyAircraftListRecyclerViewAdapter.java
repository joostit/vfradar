package com.joostit.vfradar.listing;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joostit.vfradar.R;
import com.joostit.vfradar.listing.AircraftListFragment.OnListFragmentInteractionListener;

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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).name);
        holder.altitudeView.setText(mValues.get(position).altitude);
        holder.modelView.setText(mValues.get(position).model);
        holder.vrateView.setText(mValues.get(position).vRate);
        holder.relPositionView.setText(mValues.get(position).relativePosition);
        holder.cnView.setText(mValues.get(position).cn);

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
        public final TextView relPositionView;
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
            relPositionView = (TextView) view.findViewById(R.id.relativePositionView);
            cnView = (TextView) view.findViewById(R.id.competitionNumberView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + altitudeView.getText() + "'";
        }
    }
}
