package com.joostit.vfradar.Startup;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.joostit.vfradar.R;
import com.joostit.vfradar.scenarios.Scenario;
import com.joostit.vfradar.scenarios.ScenarioLoader;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStartupFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SetupScenarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetupScenarioFragment extends Fragment {

    private OnStartupFragmentInteractionListener mListener;

    private ListView scenarioListView;

    public SetupScenarioFragment() {
        // Required empty public constructor
    }



    public static SetupScenarioFragment newInstance() {
        SetupScenarioFragment fragment = new SetupScenarioFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setup_scenario, container, false);

        Button button = view.findViewById(R.id.nextPageButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.userSelectedNextTab();
            }
        });

        scenarioListView = (ListView)view.findViewById(R.id.scenarioSelectionList);

        initScenariosList();

        return view;
    }


    private void initScenariosList(){

        ScenarioLoader loader = new ScenarioLoader();
        List<Scenario> scenarios = loader.getAvailableScenarios();
        ArrayAdapter<Scenario> myarrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, scenarios);
        scenarioListView.setAdapter(myarrayAdapter);

        scenarioListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long i)
            {

            }
        });

        scenarioListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStartupFragmentInteractionListener) {
            mListener = (OnStartupFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStartupFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
