package com.amary.sisosmed.core.source.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.amary.sisosmed.constant.DataStore
import com.amary.sisosmed.constant.EmptyValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(name = DataStore.STORE_NAME)

class PrefDataStore(context: Context) {
    private val dataStore = context.dataStore

    suspend fun setName(name: String){
        dataStore.edit { it[DataStore.KEY_NAME] = name }
    }

    val getName: Flow<String> = dataStore.data
        .map { it[DataStore.KEY_NAME] ?: EmptyValue.STRING }

    suspend fun setToken(token: String){
        dataStore.edit { it[DataStore.KEY_TOKEN] = token }
    }

    val getToken: Flow<String> = dataStore.data
        .map { it[DataStore.KEY_TOKEN] ?: EmptyValue.STRING }

    suspend fun clear(){
        dataStore.edit { it.clear() }
    }
}