package com.bodisoftware.fitnesslog.ui.tabs;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bodisoftware.fitnesslog.Constants;
import com.bodisoftware.fitnesslog.MainActivity;
import com.bodisoftware.fitnesslog.R;
import com.bodisoftware.fitnesslog.database.FitnessDBHelper;
import com.bodisoftware.fitnesslog.database.dao.RoutineDataSource;
import com.bodisoftware.fitnesslog.database.dao.SessionDataSource;
import com.bodisoftware.fitnesslog.database.dao.WorkoutDataSource;
import com.bodisoftware.fitnesslog.ui.tabs.routine.NewWorkoutDialogFragment;
import com.bodisoftware.fitnesslog.ui.tabs.session.NewSessionActivity;
import com.bodisoftware.fitnesslog.ui.tabs.session.NewSessionDialogFragment;
import com.bodisoftware.fitnesslog.ui.tabs.session.SessionData;
import com.bodisoftware.fitnesslog.ui.tabs.session.SessionHistoryAdapter;
import com.getbase.floatingactionbutton.AddFloatingActionButton;

import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

public class TabFragmentSessions extends Fragment implements NewSessionDialogFragment.NoticeDialogListener {

    private static final int NEW_SESSION_RESULT_CODE = 1;
   // private ListView mListView = null; // @todo: temp, remove later
    private RecyclerView mRecyclerViewSessionsHistory = null;
    private AddFloatingActionButton mBtnAddSession = null;
   // private SessionDataSource mSessionDAO = null;
  //  private Cursor mSessionCursor = null;
   // private SimpleCursorAdapter mCursorAdapter = null; // @todo: temp, remove later
    private SessionHistoryAdapter mSessionHistoryAdapter = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_sessions, container, false);

        //mListView = (ListView) view.findViewById(R.id.listview_with_fab);
        mBtnAddSession = (AddFloatingActionButton) view.findViewById(R.id.btnAddSession);
      // final TextView txtListEmpty = (TextView) view.findViewById(R.id.empty);

      //  mSessionCursor = mSessionDAO.getAllSessionsCursor();


//        mCursorAdapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_1, mSessionCursor, new String[] {FitnessDBHelper.COL_DATE}, new int[] {android.R.id.text1}, 0);
//        mListView.setEmptyView(txtListEmpty);
//        mListView.setAdapter(mCursorAdapter);

        mRecyclerViewSessionsHistory = (RecyclerView) view.findViewById(R.id.recyclerViewSessionHistory);
        mSessionHistoryAdapter = new SessionHistoryAdapter(getContext());
        mRecyclerViewSessionsHistory.setAdapter(mSessionHistoryAdapter);
        mRecyclerViewSessionsHistory.setHasFixedSize(true);
        mRecyclerViewSessionsHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewSessionsHistory.setItemAnimator(new SlideInDownAnimator());


        addListeners();
        return view;
    }

    private void addListeners() {
        mBtnAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new NewSessionDialogFragment();
                ((NewSessionDialogFragment) dialog).setListener(TabFragmentSessions.this);
                dialog.show(getActivity().getSupportFragmentManager(), "NewSessionDialogFragment");
            }
        });
    }


    private void startNewSessionActivity(final SessionData sessionData) {
        Intent intent = new Intent(TabFragmentSessions.this.getContext(), NewSessionActivity.class);
        final int tabIndex = ((MainActivity) getActivity()).getCurrentTabIndex();
        intent.putExtra(Constants.CURRENT_PAGE, tabIndex);
        intent.putExtra(Constants.DATA, sessionData);
        startActivityForResult(intent, NEW_SESSION_RESULT_CODE);
    }

    @Override
    public void onStop() {
        super.onStop();
       // mSessionCursor = null;
    }

    @Override
    public void onDialogPositiveClick(SessionData sessionData) {
        startNewSessionActivity(sessionData);
    }

    @Override
    public void onDialogNegativeClick() {
        final String strMsg = getActivity().getResources().getString(R.string.session_canceled);
        Snackbar.make(getView(), strMsg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_SESSION_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra("result", false) == Boolean.TRUE.booleanValue()) {
                    mSessionHistoryAdapter.refreshSessionsList();
                    mSessionHistoryAdapter.notifyItemInserted(0);
                    mRecyclerViewSessionsHistory.smoothScrollToPosition(0);
                }
            }
        }

    }
}


