package fr.moveoteam.moveomobile.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.moveoteam.moveomobile.R;

/**
 * Created by Sylvain on 23/05/15.
 */
public class CommentCategoryFragment extends Fragment {

    private View view;
    private CommentListFragment commentListFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }

        try{
            view = inflater.inflate(R.layout.comment_category,container,false);
            commentListFragment = (CommentListFragment) getFragmentManager().findFragmentById(R.id.CommentListFragment);
        }catch (InflateException e){
            e.getMessage();
        }

        return view;
    }


}
