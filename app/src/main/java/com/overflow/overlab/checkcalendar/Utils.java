package com.overflow.overlab.checkcalendar;

import android.util.Log;

import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by over on 7/13/2016.
 */
public class Utils {

    private static FileDataStoreFactory dataStoreFactory;

    private static final File DATA_STORE_DIR =
            new File(System.getProperty("user.home"), ".store/checkcalendar/sync");

    static {
        try {
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (IOException ioe) {
            Log.d("IOE Error : ", ioe.toString());
        } catch (Throwable t) {
            Log.d("StoreFactory Error : ", t.toString());
        }
    }

    public static DataStoreFactory getDataStoreFactory() {
        return dataStoreFactory;
    }



}
