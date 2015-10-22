package com.singuloid.singuloidappstore.adapter;

import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;

/**
 * Created by scguo on 15/10/20.
 * <p/>
 * Provide a {@link android.support.v7.widget.RecyclerView.Adapter} implementation with
 * cursor support.
 * Child classes only need to implement {@link #onCreateViewHolder(android.view.ViewGroup, int)}
 * and {@link #onBindViewHolderCursor(android.support.v7.widget.RecyclerView.ViewHolder, android.database.Cursor)}
 * <p/>
 * And this provide some facility while we operating on the Cursor when we retrieve data
 * from the local SQLite database
 * <p/>
 * And we also implement the {@link android.widget.Filterable} of which Defines a filterable
 * behavior. A filterable class can have its data constrained by a filter. Filterable classes
 * are usually {@link android.widget.Adapter} implementations.
 *
 *
 */
public abstract class CursorRecyclerAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements Filterable,
        CursorFilter.CursorFilterClient {

    private static final String TAG = "CursorRecyclerAdapter";

    // the tag of which indicates whether the data is still valid or not
    // we need to use this tag to judge whether we should use this data
    private boolean mDataValid;
    private int mRowIdColumns;
    private Cursor mCursor;
    private ChangeObserver mChangeObserver;
    private DataSetObserver mDataSetObserver;
    private CursorFilter mCursorFilter;
    private FilterQueryProvider mFilterQueryProvider;

    public CursorRecyclerAdapter(Cursor cursor) {
        init(cursor);
    }

    void init(Cursor cursor) {
        boolean cursorPresent = cursor != null;
        mCursor = cursor;
        mDataValid = cursorPresent;
        // init the RowIdColumns as the value of the PrimaryKey row _ID
        // as every table will have one PrimaryKey of _ID, and this is
        // given by the android SQLite internal implementation
        mRowIdColumns = cursorPresent ? cursor.getColumnIndexOrThrow("_id") : -1;

        mChangeObserver = new ChangeObserver();
        mDataSetObserver = new MyDataSetObserver();

        if (cursorPresent) {
            if (mChangeObserver != null) {
                cursor.registerContentObserver(mChangeObserver);
            }
            if (mDataSetObserver != null) {
                cursor.registerDataSetObserver(mDataSetObserver);
            }
        }
    }

    /**
     * This method will move the Cursor to the correct position and call
     * {@link #onBindViewHolderCursor(android.support.v7.widget.RecyclerView.ViewHolder, android.database.Cursor)}
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (!mDataValid) {
            Log.d(TAG, " exception happened, the data is not valid ... ");
            throw new IllegalStateException("This should be only called when the cursor is valid");
        }

        if (!mCursor.moveToPosition(position)) {
            Log.d(TAG, " could not move the cursor to the position : " + position);
        }
        onBindViewHolderCursor(holder, mCursor);
    }

    /**
     *
     * @param holder ViewHolder
     * @param cursor The cursor from which to get the data. The cursor is already
     *               moved to the correct position.
     */
    public abstract void onBindViewHolderCursor(VH holder, Cursor cursor);

    @Override
    public int getItemCount() {
        return mDataValid && mCursor != null ? mCursor.getCount() : 0;
    }

    /**
     * @see {@link android.widget.ListAdapter#getItemId(int)}
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                return mCursor.getLong(mRowIdColumns);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }


    @Override
    public Cursor getCursor() {
        return mCursor;
    }

    /**
     * Change the underlying cursor to a new cursor, if there is an existing cursor it will be
     * closed, see {@link #swapCursor(android.database.Cursor)} for detailed implementations.
     *
     * @param cursor the new cursor to be used
     */
    @Override
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new cursor, returning the old cursor. Unlike the
     * {@link #changeCursor(android.database.Cursor)}, the returned old
     * cursor is not closed.
     *
     * @param newCursor the new cursor to be used.
     * @return Returns the previously set cursor, or null if there was not a one.
     *         If the given new cursor is the same instance is the previously set Cursor,
     *         null is also returned
     *
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        // unregister all of the previously register observers
        Cursor oldCursor = mCursor;
        if (null != oldCursor) {
            if (null != mChangeObserver) {
                oldCursor.unregisterContentObserver(mChangeObserver);
            }
            if (null != mDataSetObserver) {
                oldCursor.unregisterDataSetObserver(mDataSetObserver);
            }
        }

        // register the necessary observers to the newCursor
        mCursor = newCursor;
        if (null != newCursor) {
            if (null != mChangeObserver) {
                newCursor.registerContentObserver(mChangeObserver);
            }
            if (null != mDataSetObserver) {
                newCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumns = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            // the newCursor is not valid
            mRowIdColumns = -1;
            mDataValid = false;
            // notify the observers about the lack of the data set
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;
    }

    /**
     * Subclasses should override this method and implement their own version,
     * just like the {@link #toString()} method does
     *
     * @param cursor
     * @return
     */
    @Override
    public CharSequence convertToStr(Cursor cursor) {
        return cursor == null ? "" : cursor.toString();
    }

    /**
     *
     * @param constraint
     * @return
     */
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        return mFilterQueryProvider != null ? mFilterQueryProvider.runQuery(constraint) : mCursor;
    }

    @Override
    public Filter getFilter() {
        return mCursorFilter == null ? new CursorFilter(this) : mCursorFilter;
    }

    public FilterQueryProvider getFilterQueryProvider() {
        return mFilterQueryProvider;
    }

    public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
        this.mFilterQueryProvider = filterQueryProvider;
    }

    /**
     * Called when the {@link android.database.ContentObserver} on the cursor receives
     * a change notification. Can be implemented by sub-class
     *
     * @see {@link android.database.ContentObserver#onChange(boolean)}
     */
    protected void onContentChanged() {
        // let the sub-class implement this part
    }

    private class ChangeObserver extends ContentObserver {

        /**
         * Creates a content observer.
         */
        public ChangeObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            onContentChanged();
        }
    }

    private class MyDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            mDataValid = false;
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    /**
     * The {@link com.singuloid.singuloidappstore.adapter.CursorFilter} delegates
     * most of the work to the CursorAdapter. Subclasses should override these delegate
     * methods to run the queries and convert the results into String that can be used
     * by auto-completion widgets.
     */

}

/**
 * <p>A filter constrains data with a filtering pattern.</p>
 * <p/>
 * <p>Filters are usually created by {@link android.widget.Filterable}
 * classes.</p>
 * <p/>
 * <p>Filtering operations performed by calling {@link #filter(CharSequence)} or
 * {@link #filter(CharSequence, android.widget.Filter.FilterListener)} are
 * performed asynchronously. When these methods are called, a filtering request
 * is posted in a request queue and processed later. Any call to one of these
 * methods will cancel any previous non-executed filtering request.</p>
 * <p/>
 * * @see {@link android.widget.Filterable}
 */
class CursorFilter extends Filter {

    CursorFilterClient mClient;

    CursorFilter(CursorFilterClient client) {
        this.mClient = client;
    }

    @Override
    public CharSequence convertResultToString(Object resultValue) {
        return mClient.convertToStr((Cursor) resultValue);
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        Cursor cursor = mClient.runQueryOnBackgroundThread(constraint);
        FilterResults results = new FilterResults();
        if (null != cursor) {
            results.count = cursor.getCount();
            results.values = cursor;
        } else {
            results.count = 0;
            results.values = null;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

    }

    interface CursorFilterClient {
        CharSequence convertToStr(Cursor cursor);

        Cursor runQueryOnBackgroundThread(CharSequence constraint);

        Cursor getCursor();

        void changeCursor(Cursor cursor);
    }

}


