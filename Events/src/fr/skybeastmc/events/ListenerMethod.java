package fr.skybeastmc.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Represents a listener method in a Listener
 */
class ListenerMethod {
    private final Listener parent;
    private final Method method;
    private final EventHandler handler;
    private final Class<? extends Event> event;

    private ListenerMethod(Method method, EventHandler handler, Class<? extends Event> event, Listener parent) {
	this.method = method;
	this.handler = handler;
	this.event = event;
	this.parent = parent;
    }

    /**
     * Create a new ListenerMethod if the method given is valid
     * 
     * @param method
     * @param parent
     * @return <code>null</code> if the method is null or not a valid listener method, the <code>ListenerMethod</code> otherwise
     */
    @Nullable
    @SuppressWarnings("unchecked") // Class<?> -> Class<? extends Event> -- Safe
    public static ListenerMethod of(@Nullable Method method, @NonNull Listener parent) {
	if (method == null // The method is not null
		|| method.getReturnType() != void.class) // The method has no return type (void)
	    return null;

	EventHandler handler = method.getAnnotation(EventHandler.class);

	if (handler == null // The method has the annotation
		|| handler.priority() == null) // And the annotation is valid (priority not null)
	    return null;

	Class<?>[] parameters = method.getParameterTypes();

	if (parameters.length != 1 // The method has ONE parameter
		|| !Event.class.isAssignableFrom(parameters[0])) // And the parameter is an Event
	    return null;

	return new ListenerMethod(method, handler, (Class<? extends Event>) parameters[0], parent);
    }
    
    /**
     * Invoke the method with the argument
     * 
     * @param event the argument
     * @throws InvocationTargetException if the method threw an exception
     * @throws IllegalArgumentException if the event is not the right event
     */
    public void invoke(@NonNull Event event) throws InvocationTargetException {
	if (!this.event.isInstance(event))
	    throw new IllegalArgumentException("The parameter is not valid");
	
	try {
	    method.invoke(parent, event);
	} catch (IllegalAccessException | IllegalArgumentException e) {
	    throw new IllegalStateException(e); // Should never happen
	}
    }
    
    /**
     * Get the method
     * 
     * @return the method
     */
    @NonNull
    public Method getMethod() {
        return method;
    }
    
    /**
     * Get the parent listener
     * 
     * @return the parent listener
     */
    @NonNull
    public Listener getParent() {
	return parent;
    }

    /**
     * Get the EventHandler annotation 
     * 
     * @return the Eventhandler annotation
     */
    @NonNull
    public EventHandler getHandler() {
	return handler;
    }

    /**
     * Get the parameter of the method
     * 
     * @return the parameter of the method
     */
    @NonNull
    public Class<? extends Event> getEvent() {
	return event;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public String toString() {
	return "ListenerMethod [parent=" + parent + ", method=" + method + ", handler=" + handler + ", event=" + event
		+ "]";
    }

}
