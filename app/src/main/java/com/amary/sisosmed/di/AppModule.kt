package com.amary.sisosmed.di

import androidx.room.Room
import com.amary.sisosmed.BuildConfig
import com.amary.sisosmed.constant.KeyValue
import com.amary.sisosmed.core.Repository
import com.amary.sisosmed.core.source.local.LocalSource
import com.amary.sisosmed.core.source.local.room.DataBase
import com.amary.sisosmed.core.source.remote.RemoteSource
import com.amary.sisosmed.core.source.remote.network.ApiService
import com.amary.sisosmed.core.source.session.PrefDataStore
import com.amary.sisosmed.domain.repository.IRepository
import com.amary.sisosmed.domain.usecase.Interact
import com.amary.sisosmed.domain.usecase.UseCase
import com.amary.sisosmed.presentation.ui.auth.login.LoginViewModel
import com.amary.sisosmed.presentation.ui.auth.register.RegisterViewModel
import com.amary.sisosmed.presentation.ui.main.detail.DetailViewModel
import com.amary.sisosmed.presentation.ui.main.favorite.FavoriteViewModel
import com.amary.sisosmed.presentation.ui.main.home.HomeViewModel
import com.amary.sisosmed.presentation.ui.main.home.post.PostViewModel
import com.amary.sisosmed.presentation.ui.main.map.MapsViewModel
import com.amary.sisosmed.presentation.ui.main.setting.SettingViewModel
import com.amary.sisosmed.presentation.ui.main.setting.localization.LocalizationViewModel
import com.amary.sisosmed.presentation.ui.splash.SplashViewModel
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        val chuckedInterceptor =  ChuckerInterceptor.Builder(androidContext())
            .collector(ChuckerCollector(androidContext()))
            .maxContentLength(250000L)
            .redactHeaders("Auth-Token", "Bearer")
            .alwaysReadResponseBody(true)
            .build()

        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(chuckedInterceptor)
            .connectTimeout(KeyValue.TIME_NETWORK, TimeUnit.SECONDS)
            .readTimeout(KeyValue.TIME_NETWORK, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val databaseModule = module {
    factory { get<DataBase>().dao() }
    single {
        Room.databaseBuilder(androidContext(), DataBase::class.java, KeyValue.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}

val sessionModule = module {
    single { PrefDataStore(androidContext(), get()) }
}

val repositoryModule = module {
    factory { Dispatchers.IO }
    single { LocalSource(get()) }
    single { RemoteSource(get(), get()) }
    single<IRepository> { Repository(get(), get(), get()) }
}

val useCaseModule = module {
    factory<UseCase> { Interact(get()) }
}

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { SettingViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { PostViewModel(get()) }
    viewModel { LocalizationViewModel(get()) }
    viewModel { MapsViewModel(get()) }
}