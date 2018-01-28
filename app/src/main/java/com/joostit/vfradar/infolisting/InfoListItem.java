package com.joostit.vfradar.infolisting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joostit.vfradar.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoListItem.OnInfoListItemInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoListItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoListItem extends Fragment {

    private OnInfoListItemInteractionListener mListener;

    private View rootView;

    private final int backColorTrue = 0xFF00FF00;
    private final int backColorFalse = 0xFF005500;

    private final int foreColorTrue = 0xFF000000;
    private final int foreColorFalse = 0xFF000000;

    public TextView nameView;
    public TextView altitudeView;
    public TextView modelView;
    public TextView relDistanceView;
    public TextView relBearingView;
    public TextView vRateView;
    public TextView cnView;
    public TextView nameTypeView;
    private TextView hasOgnView;
    private TextView hasAdsbView;

    private InfoListItemData currentState = new InfoListItemData();

    public InfoListItem() {
        // Required empty public constructor
    }


    public static InfoListItem newInstance(String param1, String param2) {
        InfoListItem fragment = new InfoListItem();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_info_list_item, container, false);


        setHasOgn(false);
        setHasAdsb(false);
        return rootView;
    }

    private void getViewReferences(View view){
        nameView = (TextView) view.findViewById(R.id.nameView);
        altitudeView = (TextView) view.findViewById(R.id.altitudeView);
        modelView = (TextView) view.findViewById(R.id.modelView);
        vRateView = (TextView) view.findViewById(R.id.vRateView);
        relDistanceView = (TextView) view.findViewById(R.id.relativeDistanceView);
        relBearingView = (TextView) view.findViewById(R.id.relativeBearingView);
        cnView = (TextView) view.findViewById(R.id.competitionNumberView);
        nameTypeView = (TextView) view.findViewById(R.id.nameTypeView);
        hasOgnView = (TextView) view.findViewById(R.id.hasOgn);
        hasAdsbView = (TextView) view.findViewById(R.id.hasAdsb);
    }


    public void onPressed(Uri uri) {
        if (mListener != null) {
            mListener.onInfoListItemSelected(null);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInfoListItemInteractionListener) {
            mListener = (OnInfoListItemInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInfoListItemInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        getViewReferences(rootView);

        nameTypeView.setText(currentState.nameType);
        nameView.setText(currentState.name);
        cnView.setText(currentState.cn);
        modelView.setText(currentState.model);
        altitudeView.setText(currentState.altitude);
        vRateView.setText(currentState.vRate);
        relBearingView.setText(currentState.relativeBearing);
        relDistanceView.setText(currentState.relativeDistance);
        setHasAdsb(currentState.hasAdsb);
        setHasOgn(currentState.hasOgn);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateAircraftInfo(InfoListItemData update){

        if(update.trackId != currentState.trackId){
            currentState.trackId = update.trackId;
        }

        if(update.nameType != currentState.nameType){
            currentState.nameType = update.nameType;
            //if (nameTypeView != null) nameTypeView.setText(update.nameType);
        }

        if(update.name != currentState.name){
            currentState.name = update.name;
            //if (nameView != null) nameView.setText(update.name);
        }

        if(update.cn != currentState.cn){
            currentState.cn = update.cn;
            //if (cnView != null) cnView.setText(update.cn);
        }

        if(update.model != currentState.model){
            currentState.model = update.model;
            //if (modelView != null) modelView.setText(update.model);
        }

        if(update.altitude != currentState.altitude){
            currentState.altitude = update.altitude;
            //if (altitudeView != null) altitudeView.setText(update.altitude);
        }

        if(update.vRate != currentState.vRate){
            currentState.vRate = update.vRate;
            //if (vRateView != null) vRateView.setText(update.vRate);
        }

        if(update.relativeBearing != currentState.relativeBearing){
            currentState.relativeBearing = update.relativeBearing;
            //if (relBearingView != null) relBearingView.setText(update.relativeBearing);
        }

        if(update.relativeDistance != currentState.relativeDistance){
            currentState.relativeDistance = update.relativeDistance;
            //if (relDistanceView != null) relDistanceView.setText(update.relativeDistance);
        }

        if(update.hasAdsb != currentState.hasAdsb){
            currentState.hasAdsb = update.hasAdsb;
            //setHasAdsb(update.hasAdsb);
        }

        if(update.hasOgn != currentState.hasOgn){
            currentState.hasOgn = update.hasOgn;
            //setHasOgn(update.hasOgn);
        }

    }

    public void setHasAdsb(boolean hasAdsb){
        if(hasAdsbView != null) {
            hasAdsbView.setTextColor(getForeColor(hasAdsb));
            hasAdsbView.setBackgroundColor(getBackColor(hasAdsb));
        }
    }

    public void setHasOgn(boolean hasOgn){
        if(hasOgnView != null) {
            hasOgnView.setTextColor(getForeColor(hasOgn));
            hasOgnView.setBackgroundColor(getBackColor(hasOgn));
        }
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


    public interface OnInfoListItemInteractionListener {
        void onInfoListItemSelected(Integer trackId);
    }


}
