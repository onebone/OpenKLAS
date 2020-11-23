package org.openklas.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;


import org.openklas.R;
import org.openklas.base.impl.ActivityInterface;
import org.openklas.base.impl.AlertInterface;
import org.openklas.databinding.MenuViewBinding;

import pyxis.uzuki.live.attribute.parser.annotation.CustomView;

/**
 * FoodDiary
 * Class: MenuView
 * Created by limmoong on 2020-08-01.
 * <p>
 * Description:
 */

@CustomView
public class MenuView extends LinearLayout implements ActivityInterface, AlertInterface {
    private MenuViewBinding mBinding;

    public MenuView(Context context) {
        super(context);
        init(null);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (isInEditMode()) {
            inflate(getContext(), R.layout.menu_view, this);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        mBinding = DataBindingUtil.inflate(inflater, R.layout.menu_view, this, true);
        mBinding.setView(this);

//      if (attrs != null) {
//          TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MenuView);
//          MenuViewAttributes.apply(this, array);
//      }

        mBinding.notifyChange();
    }
}