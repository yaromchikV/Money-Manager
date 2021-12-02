package com.yaromchikv.moneymanager.di.modules

import com.yaromchikv.moneymanager.feature.data.datasource.MoneyManagerDatabase
import com.yaromchikv.moneymanager.feature.data.repository.CategoriesRepositoryImpl
import com.yaromchikv.moneymanager.feature.domain.repository.CategoriesRepository
import com.yaromchikv.moneymanager.feature.domain.usecase.CategoryUseCases
import com.yaromchikv.moneymanager.feature.domain.usecase.GetCategoryViews
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
    fun provideCategoriesRepository(db: MoneyManagerDatabase): CategoriesRepository {
        return CategoriesRepositoryImpl(db.categoriesDao)
    }

    @Provides
    @Singleton
    fun provideCategoriesUseCases(repository: CategoriesRepository): CategoryUseCases {
        return CategoryUseCases(
            getCategoryViews = GetCategoryViews(repository)
        )
    }
}