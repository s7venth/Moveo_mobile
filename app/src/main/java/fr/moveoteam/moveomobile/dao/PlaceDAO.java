package fr.moveoteam.moveomobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.model.Place;
import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by Sylvain on 18/04/15.
 */
public class PlaceDAO {

    // Base de données utilisable
    private SQLiteDatabase database = null;

    // Base de données inutilisable
    private DataBaseHandler dbHandler;

    // NOM DE LA TABLE
    private static final String TABLE_PLACE = "place";

    // LES COLONNES
    private String[] allColumns = { DataBaseHandler.KEY_PLACE_ID,
                                    DataBaseHandler.KEY_PLACE_NAME,
                                    DataBaseHandler.KEY_PLACE_ADDRESS,
                                    DataBaseHandler.KEY_PLACE_DESCRIPTION,
                                    DataBaseHandler.KEY_PLACE_CATEGORY,
                                    DataBaseHandler.KEY_PLACE_TRIP_ID};

    public PlaceDAO(Context context){
        dbHandler = new DataBaseHandler(context);
    }

    /**
     * Permet à la base de données de faire des ajouts ou des suppressions
     * @return Une base de données modifiable (Écriture + lecture)
     */
    public SQLiteDatabase open() {
        database = dbHandler.getWritableDatabase();
        return database;
    }

    /**
     * Permet de fermer la base de données
     */
    public void close() {
        dbHandler.close();
    }

    /**
     * Recupere la liste des lieux d'un voyage
     * @param tripId l'identifiant du lieu
     * @return une arrayList de lieu
     */
    public ArrayList<Place> getPlaceList(int tripId){
        ArrayList<Place> placeList = null;

        Cursor cursor = database.query(TABLE_PLACE, allColumns, DataBaseHandler.KEY_PLACE_TRIP_ID + " = " + tripId, null, null, null, null);
        if(cursor.getCount()>0) {
            placeList = new ArrayList<>(cursor.getCount());
        }
        // Se déplacer à la première ligne
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            assert placeList != null;
            placeList.add(this.cursorToPlace(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        // database.close();
        if(placeList != null) {
            Log.i("Verification taille ", "" + placeList.size());
            Log.i("Verification nom ", "" + placeList.get(0).getName());
            Log.i("Verification nom ", "" + placeList.get(1).getName());
        }
        return placeList;
    }

    public void addPlaceList(ArrayList<Place> placeList) {
        ContentValues values;
        for(Place place : placeList) {
            values = new ContentValues();
            values.put(DataBaseHandler.KEY_PLACE_ID, place.getId());
            values.put(DataBaseHandler.KEY_PLACE_NAME, place.getName());     // NOM
            values.put(DataBaseHandler.KEY_PLACE_ADDRESS, place.getAddress());   // PRÉNOM
            values.put(DataBaseHandler.KEY_PLACE_DESCRIPTION, place.getDescription());     // DATE DE NAISSANCE
            values.put(DataBaseHandler.KEY_PLACE_CATEGORY, place.getCategory());
            values.put(DataBaseHandler.KEY_PLACE_TRIP_ID, place.getTripId());
            // Insérer la ligne
            database.insert(TABLE_PLACE, null, values);
        }
    }

    /**
     * Récupere les informations du curseur pour les mettre dans la classe Place
     * @param cursor un curseur
     * @return un lieu (Place)
     */
    protected Place cursorToPlace(Cursor cursor){

        Place place = new Place();
        place.setId(cursor.getInt(DataBaseHandler.POSITION_PLACE_ID));
        place.setName(cursor.getString(DataBaseHandler.POSITION_PLACE_NAME));
        place.setAddress(cursor.getString(DataBaseHandler.POSITION_PLACE_ADDRESS));
        place.setDescription(cursor.getString(DataBaseHandler.POSITION_PLACE_DESCRIPTION));
        place.setCategory(cursor.getInt(DataBaseHandler.POSITION_PLACE_CATEGORY));
        place.setTripId(cursor.getInt(DataBaseHandler.POSITION_PLACE_TRIP_ID));

        return place;
    }

}