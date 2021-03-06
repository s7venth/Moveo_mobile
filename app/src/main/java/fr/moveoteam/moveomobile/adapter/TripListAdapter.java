package fr.moveoteam.moveomobile.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by alexMac on 08/04/15.
 */
public class TripListAdapter extends BaseAdapter {

    private ArrayList<Trip> tripList;
    private LayoutInflater layoutInflater;
    private ViewHolderTrip viewHolderTrip;
    private boolean otherUser;
    private Context context;
    StringBuilder st;

    public TripListAdapter(Context context, ArrayList<Trip> tripList, boolean otherUser){
        this.context = context;
        this.tripList = tripList;
        this.otherUser = otherUser;
    }

    public void updateResult(ArrayList<Trip> tripList){
        this.tripList = tripList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tripList.size();
    }

    @Override
    public Object getItem(int position) {
        return tripList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(tripList != null) {
            if (convertView == null) {
                this.layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.row_list_trip, null);
                viewHolderTrip = new ViewHolderTrip();
                viewHolderTrip.explore_trip_name = (TextView) convertView.findViewById(R.id.explore_trip_name);
                viewHolderTrip.explore_country = (TextView) convertView.findViewById(R.id.explore_country);
                viewHolderTrip.explore_username = (TextView) convertView.findViewById(R.id.explore_username);
                viewHolderTrip.imageViewMainPictureTrip = (ImageView) convertView.findViewById(R.id.imageViewMainPictureTrip);
                viewHolderTrip.imageButtonComments = (ImageView) convertView.findViewById(R.id.image_button_comments);
                viewHolderTrip.imageButtonPictures = (ImageView) convertView.findViewById(R.id.image_button_pictures);
                viewHolderTrip.number_of_comments = (TextView) convertView.findViewById(R.id.number_of_comments);
                viewHolderTrip.number_of_pictures = (TextView) convertView.findViewById(R.id.number_of_picture);
                convertView.setTag(viewHolderTrip);
            } else {
                viewHolderTrip = (ViewHolderTrip) convertView.getTag();
            }
            viewHolderTrip.explore_trip_name.setText(tripList.get(position).getName());
            viewHolderTrip.explore_country.setText(tripList.get(position).getCountry());
            Log.e("ExploreList", " " + position);
            Log.e("Cover", " " + tripList.get(position).getCover());
            if (this.otherUser) {
                String authorHTML = "<font color=#000>par</font> " + tripList.get(position).getAuthor_first_name() + " " + tripList.get(position).getAuthor_last_name();
                viewHolderTrip.explore_username.setText(Html.fromHtml(authorHTML));

                if(!tripList.get(position).getCover().equals("null") && !tripList.get(position).getCover().isEmpty()) {
                    viewHolderTrip.imageUrl = tripList.get(position).getCover();
                    new DownloadImage().execute(viewHolderTrip);

                }else{
                    viewHolderTrip.imageViewMainPictureTrip.setImageDrawable(context.getResources().getDrawable(R.drawable.default_journey));
                }

            } else {
                viewHolderTrip.explore_username.setVisibility(View.GONE);
                if(!tripList.get(position).getCover().equals("null") && !tripList.get(position).getCover().isEmpty()) {
                    //viewHolderTrip.imageViewMainPictureTrip.setImageBitmap(Function.decodeBase64(tripList.get(position).getCover()));
                    viewHolderTrip.imageUrl = tripList.get(position).getCover();
                    new DownloadImage().execute(viewHolderTrip);
                }else{
                    viewHolderTrip.imageViewMainPictureTrip.setImageDrawable(context.getResources().getDrawable(R.drawable.default_journey));
                    viewHolderTrip.imageViewMainPictureTrip.setAlpha((float) 0.8);
                }
            }
            Log.e("PlaceList", " " + position);
            viewHolderTrip.number_of_comments.setText((Integer.toString(tripList.get(position).getCommentCount())));
            viewHolderTrip.number_of_pictures.setText(Integer.toString(tripList.get(position).getPhotoCount()));
        }
        return convertView;
    }

    static class ViewHolderTrip {
        TextView explore_trip_name, explore_country, explore_username,number_of_comments,number_of_pictures;
        ImageView imageViewMainPictureTrip;
        ImageView imageButtonComments, imageButtonPictures;
        Bitmap bitmap;
        String imageUrl;
    }

    private class DownloadImage extends AsyncTask<ViewHolderTrip, Void, ViewHolderTrip> {

        String url;
        URL urlImage;
        HttpURLConnection connection = null;
        InputStream inputStream = null;


        @Override
        protected ViewHolderTrip doInBackground(ViewHolderTrip... args) {
            ViewHolderTrip viewHolderTripImage = args[0];
            try {
                urlImage = new URL("http://moveo.besaba.com/"+viewHolderTripImage.imageUrl);
                connection = (HttpURLConnection) urlImage.openConnection();
                if (connection != null) {
                    inputStream = connection.getInputStream();
                    viewHolderTripImage.bitmap = BitmapFactory.decodeStream(inputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return viewHolderTripImage;
        }

        @Override
        protected void onPostExecute(ViewHolderTrip result) {
            if (result.bitmap == null) {
                result.imageViewMainPictureTrip.setImageResource(R.drawable.default_journey);
            } else {
                result.imageViewMainPictureTrip.setImageBitmap(result.bitmap);
            }
        }
    }

}
