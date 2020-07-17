package com.one.fruitmanbuyer

import android.app.Application
import com.one.fruitmanbuyer.repositories.BuyerRepository
import com.one.fruitmanbuyer.repositories.OrderRepository
import com.one.fruitmanbuyer.repositories.ProductRepository
import com.one.fruitmanbuyer.ui.detail_product.DetailProductViewModel
import com.one.fruitmanbuyer.ui.login.LoginViewModel
import com.one.fruitmanbuyer.ui.main.timeline.TimelineFragment
import com.one.fruitmanbuyer.ui.main.timeline.TimelineViewModel
import com.one.fruitmanbuyer.ui.register.RegisterViewModel
import com.one.fruitmanbuyer.webservices.ApiClient
import io.fotoapparat.selector.single
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            modules(listOf(repositoryModules, viewModelModules, retrofitModule))
        }
    }
}

val retrofitModule = module {
    single { ApiClient.instance() }
}

val repositoryModules = module {
    factory { BuyerRepository(get()) }
    factory { ProductRepository(get()) }
    factory { OrderRepository(get()) }
}

val viewModelModules = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { DetailProductViewModel(get()) }

    viewModel { TimelineViewModel(get()) }
}