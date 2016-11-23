package fr.skybeastmc.events;

/**
 * Implement this class and override the methods to allow your event to be cancelled.
 * 
 * This interface should not be implemented in non-event classes.
 */
public interface Cancellable {
    
    /**
     * Get if the event is cancelled.
     * 
     * @return <code>true</code> if the event is cancelled, <code>false</code> otherwise
     */
    boolean isCancelled();

    /**
     * Set the cancelled state of this event.
     * 
     * @param cancel <code>true</code> to cancel the event, <code>false</code> otherwise
     */
    void setCancelled(boolean cancel);
}
