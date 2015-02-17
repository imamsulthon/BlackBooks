package com.blackbooks.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.blackbooks.R;
import com.blackbooks.database.SQLiteHelper;
import com.blackbooks.model.nonpersistent.BookInfo;
import com.blackbooks.model.persistent.ScannedIsbn;
import com.blackbooks.search.BookSearcher;
import com.blackbooks.services.BookServices;
import com.blackbooks.services.ScannedIsbnServices;
import com.blackbooks.utils.LogUtils;
import com.blackbooks.utils.VariableUtils;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * A service that performs background ISBN look ups.
 */
public final class BulkSearchService extends IntentService {

    private static final int NOTIFICATION_ID = 1;

    private static final int MAX_CONNECTION_ERRORS = 5;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private boolean mStop;

    /**
     * Constructor.
     */
    public BulkSearchService() {
        super(BulkSearchService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SQLiteDatabase db = SQLiteHelper.getInstance().getWritableDatabase();
        List<ScannedIsbn> scannedIsbnList = ScannedIsbnServices.getScannedIsbnList(db);

        int scannedIsbnCount = scannedIsbnList.size();

        Resources res = getResources();
        String text = res.getQuantityString(R.plurals.notification_bulk_search_running_text, scannedIsbnCount, scannedIsbnCount);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle(getString(R.string.notification_bulk_search_running_title))
                .setContentText(text);


        mBuilder.setTicker(getString(R.string.notification_bulk_search_running_title));
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        mBuilder.setTicker(null);

        int connectionErrors = 0;
        for (int i = 0; i < scannedIsbnCount; i++) {
            if (mStop) {
                break;
            }
            if (connectionErrors > MAX_CONNECTION_ERRORS) {
                break;
            }
            ScannedIsbn scannedIsbn = scannedIsbnList.get(i);
            String isbn = scannedIsbn.isbn;
            Log.d(LogUtils.TAG, String.format("Searching results for ISBN %s.", isbn));

            try {
                BookInfo bookInfo = BookSearcher.search(isbn);
                if (bookInfo == null) {
                    Log.d(LogUtils.TAG, "No results.");
                } else {
                    Log.d(LogUtils.TAG, String.format("Result: %s", bookInfo.title));
                    BookServices.saveBookInfo(db, bookInfo);
                    VariableUtils.getInstance().setReloadBookList(true);
                }

                ScannedIsbnServices.markScannedIsbnLookedUp(db, scannedIsbn.id);

            } catch (SocketException e) {
                Log.w(LogUtils.TAG, "SocketException.", e);
                connectionErrors++;
            } catch (UnknownHostException e) {
                Log.w(LogUtils.TAG, "Host name could not be resolved.", e);
                connectionErrors++;
            } catch (InterruptedException e) {
                Log.i(LogUtils.TAG, "Service interrupted.");
                break;
            } catch (Exception e) {
                Log.e(LogUtils.TAG, "An exception occurred during the background search.", e);
                break;
            }

            mBuilder.setProgress(scannedIsbnCount, i, false);
            Notification notification = mBuilder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
            mNotificationManager.notify(NOTIFICATION_ID, notification);
        }

        mBuilder.setContentTitle(getString(R.string.notification_bulk_search_finished_title));
        mBuilder.setContentText(getString(R.string.notification_bulk_search_finished_text));
        mBuilder.setProgress(0, 0, false);
        mBuilder.setTicker(getString(R.string.notification_bulk_search_finished_title));
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStop = true;
    }
}