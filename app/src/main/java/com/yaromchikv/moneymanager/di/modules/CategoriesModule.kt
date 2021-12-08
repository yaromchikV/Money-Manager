package com.yaromchikv.moneymanager.di.modules

import com.yaromchikv.moneymanager.feature.data.datasource.MoneyManagerDatabase
import com.yaromchikv.moneymanager.feature.data.repository.CategoriesRepositoryImpl
import com.yaromchikv.moneymanager.feature.domain.repository.CategoriesRepository
import com.yaromchikv.moneymanager.feature.domain.usecases.GetCategoryViewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CategoriesModule {

    @Provides
    @Singleton
    fun provideCategoriesRepository(db: MoneyManagerDatabase): CategoriesRepository =
        CategoriesRepositoryImpl(db.categoriesDao)


    @Provides
    @Singleton
    fun provideGetCategoryViewsUseCase(repository: CategoriesRepository): GetCategoryViewsUseCase =
        GetCategoryViewsUseCase(repository)

}