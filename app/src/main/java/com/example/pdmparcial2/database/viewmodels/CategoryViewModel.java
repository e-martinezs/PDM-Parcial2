package com.example.pdmparcial2.database.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.pdmparcial2.database.repositories.CategoryRepository;
import com.example.pdmparcial2.model.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel{

    private CategoryRepository repository;
    private LiveData<List<Category>> categories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repository = new CategoryRepository(application);
        categories = repository.getCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void insertCategories(List<Category> categories){
        repository.insertCategories(categories);
    }
}
