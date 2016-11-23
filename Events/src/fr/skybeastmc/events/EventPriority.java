package fr.skybeastmc.events;

/**
 * This enum represents the priorities of an event.
 */
public enum EventPriority {
    /**
     * The lowest priority
     */
    LOWEST(0),

    /**
     * Low priority
     */
    LOW(1),

    /**
     * Normal priority
     */
    NORMAL(2),

    /**
     * High priority
     */
    HIGH(3),

    /**
     * The highest priority
     */
    HIGHEST(4),

    /**
     * Monitor priority
     * If a listener method has this priority, the method will have a higher priority than HIGHEST, but the method cannot cancel the event.
     */
    MONITOR(5);

    private final int slot;

    private EventPriority(int slot) {
	this.slot = slot;
    }

    /**
     * Get the priority as an integer.
     * 
     * @return an <code>int</code> between 0 (Lowest) and 5 (Monitor)
     */
    public int getSlot() {
	return this.slot;
    }
}
