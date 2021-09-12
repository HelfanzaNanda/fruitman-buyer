package com.one.fruitmanbuyer

import android.app.Application
import com.one.fruitmanbuyer.repositories.*
import com.one.fruitmanbuyer.ui.complete.CompleteViewModel
import com.one.fruitmanbuyer.ui.detail_product.DetailProductViewModel
import com.one.fruitmanbuyer.ui.forget_password.ForgotPasswordViewModel
import com.one.fruitmanbuyer.ui.in_progress.InProgressViewModel
import com.one.fruitmanbuyer.ui.login.LoginViewModel
import com.one.fruitmanbuyer.ui.main.profile.ProfileViewModel
import com.one.fruitmanbuyer.ui.main.timeline.TimelineFragment
import com.one.fruitmanbuyer.ui.main.timeline.TimelineViewModel
import com.one.fruitmanbuyer.ui.order_in.OrderInViewModel
import com.one.fruitmanbuyer.ui.premium.PremiumViewModel
import com.one.fruitmanbuyer.ui.register.RegisterViewModel
import com.one.fruitmanbuyer.ui.report.ReportViewModel
import com.one.fruitmanbuyer.ui.update_password.UpdatePasswordViewModel
import com.one.fruitmanbuyer.ui.update_profil.UpdateProfilViewModel
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
    single { FirebaseRepository() }
}

val repositoryModules = module {
    factory { BankRepository(get()) }
    factory { BuyerRepository(get()) }
    factory { ProductRepository(get()) }
    factory { OrderRepository(get()) }
    factory { FruitRepository(get()) }
    factory { SubDistrictRepository(get()) }
    factory { ReportRepository(get()) }
}

val viewModelModules = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { DetailProductViewModel(get()) }

    viewModel { OrderInViewModel(get()) }
    viewModel { InProgressViewModel(get()) }
    viewModel { CompleteViewModel(get()) }
    viewModel { ForgotPasswordViewModel(get()) }

    viewModel { TimelineViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { UpdateProfilViewModel(get()) }

    viewModel { UpdatePasswordViewModel(get()) }

    viewModel { PremiumViewModel(get(), get()) }

    viewModel { ReportViewModel(get()) }
}