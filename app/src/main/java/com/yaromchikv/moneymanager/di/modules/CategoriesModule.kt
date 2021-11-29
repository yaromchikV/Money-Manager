package com.yaromchikv.moneymanager.di.modules

import com.yaromchikv.moneymanager.feature.data.datasource.MoneyManagerDatabase
import com.yaromchikv.moneymanager.feature.data.repository.CategoriesRepositoryImpl
import com.yaromchikv.moneymanager.feature.domain.repository.CategoriesRepository
import com.yaromchikv.moneymanager.feature.domain.usecase.AddCategory
import com.yaromchikv.moneymanager.feature.domain.usecase.CategoryUseCases
import com.yaromchikv.moneymanager.feature.domain.usecase.DeleteCategory
import com.yaromchikv.moneymanager.feature.domain.usecase.GetCategories
import com.yaromchikv.moneymanager.feature.domain.usecase.GetCategory
import com.yaromchikv.moneymanager.feature.domain.usecase.UpdateCategory
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
            getCategories = GetCategories(repository),
            getCategory = GetCategory(repository),
            addCategory = AddCategory(repository),
            updateCategory = UpdateCategory(repository),
            deleteCategory = DeleteCategory(repository)
        )
    }
}