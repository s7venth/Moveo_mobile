package fr.moveoteam.moveomobile.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.activity.TripActivity;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.adapter.TripListAdapter;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 29/04/15.
 */
public class ExploreFragment extends ListFragment {

    private AdapterView.OnItemSelectedListener listener;
    private ArrayList<Trip> tripArrayList;
    private UserDAO userDAO;
    private ExecuteThread executeThread;

    public  ExploreFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        executeThread = new ExecuteThread();
        executeThread.execute();
    }

    /*
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // L'inflater permet de deserializer un fichier XML
        View view = inflater.inflate(R.layout.explore, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listViewExploreTrip);
        return view;
    }*/

    public interface OnItemSelectedListener {
        public void onRssItemSelected(String link);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       /* if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet MyListFragment.OnItemSelectedListener");
        } */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onPause() {
        this.onDestroy();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Trip trip = tripArrayList.get(position);
        Log.e("Recuperation",trip.getName());
        Intent intent = new Intent(getActivity(), TripActivity.class);
        intent.putExtra("id",trip.getId());
        startActivity(intent);
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
       // private ProgressDialog pDialog;
        int id;
        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            userDAO = new UserDAO(getActivity());
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Récupération des voyages...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }*/
        @Override
        protected JSONObject doInBackground(String... args) {
            userDAO = new UserDAO(getActivity());
            userDAO.open();
            id = userDAO.getUserDetails().getId();
            userDAO.close();
            JSONTrip jsonTrip = new JSONTrip();
            //Log.e("ID",Integer.toString(userDAO.getUserDetails().getId()));
            return jsonTrip.getExploreTrips(Integer.toString(id));
        }

        @Override
        protected void onCancelled() {
            Log.e("Explore","onCancelled");
            super.onCancelled();

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            //pDialog.dismiss();
            try {
                if(json == null){
                    setListAdapter(null);
                    Toast.makeText(getActivity(),"Les voyages n'ont pas pu être récupérer",Toast.LENGTH_SHORT).show();
                }
                // Si la récupération des voyages a été un succès on affecte les voyages dans un ArrayList
                else if(json.getString("success").equals("1")) {
                    // Recuperation des voyages sous la forme d'un JSONArray
                    JSONArray tripList = json.getJSONArray("trip");
                    tripArrayList = new ArrayList<>(tripList.length());

                    for (int i = 0; i < tripList.length(); i++) {
                        tripArrayList.add(new Trip(
                                tripList.getJSONObject(i).getInt("trip_id"),
                                tripList.getJSONObject(i).getInt("comment_count"),
                                tripList.getJSONObject(i).getInt("photo_count"),
                                tripList.getJSONObject(i).getString("trip_name"),
                                tripList.getJSONObject(i).getString("trip_country"),
                                tripList.getJSONObject(i).getString("trip_cover"),
                                tripList.getJSONObject(i).getString("user_last_name"),
                                tripList.getJSONObject(i).getString("user_first_name")
                        ));
                        Log.e("ExploreFragment",tripList.getJSONObject(i).getString("trip_cover"));
                    }
                    if(tripArrayList != null) {
                        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        //      R.layout.element_menu,R.id.title,values);

                        Log.e("afficher list", tripArrayList.get(1).getName());
                        setListAdapter(new TripListAdapter(getActivity(), tripArrayList, true));
                        Log.e("Message ", "" + tripArrayList.get(0).getName() + "" + tripArrayList.get(0).getName());
                    }
                }else{
                    setListAdapter(null);
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }

}