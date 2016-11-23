package fr.skybeastmc.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.jdt.annotation.NonNull;

/**
 * This annotation must be used to listen an Event.
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    
    /**
     * Get the priority of the listener method.
     * 
     * @return the priority of the listener method
     */
    @NonNull
    EventPriority priority() default EventPriority.NORMAL;

    /**
     * Get if this listener method will be called even if the event is cancelled.
     * 
     * @return <code>true</code> if the listener will ignore cancelled, <code>false</code> otherwise
     */
    boolean ignoreCancelled() default false;
}
