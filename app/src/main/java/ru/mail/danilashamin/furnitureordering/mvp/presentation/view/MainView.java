package ru.mail.danilashamin.furnitureordering.mvp.presentation.view;

import android.graphics.drawable.Drawable;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.mail.danilashamin.furnitureordering.mvp.model.Furniture;

public interface MainView extends MvpView {
    @StateStrategyType(AddToEndStrategy.class)
    void addFurnitureOnScreen(Furniture furniture);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSingleUnitModuleCounter(Integer singleUnitModuleCounter);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setFourUnitModuleCounter(Integer fourUnitModuleCounter);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setEightUnitModuleCounter(Integer eightUnitModuleCounter);
    
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setPillowCounter(Integer pillowCounter);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setBackgroundPhoto(Drawable photo);

    @StateStrategyType(AddToEndStrategy.class)
    void deleteFurnitureView(Furniture furnitureForDelete);

    @StateStrategyType(AddToEndStrategy.class)
    void showColorPickerDialog();

    @StateStrategyType(AddToEndStrategy.class)
    void dismissColorPickerDialog();

    @StateStrategyType(AddToEndStrategy.class)
    void changeCurrentFurnitureColor(int color, Furniture currentFurniture);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void startCamera();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void stopCamera();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void deleteAllFurniture();

    @StateStrategyType(AddToEndStrategy.class)
    void setCurrentFurniture(Furniture currentFurniture);

    @StateStrategyType(AddToEndStrategy.class)
    void unsetCurrentFurniture(Furniture currentFurniture);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setPrice(double price);
}
