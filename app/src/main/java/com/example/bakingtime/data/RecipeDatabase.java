package com.example.bakingtime.data;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
    entities = {Recipe.class},
    version = 1,
    exportSchema = false
)
@TypeConverters({ListJsonConverter.class})
public abstract class RecipeDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 2;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static final String TAG = RecipeDatabase.class.getSimpleName();
    private static volatile RecipeDatabase INSTANCE;

    static RecipeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(
                                            context.getApplicationContext(),
                                            RecipeDatabase.class,
                                            "recipe_database")
                                    .addCallback(getCallback(context))
                                    .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback getCallback(Context context) {
        return new Callback() {
            /**
             * Called when the database has been opened.
             *
             * @param db The database.
             */
            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);

                databaseWriteExecutor.execute(
                        () -> {

                            // Delete everything from Database.
                            INSTANCE.recipeDao().deleteAllRecipes();

                            // Fetch the latest JSON data and
                            // insert.
                            fetchFromNetworkAndInsertToDatabase();
                        });
            }

            private void fetchFromNetworkAndInsertToDatabase() {
                RecipeNetworkService_Factory.newInstance(context)
                        .getRecipes()
                        .subscribe(
                                recipes -> {
                                    Log.d(TAG, "Recipes got fetched from the network\n" + recipes);
                                    databaseWriteExecutor.execute(
                                            () -> INSTANCE.recipeDao().addRecipes(recipes));
                                });
            }
        };
    }

    public abstract RecipeDao recipeDao();
}
