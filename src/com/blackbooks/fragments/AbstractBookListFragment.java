package com.blackbooks.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.blackbooks.activities.BookDisplay;
import com.blackbooks.adapters.BookItem;
import com.blackbooks.adapters.ListItem;
import com.blackbooks.adapters.ListItemType;
import com.blackbooks.utils.VariableUtils;

/**
 * Abstract book list fragment.
 */
public abstract class AbstractBookListFragment extends ListFragment {

	private ArrayAdapter<ListItem> mBookListAdapter;

	private BookListListener mBookListListener;

	private BookListLoadTask mBookListLoadTask;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof BookListListener) {
			mBookListListener = (BookListListener) activity;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBookListAdapter = getBookListAdapter();
		setRetainInstance(true);
		setReloadBookListToTrue();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (getReloadBookList()) {
			setReloadBookListToFalse();
			mBookListLoadTask = new BookListLoadTask();
			mBookListLoadTask.execute();
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mBookListListener = null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBookListLoadTask != null) {
			mBookListLoadTask.cancel(true);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ListItem item = (ListItem) getListAdapter().getItem(position);
		ListItemType itemType = item.getListItemType();

		if (itemType == ListItemType.Entry) {
			BookItem bookItem = (BookItem) item;
			Intent i = new Intent(this.getActivity(), BookDisplay.class);
			i.putExtra(BookDisplay.EXTRA_BOOK_ID, bookItem.getId());
			this.startActivity(i);
		}
	}

	/**
	 * Load the book list and notify the list adapter of the fragment. This
	 * method is run inside an AsyncTask (i.e. in a different thread) to avoid
	 * blocking the activity with a long running database load.
	 */
	protected abstract ArrayList<ListItem> loadBookList();

	/**
	 * Return a new instance of the adapter used to draw the list of books.
	 * 
	 * @return ArrayAdapter of {@link ListItem}.
	 */
	protected abstract ArrayAdapter<ListItem> getBookListAdapter();

	/**
	 * Return a value indicating if the book list should be reloaded.
	 * 
	 * @return True to refresh the book list, false otherwise.
	 */
	private boolean getReloadBookList() {
		return VariableUtils.getInstance().getReloadBookList();
	}

	/**
	 * Set the value indicating if the book list should be reloaded to false.
	 */
	private void setReloadBookListToFalse() {
		VariableUtils.getInstance().setReloadBookList(false);
	}

	/**
	 * Set the value indicating if the book list should be reloaded to true.
	 */
	private void setReloadBookListToTrue() {
		VariableUtils.getInstance().setReloadBookList(true);
	}

	/**
	 * Activites hosting {@link AbstractBookListFragment} or any fragment that
	 * inherits from it should implement this interface to be notified when the
	 * loading of the book list is complete.
	 */
	public interface BookListListener {

		/**
		 * Called when the book list is loaded.
		 */
		void onBookListLoaded();
	}

	/**
	 * Implementation of AsyncTask used to load the book list without blocking
	 * the UI.
	 */
	private class BookListLoadTask extends AsyncTask<Void, Void, ArrayList<ListItem>> {

		@Override
		protected void onPreExecute() {
			AbstractBookListFragment.this.setListShown(false);
		}

		@Override
		protected ArrayList<ListItem> doInBackground(Void... params) {
			return loadBookList();
		}

		@Override
		protected void onPostExecute(ArrayList<ListItem> result) {
			if (AbstractBookListFragment.this.getListAdapter() == null) {
				AbstractBookListFragment.this.setListAdapter(mBookListAdapter);
			}

			mBookListAdapter.clear();
			mBookListAdapter.addAll(result);
			mBookListAdapter.notifyDataSetChanged();
			if (AbstractBookListFragment.this.getView() != null) {
				AbstractBookListFragment.this.setListShown(true);
			}

			if (mBookListListener != null) {
				mBookListListener.onBookListLoaded();
			}
		}
	}
}
