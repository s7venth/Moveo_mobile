package fr.moveoteam.moveomobile.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.adapter.MessageListAdapter;
import fr.moveoteam.moveomobile.dao.DialogDAO;
import fr.moveoteam.moveomobile.model.Dialog;

/**
 * Created by Sylvain on 31/05/15.
 */
public class InboxListFragment extends ListFragment {

    ArrayList<Dialog> dialogArrayList;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DialogDAO dialogDAO = new DialogDAO(getActivity());
        dialogDAO.open();
        dialogArrayList = dialogDAO.getInboxList();
		dialogDAO.close();

        Log.e("Inbox"," ok ");

        if(dialogArrayList != null) {
            setListAdapter(new MessageListAdapter(getActivity(), dialogArrayList));
            Log.e("Inbox"," ok2 ");
        } else setListAdapter(null);
    }


}
