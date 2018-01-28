package com.joostit.vfradar.infolisting;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.joostit.vfradar.R;
import com.joostit.vfradar.data.TrackedAircraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InfoListFragment extends Fragment {

    private final AircraftListCollection list = new AircraftListCollection();
    private final Map<Integer, ListItemView> itemViews = new HashMap<>();
    private OnListFragmentInteractionListener mListener;
    private View rootView;
    private LinearLayout listView;

    public InfoListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_info_list, container, false);

        listView = (LinearLayout) rootView.findViewById(R.id.scrollLayout).findViewById(R.id.listLayout);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        for(int i = 0; i < 20; i++) {
//
//            ListItemView vw = new ListItemView(getContext());
//            listView.addView(vw);
//        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void UpdateAircraft(List<TrackedAircraft> ac) {
        InfoListUpdateResults results = list.updateItems(ac, getContext());

        if (results.added.size() > 0 || results.removed.size() > 0) {

            for (InfoListItemData toRemove : results.removed){
                listView.removeView(itemViews.get(toRemove.trackId));
                itemViews.remove(toRemove.trackId);
            }

            for (InfoListItemData toAdd : results.added) {
                ListItemView newItem = new ListItemView(getContext());
                listView.addView(newItem);
                itemViews.put(toAdd.trackId, newItem);
            }
        }

        for (InfoListItemData dataItem: list.getListItems()) {
            itemViews.get(dataItem.trackId).updateAircraftInfo(dataItem);
        };

    }


    private String getListItemTag(int trackId){
        return "trackId_" + trackId;
    }


    public interface OnListFragmentInteractionListener {

        void onAircraftSelectedFromList(Integer trackId);
    }
}
