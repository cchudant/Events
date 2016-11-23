package fr.skybeastmc.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

/**
 * "Static" class for calling events, registering and unregistering listeners.
 */
public final class EventManager {
    private static final Map<Class<? extends Event>, EventPriorityMap> LISTENERS = new HashMap<>();

    private EventManager() {
    }
    
    /**
     * Register a listener to the EventManager.
     * 
     * @param listener the listener to register
     */
    public static void registerListener(@NonNull Listener listener) {
	for (Method method : listener.getClass().getMethods()) { // For each method in the class
	    ListenerMethod ls = ListenerMethod.of(method, listener); // Create a listener of the method
	    
	    if (ls != null) { // If the listener was created -- If the listener is valid
		Class<? extends Event> event = ls.getEvent();
		EventPriorityMap priorityMap = LISTENERS.get(event); // Get the priority map
		
		if (priorityMap == null) {
		    priorityMap = new EventPriorityMap(); // Create it if it does not exist
		    LISTENERS.put(event, priorityMap); // And Add it to the listeners for further use
		}
		
		priorityMap.add(ls); // Add the new listener to the priority list
	    }
	}
    }
    
    /**
     * Unregister the listener to the EventManager.
     * 
     * Warning: This method should not be used.
     * If you want to temporary unregister the listener, you should do like this:
     * 
     * <code>
     * private boolean enabled = true;
     * 
     * @EventHandler
     * public void onEvent(Event event) {
     * 	 if(!enabled) return;
     *   // Your code
     * }
     * </code>
     * 
     * @param listener the listener to unregister
     */
    public static void unregisterListener(@NonNull Listener listener) {
	for (Method method : listener.getClass().getMethods()) { // For each method in the class
	    Class<?>[] parameters = method.getParameterTypes();

	    if (parameters.length != 1 // The method need ONE parameter
		    || !Event.class.isAssignableFrom(parameters[0])) // And the parameter need to be an Event
		return;
	    
	    LISTENERS.get(parameters[0]).remove(method); // Remove the method
	}
    }

    /**
     * Call an event and its parents, so the listeners listening it will get the event.
     * 
     * The event manager will stop dispatching cancellable events when cancelled, except if the listener ignore cancelled.
     * The monitor listeners cannot cancel the event.
     * 
     * This method will call each parent's listeners, if they have one.
     * 
     * If an exception is caught while invoking the listener method, the exception will be logged.
     * 
     * @param event the event to dispatch
     * @return the event passed as argument
     */
    @NonNull
    @SuppressWarnings("unchecked") // Class<?> -> Class<? extends Event> -- Safe
    public static <T extends Event> T callEvent(@NonNull T event) {
	Class<?> cl = event.getClass();
	
	while (cl != Object.class) { // Iterate through the super classes until Object.class
	    call((Class<? extends Event>) cl, event); // Call the listeners for this event

	    cl = cl.getSuperclass();
	}

	return event;
    }

    private static void call(@NonNull Class<? extends Event> clazz, @NonNull Event event) {
	EventPriorityMap map = LISTENERS.get(clazz);
	if (map == null) // Exit if no listener is registered for this class
	    return;

	for (ListenerMethod listener : map.values()) { // For each listener
	    EventHandler handler = listener.getHandler();
	    
	    if (!handler.ignoreCancelled() &&
		    event instanceof Cancellable && ((Cancellable) event).isCancelled()) { // Cancellable implementation
		continue; // Don't pass the event
	    }
	    
	    try {
		listener.invoke(event); // Invoke the listener
	    } catch (InvocationTargetException e) {
		log(listener, e.getTargetException()); // Log if an exception was thrown
	    }
	    
	    if (event instanceof Cancellable
		    && handler.priority() == EventPriority.MONITOR) // If the listener is monitor
		((Cancellable) event).setCancelled(false); // Then it cannot cancel the event
	}
    }
    
    private static void log(@NonNull ListenerMethod listener, @NonNull Throwable throwable) {
	Logger.getGlobal().log(Level.SEVERE, "Exception while calling listener",
		new EventException("Exception while disapatching event to method listener " + listener, throwable));
    }

}
