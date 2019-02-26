package com.aidan.log.viewer;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.aidan.log.Database;
import com.aidan.log.LogBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An implementation of AsyncTaskLoader which loads a {@code List<AppEntry>} containing all installed applications on the device.
 */
public class LogsLoader extends AsyncTaskLoader<List<LogBean>> {

    private String TAG = "LogsActivity";

    public static final int ID = "LogsLoader".hashCode();

    private static List<LogBean> lBeans;

    private static boolean contentChangedFromObserver;

    private String search;
    private int level = 1;
    private boolean desc = false;

    public static void updated() {
        contentChangedFromObserver = true;
    }

    public LogsLoader(Context context, Bundle b) {
        // Loaders may be used across multiple Activitys (assuming they aren't
        // bound to the LoaderManager), so NEVER hold a reference to the context
        // directly. Doing so will cause you to leak an entire Activity's context.
        // The superclass constructor will store a reference to the Application
        // Context instead, and can be retrieved with a call to getContext().
        super(context);
        if (b != null) {
            search = b.getString("search", null);
            level = b.getInt("level", 0);
            desc = b.getBoolean("desc", false);
        }
        Log.i(TAG, "+++ LogsLoader called! +++ search : [" + search + "] level : [" + level + "] desc : [" + desc + "]");
    }

    /****************************************************/
    /** (1) A task that performs the asynchronous load **/
    /****************************************************/

    /**
     * This method is called on a background thread and generates a List of {@link LogBean} objects. Each entry corresponds to a single installed
     * application on the device.
     */
    @Override
    public List<LogBean> loadInBackground() {
        Log.e(TAG, "+++ loadInBackground() called! +++   " + contentChangedFromObserver + "    search : [" + search + "] level : [" + level + "] desc : [" + desc + "]");

        List<LogBean> returnThis = null;

        if (lBeans == null || contentChangedFromObserver) {
            try {
                lBeans = Database.getLogs();
                contentChangedFromObserver = false;
            } catch(Exception e) {
                e.printStackTrace();
                if(lBeans == null) {
                    lBeans = new ArrayList<>();
                }
            }
        }

        List<LogBean> lBeans_level = new ArrayList<>();
        if (level > 1) {
            for (LogBean lBean : lBeans) {
                if (lBean.level == level) {
                    lBeans_level.add(lBean);
                }
            }
        } else {
            lBeans_level = new ArrayList<>(lBeans);
        }

        if (!TextUtils.isEmpty(search)) {
            List<LogBean> lBeans_search = new ArrayList<>();

            String searchLowercase = search.toLowerCase();

            for (LogBean lBean : lBeans_level) {
                String tagLowercase = lBean.tag.toLowerCase();
                if (tagLowercase.contains(searchLowercase)) {
                    lBeans_search.add(lBean);
                    continue;
                }

                String textLowercase = lBean.text.toLowerCase();
                if (textLowercase.contains(searchLowercase)) {
                    lBeans_search.add(lBean);
                }
            }
            returnThis = lBeans_search;
        } else {
            returnThis = new ArrayList<>(lBeans_level);
        }

        if (desc) {
            Collections.reverse(returnThis);
        }

        Log.e(TAG, "returnThis.size() : " + returnThis.size());
        return returnThis;
    }

    public static void release() {
        if (lBeans != null) {
            lBeans.clear();
        }
        lBeans = null;
    }

    /*******************************************/
    /** (2) Deliver the results to the client **/
    /*******************************************/

    /**
     * Called when there is new data to deliver to the client. The superclass will
     * deliver it to the registered listener (i.e. the LoaderManager), which will
     * forward the results to the client through a call to onLoadFinished.
     */
    @Override
    public void deliverResult(List<LogBean> _lBeans) {
        if (isReset()) {
            Log.w(TAG, "+++ Warning! An async query came in while the Loader was reset! +++");
            // The Loader has been reset; ignore the result and invalidate the data.
            // This can happen when the Loader is reset while an asynchronous query
            // is working in the background. That is, when the background thread
            // finishes its work and attempts to deliver the results to the client,
            // it will see here that the Loader has been reset and discard any
            // resources associated with the new data as necessary.
            if (_lBeans != null) {
                return;
            }
        }

        if (isStarted()) {
            Log.i(TAG, "+++ Delivering results to the LoaderManager for" + " the ListFragment to display! +++" + _lBeans.size());
            // If the Loader is in a started state, have the superclass deliver the
            // results to the client.
            super.deliverResult(_lBeans);
        }
    }

    /*********************************************************/
    /** (3) Implement the Loaders state-dependent behavior **/
    /*********************************************************/

    @Override
    protected void onStartLoading() {
        Log.i(TAG, "+++ onStartLoading() called! +++   " + contentChangedFromObserver + "    search : [" + search + "] level : [" + level + "] desc : [" + desc + "]");
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        Log.i(TAG, "+++ onStopLoading() called! +++");

        // The Loader has been put in a stopped state, so we should attempt to
        // cancel the current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is; Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        Log.i(TAG, "+++ onReset() called! +++");

        // Ensure the loader is stopped.
        onStopLoading();
    }

    @Override
    public void onCanceled(List<LogBean> _lBeans) {
        Log.i(TAG, "+++ onCanceled() called! +++");

        // Attempt to cancel the current asynchronous load.
        super.onCanceled(_lBeans);

        if (_lBeans != null) {
            _lBeans.clear();
            _lBeans = null;
        }
    }

    @Override
    public void forceLoad() {
        Log.i(TAG, "+++ forceLoad() called! +++");
        super.forceLoad();
    }
}
