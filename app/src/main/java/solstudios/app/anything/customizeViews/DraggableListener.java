package solstudios.app.anything.customizeViews;

/**
 * Listener created to be notified when some drag actions are performed over DraggablePanel or
 * DraggableView instances.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */

public interface DraggableListener {

    void onTopViewMoving(float offset);

    /**
     * Called when the view is maximized.
     */
    void onMaximized();

    /**
     * Called when the view is minimized.
     */
    void onMinimized();

    /**
     * Called when the view is minimized.
     */
    void beforeMinimized();

    /**
     * Called when the view is closed to the left.
     */
    void onClosedToLeft();

    /**
     * Called when the view is closed to the right.
     */
    void onClosedToRight();

    /**
     * Called when the view is maximized.
     */
    void onMaximizedExpanding();
}