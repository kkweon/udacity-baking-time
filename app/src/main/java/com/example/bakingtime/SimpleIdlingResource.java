package com.example.bakingtime;

import androidx.test.espresso.IdlingResource;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;

public class SimpleIdlingResource implements IdlingResource {

    @Nullable private volatile ResourceCallback mResourceCallback;
    private AtomicBoolean mAtomicBoolean = new AtomicBoolean(false);

    /** Returns the name of the resources (used for logging and idempotency of registration). */
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Returns {@code true} if resource is currently idle. Espresso will <b>always</b> call this
     * method from the main thread, therefore it should be non-blocking and return immediately.
     */
    @Override
    public boolean isIdleNow() {
        return mAtomicBoolean.get();
    }

    /**
     * Registers the given {@link ResourceCallback} with the resource. Espresso will call this
     * method:
     *
     * <ul>
     *   <li>with its implementation of {@link ResourceCallback} so it can be notified
     *       asynchronously that your resource is idle
     *   <li>from the main thread, but you are free to execute the callback's onTransitionToIdle
     *       from any thread
     *   <li>once (when it is initially given a reference to your IdlingResource)
     * </ul>
     *
     * <p>You only need to call this upon transition from busy to idle - if the resource is already
     * idle when the method is called invoking the call back is optional and has no significant
     * impact.
     *
     * @param callback
     */
    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mResourceCallback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        mAtomicBoolean.set(isIdleNow);
        if (isIdleNow && mResourceCallback != null) {
            mResourceCallback.onTransitionToIdle();
        }
    }
}
