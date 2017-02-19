package solstudios.app.moduls.mapviews.MapViewgroup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.AbsListView;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SolbadguyKY on 16-Feb-17.
 */

public abstract class MyAbsMapView extends MyAbsAdapterView<MyListAdapter> implements TextWatcher, ViewTreeObserver.OnGlobalLayoutListener, Filter.FilterListener, ViewTreeObserver.OnTouchModeChangeListener {
    public static final int CHOICE_MODE_MULTIPLE = 2;
    public static final int CHOICE_MODE_MULTIPLE_MODAL = 3;
    public static final int CHOICE_MODE_NONE = 0;
    public static final int CHOICE_MODE_SINGLE = 1;
    public static final int TRANSCRIPT_MODE_ALWAYS_SCROLL = 2;
    public static final int TRANSCRIPT_MODE_DISABLED = 0;
    public static final int TRANSCRIPT_MODE_NORMAL = 1;

    public MyAbsMapView(Context context) {
        super(context, null, 0, 0);
        throw new RuntimeException("Stub!");
    }

    public MyAbsMapView(Context context, AttributeSet attrs) {
        super(context, attrs, 0, 0);
        throw new RuntimeException("Stub!");
    }

    public MyAbsMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
        throw new RuntimeException("Stub!");
    }

    public MyAbsMapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        throw new RuntimeException("Stub!");
    }

    public void setOverScrollMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    public void setAdapter(MyListAdapter adapter) {

        throw new RuntimeException("Stub!");
    }

    public int getCheckedItemCount() {
        throw new RuntimeException("Stub!");
    }

    public boolean isItemChecked(int position) {
        throw new RuntimeException("Stub!");
    }

    public int getCheckedItemPosition() {
        throw new RuntimeException("Stub!");
    }

    public SparseBooleanArray getCheckedItemPositions() {
        throw new RuntimeException("Stub!");
    }

    public long[] getCheckedItemIds() {
        throw new RuntimeException("Stub!");
    }

    public void clearChoices() {
        throw new RuntimeException("Stub!");
    }

    public void setItemChecked(int position, boolean value) {
        throw new RuntimeException("Stub!");
    }

    public boolean performItemClick(View view, int position, long id) {
        throw new RuntimeException("Stub!");
    }

    public int getChoiceMode() {
        throw new RuntimeException("Stub!");
    }

    public void setChoiceMode(int choiceMode) {
        throw new RuntimeException("Stub!");
    }

    public void setMultiChoiceModeListener(AbsListView.MultiChoiceModeListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setFastScrollStyle(int styleResId) {
        throw new RuntimeException("Stub!");
    }

    public boolean isFastScrollAlwaysVisible() {
        throw new RuntimeException("Stub!");
    }

    public void setFastScrollAlwaysVisible(boolean alwaysShow) {
        throw new RuntimeException("Stub!");
    }

    public int getVerticalScrollbarWidth() {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty
    public boolean isFastScrollEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setFastScrollEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public void setVerticalScrollbarPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    public void setScrollBarStyle(int style) {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty
    public boolean isSmoothScrollbarEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setSmoothScrollbarEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getAccessibilityClassName() {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty
    public boolean isScrollingCacheEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setScrollingCacheEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty
    public boolean isTextFilterEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setTextFilterEnabled(boolean textFilterEnabled) {
        throw new RuntimeException("Stub!");
    }

    public void getFocusedRect(Rect r) {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty
    public boolean isStackFromBottom() {
        throw new RuntimeException("Stub!");
    }

    public void setStackFromBottom(boolean stackFromBottom) {
        throw new RuntimeException("Stub!");
    }

    public Parcelable onSaveInstanceState() {
        throw new RuntimeException("Stub!");
    }

    public void onRestoreInstanceState(Parcelable state) {
        throw new RuntimeException("Stub!");
    }

    public void setFilterText(String filterText) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getTextFilter() {
        throw new RuntimeException("Stub!");
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        throw new RuntimeException("Stub!");
    }

    public void requestLayout() {
        throw new RuntimeException("Stub!");
    }

    protected int computeVerticalScrollExtent() {
        throw new RuntimeException("Stub!");
    }

    protected int computeVerticalScrollOffset() {
        throw new RuntimeException("Stub!");
    }

    protected int computeVerticalScrollRange() {
        throw new RuntimeException("Stub!");
    }

    protected float getTopFadingEdgeStrength() {
        throw new RuntimeException("Stub!");
    }

    protected float getBottomFadingEdgeStrength() {
        throw new RuntimeException("Stub!");
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        throw new RuntimeException("Stub!");
    }

    protected void layoutChildren() {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty
    public View getSelectedView() {
        throw new RuntimeException("Stub!");
    }

    public int getListPaddingTop() {
        throw new RuntimeException("Stub!");
    }

    public int getListPaddingBottom() {
        throw new RuntimeException("Stub!");
    }

    public int getListPaddingLeft() {
        throw new RuntimeException("Stub!");
    }

    public int getListPaddingRight() {
        throw new RuntimeException("Stub!");
    }

    public void onInitializeAccessibilityNodeInfoForItem(View view, int position, AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }

    protected void dispatchDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    protected boolean isPaddingOffsetRequired() {
        throw new RuntimeException("Stub!");
    }

    protected int getLeftPaddingOffset() {
        throw new RuntimeException("Stub!");
    }

    protected int getTopPaddingOffset() {
        throw new RuntimeException("Stub!");
    }

    protected int getRightPaddingOffset() {
        throw new RuntimeException("Stub!");
    }

    protected int getBottomPaddingOffset() {
        throw new RuntimeException("Stub!");
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        throw new RuntimeException("Stub!");
    }

    public void setDrawSelectorOnTop(boolean onTop) {
        throw new RuntimeException("Stub!");
    }

    public void setSelector(int resID) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getSelector() {
        throw new RuntimeException("Stub!");
    }

    public void setSelector(Drawable sel) {
        throw new RuntimeException("Stub!");
    }

    public void setScrollIndicators(View up, View down) {
        throw new RuntimeException("Stub!");
    }

    protected void drawableStateChanged() {
        throw new RuntimeException("Stub!");
    }

    public boolean verifyDrawable(Drawable dr) {
        throw new RuntimeException("Stub!");
    }

    public void jumpDrawablesToCurrentState() {
        throw new RuntimeException("Stub!");
    }

    protected void onAttachedToWindow() {
        throw new RuntimeException("Stub!");
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        throw new RuntimeException("Stub!");
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        throw new RuntimeException("Stub!");
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        throw new RuntimeException("Stub!");
    }

    public void onCancelPendingInputEvents() {
        throw new RuntimeException("Stub!");
    }

    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        throw new RuntimeException("Stub!");
    }

    public boolean showContextMenu() {
        throw new RuntimeException("Stub!");
    }

    public boolean showContextMenu(float x, float y) {
        throw new RuntimeException("Stub!");
    }

    public boolean showContextMenuForChild(View originalView) {
        throw new RuntimeException("Stub!");
    }

    public boolean showContextMenuForChild(View originalView, float x, float y) {
        throw new RuntimeException("Stub!");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    protected void dispatchSetPressed(boolean pressed) {
        throw new RuntimeException("Stub!");
    }

    public void dispatchDrawableHotspotChanged(float x, float y) {
        throw new RuntimeException("Stub!");
    }

    public int pointToPosition(int x, int y) {
        throw new RuntimeException("Stub!");
    }

    public long pointToRowId(int x, int y) {
        throw new RuntimeException("Stub!");
    }

    public void onTouchModeChanged(boolean isInTouchMode) {
        throw new RuntimeException("Stub!");
    }

    public boolean onTouchEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        throw new RuntimeException("Stub!");
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    public void fling(int velocityY) {
        throw new RuntimeException("Stub!");
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        throw new RuntimeException("Stub!");
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        throw new RuntimeException("Stub!");
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        throw new RuntimeException("Stub!");
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        throw new RuntimeException("Stub!");
    }

    public void draw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        throw new RuntimeException("Stub!");
    }

    public boolean onInterceptHoverEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    public void addTouchables(ArrayList<View> views) {
        throw new RuntimeException("Stub!");
    }

    public void setFriction(float friction) {
        throw new RuntimeException("Stub!");
    }

    public void setVelocityScale(float scale) {
        throw new RuntimeException("Stub!");
    }

    public void smoothScrollToPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    public void smoothScrollToPositionFromTop(int position, int offset, int duration) {
        throw new RuntimeException("Stub!");
    }

    public void smoothScrollToPositionFromTop(int position, int offset) {
        throw new RuntimeException("Stub!");
    }

    public void smoothScrollToPosition(int position, int boundPosition) {
        throw new RuntimeException("Stub!");
    }

    public void smoothScrollBy(int distance, int duration) {
        throw new RuntimeException("Stub!");
    }

    public void scrollListBy(int y) {
        throw new RuntimeException("Stub!");
    }

    public boolean canScrollList(int direction) {
        throw new RuntimeException("Stub!");
    }

    public void invalidateViews() {
        throw new RuntimeException("Stub!");
    }

    protected void handleDataChanged() {
        throw new RuntimeException("Stub!");
    }

    protected void onDisplayHint(int hint) {
        throw new RuntimeException("Stub!");
    }

    protected boolean isInFilterMode() {
        throw new RuntimeException("Stub!");
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        throw new RuntimeException("Stub!");
    }

    public boolean checkInputConnectionProxy(View view) {
        throw new RuntimeException("Stub!");
    }

    public void clearTextFilter() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasTextFilter() {
        throw new RuntimeException("Stub!");
    }

    public void onGlobalLayout() {
        throw new RuntimeException("Stub!");
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        throw new RuntimeException("Stub!");
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        throw new RuntimeException("Stub!");
    }

    public void afterTextChanged(Editable s) {
        throw new RuntimeException("Stub!");
    }

    public void onFilterComplete(int count) {
        throw new RuntimeException("Stub!");
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        throw new RuntimeException("Stub!");
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        throw new RuntimeException("Stub!");
    }

    public AbsListView.LayoutParams generateLayoutParams(AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        throw new RuntimeException("Stub!");
    }

    public int getTranscriptMode() {
        throw new RuntimeException("Stub!");
    }

    public void setTranscriptMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    public int getSolidColor() {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty(
            category = "drawing"
    )
    public int getCacheColorHint() {
        throw new RuntimeException("Stub!");
    }

    public void setCacheColorHint(int color) {
        throw new RuntimeException("Stub!");
    }

    public void reclaimViews(List<View> views) {
        throw new RuntimeException("Stub!");
    }

    public void setRemoteViewsAdapter(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    public void deferNotifyDataSetChanged() {
        throw new RuntimeException("Stub!");
    }

    public boolean onRemoteAdapterConnected() {
        throw new RuntimeException("Stub!");
    }

    public void onRemoteAdapterDisconnected() {
        throw new RuntimeException("Stub!");
    }

    public void setRecyclerListener(AbsListView.RecyclerListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setSelectionFromTop(int position, int y) {
        throw new RuntimeException("Stub!");
    }

    public interface RecyclerListener {
        void onMovedToScrapHeap(View var1);
    }

    public interface MultiChoiceModeListener extends ActionMode.Callback {
        void onItemCheckedStateChanged(ActionMode var1, int var2, long var3, boolean var5);
    }

    public interface SelectionBoundsAdjuster {
        void adjustListItemSelectionBounds(Rect var1);
    }

    public interface OnScrollListener {
        int SCROLL_STATE_FLING = 2;
        int SCROLL_STATE_IDLE = 0;
        int SCROLL_STATE_TOUCH_SCROLL = 1;

        void onScrollStateChanged(AbsListView var1, int var2);

        void onScroll(AbsListView var1, int var2, int var3, int var4);
    }

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(int w, int h) {
            super(null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(int w, int h, int viewType) {
            super(null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(null);
            throw new RuntimeException("Stub!");
        }
    }
}
