package com.example.bakingtime.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bakingtime.data.Recipe;
import com.example.bakingtime.databinding.RowRecipeListBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private static final String TAG = RecipeAdapter.class.getSimpleName();
    @Setter private List<Recipe> mRecipes;
    private Context mContext;
    private OnCardClickListener mOnCardClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowRecipeListBinding rowRecipeListBinding =
                RowRecipeListBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(rowRecipeListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        holder.rowRecipeListBinding.textViewRowRecipeTitle.setText(recipe.getName());
        if (recipe.getImage().isEmpty()) {
            // Most are actually empty.
        } else {
            Picasso.get()
                    .load(recipe.getImage())
                    .into(
                            new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    holder.rowRecipeListBinding.cardViewContainer.setBackground(
                                            new BitmapDrawable(mContext.getResources(), bitmap));
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                    Log.d(
                                            TAG,
                                            String.format(
                                                    "onBitmapFailed() called with: e = [%s], errorDrawable = [%s]",
                                                    e, errorDrawable));
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    Log.d(
                                            TAG,
                                            String.format(
                                                    "onPrepareLoad() called with: placeHolderDrawable = [%s]",
                                                    placeHolderDrawable));
                                }
                            });
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes == null ? 0 : mRecipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RowRecipeListBinding rowRecipeListBinding;

        ViewHolder(RowRecipeListBinding rowRecipeListBinding) {
            super(rowRecipeListBinding.getRoot());
            this.rowRecipeListBinding = rowRecipeListBinding;

            this.rowRecipeListBinding.cardViewContainer.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            mOnCardClickListener.onClick(mRecipes.get(getAdapterPosition()));
        }
    }
}
