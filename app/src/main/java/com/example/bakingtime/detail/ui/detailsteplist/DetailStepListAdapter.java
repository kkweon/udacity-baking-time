package com.example.bakingtime.detail.ui.detailsteplist;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bakingtime.data.Recipe;
import com.example.bakingtime.data.RecipeStep;
import com.example.bakingtime.databinding.RowRecipeStepDetailBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import lombok.Setter;

class DetailStepListAdapter
        extends RecyclerView.Adapter<DetailStepListAdapter.DetailStepListViewHolder> {

    private static final String TAG = DetailStepListAdapter.class.getSimpleName();

    @Setter private Integer mActiveRecipeStepId = null;
    @Setter private OnCardViewClickListener onCardViewClickListener;
    @Setter private Recipe mRecipe;

    @NonNull
    @Override
    public DetailStepListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowRecipeStepDetailBinding binding =
                RowRecipeStepDetailBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);
        return new DetailStepListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailStepListViewHolder holder, int position) {
        RecipeStep recipeStep = mRecipe.getSteps().get(position);
        holder.rowRecipeStepDetailBinding.textViewRowRecipeStepShortDescription.setText(
                String.format(
                        holder.itemView
                                .getResources()
                                .getConfiguration()
                                .getLocales()
                                .get(position),
                        "Step %d. %s",
                        // Step should start from 1.
                        position + 1,
                        recipeStep.getShortDescription()));

        holder.setActive(position == mActiveRecipeStepId);

        if (!recipeStep.getThumbnailURL().isEmpty()) {
            Picasso.get()
                    .load(recipeStep.getThumbnailURL())
                    .fit()
                    .into(
                            new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    holder.rowRecipeStepDetailBinding
                                            .cardViewRowRecipeStepDetailContainer.setBackground(
                                            new BitmapDrawable(
                                                    holder.itemView.getContext().getResources(),
                                                    bitmap));
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

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mRecipe == null || mRecipe.getSteps() == null ? 0 : mRecipe.getSteps().size();
    }

    interface OnCardViewClickListener {
        void onClick(RecipeStep recipeStep);
    }

    class DetailStepListViewHolder extends RecyclerView.ViewHolder {
        RowRecipeStepDetailBinding rowRecipeStepDetailBinding;

        public DetailStepListViewHolder(RowRecipeStepDetailBinding rowRecipeStepDetailBinding) {
            super(rowRecipeStepDetailBinding.getRoot());
            this.rowRecipeStepDetailBinding = rowRecipeStepDetailBinding;

            this.rowRecipeStepDetailBinding.cardViewRowRecipeStepDetailContainer.setOnClickListener(
                    v ->
                            onCardViewClickListener.onClick(
                                    mRecipe.getSteps().get(getAdapterPosition())));
        }

        public void setActive(boolean isActive) {
            if (isActive) {
                rowRecipeStepDetailBinding.cardViewRowRecipeStepDetailContainer
                        .setCardBackgroundColor(Color.argb(50, 50, 100, 100));
            } else {
                rowRecipeStepDetailBinding.cardViewRowRecipeStepDetailContainer
                        .setCardBackgroundColor(Color.TRANSPARENT);
            }
        }
    }
}
