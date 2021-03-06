package com.blackbooks.fragments.databasedelete;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.blackbooks.R;
import com.blackbooks.database.Database;
import com.blackbooks.database.SQLiteHelper;
import com.blackbooks.fragments.dialogs.ConfirmDialogFragment;
import com.blackbooks.fragments.dialogs.ProgressDialogFragment;
import com.blackbooks.utils.LogUtils;
import com.blackbooks.utils.VariableUtils;

import java.io.File;

/**
 * Database delete fragment.
 */
public final class DatabaseDeleteFragment extends Fragment implements ProgressDialogFragment.OnProgressDialogListener, ConfirmDialogFragment.OnConfirmListener {

    private static final String TAG_CONFIRM_DIALOG = "TAG_CONFIRM_DIALOG";
    private static final String TAG_PROGRESS_DIALOG_FRAGMENT = "TAG_PROGRESS_DIALOG_FRAGMENT";
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    private Button mButtonDeleteDatabase;

    private DatabaseDeleteTask mDatabaseDeleteTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_database_delete, container, false);

        mButtonDeleteDatabase = (Button) view.findViewById(R.id.databaseDelete_buttonDeleteDb);

        mButtonDeleteDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentActivity activity = DatabaseDeleteFragment.this.getActivity();
                final FragmentManager fm = activity.getSupportFragmentManager();
                final ConfirmDialogFragment fragment = ConfirmDialogFragment.newInstance(
                        R.string.title_dialog_confirm_delete_database,
                        R.string.message_confirm_delete_database,
                        R.string.message_confirm_delete_database_confirm_message,
                        R.string.message_confirm_delete_database_cancel,
                        R.string.message_confirm_delete_database_confirm
                );

                fragment.setTargetFragment(DatabaseDeleteFragment.this, 0);

                fragment.show(fm, TAG_CONFIRM_DIALOG);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelAsyncTask();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            boolean permissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (permissionGranted) {
                executeDatabaseDeleteTask();
            }
        }
    }

    /**
     * Cancel the asynchronous task.
     */
    private void cancelAsyncTask() {
        if (mDatabaseDeleteTask != null) {
            mDatabaseDeleteTask.cancel(true);
        }
    }

    @Override
    public void onConfirm() {
        if (VariableUtils.getInstance().getBulkSearchRunning()) {
            Toast.makeText(getActivity(), getString(R.string.message_stop_background_search), Toast.LENGTH_LONG).show();
        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                executeDatabaseDeleteTask();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private void executeDatabaseDeleteTask() {
        mDatabaseDeleteTask = new DatabaseDeleteTask();
        mDatabaseDeleteTask.execute();
    }

    @Override
    public void onCancel() {
        cancelAsyncTask();
        mButtonDeleteDatabase.setEnabled(true);
    }

    /**
     * The asynchronous task that will delete the application's database..
     */
    private final class DatabaseDeleteTask extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialogFragment mProgressDialogFragment;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mButtonDeleteDatabase.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i(LogUtils.TAG, "Deleting database.");

            mProgressDialogFragment = ProgressDialogFragment.newInstanceSpinner(
                    R.string.title_dialog_delete_database,
                    R.string.message_db_delete
            );
            mProgressDialogFragment.setTargetFragment(DatabaseDeleteFragment.this, 0);

            final FragmentManager fm = getActivity().getSupportFragmentManager();
            mProgressDialogFragment.show(fm, TAG_PROGRESS_DIALOG_FRAGMENT);

            SQLiteHelper sqliteHelper = SQLiteHelper.getInstance();
            sqliteHelper.close();

            Activity activity = getActivity();
            File currentDB = activity.getDatabasePath(Database.NAME);

            boolean result;
            if (isCancelled()) {
                Log.i(LogUtils.TAG, "Delete database task cancelled, aborting.");
                result = false;
            } else {
                result = currentDB.delete();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                Log.i(LogUtils.TAG, "Database successfully deleted.");
                Toast.makeText(getActivity(), R.string.message_db_delete_success, Toast.LENGTH_LONG).show();
            } else {
                Log.i(LogUtils.TAG, "The database could not be deleted.");
                Toast.makeText(getActivity(), R.string.message_db_delete_failure, Toast.LENGTH_LONG).show();
            }

            mButtonDeleteDatabase.setEnabled(true);
            mProgressDialogFragment.dismiss();
        }
    }
}