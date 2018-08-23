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
    TextView nameTxt;
    TextView authorTxt;
    TextView lastEditedTxt;
    TextView descriptionTxt;
    View view;
    String NoneScenarioFileName = "JFGTW(KY^@$#(FJSGDRWTSJC&&@$UM,.HST";
    private Scenario selectedScenario = null;


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
        view = inflater.inflate(R.layout.fragment_setup_scenario, container, false);

        Button button = view.findViewById(R.id.nextPageButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.userSelectedNextTab();
            }
        });

        scenarioListView = (ListView) view.findViewById(R.id.scenarioSelectionList);

        nameTxt = (TextView) view.findViewById(R.id.scenarioNameTxt);
        authorTxt = (TextView) view.findViewById(R.id.scenarioAuthorTxt);
        lastEditedTxt = (TextView) view.findViewById(R.id.scenarioLastEditedTxt);
        descriptionTxt = (TextView) view.findViewById(R.id.scenarioDescriptionTxt);

        initScenariosList();

        return view;
    }


    private void initScenariosList() {

        ScenarioLoader loader = new ScenarioLoader();
        List<Scenario> scenarios = loader.getAvailableScenarios();

        Scenario noScenario = new Scenario();
        noScenario.fileName = NoneScenarioFileName;
        noScenario.name = getResources().getString(R.string.no_scenario);
        noScenario.author = "";
        noScenario.lastUpdated = "";
        noScenario.name = getResources().getString(R.string.no_scenario);
        noScenario.description = getResources().getString(R.string.no_scenario_description);

        scenarios.add(0, noScenario);
        ArrayAdapter<Scenario> myarrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, scenarios);
        scenarioListView.setAdapter(myarrayAdapter);

        scenarioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                scenarioListView.setSelection(position);

                Scenario selected = (Scenario) parent.getItemAtPosition(position);

                updateSummary(selected);
                processSelection(selected);
            }
        });

        scenarioListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateSummary(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                updateSummary(null);
            }
        });
    }


    private void updateSummary(Scenario selected) {
        String name = "-";
        String author = "";
        String date = "";
        String description = "";

        if (selected != null) {
            if (selected.name != null) {
                name = selected.name;
            }
            if (selected.author != null) {
                author = selected.author;
            }

            if (selected.lastUpdated != null) {
                date = selected.lastUpdated;
            }

            if (selected.description != null) {
                description = selected.description;
            }
        }

        nameTxt.setText(name);
        authorTxt.setText(author);
        lastEditedTxt.setText(date);
        descriptionTxt.setText(description);
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


    private void processSelection(Scenario selected) {

        if (selected == null) {
            selectedScenario = null;
        } else if (selected.fileName.equals(NoneScenarioFileName)) {
            selectedScenario = null;
        } else if (selected.name == null) {
            selectedScenario = null;
        }
        else{
            selectedScenario = selected;
        }
    }


}
