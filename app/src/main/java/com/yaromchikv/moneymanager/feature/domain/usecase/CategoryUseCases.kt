package com.yaromchikv.moneymanager.feature.domain.usecase

import com.yaromchikv.moneymanager.feature.domain.model.Category
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository
import com.yaromchikv.moneymanager.feature.domain.repository.CategoriesRepository
import kotlinx.coroutines.flow.Flow

data class CategoryUseCases(
    val getCategories: GetCategories,
    val getCategory: GetCategory,
    val addCategory: AddCategory,
    val updateCategory: UpdateCategory,
    val deleteCategory: DeleteCategory,
)

class GetCategories(private val repository: CategoriesRepository) {
    operator fun invoke(): Flow<List<Category>> {
        return repository.getCategories()
    }
}

class GetCategory(private val repository: CategoriesRepository) {
    suspend operator fun invoke(id: Int): Category? {
        return repository.getCategoryById(id)
    }
}

class AddCategory(private val repository: CategoriesRepository) {
    suspend operator fun invoke(category: Category) {
        repository.insertCategory(category)
    }
}

class UpdateCategory(private val repository: CategoriesRepository) {
    suspend operator fun invoke(category: Category) {
        repository.updateCategory(category)
    }
}

class DeleteCategory(private val repository: CategoriesRepository) {
    suspend operator fun invoke(category: Category) {
        repository.deleteCategory(category);
    }
}