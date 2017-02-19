package solstudios.app.moduls.mapviews.MapViewgroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * Created by SolbadguyKY on 16-Feb-17.
 */

public abstract class MyAbsAdapterView<T extends MyAdapter> extends ViewGroup {
    public static final int INVALID_POSITION = -1;
    public static final long INVALID_ROW_ID = -9223372036854775808L;
    public static final int ITEM_VIEW_TYPE_HEADER_OR_FOOTER = -2;
    public static final int ITEM_VIEW_TYPE_IGNORE = -1;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyAbsAdapterView(Context context) {
        super(context);
        throw new RuntimeException("Stub!");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyAbsAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        throw new RuntimeException("Stub!");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyAbsAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        throw new RuntimeException("Stub!");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyAbsAdapterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        throw new RuntimeException("Stub!");
    }

    public final AdapterView.OnItemClickListener getOnItemClickListener() {
        throw new RuntimeException("Stub!");
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        throw new RuntimeException("Stub!");
    }

    public boolean performItemClick(View view, int position, long id) {
        throw new RuntimeException("Stub!");
    }

    public final AdapterView.OnItemLongClickListener getOnItemLongClickListener() {
        throw new RuntimeException("Stub!");
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        throw new RuntimeException("Stub!");
    }

    public final AdapterView.OnItemSelectedListener getOnItemSelectedListener() {
        throw new RuntimeException("Stub!");
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        throw new RuntimeException("Stub!");
    }

    public abstract T getAdapter();

    public abstract void setAdapter(T var1);

    public void addView(View child) {
        throw new RuntimeException("Stub!");
    }

    public void addView(View child, int index) {
        throw new RuntimeException("Stub!");
    }

    public void addView(View child, LayoutParams params) {
        throw new RuntimeException("Stub!");
    }

    public void addView(View child, int index, LayoutParams params) {
        throw new RuntimeException("Stub!");
    }

    public void removeView(View child) {
        throw new RuntimeException("Stub!");
    }

    public void removeViewAt(int index) {
        throw new RuntimeException("Stub!");
    }

    public void removeAllViews() {
        throw new RuntimeException("Stub!");
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.CapturedViewProperty
    public int getSelectedItemPosition() {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.CapturedViewProperty
    public long getSelectedItemId() {
        throw new RuntimeException("Stub!");
    }

    public abstract View getSelectedView();

    public Object getSelectedItem() {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.CapturedViewProperty
    public int getCount() {
        throw new RuntimeException("Stub!");
    }

    public int getPositionForView(View view) {
        throw new RuntimeException("Stub!");
    }

    public int getFirstVisiblePosition() {
        throw new RuntimeException("Stub!");
    }

    public int getLastVisiblePosition() {
        throw new RuntimeException("Stub!");
    }

    public abstract void setSelection(int var1);

    public View getEmptyView() {
        throw new RuntimeException("Stub!");
    }

    public void setEmptyView(View emptyView) {
        throw new RuntimeException("Stub!");
    }

    public void setFocusable(boolean focusable) {
        throw new RuntimeException("Stub!");
    }

    public void setFocusableInTouchMode(boolean focusable) {
        throw new RuntimeException("Stub!");
    }

    public Object getItemAtPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    public long getItemIdAtPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    public void setOnClickListener(OnClickListener l) {
        throw new RuntimeException("Stub!");
    }

    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        throw new RuntimeException("Stub!");
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        throw new RuntimeException("Stub!");
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        throw new RuntimeException("Stub!");
    }

    public CharSequence getAccessibilityClassName() {
        throw new RuntimeException("Stub!");
    }

    protected boolean canAnimate() {
        throw new RuntimeException("Stub!");
    }

    public interface OnItemSelectedListener {
        void onItemSelected(AdapterView<?> var1, View var2, int var3, long var4);

        void onNothingSelected(AdapterView<?> var1);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(AdapterView<?> var1, View var2, int var3, long var4);
    }

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> var1, View var2, int var3, long var4);
    }

    public static class AdapterContextMenuInfo implements ContextMenu.ContextMenuInfo {
        public long id;
        public int position;
        public View targetView;

        public AdapterContextMenuInfo(View targetView, int position, long id) {
            throw new RuntimeException("Stub!");
        }
    }
}
