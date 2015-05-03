package fr.moveoteam.moveomobile.webservice;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.moveoteam.moveomobile.dao.DataBaseHandler;

/**
 * Created by Sylvain on 01/04/15.
 */
public class JSONUser {

    private JSONParser jsonParser;

    // Il faut utiliser l'adresse http://10.0.2.2/ pour se connecter au localhost : http://localhost/
    // 10.0.3.2 pour genymotion
    private static String userURL = "http://192.168.1.33/Moveo_webservice/user.php";

    // constructor
    public JSONUser(){
        this.jsonParser = new JSONParser();
    }

    /**
     * Envoie les informations saisies du formulaire d'inscription sous la forme d'un objet json vers le webservice
     * @param email une adresse mail saisie par l'utilisateur dans le formulaire d'inscription
     * @param password un mot de passe saisi par l'utilisateur dans le formulaire d'inscription
     * @param name un nom saisi par l'utilisateur
     * @param firstName un prénom saisi par l'utilisateur
     * @return un objet <b>json</b> contenant une réponse indiquant si l'ajout a réussi ou non
     */
    public JSONObject addUser(String email, String password, String name, String firstName){

        //ArrayList sous la forme <clé,valeur> contenant les informations du formulaire d'inscription
        List<NameValuePair> registerForm = new ArrayList<>();
        registerForm.add(new BasicNameValuePair("tag", "register"));
        registerForm.add(new BasicNameValuePair("email", email));
        registerForm.add(new BasicNameValuePair("password", password));
        registerForm.add(new BasicNameValuePair("name", name));
        registerForm.add(new BasicNameValuePair("firstName", firstName));

        return jsonParser.getJSONFromUrl(userURL, registerForm);
    }

   /**
    * Envoie les informations saisies du formulaire de connexion sous la forme d'un objet json vers le webservice
    * @param email une adresse mail saisie par l'utilisateur dans le formulaire de connexion
    * @param password un mot de passe saisi par l'utilisateur dans le formulaire de connexion
    * @see JSONParser
    * @return un objet json contenant soit un message d'erreur ou soit les informations de l'utilisateur
    */
    public JSONObject loginUser(String email, String password){

        //ArrayList sous la forme <clé,valeur> contenant les informations du formulaire de connexion
        List<NameValuePair> loginForm = new ArrayList<>();
        loginForm.add(new BasicNameValuePair("tag","login"));
        loginForm.add(new BasicNameValuePair("email",email));
        loginForm.add(new BasicNameValuePair("password",password));

        return jsonParser.getJSONFromUrl(userURL,loginForm);
    }

    /**
     * Méthode qui envoie l'adresse mail saisie dans la popup Lost_password sous la forme d'un objet json vers le Webservice
     * @param email l'adresse mail saisie par l'utilisateur en question
     * @return un objet json contenant l'adresse mail de l'utilisateur
     */
    public JSONObject lostPassword (String email){
        List<NameValuePair> lostPasswordForm = new ArrayList<>();
        lostPasswordForm.add(new BasicNameValuePair("tag", "forgetPassword"));
        lostPasswordForm.add(new BasicNameValuePair("user_email", email));

        return jsonParser.getJSONFromUrl(userURL,lostPasswordForm);
    }

}
