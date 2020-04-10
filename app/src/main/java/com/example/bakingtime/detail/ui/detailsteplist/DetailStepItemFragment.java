package com.example.bakingtime.detail.ui.detailsteplist;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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

import java.util.function.Function;

import static android.view.View.GONE;

/** A simple {@link Fragment} subclass. */
public class DetailStepItemFragment extends Fragment {

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

    public static DetailStepItemFragment create(RecipeStep recipeStep) {
        // Required empty public constructor
        DetailStepItemFragment fragment = new DetailStepItemFragment();
        fragment.recipeStep = recipeStep;
        return fragment;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentDetailStepItemBinding =
                FragmentDetailStepItemBinding.inflate(inflater, container, false);

        exoPlayer = ExoPlayerFactory.newSimpleInstance(container.getContext());
        fragmentDetailStepItemBinding.playerViewActivityDetailStepItem.setPlayer(exoPlayer);

        if (savedInstanceState != null) {
            recipeStep = savedInstanceState.getParcelable(KEY_RECIPE_STEP_OUT_STATE);
            restoreExoPlayStats(savedInstanceState);
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
                fragmentDetailStepItemBinding.imageViewActivityDetailStepItemThumbnail);
    }

    private void maybeDoSomething(String param, Function<Void, Void> doFn, View view) {
        if (!param.isEmpty()) {
            doFn.apply(null);
        } else {
            view.setVisibility(GONE);
        }
    }

    private void initializePlayer() {
        if (recipeStep.getVideoURL().isEmpty()) {
            fragmentDetailStepItemBinding.playerViewActivityDetailStepItem.setVisibility(GONE);
            return;
        }
        Uri uri = Uri.parse(recipeStep.getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);

        exoPlayer.setPlayWhenReady(playWhenReady);
        exoPlayer.seekTo(currentWindow, playbackPosition);
        exoPlayer.prepare(mediaSource, false, false);
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
