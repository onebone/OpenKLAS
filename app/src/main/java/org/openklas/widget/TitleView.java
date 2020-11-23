package org.openklas.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;

import com.github.windsekirun.bindadapters.observable.ObservableString;

import org.greenrobot.eventbus.EventBus;
import org.openklas.R;
import org.openklas.databinding.TitleViewBinding;
import org.openklas.event.ControlDrawerEvent;
import org.openklas.event.Mode;

import pyxis.uzuki.live.attribute.parser.TitleViewAttributes;
import pyxis.uzuki.live.attribute.parser.annotation.AttrInt;
import pyxis.uzuki.live.attribute.parser.annotation.AttrString;
import pyxis.uzuki.live.attribute.parser.annotation.CustomView;

/**
 * OpenKlas
 * Class: TitleView
 * Created by limmoong on 2020/11/23.
 * <p>
 * Description:
 */

@CustomView
public class TitleView extends LinearLayout {
	@AttrString
	public String titleName;
	@AttrInt
	public int titleMode;
	private TitleViewBinding mBinding;

	private OnClickBackListener mOnClickBackListener;
	private OnClickMypageListener mOnClickMypageListner;

	public ObservableString mTitle = new ObservableString("");
	public ObservableInt mMode = new ObservableInt(0);

	public TitleView(Context context) {
		super(context);
		init(null);
	}

	public TitleView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public void clickBack(View view) {
		if (mOnClickBackListener != null) {
			mOnClickBackListener.onClickBack(view);
		}
//		else {
//			BaseActivity activity = (BaseActivity) getContext();
//			activity.finish();
//		}
	}

	public void clickMypage(View view) {
		if (mOnClickMypageListner != null) {
			mOnClickMypageListner.onClickMypage(view);
		}
		else {
			EventBus.getDefault().post(new ControlDrawerEvent(Mode.Open));
		}
	}

	public void setOnClickBackListener(OnClickBackListener onClickBackListener) {
		mOnClickBackListener = onClickBackListener;
	}

	public void setOnClickMypageListner(OnClickMypageListener onClickMypageListner) {
		mOnClickMypageListner = onClickMypageListner;
	}

	private void init(AttributeSet attrs) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		mBinding = DataBindingUtil.inflate(inflater, R.layout.title_view, this, true);
		mBinding.setView(this);
		if (attrs != null) {
			TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
			TitleViewAttributes.apply(this, array);
		}

		if (titleName != null)
			mTitle.set(titleName);
		mMode.set(titleMode);

		mBinding.notifyChange();
	}

	public void setTitle(String title) {
		mTitle.set(title);
	}

	public void setMode(int mode) {
		mMode.set(mode);
	}

	@BindingAdapter("bindTitle")
	public static void bindTitleName(TitleView view, String title) {
		view.setTitle(title);
	}

	@BindingAdapter("bindMode")
	public static void bindTitleMode(TitleView view, int mode) {
		view.setMode(mode);
	}

	@BindingAdapter("onClickBack")
	public static void bindClickBack(TitleView view, OnClickBackListener listener) {
		view.setOnClickBackListener(listener);
	}

	@BindingAdapter("onClickMypage")
	public static void bindClickBack(TitleView view, OnClickMypageListener listener) {
		view.setOnClickMypageListner(listener);
	}

	public interface OnClickBackListener {
		void onClickBack(View view);
	}

	public interface OnClickMypageListener {
		void onClickMypage(View view);
	}
}
