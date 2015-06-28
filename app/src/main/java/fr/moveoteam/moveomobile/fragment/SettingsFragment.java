package fr.moveoteam.moveomobile.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.User;
import fr.moveoteam.moveomobile.webservice.JSONUser;

/**
 * Created by Sylvain on 31/05/15.
 */
public class SettingsFragment extends Fragment {

    EditText password;
    EditText newPassword;
    EditText checkNewPassword;
    Button saveButton;
    private String passwordSave;
    private String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings,container,false);
        password = (EditText) view.findViewById(R.id.settings_password);
        newPassword = (EditText) view.findViewById(R.id.settings_new_password);
        checkNewPassword = (EditText) view.findViewById(R.id.settings_check_new_password);
        saveButton = (Button) view.findViewById(R.id.save_settings);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final UserDAO userDAO = new UserDAO(getActivity());
        userDAO.open();
        passwordSave = userDAO.getUserDetails().getPassword();
        id = Integer.toString(userDAO.getUserDetails().getId());
        userDAO.close();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDAO.open();
                int count = 0;

                if(!password.getText().toString().isEmpty()){
                    if(password.getText().toString().equals(passwordSave)){
                        count ++;
                    }else{
                        password.setError("Mot de passe incorrect");
                    }
                }else {
                    password.setError("Ce champ est vide");
                }

                if(!newPassword.getText().toString().isEmpty()){
                    count++;
                }else if(newPassword.getText().length() <= 7) {
                    newPassword.setError("Votre mot de passe doit contenir 8 caractères ou plus.");
                }else{
                    newPassword.setError("Ce champ est vide");
                }

                if(!checkNewPassword.getText().toString().isEmpty()){
                    count++;
                }else {
                    checkNewPassword.setError("Ce champ est vide");
                }



                if(count == 3){
                    if(newPassword.getText().equals(checkNewPassword.getText())){
                        new ExecuteThread().execute();
                    }else{
                        Toast.makeText(getActivity(),"Les mots de passes ne sont pas identiques",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Modification du mot de passe en cours");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONUser jsonUser = new JSONUser();
            return jsonUser.changePassword(id, passwordSave, newPassword.getText().toString());
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if(json == null){
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                builder.setMessage("Connexion perdu");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }else try {
                if(json.getString("success").equals("1")){
                    UserDAO userDAO = new UserDAO(getActivity());
                    userDAO.open();
                    userDAO.modifyPassword(newPassword.getText().toString());
                    userDAO.close();

                    password.setText("");
                    newPassword.setText("");
                    checkNewPassword.setError("");

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Votre mot de passe a été modifié");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Une erreur est survenue lors de l'enregistrement du mot de passe");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
