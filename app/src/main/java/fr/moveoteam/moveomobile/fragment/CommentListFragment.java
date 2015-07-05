package fr.moveoteam.moveomobile.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.activity.OtherUserProfileActivity;
import fr.moveoteam.moveomobile.activity.TripActivity;
import fr.moveoteam.moveomobile.activity.FriendProfileActivity;
import fr.moveoteam.moveomobile.adapter.CommentListAdapter;
import fr.moveoteam.moveomobile.model.Comment;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 23/05/15.
 */
public class CommentListFragment extends ListFragment {

    ArrayList<Comment> commentArrayList;
    int tripId;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*if(getArguments().getParcelableArrayList("commentList") != null) {

            commentArrayList = getArguments().getParcelableArrayList("commentList");
            Log.e("array", commentArrayList.toString());
        }
        else */ //commentArrayList = null;
        TripActivity tripActivity = (TripActivity) getActivity();
        tripId = ((TripActivity) getActivity()).getId();
        // tripId = getArguments().getInt("tripId");
        ExecuteThread executeThread = new ExecuteThread();
        executeThread.execute();

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        Comment comment = commentArrayList.get(position);
        Intent intent = new Intent(getActivity(), OtherUserProfileActivity.class);
        intent.putExtra("id",comment.getIdUser());
        startActivity(intent);

    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
       // private ProgressDialog pDialog;

       /* @Override //Procedure appelée avant le traitement (optionnelle)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }*/

        @Override
        //Méthode appelée pendant le traitement (obligatoire)
        protected JSONObject doInBackground(String... args) {

            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.getComments(Integer.toString(tripId));
        }

        @Override
        //Procedure appelée après le traitement (optionnelle)
        protected void onPostExecute(JSONObject json) {
            //pDialog.dismiss();

            try {
                if(json == null){
                    setListAdapter(null);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    });
                    builder.setMessage("Récupération des commentaires échoué");
                    builder.setPositiveButton("OK", null);
                    builder.show();

                }else if (json.getString("success").equals("1")) {
                    JSONArray commentList = json.getJSONArray("comment");
                    commentArrayList = new ArrayList<>(commentList.length());
                    for (int i = 0; i < commentList.length(); i++) {
                        commentArrayList.add(new Comment(
                                commentList.getJSONObject(i).getInt("comment_id"),
                                commentList.getJSONObject(i).getString("comment_message"),
                                commentList.getJSONObject(i).getString("comment_added_datetime"),
                                commentList.getJSONObject(i).getInt("user_id"),
                                commentList.getJSONObject(i).getString("user_last_name"),
                                commentList.getJSONObject(i).getString("user_first_name"),
                                commentList.getJSONObject(i).getString("user_link_avatar")
                        ));
                        Log.e("comment", commentArrayList.get(i).toString());
                    }

                    setListAdapter(new CommentListAdapter(getActivity(), commentArrayList));

                }else{
                    setListAdapter(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
