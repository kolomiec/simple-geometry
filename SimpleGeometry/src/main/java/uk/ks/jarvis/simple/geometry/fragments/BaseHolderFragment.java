package uk.ks.jarvis.simple.geometry.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import uk.ks.jarvis.simple.geometry.R;
import uk.ks.jarvis.simple.geometry.activities.PreferenceActivity;
import uk.ks.jarvis.simple.geometry.holders.BaseHolder;


/**
 * Created by sayenko on 7/14/13.
 */
public class BaseHolderFragment extends Fragment {

    private View view, baseHolder;
    private Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.base_holder, container, false);
        assert view != null;
        context = view.getContext();
        baseHolder = (View) view.findViewById(R.id.baseHolder);
        baseHolder = new BaseHolder(context, getActivity());
        return baseHolder;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_figure_item:
//                CreateFigureDialog createFigureDialog = new CreateFigureDialog((BaseHolder) baseHolder);
//                createFigureDialog.show(this.getActivity().getSupportFragmentManager(), "");
                return true;
            case R.id.menu_preferences_item:
                startActivity(new Intent(getActivity(), PreferenceActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}