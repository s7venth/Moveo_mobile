package fr.moveoteam.moveomobile.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe contenant divers fonctions utilisable n'importe où
 * Created by Sylvain on 14/04/15.
 */
public class Function {

    /**
     * VERIFIER LA CONNECTION SUR INTERNET (VIA 3G,WIFI,...)
     */
    public static boolean beConnectedToInternet(Context c){

        boolean bool = false;
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) bool = true;

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) bool = true;

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) bool = true;

        return bool;
    }


    /**
     * VERIFIER LA CONFORMITÉ DE L'EMAIL
     * @param email l'adresse mail à vérifier
     * @return vrai si l'email est conforme sinon faux
     */
    public static boolean isEmailAddress(String email){
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
        Matcher m = p.matcher(email.toUpperCase());
        return m.matches();

    }

    /**
     * Verification d'un champ contenant seulement des lettres
     * @param text
     * @return
     */
    public static boolean isString(String text){
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = p.matcher(text);
        if(matcher.matches()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Verification de la date de naissance
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static boolean testDate(int year, int month, int day){
        Boolean validation = false;

        GregorianCalendar calendrier = new GregorianCalendar();
        calendrier.set(year, month, day);
        int yearOk = calendrier.get(Calendar.YEAR);
        int monthOk = calendrier.get(Calendar.MONTH);
        int dayOk = calendrier.get(Calendar.DAY_OF_MONTH);
        System.out.println("MA DATE : "+ year +" "+ month +" "+ day );
        System.out.println("MA NOUVELLE DATE : "+yearOk+" "+ monthOk +" "+dayOk);

        if(yearOk == year & monthOk == month & dayOk == day){
            System.out.println("ok");
            validation = true;
        }

        return false;
    }

    public static Bitmap decodeBase64(String input)
    {
        Bitmap bitmap = null;
        byte[] bytes;

        if(input != null){
            Log.e("Image64", "taille : " + input.length());
            try{
                bytes = Base64.decode(input, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }catch (OutOfMemoryError e){
                e.getMessage();
                bitmap = null;
            }catch (IllegalArgumentException e){
                e.getMessage();
                bitmap = null;
            }
        }else bitmap = null;
        return bitmap;
    }

    public static String encodeBase64(Bitmap photo)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    public static String displayCurrentDate() {
        String format = "dd/MM/yy H:mm:ss";

        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(format);
        java.util.Date date = new java.util.Date();

        return formater.format(date);
    }

    public static String dateDifference (Date date){

        /** Date du jour */
        Date today = new Date( );

        // Calcul de différence
        long diff = today.getTime( ) - date.getTime( );

        return "Différence en nombre de jour entre "+date+ " et " + today +
                " nest " + (diff / (1000*60*60*24)) + " jours.";
    }

    public static String dateJavaToDateSql (String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Log.i("Function",sdf.toString());
        Date convertedDate = null;
        try {
            convertedDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("Function",convertedDate.toString());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

       // String newDate = sdf.format(convertedCurrentDate);
        return dateFormat.format(convertedDate);
    }

    public static String dateSqlToDateJava (String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Log.i("Function",sdf.toString());
        Date convertedDate = null;
        try {
            convertedDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("Function",convertedDate.toString());


        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // String newDate = sdf.format(convertedCurrentDate);
        return dateFormat.format(convertedDate);
    }

    public static String dateSqlToFullDateJava (String date) {

        Locale locale = Locale.getDefault();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Log.i("Function",sdf.toString());
        Date convertedDate = null;
        try {
            convertedDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Date sous le format complet (1 janvier 2015)
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);

        return dateFormat.format(convertedDate);
    }

    public static String dateTimeSqlToFullDateJava (String date) {

        Locale locale = Locale.getDefault();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Log.i("Function",sdf.toString());
        Date convertedDate = null;
        try {
            convertedDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Date sous le format complet (1 janvier 2015)
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.MEDIUM, locale);

        return dateFormat.format(convertedDate);
    }

    public static String dateJavaToFullDateJava (String date) {

        Locale locale = Locale.getDefault();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Log.i("Function",sdf.toString());
        Date convertedDate = null;
        try {
            convertedDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("Function",convertedDate.toString());
        // Date sous le format complet (1 janvier 2015)
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);

        return dateFormat.format(convertedDate);
    }

    public static String differenceDate(String date){

        Date actualyDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Log.i("Function",sdf.toString());
        Date convertedDate = null;
        try {
            convertedDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert convertedDate != null;
        String dateDisplay;
        long diffSeconds = (actualyDate.getTime() - convertedDate.getTime()) / 1000;
        if(diffSeconds > 60){
            long diffMinutes = (actualyDate.getTime() - convertedDate.getTime()) / (1000*60);
            if(diffMinutes > 60){
                long diffHours = (actualyDate.getTime() - convertedDate.getTime()) / (1000*60*60);
                if(diffHours > 24){
                    long diffDays = (actualyDate.getTime() - convertedDate.getTime()) / (1000*60*60*24);
                    if(diffDays > 7){
                        long diffWeeks = (actualyDate.getTime() - convertedDate.getTime()) /   ((1000 * 60 * 60) * (24 * 7));
                        if(diffWeeks > 4){
                            long diffMonths = (actualyDate.getTime() - convertedDate.getTime()) / (60000 * 60 * 24 * 7 * 30);
                            if(diffMonths > 12){
                                dateDisplay = " + d'un an";
                            }else{
                                dateDisplay = diffMonths + " mois";
                            }
                        }else{
                            dateDisplay = diffWeeks + " semaines";
                        }
                    }else{
                        dateDisplay = diffDays + " jours";
                    }

                }else{
                    dateDisplay = diffHours + " heures";
                }
            }else{
                dateDisplay = diffMinutes + " minutes";
            }
        }else{
            dateDisplay = diffSeconds + " secondes";
        }
        Log.e("test date","date actuel : "+actualyDate.toString()+" commentaire après : "+convertedDate.toString()+" commentaire avant : "+date);

        return dateDisplay;
    }

    public static String encrypt(String password){
        String crypte="";
        for (int i=0; i<password.length();i++)  {
            int c=password.charAt(i)^48;
            crypte=crypte+(char)c;
        }
        return crypte;
    }

    public static String decrypt(String password){
        String aCrypter="";
        for (int i=0; i<password.length();i++)  {
            int c=password.charAt(i)^48;
            aCrypter=aCrypter+(char)c;
        }
        return aCrypter;
    }
}
