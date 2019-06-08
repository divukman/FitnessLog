package com.bodisoftware.fitnesslog.database;

import android.content.Context;
import android.database.Cursor;

import com.bodisoftware.fitnesslog.database.dao.CategoryDataSource;
import com.bodisoftware.fitnesslog.database.dao.ExerciseDataSource;
import com.bodisoftware.fitnesslog.database.dao.RoutineDataSource;
import com.bodisoftware.fitnesslog.database.dao.WorkoutDataSource;
import com.bodisoftware.fitnesslog.database.dao.WorkoutExerciseDataSource;
import com.bodisoftware.fitnesslog.database.dto.Category;
import com.bodisoftware.fitnesslog.database.dto.Exercise;
import com.bodisoftware.fitnesslog.database.dto.Routine;
import com.bodisoftware.fitnesslog.database.dto.Workout;

/**
 * Created by dvukman on 12/5/2016.
 */

public class Bootstrap {

    public static boolean isFirstTimeRunning(final Context context) {
        CategoryDataSource categoryDAO = new CategoryDataSource(context);
        categoryDAO.open();
        Cursor cursor = categoryDAO.getAllCategoriesCursor();

        return cursor != null && cursor.getCount() == 0;
    }

    public static void bootstrapDatabase(final Context context) {
        // Data from: http://www.exrx.net/Lists/Directory.html

        //Add categories
        CategoryDataSource categoryDAO = new CategoryDataSource(context);
        categoryDAO.open();
        final Category categoryNeck = categoryDAO.createCategory("Neck");
        final Category categoryShoulders = categoryDAO.createCategory("Shoulders");
        final Category categoryUpperArms =categoryDAO.createCategory("Upper Arms");
        final Category categoryForearms =categoryDAO.createCategory("Forearms");
        final Category categoryBack =categoryDAO.createCategory("Back");
        final Category categoryChest =categoryDAO.createCategory("Chest");
        final Category categoryWaist =categoryDAO.createCategory("Waist");
        final Category categoryHipsAndThighs =categoryDAO.createCategory("Hips and Thighs");
        final Category categoryCalves =categoryDAO.createCategory("Calves");
        categoryDAO.close();

        //Add exercises
        ExerciseDataSource exerciseDAO = new ExerciseDataSource(context);
        exerciseDAO.open();

        exerciseDAO.createExercise(categoryNeck.getId(), "Neck Flexion");
        exerciseDAO.createExercise(categoryNeck.getId(), "Lying Neck Flexion");
        exerciseDAO.createExercise(categoryNeck.getId(), "Lateral Neck Flexion");

        exerciseDAO.createExercise(categoryShoulders.getId(), "Barbell Behind Neck Press");
        final Exercise exerciseBarbellMilitaryPress = exerciseDAO.createExercise(categoryShoulders.getId(), "Barbell Military Press");
        exerciseDAO.createExercise(categoryShoulders.getId(), "Barbell Shoulder Press");
        exerciseDAO.createExercise(categoryShoulders.getId(), "Dumbbell Arnold Press");
        exerciseDAO.createExercise(categoryShoulders.getId(), "Dumbbell Front Raise");
        exerciseDAO.createExercise(categoryShoulders.getId(), "Dumbbell Shoulder Press");
        exerciseDAO.createExercise(categoryShoulders.getId(), "Barbell Upright Row");
        exerciseDAO.createExercise(categoryShoulders.getId(), "Dumbbell Lateral Raise");
        exerciseDAO.createExercise(categoryShoulders.getId(), "Dumbbell Upright Row");
        exerciseDAO.createExercise(categoryShoulders.getId(), "Handstand Push-Up");
        final Exercise exercisePikePushUp = exerciseDAO.createExercise(categoryShoulders.getId(), "Pike Push-Up");

        final Exercise exerciseBarbellCurl =  exerciseDAO.createExercise(categoryUpperArms.getId(), "Barbell Curl");
        exerciseDAO.createExercise(categoryUpperArms.getId(), "Dumbbell Curl");
        final Exercise exerciseDips = exerciseDAO.createExercise(categoryUpperArms.getId(), "Triceps Dips");
        final Exercise exerciseBenchDips = exerciseDAO.createExercise(categoryUpperArms.getId(), "Bench Dips");
        final Exercise exerciseStraightBarDips = exerciseDAO.createExercise(categoryUpperArms.getId(), "Straight Bar Dips");
        exerciseDAO.createExercise(categoryUpperArms.getId(), "Dumbbell Kickback");
        final Exercise exerciseBarbellLyingTricepsExtension = exerciseDAO.createExercise(categoryUpperArms.getId(), "Barbell Lying Triceps Extension");

        exerciseDAO.createExercise(categoryForearms.getId(), "Barbell Reverse Curl");
        exerciseDAO.createExercise(categoryForearms.getId(), "Dumbbell Hammer Curl");
        exerciseDAO.createExercise(categoryForearms.getId(), "Barbell Wrist Curl");
        exerciseDAO.createExercise(categoryForearms.getId(), "Dumbbell Wrist Curl");
        exerciseDAO.createExercise(categoryForearms.getId(), "Barbell Reverse Wrist Curl");
        exerciseDAO.createExercise(categoryForearms.getId(), "Dumbbell Reverse Wrist Curl");

        final Exercise exerciseBarbellBentOverRow =  exerciseDAO.createExercise(categoryBack.getId(), "Barbell Bent-over Row");
        exerciseDAO.createExercise(categoryBack.getId(), "Dumbbell Bent-over Row");
        final Exercise exerciseInvertedRow =  exerciseDAO.createExercise(categoryBack.getId(), "Inverted Row");
        final Exercise exerciseStaticChinUp = exerciseDAO.createExercise(categoryBack.getId(), "Static Chin-up");
        final Exercise exerciseChinUp = exerciseDAO.createExercise(categoryBack.getId(), "Chin-up");
        final Exercise exercisePullup = exerciseDAO.createExercise(categoryBack.getId(), "Pull-up");
        exerciseDAO.createExercise(categoryBack.getId(), "Barbell Shrug");

        exerciseDAO.createExercise(categoryChest.getId(), "Barbell Bench Press");
        final Exercise exerciseBarbellBenchPressMedium = exerciseDAO.createExercise(categoryChest.getId(), "Barbell Bench Press Medium Grip");
        exerciseDAO.createExercise(categoryChest.getId(), "Dumbbell Bench Press");
        exerciseDAO.createExercise(categoryChest.getId(), "Dumbbell Fly");
        exerciseDAO.createExercise(categoryChest.getId(), "Chest Dip");
        final Exercise exercisePushUp = exerciseDAO.createExercise(categoryChest.getId(), "Push-Up");
        final Exercise exerciseElevatedPushUp = exerciseDAO.createExercise(categoryChest.getId(), "Elevated Push-Up");

        final Exercise exerciseCrunch = exerciseDAO.createExercise(categoryWaist.getId(), "Crunch");
        exerciseDAO.createExercise(categoryWaist.getId(), "Hanging Leg Hip Raise");
        exerciseDAO.createExercise(categoryWaist.getId(), "Incline Leg Hip Raise");
        exerciseDAO.createExercise(categoryWaist.getId(), "Lying Leg Hip Raise");
        final Exercise exerciseLyingLegRise = exerciseDAO.createExercise(categoryWaist.getId(), "Lying Leg Raise");
        final Exercise exerciseSitUp =  exerciseDAO.createExercise(categoryWaist.getId(), "Sit-up");
        final Exercise exercisePlank =  exerciseDAO.createExercise(categoryWaist.getId(), "Front Plank");
        exerciseDAO.createExercise(categoryWaist.getId(), "Lying Twist");
        exerciseDAO.createExercise(categoryWaist.getId(), "Side Plank");
        final Exercise exerciseMountainClimbers =  exerciseDAO.createExercise(categoryWaist.getId(), "Mountain Climbers");

        final Exercise exerciseBarbellDeadlift = exerciseDAO.createExercise(categoryHipsAndThighs.getId(), "Barbell Deadlifts");
        exerciseDAO.createExercise(categoryHipsAndThighs.getId(), "Barbell Lunge");
        exerciseDAO.createExercise(categoryHipsAndThighs.getId(), "Barbell Squat");
        final Exercise exerciseBarbellFrontSquat = exerciseDAO.createExercise(categoryHipsAndThighs.getId(), "Front Barbell Squat");
        final Exercise exerciseBarbellFullSquat = exerciseDAO.createExercise(categoryHipsAndThighs.getId(), "Barbell Full Squat");
        exerciseDAO.createExercise(categoryHipsAndThighs.getId(), "Dumbbell Lunge");
        exerciseDAO.createExercise(categoryHipsAndThighs.getId(), "Dumbbell Squat");
        final Exercise exerciseLungeBodyweight = exerciseDAO.createExercise(categoryHipsAndThighs.getId(), "Body Weight Lunge");
        exerciseDAO.createExercise(categoryHipsAndThighs.getId(), "Body Weight Single Leg Squat");
        final Exercise exerciseBodyweightSquat= exerciseDAO.createExercise(categoryHipsAndThighs.getId(), "Body Weight Squat");

        exerciseDAO.createExercise(categoryCalves.getId(), "Barbell Standing Calf Raise");
        exerciseDAO.createExercise(categoryCalves.getId(), "Dumbbell Standing Calf Raise");
        exerciseDAO.createExercise(categoryCalves.getId(), "Body Weight Standing Calf Raise");

        exerciseDAO.close();


        //Add few sample routines
        RoutineDataSource routineDAO = new RoutineDataSource(context);
        routineDAO.open();
        final Routine routine5x5 = routineDAO.createRoutine("5x5");
        final Routine routineCalisthenicsAbsoluteBeginner = routineDAO.createRoutine("Calisthenics - Absolute Beginner");
        final Routine routineCalisthenicsBeginner = routineDAO.createRoutine("Calisthenics - Beginner");
//        routineDAO.createRoutine("German Volume Training");
//        routineDAO.createRoutine("Calisthenics Workout #1");
        routineDAO.close();

        // Add workouts to routines
        WorkoutDataSource workoutDAO = new WorkoutDataSource(context);
        workoutDAO.open();

        // 5x5 workouts
        final Workout workoutDayA = workoutDAO.createWorkout(routine5x5.getId(), "Workout A");
        final Workout workoutDayB = workoutDAO.createWorkout(routine5x5.getId(), "Workout B");
        final Workout workoutDayC = workoutDAO.createWorkout(routine5x5.getId(), "Workout C");

        // Calisthenics - Absolute Beginner Workouts
        final Workout workoutCalisthenicsAbsoluteBeginner = workoutDAO.createWorkout(routineCalisthenicsAbsoluteBeginner.getId(), "Workout Day");

        // Calisthenics - Beginner Workouts
        final Workout workoutCalisthenicsBeginnerDayA = workoutDAO.createWorkout(routineCalisthenicsBeginner.getId(), "Day A");
        final Workout workoutCalisthenicsBeginnerDayB = workoutDAO.createWorkout(routineCalisthenicsBeginner.getId(), "Day B");

        workoutDAO.close();

        WorkoutExerciseDataSource workoutExerciseDAO = new WorkoutExerciseDataSource(context);
        workoutExerciseDAO.open();

        // 5x5, Day A
        workoutExerciseDAO.createWorkoutExercise(workoutDayA.getId(), exerciseBarbellFullSquat.getId(), 5, 5, 90);
        workoutExerciseDAO.createWorkoutExercise(workoutDayA.getId(), exerciseBarbellBenchPressMedium.getId(), 5, 5, 90);
        workoutExerciseDAO.createWorkoutExercise(workoutDayA.getId(), exerciseBarbellBentOverRow.getId(), 5, 5, 90);
        workoutExerciseDAO.createWorkoutExercise(workoutDayA.getId(), exerciseSitUp.getId(), 5, 5, 90);
        workoutExerciseDAO.createWorkoutExercise(workoutDayA.getId(), exerciseBarbellLyingTricepsExtension.getId(), 5, 5, 90);

        // 5x5, Day B
        workoutExerciseDAO.createWorkoutExercise(workoutDayB.getId(), exerciseBarbellFrontSquat.getId(), 5, 5, 90);
        workoutExerciseDAO.createWorkoutExercise(workoutDayB.getId(), exerciseBarbellMilitaryPress.getId(), 5, 5, 90);
        workoutExerciseDAO.createWorkoutExercise(workoutDayB.getId(), exerciseBarbellDeadlift.getId(), 5, 5, 90);
        workoutExerciseDAO.createWorkoutExercise(workoutDayB.getId(), exercisePullup.getId(), 5, 5, 90);
        workoutExerciseDAO.createWorkoutExercise(workoutDayB.getId(), exerciseBarbellCurl.getId(), 5, 5, 90);
        workoutExerciseDAO.createWorkoutExercise(workoutDayB.getId(), exerciseCrunch.getId(), 5, 5, 90);

        // 5x5, Day C
        workoutExerciseDAO.createWorkoutExercise(workoutDayC.getId(), exerciseBarbellFullSquat.getId(), 5, 5, 90);
        workoutExerciseDAO.createWorkoutExercise(workoutDayC.getId(), exerciseBarbellBenchPressMedium.getId(), 5, 5, 90);
        workoutExerciseDAO.createWorkoutExercise(workoutDayC.getId(), exerciseBarbellBentOverRow.getId(), 5, 5, 90);
        workoutExerciseDAO.createWorkoutExercise(workoutDayC.getId(), exerciseDips.getId(), 5, 5, 90);
        workoutExerciseDAO.createWorkoutExercise(workoutDayC.getId(), exerciseCrunch.getId(), 5, 5, 90);

        // Calisthenics - Absolute Beginner, Workout Day
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsAbsoluteBeginner.getId(), exerciseStaticChinUp.getId(), 3, 8, 60);
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsAbsoluteBeginner.getId(), exerciseElevatedPushUp.getId(), 3, 10, 60);
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsAbsoluteBeginner.getId(), exerciseLyingLegRise.getId(), 3, 10, 60);
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsAbsoluteBeginner.getId(), exerciseBodyweightSquat.getId(), 3, 10, 60);
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsAbsoluteBeginner.getId(), exerciseInvertedRow.getId(), 3, 10, 60);

        // Calisthenics - Beginner, Day A
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsBeginnerDayA.getId(), exercisePushUp.getId(), 3, 10, 60);
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsBeginnerDayA.getId(), exerciseChinUp.getId(), 3, 5, 60);
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsBeginnerDayA.getId(), exerciseDips.getId(), 3, 5, 60);
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsBeginnerDayA.getId(), exerciseStraightBarDips.getId(), 3, 5, 60);
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsBeginnerDayA.getId(), exercisePullup.getId(), 3, 5, 60);

        // Calisthenics - Beginner, Day B
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsBeginnerDayB.getId(), exercisePlank.getId(), 3, 1, 60);
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsBeginnerDayB.getId(), exerciseBodyweightSquat.getId(), 3, 10, 60);
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsBeginnerDayB.getId(), exerciseLungeBodyweight.getId(), 3, 10, 60);
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsBeginnerDayB.getId(), exercisePushUp.getId(), 3, 10, 60);
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsBeginnerDayB.getId(), exerciseLyingLegRise.getId(), 3, 10, 60);
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsBeginnerDayB.getId(), exerciseMountainClimbers.getId(), 3, 1, 60);
        workoutExerciseDAO.createWorkoutExercise(workoutCalisthenicsBeginnerDayB.getId(), exercisePikePushUp.getId(), 3, 10, 60);

        workoutExerciseDAO.close();
    }
}
