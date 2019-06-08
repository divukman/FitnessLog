package com.bodisoftware.fitnesslog.ui.tabs;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bodisoftware.fitnesslog.Constants;
import com.bodisoftware.fitnesslog.MainActivity;
import com.bodisoftware.fitnesslog.R;
import com.bodisoftware.fitnesslog.database.FitnessDBHelper;
import com.bodisoftware.fitnesslog.database.dao.RoutineDataSource;
import com.bodisoftware.fitnesslog.database.dto.Routine;
import com.bodisoftware.fitnesslog.ui.tabs.routine.DisplayWorkoutsActivity;
import com.getbase.floatingactionbutton.AddFloatingActionButton;

public class TabFragmentRoutines extends Fragment {

    private ListView listView;
    private AddFloatingActionButton mBtnAddCategory = null;
    SimpleCursorAdapter mLstadapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_routines, container, false);

        listView = (ListView) view.findViewById(R.id.listview_with_fab);

        Cursor cursor = getRoutinesCursor();
        mLstadapter = new SimpleCursorAdapter(getContext(), R.layout.list_item_fitnesslog, cursor, new String[] {FitnessDBHelper.COL_NAME}, new int[] {android.R.id.text1}, 0);

        listView.setAdapter(mLstadapter);
        mBtnAddCategory = (AddFloatingActionButton) view.findViewById(R.id.btnAddRoutine);

        addListeners();

        return view;
    }


    private void addListeners() {
        mBtnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title("Routine name")
                        .content("Please enter routine name")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("Name", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                final String routineName = input.toString().trim();
                                if (routineName != null && routineName.toString().trim().length() > 0) {
                                    final Routine routine = addRoutine(routineName);
                                    final Cursor cursor = getRoutinesCursor();
                                    mLstadapter.swapCursor(cursor);
                                    Toast.makeText(TabFragmentRoutines.this.getContext(), "Routine: " + routine.toString() + " added.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TabFragmentRoutines.this.getContext(), "Routine name is empty!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String routineName = ((TextView)view).getText().toString().trim();
                Toast.makeText(TabFragmentRoutines.this.getContext(), "Item clicked: " + routineName, Toast.LENGTH_SHORT).show();
                startDisplayRoutineActivity(routineName);
            }
        });
    }

    private Routine addRoutine(final String name) {
        Routine result = null;

        RoutineDataSource routineDAO = new RoutineDataSource(TabFragmentRoutines.this.getContext());
        routineDAO.open();
        result =  routineDAO.createRoutine(name);
        routineDAO.close();

        return result;
    }

    private Cursor getRoutinesCursor() {
        RoutineDataSource routineDAO = new RoutineDataSource(getContext());
        //@todo: leave dao open until activity is closed?
        routineDAO.open();
        Cursor cursor = routineDAO.getAllRoutinesCursor();
        routineDAO.close();

        return cursor;
    }

    private void startDisplayRoutineActivity(final String routineName) {
        Intent intent = new Intent(TabFragmentRoutines.this.getContext(), DisplayWorkoutsActivity.class);
        final int tabIndex = ((MainActivity) getActivity()).getCurrentTabIndex();
        intent.putExtra(Constants.CURRENT_PAGE, tabIndex);
        intent.putExtra(Constants.ROUTINE_NAME, routineName);
        startActivity(intent);
    }

}