package com.android.wealth.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Set;

public class VerticalItemDecoration extends RecyclerView.ItemDecoration {

    private final Drawable mDivider;
    private final int mHeight;
    private final Drawable mMarginFiller;
    private final int mMarginLeft;
    private final int mMarginRight;
    private final Set<Integer> mIgnorePosition;
    private final Set<Integer> mReverseIgnorePosition;
    private final Set<Integer> mPosition;
    private final Set<Integer> mReversePosition;

    public VerticalItemDecoration(Drawable divider,
                                  int height,
                                  Drawable marginFiller,
                                  int marginLeft,
                                  int marginRight,
                                  Set<Integer> ignorePosition,
                                  Set<Integer> reverseIgnorePosition,
                                  Set<Integer> position,
                                  Set<Integer> reversePosition) {
        this.mDivider = divider;
        this.mHeight = height;
        this.mMarginFiller = marginFiller;
        this.mMarginLeft = marginLeft;
        this.mMarginRight = marginRight;
        this.mIgnorePosition = ignorePosition;
        this.mReverseIgnorePosition = reverseIgnorePosition;
        this.mPosition = position;
        this.mReversePosition = reversePosition;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if (adapter == null) return;
        int itemCount = adapter.getItemCount();
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (itemPosition < 0) {
            return;
        }
        int itemReversePosition = itemCount - itemPosition - 1;

        if (itemReversePosition == 0) {//最后一个item
            if (!hasIgnore(itemPosition, itemReversePosition) &&
                    !hasEndRowIgnore(itemCount)) {
                outRect.set(0, mHeight, 0, mHeight);
            } else if (!hasIgnore(itemPosition, itemReversePosition) &&
                    hasEndRowIgnore(itemCount)) {
                outRect.set(0, mHeight, 0, 0);
            } else if (hasIgnore(itemPosition, itemReversePosition) &&
                    !hasEndRowIgnore(itemCount)) {
                outRect.set(0, 0, 0, mHeight);
            }
        } else {
            if (!hasIgnore(itemPosition, itemReversePosition)) {
                outRect.set(0, mHeight, 0, 0);
            }
        }
    }

    /**
     * 由于计算还是从上到下计算故返回divider是否绘制还是
     * RecyclerView 最上面是0 所以RecyclerView有多少个item
     * divider就有item+1个
     *
     * @param position        item索引
     * @param reversePosition item逆向索引
     * @return 返回是否绘制divider
     */
    private boolean hasIgnore(int position, int reversePosition) {
        if (mPosition != null) {
            if (!mPosition.contains(position)) {
                return true;
            }
        }
        if (mReversePosition != null) {
            if (!mReversePosition.contains(reversePosition + 1)) {
                return true;
            }
        }
        return mIgnorePosition.contains(position) ||
                mReverseIgnorePosition.contains(reversePosition + 1);
    }

    /**
     * 只判断最后一个的divider
     *
     * @param itemCount adapter 中item的总条数
     */
    private boolean hasEndRowIgnore(int itemCount) {
        if (mPosition != null) {
            if (!mPosition.contains(itemCount)) {
                return true;
            }
        }
        if (mReversePosition != null) {
            if (!mReversePosition.contains(0)) {
                return true;
            }
        }

        return mIgnorePosition.contains(itemCount) ||
                mReverseIgnorePosition.contains(0);
    }

    public int getDividerHeight(int position) {
        if (mIgnorePosition.contains(position)) {
            return 0;
        }
        return mHeight;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (parent.getAdapter() == null) return;

        int itemCount = parent.getAdapter().getItemCount();

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int itemPosition = params.getViewLayoutPosition();
            int itemReversePosition = itemCount - itemPosition - 1;

            if (itemReversePosition == 0) {//最后一个item
                if (!hasIgnore(itemPosition, itemReversePosition) &&
                        !hasEndRowIgnore(itemCount)) {
                    {
                        final int left = child.getLeft() - params.leftMargin + mMarginLeft;
                        final int right = child.getRight() + params.rightMargin - mMarginRight;
                        final int top = child.getTop() + params.bottomMargin - mHeight;
                        final int bottom = top + mHeight;

                        mDivider.setBounds(left, top, right, bottom);
                        mDivider.draw(c);

                        if (mMarginFiller != null) {
                            if (mMarginLeft > 0) {
                                mMarginFiller.setBounds(left - mMarginLeft, top, left, bottom);
                                mMarginFiller.draw(c);
                            }
                            if (mMarginRight > 0) {
                                mMarginFiller.setBounds(right, top, right + mMarginRight, bottom);
                                mMarginFiller.draw(c);
                            }
                        }
                    }

                    {
                        final int left = child.getLeft() - params.leftMargin + mMarginLeft;
                        final int right = child.getRight() + params.rightMargin - mMarginRight;
                        final int top = child.getBottom() + params.bottomMargin;
                        final int bottom = top + mHeight;

                        mDivider.setBounds(left, top, right, bottom);
                        mDivider.draw(c);

                        if (mMarginFiller != null) {
                            if (mMarginLeft > 0) {
                                mMarginFiller.setBounds(left - mMarginLeft, top, left, bottom);
                                mMarginFiller.draw(c);
                            }
                            if (mMarginRight > 0) {
                                mMarginFiller.setBounds(right, top, right + mMarginRight, bottom);
                                mMarginFiller.draw(c);
                            }
                        }
                    }
                } else if (!hasIgnore(itemPosition, itemReversePosition) &&
                        hasEndRowIgnore(itemCount)) {

                    final int left = child.getLeft() - params.leftMargin + mMarginLeft;
                    final int right = child.getRight() + params.rightMargin - mMarginRight;
                    final int top = child.getTop() + params.bottomMargin - mHeight;
                    final int bottom = top + mHeight;

                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);

                    if (mMarginFiller != null) {
                        if (mMarginLeft > 0) {
                            mMarginFiller.setBounds(left - mMarginLeft, top, left, bottom);
                            mMarginFiller.draw(c);
                        }
                        if (mMarginRight > 0) {
                            mMarginFiller.setBounds(right, top, right + mMarginRight, bottom);
                            mMarginFiller.draw(c);
                        }
                    }

                } else if (hasIgnore(itemPosition, itemReversePosition) &&
                        !hasEndRowIgnore(itemCount)) {
                    final int left = child.getLeft() - params.leftMargin + mMarginLeft;
                    final int right = child.getRight() + params.rightMargin - mMarginRight;
                    final int top = child.getBottom() + params.bottomMargin;
                    final int bottom = top + mHeight;

                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);

                    if (mMarginFiller != null) {
                        if (mMarginLeft > 0) {
                            mMarginFiller.setBounds(left - mMarginLeft, top, left, bottom);
                            mMarginFiller.draw(c);
                        }
                        if (mMarginRight > 0) {
                            mMarginFiller.setBounds(right, top, right + mMarginRight, bottom);
                            mMarginFiller.draw(c);
                        }
                    }
                }
            } else {
                if (!hasIgnore(itemPosition, itemReversePosition)) {

                    final int left = child.getLeft() - params.leftMargin + mMarginLeft;
                    final int right = child.getRight() + params.rightMargin - mMarginRight;
                    final int top = child.getTop() + params.bottomMargin - mHeight;
                    final int bottom = top + mHeight;

                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);

                    if (mMarginFiller != null) {
                        if (mMarginLeft > 0) {
                            mMarginFiller.setBounds(left - mMarginLeft, top, left, bottom);
                            mMarginFiller.draw(c);
                        }
                        if (mMarginRight > 0) {
                            mMarginFiller.setBounds(right, top, right + mMarginRight, bottom);
                            mMarginFiller.draw(c);
                        }
                    }
                }
            }
        }
    }

    public static class Builder {

        private Context mContext;
        private Resources mResources;

        private Drawable mDivider;

        private int mHeight;

        private Drawable mMarginFiller;
        private int mMarginLeft;
        private int mMarginRight;

        private final Set<Integer> mIgnorePosition = new HashSet<>(3);
        private final Set<Integer> mReverseIgnorePosition = new HashSet<>(3);

        private Set<Integer> mPosition;
        private Set<Integer> mReversePosition;

        public Builder(Context context) {
            this.mContext = context;
            this.mResources = context.getResources();
            this.mDivider = new ColorDrawable(Color.WHITE);
        }

        /**
         * 通过资源文件设置分隔线颜色
         */
        public Builder setColorResource(@ColorRes int resource) {
            return setColor(ContextCompat.getColor(mContext, resource));
        }

        /**
         * 设置颜色
         */
        public Builder setColor(@ColorInt int color) {
            if (this.mDivider instanceof ColorDrawable) {
                ((ColorDrawable) this.mDivider).setColor(color);
            } else {
                this.mDivider = new ColorDrawable(color);
            }
            return this;
        }

        /**
         * 设置Drawable
         */
        public Builder setDrawable(Drawable divider) {
            this.mDivider = divider;
            return this;
        }

        /**
         * 设置Drawable资源
         */
        public Builder setDrawableResource(@DrawableRes int resId) {
            return setDrawable(ContextCompat.getDrawable(mContext, resId));
        }

        /**
         * 设置高度
         */
        public Builder setHeight(@DimenRes int height) {
            this.mHeight = mResources.getDimensionPixelSize(height);
            return this;
        }

        /**
         * 设置左偏移
         */
        public Builder setMarginLeft(@DimenRes int marginLeft) {
            this.mMarginLeft = mResources.getDimensionPixelSize(marginLeft);
            return this;
        }

        /**
         * 设置右偏移
         */
        public Builder setMarginRight(@DimenRes int marginRight) {
            this.mMarginRight = mResources.getDimensionPixelSize(marginRight);
            return this;
        }

        public Builder setMargin(@DimenRes int margin) {
            return setMarginLeft(margin).setMarginRight(margin);
        }

        public Builder setMarginFillerColorResource(@ColorRes int color) {
            return setMarginFillerColor(ContextCompat.getColor(mContext, color));
        }

        public Builder setMarginFillerColor(@ColorInt int color) {
            this.mMarginFiller = new ColorDrawable(color);
            return this;
        }

        /**
         * 不设置的话代表所有的position都回执,设置了只绘制设置的
         */
        public Builder setPosition(int... position) {
            if (position != null) {
                if (mPosition == null) {
                    mPosition = new HashSet<>();
                }
                for (int p : position) {
                    this.mPosition.add(p);
                }
            }
            return this;
        }

        /**
         * 不设置的话代表所有的position都回执，设置了只绘制设置的
         */
        public Builder setReversePosition(int... position) {
            if (position != null) {
                if (mReversePosition == null) {
                    mReversePosition = new HashSet<>();
                }
                for (int p : position) {
                    this.mReversePosition.add(p);
                }
            }
            return this;
        }

        /**
         * 设置不绘制的倒叙索引
         */
        public Builder setReverseIgnorePosition(int... position) {
            if (position != null) {
                for (int p : position) {
                    this.mReverseIgnorePosition.add(p);
                }
            }
            return this;
        }

        /**
         * 设置不绘制的索引
         */
        public Builder setIgnorePosition(int... position) {
            if (position != null) {
                for (int p : position) {
                    this.mIgnorePosition.add(p);
                }
            }
            return this;
        }

        public Builder setDefIgnorePosition() {
            return setIgnorePosition(0).setReverseIgnorePosition(0);
        }

        public VerticalItemDecoration build() {
            return new VerticalItemDecoration(mDivider,
                    mHeight,
                    mMarginFiller, mMarginLeft, mMarginRight,
                    mIgnorePosition, mReverseIgnorePosition,
                    mPosition, mReversePosition);
        }
    }

}
