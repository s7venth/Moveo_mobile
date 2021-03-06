package fr.moveoteam.moveomobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by Sylvain on 18/04/15.
 */
public class TripDAO {

    // Base de données utilisable
    private SQLiteDatabase database = null;

    // Base de données inutilisable
    private DataBaseHandler dbHandler;

    // NOM DE LA TABLE
    private static final String TABLE_TRIP = "trip";

    // LES CHAMPS
    private static final String KEY_TRIP_ID = "trip_id";
    public static final String KEY_TRIP_NAME = "trip_name";
    public static final String KEY_TRIP_COUNTRY = "trip_country";
    private static final String KEY_TRIP_DESCRIPTION = "trip_description";
    public static final String KEY_TRIP_CREATED_AT = "trip_created_at";
    private static final String KEY_TRIP_COVER = "trip_cover";

    // LES COLONNES
    private String[] allColumns = { DataBaseHandler.KEY_TRIP_ID,
                                    DataBaseHandler.KEY_TRIP_NAME,
                                    DataBaseHandler.KEY_TRIP_COUNTRY,
                                    DataBaseHandler.KEY_TRIP_DESCRIPTION,
                                    DataBaseHandler.KEY_TRIP_CREATED_AT,
                                    DataBaseHandler.KEY_TRIP_COVER,
                                    DataBaseHandler.KEY_TRIP_COMMENT_COUNT,
                                    DataBaseHandler.KEY_TRIP_PHOTO_COUNT};

    public TripDAO(Context context){
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
	 *
	 */
	public void addTrip(Trip trip){
		
		ContentValues values = new ContentValues();
		if(trip.getId() != 0)values.put(DataBaseHandler.KEY_TRIP_ID, trip.getId());
        if(trip.getName() != null)values.put(DataBaseHandler.KEY_TRIP_NAME, trip.getName());     // NOM
        if(trip.getCountry() != null)values.put(DataBaseHandler.KEY_TRIP_COUNTRY, trip.getCountry());   // PRÉNOM
        if(trip.getDescription() != null)values.put(DataBaseHandler.KEY_TRIP_DESCRIPTION, trip.getDescription());     // DATE DE NAISSANCE
        if(trip.getDate() != null)values.put(DataBaseHandler.KEY_TRIP_CREATED_AT, String.valueOf(trip.getDate()));
        values.put(DataBaseHandler.KEY_TRIP_COMMENT_COUNT, String.valueOf(trip.getCommentCount()));
        values.put(DataBaseHandler.KEY_TRIP_PHOTO_COUNT, String.valueOf(trip.getPhotoCount()));
        if(trip.getCover() != null)values.put(DataBaseHandler.KEY_TRIP_COVER, trip.getCover());
		
		database.insert(TABLE_TRIP, null, values);
	}

    public ArrayList<Trip> getTripList(){
        ArrayList<Trip> tripList = null;
        String selectQuery = "SELECT  * FROM " + TABLE_TRIP;

        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.getCount()>0) {
            tripList = new ArrayList<>(cursor.getCount());
        }
        // Se déplacer à la première ligne
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            assert tripList != null;
            tripList.add(this.cursorToTrip(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        // database.close();
        if(tripList != null) {
            Log.i("Verification taille ", "" + tripList.size());
            Log.i("Verification nom ", "" + tripList.get(0).getName());
            //Log.i("Verification nom ", "" + tripList.get(1).getName());
        }
        return tripList;
    }

    public void addTripListUser(ArrayList<Trip> tripList) {
        ContentValues values;
        for(Trip trip : tripList) {
            values = new ContentValues();
            values.put(DataBaseHandler.KEY_TRIP_ID, trip.getId());
            values.put(DataBaseHandler.KEY_TRIP_NAME, trip.getName());     // NOM
            values.put(DataBaseHandler.KEY_TRIP_COUNTRY, trip.getCountry());   // PRÉNOM
            values.put(DataBaseHandler.KEY_TRIP_DESCRIPTION, trip.getDescription());     // DATE DE NAISSANCE
            values.put(DataBaseHandler.KEY_TRIP_CREATED_AT, String.valueOf(trip.getDate()));
            values.put(DataBaseHandler.KEY_TRIP_COMMENT_COUNT, String.valueOf(trip.getCommentCount()));
            values.put(DataBaseHandler.KEY_TRIP_PHOTO_COUNT, String.valueOf(trip.getPhotoCount()));
            values.put(DataBaseHandler.KEY_TRIP_COVER, trip.getCover());
            // Insérer la ligne
            database.insert(TABLE_TRIP, null, values);
        }
    }

    public Trip getTrip(int tripId){
        Trip trip = null;
        Cursor cursor = database.query(TABLE_TRIP, allColumns, DataBaseHandler.KEY_TRIP_ID+ " = " + tripId, null, null, null, null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            trip = cursorToTrip(cursor);
        }
        cursor.close();
        // database.close()
        if(trip != null) {
            Log.i("Verification nom ", "" + trip.getName());
            Log.i("Verification pays ", "" + trip.getCountry());
        }
        return trip;
    }

    /**
     * Récupere les informations du curseur pour les mettre dans la classe Trip
     * @param cursor un curseur
     * @return un voyage (Trip)
     */
    Trip cursorToTrip(Cursor cursor){
        Trip trip = new Trip();
        trip.setId(cursor.getInt(DataBaseHandler.POSITION_TRIP_ID));
        trip.setName(cursor.getString(DataBaseHandler.POSITION_TRIP_NAME));
        trip.setCountry(cursor.getString(DataBaseHandler.POSITION_TRIP_COUNTRY));
        trip.setDescription(cursor.getString(DataBaseHandler.POSITION_TRIP_DESCRIPTION));
        trip.setInsert(cursor.getString(DataBaseHandler.POSITION_TRIP_CREATED_AT));
        trip.setCover(cursor.getString(DataBaseHandler.POSITION_TRIP_COVER));
        trip.setCommentCount(cursor.getInt(DataBaseHandler.POSITION_TRIP_COMMENT_COUNT));
        trip.setPhotoCount(cursor.getInt(DataBaseHandler.POSITION_TRIP_PHOTO_COUNT));
        return trip;
    }

    /**
     * Fonction qui supprime un voyage
     */
    public boolean removeTrip(int tripId){
        return database.delete(TABLE_TRIP, DataBaseHandler.KEY_TRIP_ID + " = " + tripId, null) > 0;
    }

    /**
     * Fonction qui met à jour la description du voyage
     */
    public void updateTripDescription(int tripId, String trip_description){
        ContentValues values = new ContentValues();
        values.put(KEY_TRIP_DESCRIPTION, trip_description);
        database.update(TABLE_TRIP, values, KEY_TRIP_ID + " = " + tripId, null);
    }

    /**
     * Fonction qui met à jour l'image couverture du voyage
     */
    public void updateCover(int tripId, String photo){
        ContentValues values = new ContentValues();
        values.put(KEY_TRIP_COVER, photo);
        database.update(TABLE_TRIP, values, KEY_TRIP_ID + " = " + tripId, null);
    }

}
