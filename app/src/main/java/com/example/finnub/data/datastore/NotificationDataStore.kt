package com.example.finnub.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.notificationDataStore by preferencesDataStore(DataStoreConstants.notificationDataStore)