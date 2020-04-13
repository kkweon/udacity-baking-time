package com.example.bakingtime.detail.ui.detailsteplist;

import static android.view.View.GONE;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.bakingtime.data.RecipeStep;
import com.example.bakingtime.databinding.FragmentDetailStepItemBinding;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/** A simple {@link Fragment} subclass. */
public class DetailStepItemFragment extends Fragment {

    private static final String KEY_ON_NAV_CLICK_LISTENER = "KEY_ON_NAV_CLICK_LISTENER";

    public interface OnNavClickListener extends Serializable {
        void onClickPrev(RecipeStep recipeStep);

        void onClickNext(RecipeStep recipeStep);
    }

    private OnNavClickListener mOnNavClickListener;

    private static final String KEY_RECIPE_STEP_OUT_STATE = "KEY_RECIPE_STEP_OUT_STATE";
    private static final String TAG = DetailStepItemFragment.class.getSimpleName();
    private static final String KEY_PLAY_WHEN_READY_STATE = "KEY_PLAY_WHEN_READY_STATE";
    private static final String KEY_PLAYBACK_POSITION_STATE = "KEY_PLAYBACK_POSITION_STATE";
    private static final String KEY_CURRENT_WINDOW_STATE = "KEY_CURRENT_WINDOW_STATE";
    private FragmentDetailStepItemBinding fragmentDetailStepItemBinding;
    // Exo player properties.
    private SimpleExoPlayer exoPlayer;
    private boolean playWhenReady = true;
    private int currentWindow;
    private long playbackPosition;
    private RecipeStep recipeStep;
    private String userAgent;

    public static DetailStepItemFragment create(
            RecipeStep recipeStep, @Nullable OnNavClickListener onNavClickListener) {
        // Required empty public constructor
        DetailStepItemFragment fragment = new DetailStepItemFragment();

        fragment.recipeStep = recipeStep;
        fragment.mOnNavClickListener = onNavClickListener;
        return fragment;
    }

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentDetailStepItemBinding =
                FragmentDetailStepItemBinding.inflate(inflater, container, false);

        exoPlayer = ExoPlayerFactory.newSimpleInstance(container.getContext());
        fragmentDetailStepItemBinding.playerViewActivityDetailStepItem.setPlayer(exoPlayer);

        if (savedInstanceState != null) {
            recipeStep = savedInstanceState.getParcelable(KEY_RECIPE_STEP_OUT_STATE);
            restoreExoPlayStats(savedInstanceState);

            mOnNavClickListener =
                    (OnNavClickListener)
                            savedInstanceState.getSerializable(KEY_ON_NAV_CLICK_LISTENER);
        }

        initializeView(recipeStep);
        return fragmentDetailStepItemBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recipeStep != null) {
            outState.putParcelable(KEY_RECIPE_STEP_OUT_STATE, recipeStep);
        }

        outState.putBoolean(KEY_PLAY_WHEN_READY_STATE, playWhenReady);
        outState.putLong(KEY_PLAYBACK_POSITION_STATE, playbackPosition);
        outState.putInt(KEY_CURRENT_WINDOW_STATE, currentWindow);

        if (mOnNavClickListener != null) {
            outState.putSerializable(KEY_ON_NAV_CLICK_LISTENER, mOnNavClickListener);
        }
    }

    private void restoreExoPlayStats(Bundle savedInstanceState) {
        playWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY_STATE);
        playbackPosition = savedInstanceState.getLong(KEY_PLAYBACK_POSITION_STATE);
        currentWindow = savedInstanceState.getInt(KEY_CURRENT_WINDOW_STATE);
    }

    private void initializeView(RecipeStep recipeStep) {
        if (recipeStep == null) {
            Log.d(TAG, "initializeView() called with: recipeStep = [" + recipeStep + "]");
            return;
        }

        maybeDoSomething(
                recipeStep.getThumbnailURL(),
                aVoid -> {
                    Picasso.get()
                            .load(recipeStep.getThumbnailURL())
                            .fit()
                            .into(
                                    fragmentDetailStepItemBinding
                                            .imageViewActivityDetailStepItemThumbnail);
                    return null;
                },
                fragmentDetailStepItemBinding.imageViewActivityDetailStepItemThumbnail);

        maybeDoSomething(
                recipeStep.getDescription(),
                aVoid -> {
                    fragmentDetailStepItemBinding.textViewActivityDetailStepItemDescription.setText(
                            recipeStep.getDescription());
                    return null;
                },
                fragmentDetailStepItemBinding.textViewActivityDetailStepItemDescription);

        showImageButtonOn(
                fragmentDetailStepItemBinding.imageButtonFragmentDetailStepItemPrev,
                recipeStep.hasPrev());
        if (recipeStep.hasPrev()
                && mOnNavClickListener != null
                && fragmentDetailStepItemBinding.imageButtonFragmentDetailStepItemPrev != null) {
            fragmentDetailStepItemBinding.imageButtonFragmentDetailStepItemPrev.setOnClickListener(
                    v -> mOnNavClickListener.onClickPrev(recipeStep));
        }

        showImageButtonOn(
                fragmentDetailStepItemBinding.imageButtonFragmentDetailStepItemNext,
                recipeStep.hasNext());
        if (recipeStep.hasNext()
                && mOnNavClickListener != null
                && fragmentDetailStepItemBinding.imageButtonFragmentDetailStepItemNext != null) {
            fragmentDetailStepItemBinding.imageButtonFragmentDetailStepItemNext.setOnClickListener(
                    v -> mOnNavClickListener.onClickNext(recipeStep));
        }
    }

    private void showImageButtonOn(ImageButton imageButton, boolean condition) {
        // Do nothing if imageButton is null. It means the current layout is a landscape mode.
        if (imageButton == null) {
            return;
        }
        if (condition) {
            imageButton.setVisibility(View.VISIBLE);
        } else {
            imageButton.setVisibility(View.INVISIBLE);
        }
    }

    private void maybeDoSomething(String param, Function<Void, Void> doFn, View view) {
        // do nothing if view is hidden (For example, the layout is a landscape mode).
        if (view == null) {
            return;
        }

        if (!param.isEmpty()) {
            doFn.apply(null);
        } else {
            view.setVisibility(GONE);
        }
    }

    private void initializePlayer() {
        if (recipeStep == null || recipeStep.getVideoURL().isEmpty()) {
            fragmentDetailStepItemBinding.playerViewActivityDetailStepItem.setVisibility(GONE);
            return;
        }
        Uri uri = Uri.parse(recipeStep.getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);

        exoPlayer.setPlayWhenReady(playWhenReady);
        exoPlayer.seekTo(currentWindow, playbackPosition);
        exoPlayer.prepare(mediaSource, false, false);

        // if landscape hide description
        if (fragmentDetailStepItemBinding.frameLayoutFragmentDetailStepItemLandscape != null) {
            fragmentDetailStepItemBinding.textViewActivityDetailStepItemDescription.setVisibility(
                    GONE);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        userAgent = "exoplayer-bakingapp";
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(userAgent);
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            playWhenReady = exoPlayer.getPlayWhenReady();
            playbackPosition = exoPlayer.getCurrentPosition();
            currentWindow = exoPlayer.getCurrentWindowIndex();

            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
