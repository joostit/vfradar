package com.joostit.vfradar.infolisting;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.joostit.vfradar.R;
import com.joostit.vfradar.data.AircraftTrackingUpdate;
import com.joostit.vfradar.data.TrackedAircraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InfoListFragment extends Fragment implements ListItemViewEventHandler {

    private final AircraftListCollection list = new AircraftListCollection();
    private final Map<Integer, ListItemView> itemViews = new HashMap<>();
    private OnListFragmentInteractionListener mListener;
    private View rootView;
    private ScrollView scrollView;
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
        scrollView = (ScrollView) rootView.findViewById(R.id.scrollLayout).findViewById(R.id.scrollLayout);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void selectAircraft(Integer trackId) {
        for (ListItemView vw : itemViews.values()) {
            vw.setSelected(false);
        }

        if (trackId != null) {

            ListItemView selectedView = itemViews.get(trackId);
            selectedView.setSelected(true);
            scrollToView(selectedView);
        }
    }


    private void scrollToView(ListItemView vw) {
        Rect viewBounds = new Rect();
        vw.getHitRect(viewBounds);

        Rect scrollBounds = new Rect();
        scrollView.getDrawingRect(scrollBounds);

        if (!Rect.intersects(scrollBounds, viewBounds)) {
            scrollView.scrollTo(0, (int) vw.getY());
        }
    }

    public void updateAircraft(AircraftTrackingUpdate lastUpdateState) {
        InfoListUpdateResults results = list.updateItems(lastUpdateState, getContext());

        if (results.added.size() > 0 || results.removed.size() > 0) {

            for (InfoListItemData toRemove : results.removed) {
                listView.removeView(itemViews.get(toRemove.trackId));
                itemViews.remove(toRemove.trackId);
            }

            for (InfoListItemData toAdd : results.added) {
                ListItemView newItem = new ListItemView(getContext(), this);
                listView.addView(newItem);
                itemViews.put(toAdd.trackId, newItem);
            }
        }

        for (InfoListItemData dataItem : list.getListItems()) {
            itemViews.get(dataItem.trackId).updateAircraftInfo(dataItem);
        }
        ;

    }

    @Override
    public void onPressed(Integer trackId) {
        mListener.onAircraftSelectedFromList(trackId);
    }


    public interface OnListFragmentInteractionListener {
        void onAircraftSelectedFromList(Integer trackId);
    }
}
