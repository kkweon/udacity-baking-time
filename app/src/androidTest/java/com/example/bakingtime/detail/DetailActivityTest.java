package com.example.bakingtime.detail;

import android.content.Context;
import android.content.Intent;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.bakingtime.R;
import com.example.bakingtime.data.Recipe;
import com.example.bakingtime.data.RecipeDao;
import com.example.bakingtime.data.RecipeDatabase;
import com.example.bakingtime.data.RecipeStep;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.example.bakingtime.detail.DetailActivity.EXTRA_RECIPE_ID;

@RunWith(AndroidJUnit4.class)
@Ignore
public class DetailActivityTest {

    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule =
            new ActivityTestRule<>(DetailActivity.class, true, false);
    private IdlingResource mIdlingResource;
    private RecipeDatabase mDb;
    private RecipeDao mRecipeDao;

    @Before
    public void registerIdlingResource() {
        Context context = ApplicationProvider.getApplicationContext();
        mDb = Room.inMemoryDatabaseBuilder(context, RecipeDatabase.class).build();
        mRecipeDao = mDb.recipeDao();

        List<RecipeStep> recipeSteps = new ArrayList<>();
        recipeSteps.add(RecipeStep.builder().id(0).shortDescription("Wow").build());
        Recipe recipe = Recipe.builder().id(1L).steps(recipeSteps).build();
        mRecipeDao.addRecipe(recipe);

        mIdlingResource = DetailActivity.getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);

        Intent intent = new Intent();
        intent.putExtra(EXTRA_RECIPE_ID, 1L);
        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testDetailActivity() {
        onView(withId(R.id.card_view_row_recipe_step_detail_container))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }

        mDb.close();
    }
}
