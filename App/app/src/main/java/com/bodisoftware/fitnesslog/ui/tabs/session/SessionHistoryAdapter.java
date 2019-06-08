package com.bodisoftware.fitnesslog.ui.tabs.session;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bodisoftware.fitnesslog.R;
import com.bodisoftware.fitnesslog.database.dao.ExerciseDataSource;
import com.bodisoftware.fitnesslog.database.dao.ExerciseHistoryDataSource;
import com.bodisoftware.fitnesslog.database.dao.RoutineDataSource;
import com.bodisoftware.fitnesslog.database.dao.SessionDataSource;
import com.bodisoftware.fitnesslog.database.dao.WorkoutDataSource;
import com.bodisoftware.fitnesslog.database.dto.Exercise;
import com.bodisoftware.fitnesslog.database.dto.ExerciseHistory;
import com.bodisoftware.fitnesslog.database.dto.Routine;
import com.bodisoftware.fitnesslog.database.dto.Session;
import com.bodisoftware.fitnesslog.database.dto.Workout;
import com.bodisoftware.fitnesslog.util.DateUtil;
import com.bodisoftware.fitnesslog.widgets.DisplayUtils;
import com.bodisoftware.fitnesslog.widgets.session.HistoryWidget;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dvukman on 9/22/2017.
 */

public class SessionHistoryAdapter extends
        RecyclerView.Adapter<SessionHistoryAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtHeader = null;
        public TextView txtDate = null;
        public LinearLayout tblHistory = null;

        public ViewHolder(View itemView) {
            super(itemView);

            txtHeader = (TextView) itemView.findViewById(R.id.txtHeader);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            tblHistory = (LinearLayout) itemView.findViewById(R.id.tblHistory);
        }
    }

    private Context mContext = null;
    private SessionDataSource mSessionDAO = null;
    private WorkoutDataSource mWorkoutDAO = null;
    private RoutineDataSource mRoutineDAO = null;
    private ExerciseHistoryDataSource mExerciseHistoryDAO = null;
    private ExerciseDataSource mExerciseDAO = null;
    private List<Session> mLstSessions = null;
    private LayoutInflater mLayoutInflater = null;


    public SessionHistoryAdapter(final Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);

        mSessionDAO = new SessionDataSource(context);
        mSessionDAO.open();

        mWorkoutDAO = new WorkoutDataSource(context);
        mWorkoutDAO.open();

        mRoutineDAO = new RoutineDataSource(context);
        mRoutineDAO.open();

        mExerciseHistoryDAO = new ExerciseHistoryDataSource(context);
        mExerciseHistoryDAO.open();

        mExerciseDAO = new ExerciseDataSource(context);
        mExerciseDAO.open();

        mLstSessions = mSessionDAO.getAllSessions();
    }

    public void refreshSessionsList() {
        mSessionDAO.open();
        mLstSessions = mSessionDAO.getAllSessions();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(mContext);

        final View sessionHistoryView = inflater.inflate(R.layout.card_session_history, parent, false);
        final ViewHolder viewHolder = new ViewHolder(sessionHistoryView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        final Session session = mLstSessions.get(position);
        final long date = session.getDate();
        final String notes = session.getNotes();// @todo: not used currently

        final long sessionID = session.getId();
        final long workoutID = session.getWorkout_id();

        mWorkoutDAO.open(); // @todo: saznati zasto je ovo potrebno! tko closa konekciju na bazu!?!
        mRoutineDAO.open();

        final Workout workout = mWorkoutDAO.getWorkout(workoutID);
        final String workoutName = workout.getName();
        final long routineID = workout.getRoutineId();
        final Routine routine = mRoutineDAO.getRoutine(routineID);
        final String routineName = routine.getName();

        holder.txtHeader.setText(routineName + ", " + workoutName);
        holder.txtDate.setText(DateUtil.getDateStr(new Date(date), DateUtil.PARSE_FORMAT_DAY_AND_DATE));

        mExerciseHistoryDAO.open();
        final List<ExerciseHistory> lstExerciseHistory = mExerciseHistoryDAO.getExerciseHistory(sessionID);
        final ArrayList<ExerciseHistory> lstTemp = new ArrayList<ExerciseHistory>();

        holder.tblHistory.removeAllViews();

        boolean gray = false;
        if (lstExerciseHistory.size() > 0) {
            Iterator<ExerciseHistory> iterator = lstExerciseHistory.iterator();

            while (iterator.hasNext()) {
                final ExerciseHistory nextEntry = iterator.next();

                if (lstTemp.size() == 0) {
                    lstTemp.add(nextEntry);
                } else {
                    final ExerciseHistory firstEntry = lstTemp.get(0);
                    if (nextEntry.getExerciseId() == firstEntry.getExerciseId()) {
                        lstTemp.add(nextEntry);
                    } else {
                        addExerciseHistoryCardRow(holder.tblHistory, lstTemp, gray);
                        gray = !gray;
                        lstTemp.clear();
                        lstTemp.add(nextEntry);
                    }
                }

            }

            if (lstTemp.size() > 0) {
                addExerciseHistoryCardRow(holder.tblHistory, lstTemp, gray);
                gray = !gray;
                lstTemp.clear();
            }
        }


    }

    private void addExerciseHistoryCardRow (final LinearLayout table, final ArrayList<ExerciseHistory> lstTemp, final boolean gray) {
//        LinearLayout linearLayoutRow = new LinearLayout(mContext);
//        linearLayoutRow.setLayoutParams(new LinearLayout.LayoutParams(
//                TableLayout.LayoutParams.MATCH_PARENT,
//                TableLayout.LayoutParams.WRAP_CONTENT));
//        linearLayoutRow.setOrientation(LinearLayout.HORIZONTAL);

        mExerciseDAO.open();
        ExerciseHistory firstExerciseHistoryEntry = lstTemp.get(0);
        Exercise exercise = mExerciseDAO.getExercise(firstExerciseHistoryEntry.getExerciseId());
//
//        final TextView txtName = (TextView) new TextView(mContext);
//        txtName.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//        txtName.setText(exercise.getName());
//        linearLayoutRow.addView(txtName);

//        final Iterator<ExerciseHistory> iterator = lstTemp.iterator();
//        while (iterator.hasNext()) {
//            final ExerciseHistory set = iterator.next();
//
//            final TextView txtView = (TextView) new TextView(mContext);
//            txtView.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT));
//            final float factor = DisplayUtils.getDIPFactor(mContext);
//            final int paddingLeft = (int) (10 * factor);
//            txtView.setPadding(paddingLeft,0,0,0);
//
//            final String str =  set.getReps() + (set.getWeight() > 0 ? "x" + set.getWeight() : "");
//            txtView.setText(str);
//            linearLayoutRow.addView(txtView);
//        }

        final HistoryWidget historyWidget = new HistoryWidget(mContext, exercise.getName(), lstTemp);
        if (gray) {
            historyWidget.setBackgroundResource(R.color.grey);
        }
        table.addView(historyWidget);
    }

    @Override
    public int getItemCount() {
        return mLstSessions.size();
    }


}
