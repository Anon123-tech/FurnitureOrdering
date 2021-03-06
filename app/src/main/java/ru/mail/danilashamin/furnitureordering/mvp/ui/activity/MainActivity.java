package ru.mail.danilashamin.furnitureordering.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mail.danilashamin.furnitureordering.R;
import ru.mail.danilashamin.furnitureordering.mvp.model.Furniture;
import ru.mail.danilashamin.furnitureordering.mvp.model.FurnitureType;
import ru.mail.danilashamin.furnitureordering.mvp.model.ZodiacSign;
import ru.mail.danilashamin.furnitureordering.mvp.presentation.presenter.MainPresenter;
import ru.mail.danilashamin.furnitureordering.mvp.presentation.view.FurnitureView;
import ru.mail.danilashamin.furnitureordering.mvp.presentation.view.MainView;
import ru.mail.danilashamin.furnitureordering.mvp.presentation.view.dialog.ColorPickerDialog;
import ru.mail.danilashamin.furnitureordering.mvp.presentation.view.dialog.ColorPickerDialogListener;
import ru.mail.danilashamin.furnitureordering.mvp.presentation.view.dialog.DialogEnterPhoneNumber;
import ru.mail.danilashamin.furnitureordering.mvp.presentation.view.dialog.DialogEnterPhoneNumberListener;
import ru.mail.danilashamin.furnitureordering.mvp.presentation.view.dialog.FitoPickerDialog;
import ru.mail.danilashamin.furnitureordering.mvp.presentation.view.dialog.FitoPickerDialogListener;

import static ru.mail.danilashamin.furnitureordering.mvp.model.Constants.EMAIL.MAILTO;
import static ru.mail.danilashamin.furnitureordering.mvp.model.Constants.EMAIL.PASSWORD;
import static ru.mail.danilashamin.furnitureordering.mvp.model.Constants.EMAIL.SUBJECT;
import static ru.mail.danilashamin.furnitureordering.mvp.model.Constants.EMAIL.USERNAME;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter
    MainPresenter mainPresenter;

    @BindView(R.id.photoView)
    CameraView photoView;
    @BindView(R.id.tvSingleUnitModuleCounter)
    TextView tvSingleUnitModuleCounter;
    @BindView(R.id.btnAddSingleModuleUnit)
    ImageView btnAddSingleModuleUnit;
    @BindView(R.id.tvFourModuleUnitCounter)
    TextView tvFourModuleUnitCounter;
    @BindView(R.id.btnAddFourModuleUnit)
    ImageView btnAddFourModuleUnit;
    @BindView(R.id.tvEightModuleUnitCounter)
    TextView tvEightModuleUnitCounter;
    @BindView(R.id.btnAddEightModuleUnit)
    ImageView btnAddEightModuleUnit;
    @BindView(R.id.btnBuy)
    Button btnBuy;
    @BindView(R.id.ivTrashCan)
    ImageView ivTrashCan;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.btnColorPick)
    ImageView btnColorPick;
    @BindView(R.id.btnCamera)
    ImageView btnCamera;
    @BindView(R.id.fieldForFurniture)
    FrameLayout fieldForFurniture;
    @BindView(R.id.tvPillowCounter)
    TextView tvPillowCounter;
    @BindView(R.id.btnAddPillow)
    ImageView btnAddPillow;

    private ColorPickerDialog colorPickerDialog;
    private FitoPickerDialog fitoPickerDialog;
    private DialogEnterPhoneNumber enterPhoneNumberDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initDialogs();
        initPhotoView();
        initFurnitureFieldView();
    }

    public void initDialogs() {
        initColorPickerDialog();
        initFitoPickerDialog();
        initPhoneNumberDialog();
    }

    public void initColorPickerDialog() {
        colorPickerDialog = new ColorPickerDialog(this, new ColorPickerDialogListener() {
            @Override
            public void onColorPicked(int color, String article) {
                mainPresenter.changeCurrentFurnitureColor(color, article);
                mainPresenter.dismissColorPickerDialog();
            }

            @Override
            public void onDismissButtonClicked() {
                mainPresenter.dismissColorPickerDialog();
            }
        });
    }

    public void initFitoPickerDialog() {
        fitoPickerDialog = new FitoPickerDialog(this, new FitoPickerDialogListener() {
            @Override
            public void onFitoPicked(ZodiacSign sign) {
                mainPresenter.setFitoOnCurrentFurniture(sign);
                mainPresenter.dismissSelectFitoDialog();
            }

            @Override
            public void onDismiss() {
                mainPresenter.dismissSelectFitoDialog();
            }
        });
    }

    public void initPhoneNumberDialog() {
        enterPhoneNumberDialog = new DialogEnterPhoneNumber(this, new DialogEnterPhoneNumberListener() {
            @Override
            public void onMakeOrder(@NonNull String phoneNumber) {
                mainPresenter.buy(phoneNumber);
                mainPresenter.dismissEnterPhoneNumberDialog();
            }

            @Override
            public void onDismiss() {
                mainPresenter.dismissEnterPhoneNumberDialog();
            }
        });
    }

    public void initPhotoView() {
        photoView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] jpeg) {
                CameraUtils.decodeBitmap(jpeg,
                        bitmap -> {
                            mainPresenter.stopCamera();
                            mainPresenter.setBackgroundPhoto(new BitmapDrawable(getResources(), bitmap));
                        });
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initFurnitureFieldView() {
        fieldForFurniture.setOnTouchListener((v, event) -> {
            mainPresenter.unsetCurrentFurniture();
            return true;
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        photoView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        photoView.destroy();
    }

    @Override
    public void addFurnitureOnScreen(Furniture furniture) {
        FurnitureView furnitureView = new FurnitureView(this, furniture);
        furnitureView.setOnTouchListener(new OnFurnitureTouchListener(furniture));
        fieldForFurniture.addView(furnitureView);
        mainPresenter.setCurrentFurniture(furniture);
    }

    @Override
    public void setSingleUnitModuleCounter(Integer singleUnitModuleCounter) {
        tvSingleUnitModuleCounter.setText(String.valueOf(singleUnitModuleCounter));
    }

    @Override
    public void setFourUnitModuleCounter(Integer fourUnitModuleCounter) {
        tvFourModuleUnitCounter.setText(String.valueOf(fourUnitModuleCounter));
    }

    @Override
    public void setEightUnitModuleCounter(Integer eightUnitModuleCounter) {
        tvEightModuleUnitCounter.setText(String.valueOf(eightUnitModuleCounter));
    }

    @Override
    public void setPillowCounter(Integer pillowCounter) {
        tvPillowCounter.setText(String.valueOf(pillowCounter));
    }

    @Override
    public void setBackgroundPhoto(Drawable photo) {
        photoView.setBackground(photo);
    }


    @Override
    public void deleteFurnitureView(Furniture furnitureForDelete) {
        fieldForFurniture.removeView(findFurnitureView(furnitureForDelete));
        ivTrashCan.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_forever));
    }

    private FurnitureView findFurnitureView(Furniture furniture) {
        return fieldForFurniture.findViewWithTag(furniture.getId());
    }

    @Override
    public void showColorPickerDialog() {
        colorPickerDialog.show();
    }

    @Override
    public void dismissColorPickerDialog() {
        colorPickerDialog.dismiss();
    }

    @Override
    public void changeCurrentFurnitureColor(int color, Furniture currentFurniture) {
        findFurnitureView(currentFurniture).setColorFilterOnBitmap(color);
    }

    @Override
    public void startCamera() {
        photoView.start();
        btnCamera.setOnClickListener(v -> {
                    photoView.capturePicture();
                    btnCamera.setOnClickListener(v1 -> mainPresenter.startCamera());
                }
        );
    }

    @Override
    public void stopCamera() {
        photoView.stop();
    }

    @Override
    public void deleteAllFurniture() {
        fieldForFurniture.removeAllViews();
    }

    @Override
    public void setCurrentFurniture(Furniture currentFurniture) {
        FurnitureView view = findFurnitureView(currentFurniture);
        if (view != null) {
            view.setCurrent();
        }
    }

    @Override
    public void unsetCurrentFurniture(Furniture currentFurniture) {
        FurnitureView view = findFurnitureView(currentFurniture);
        if (view != null) {
            view.unsetCurrent();
        }
    }

    @Override
    public void setPrice(double price) {
        tvPrice.setText(String.format("\u20BD%s", String.valueOf(price)));
    }

    @Override
    public void buy(String order) {
        BackgroundMail.newBuilder(this)
                .withUsername(USERNAME)
                .withPassword(PASSWORD)
                .withMailto(MAILTO)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject(SUBJECT)
                .withBody(order)
                .withOnSuccessCallback(() -> Toast.makeText(this, getString(R.string.email_succcess), Toast.LENGTH_LONG).show())
                .withOnFailCallback(() -> Toast.makeText(this, getString(R.string.email_fail), Toast.LENGTH_LONG).show())
                .send();
    }


    @Override
    public void showFitoPickerDialog(ZodiacSign currentFurnitureZodiacSign) {
        fitoPickerDialog.show(currentFurnitureZodiacSign);
    }

    @Override
    public void dismissFitoPickerDialog() {
        fitoPickerDialog.dismiss();
    }

    @Override
    public void showPhoneNumberDialog() {
        enterPhoneNumberDialog.show();
    }

    @Override
    public void dismissPhoneNumberDialog() {
        enterPhoneNumberDialog.dismiss();
    }

    @Override
    public void changeFitoOnCurrentFurnitureView(Furniture currentFurniture, boolean selected) {
        FurnitureView view = findFurnitureView(currentFurniture);
        if (view != null) {
            view.setFitoSelected(selected);
        }
    }

    @Override
    public void showEmptyOrderMessage() {
        Toast.makeText(this, getString(R.string.order_empty), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeFurniturePicture(Furniture furniture) {
        findFurnitureView(furniture).changePicture();
        //vibrate();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }

    @OnClick(R.id.btnBuy)
    public void onBtnBuyClicked() {
        mainPresenter.showEnterPhoneNumberDialog();
    }


    @OnClick(R.id.btnColorPick)
    public void onBtnColorPickClicked() {
        mainPresenter.showColorPickerDialog();
    }

    @OnClick(R.id.btnCamera)
    public void onCameraBtnClicked() {
        mainPresenter.startCamera();
    }

    @OnClick(R.id.ivTrashCan)
    public void onTrashCanClicked() {
        mainPresenter.deleteAllFurniture();
    }

    @OnClick({R.id.btnAddSingleModuleUnit, R.id.btnAddFourModuleUnit, R.id.btnAddEightModuleUnit, R.id.btnAddPillow})
    public void onAddButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.btnAddSingleModuleUnit:
                mainPresenter.addFurniture(FurnitureType.SINGLE_UNIT_MODULE);
                break;
            case R.id.btnAddFourModuleUnit:
                mainPresenter.addFurniture(FurnitureType.DOUBLE_UNIT_MODULE);
                break;
            case R.id.btnAddEightModuleUnit:
                mainPresenter.addFurniture(FurnitureType.TRIPLE_UNIT_MODULE);
                break;
            case R.id.btnAddPillow:
                mainPresenter.addFurniture(FurnitureType.FOURTH_MODULE_UNIT);
                break;
        }
    }

    @OnClick(R.id.btnSelectFito)
    public void onbtnSelectFitoClicked() {
        mainPresenter.showSelectFitoDialog();
    }


    public class OnFurnitureTouchListener implements View.OnTouchListener {
        private int _xDelta;
        private int _yDelta;
        private Furniture furniture;
        private GestureDetector gestureDetector;

        OnFurnitureTouchListener(Furniture furniture) {
            this.furniture = furniture;
            initGestureDetector();
        }

        void initGestureDetector() {
            gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    mainPresenter.changeFurniturePicture(furniture);
                    return super.onDoubleTap(e);
                }
            });
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:

                    FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) v.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;

                    mainPresenter.setCurrentFurniture(((FurnitureView) v).getFurniture());
                    break;
                case MotionEvent.ACTION_UP:
                    if (doViewsIntersect(v, ivTrashCan)) {
                        mainPresenter.deleteFurniture(((FurnitureView) v).getFurniture());
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    FrameLayout.LayoutParams layoutParams = setLayoutParams(X, Y, v);
                    furniture.setLayoutParams(layoutParams);
                    v.setLayoutParams(layoutParams);
                    changeTrashCanIcon(v);
                    break;
            }

            return true;
        }

        private FrameLayout.LayoutParams setLayoutParams(int X, int Y, View v) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) v.getLayoutParams();
            layoutParams.leftMargin = X - _xDelta;
            layoutParams.topMargin = Y - _yDelta;
            return layoutParams;
        }
    }

    public void changeTrashCanIcon(View v) {
        if (doViewsIntersect(v, ivTrashCan)) {
            ivTrashCan.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_forever_activated));
        } else {
            ivTrashCan.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_forever));
        }
    }

    private boolean doViewsIntersect(View dropTarget, View item) {
        Rect dropRect = getScreenBounds(dropTarget);
        Rect itemRect = getScreenBounds(item);
        return Rect.intersects(dropRect, itemRect);
    }

    private Rect getScreenBounds(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Rect(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight());
    }

}
