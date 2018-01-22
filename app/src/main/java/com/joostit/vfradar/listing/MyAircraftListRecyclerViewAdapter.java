package com.joostit.vfradar.listing;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joostit.vfradar.R;
import com.joostit.vfradar.SysConfig;
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
        holder.nameTypeView.setText(item.nameType);
        holder.setHasAdsb(item.hasAdsb);
        holder.setHasOgn(item.hasOgn);

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

        private final int backColorTrue = 0xFF00FF00;
        private final int backColorFalse = 0xFF005500;

        private final int foreColorTrue = 0xFF000000;
        private final int foreColorFalse = 0xFF000000;

        public final View mView;
        public final TextView mNameView;
        public final TextView altitudeView;
        public final TextView modelView;
        public final TextView relDistanceView;
        public final TextView relBearingView;
        public final TextView vrateView;
        public final TextView cnView;
        public final TextView nameTypeView;
        private final TextView hasOgnView;
        private final TextView hasAdsbView;

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
            nameTypeView = (TextView) view.findViewById(R.id.nameTypeView);
            hasOgnView = (TextView) view.findViewById(R.id.hasOgn);
            hasAdsbView = (TextView) view.findViewById(R.id.hasAdsb);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + altitudeView.getText() + "'";
        }


        public void setHasAdsb(boolean hasAdsb){
            hasAdsbView.setTextColor(getForeColor(hasAdsb));
            hasAdsbView.setBackgroundColor(getBackColor(hasAdsb));
        }

        public void setHasOgn(boolean hasOgn){
            hasOgnView.setTextColor(getForeColor(hasOgn));
            hasOgnView.setBackgroundColor(getBackColor(hasOgn));
        }

        private int getBackColor(boolean valueTrue){
            if(valueTrue){
                return backColorTrue;
            }
            else{
                return backColorFalse;
            }
        }

        private int getForeColor(boolean valueTrue){
            if(valueTrue){
                return foreColorTrue;
            }
            else{
                return foreColorFalse;
            }
        }
    }
}
