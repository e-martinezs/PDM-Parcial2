package com.example.pdmparcial2.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.pdmparcial2.database.NewsRoomDatabase;
import com.example.pdmparcial2.database.daos.CategoryDao;
import com.example.pdmparcial2.model.Category;

import java.util.List;

public class CategoryRepository {

    private CategoryDao categoryDao;
    private LiveData<List<Category>> categories;

    public CategoryRepository(Application application){
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        categoryDao = db.categoryDao();
        categories = categoryDao.getCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void insertCategories(List<Category> categories) {
        new insertCategoriesAsyncTask(categoryDao).execute(categories);
    }


    private class insertCategoriesAsyncTask extends AsyncTask<List<Category>, Void, Void> {
        private CategoryDao categoryDao;

        public insertCategoriesAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(List<Category>... categories) {
            categoryDao.deleteCategories();
            for (Category c : categories[0]) {
                categoryDao.insertCategory(c);
            }
            return null;
        }
    }
}
