package fr.skybeastmc.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Represents a sorted event priority map.
 */
class EventPriorityMap {
    private final List<ListenerMethod> lowest = new ArrayList<>();
    private final List<ListenerMethod> low = new ArrayList<>();
    private final List<ListenerMethod> normal = new ArrayList<>();
    private final List<ListenerMethod> high = new ArrayList<>();
    private final List<ListenerMethod> highest = new ArrayList<>();
    private final List<ListenerMethod> monitor = new ArrayList<>();

    private final List<ListenerMethod> values = new ArrayList<>();

    /**
     * Add a ListenerMethod to the lists.
     * 
     * @param method the method to add
     * @return <code>true</code> if the method was added, <code>false</code> otherwise
     */
    public boolean add(@NonNull ListenerMethod method) {
	boolean ret;

	switch (method.getHandler().priority()) {
	case LOWEST:
	    ret = lowest.add(method);
	    break;
	case LOW:
	    ret = low.add(method);
	    break;
	case NORMAL:
	    ret = normal.add(method);
	    break;
	case HIGH:
	    ret = high.add(method);
	    break;
	case HIGHEST:
	    ret = highest.add(method);
	    break;
	case MONITOR:
	    ret = monitor.add(method);
	    break;
	default:
	    throw new IllegalArgumentException(); // Should never happen
	}

	makeValues();

	return ret;
    }

    /**
     * Remove a ListenerMethod from the lists.
     * 
     * @param method the method to remove
     * @return <code>true</code> if the method was removed, <code>false</code> otherwise
     */
    public boolean remove(@NonNull ListenerMethod method) {
	boolean ret;

	switch (method.getHandler().priority()) {
	case LOWEST:
	    ret = lowest.remove(method);
	    break;
	case LOW:
	    ret = low.remove(method);
	    break;
	case NORMAL:
	    ret = normal.remove(method);
	    break;
	case HIGH:
	    ret = high.remove(method);
	    break;
	case HIGHEST:
	    ret = highest.remove(method);
	    break;
	case MONITOR:
	    ret = monitor.remove(method);
	    break;
	default:
	    throw new IllegalArgumentException(); // Should never happen
	}

	makeValues();

	return ret;
    }

    /**
     * Remove a ListenerMethod by its Method from the lists.
     * 
     * @param method the method to remove
     * @return <code>true</code> if the method was removed, <code>false</code> otherwise
     */
    public boolean remove(@NonNull Method method) {
	for (ListenerMethod m : values) {
	    if (m.getMethod().equals(method)) {
		return remove(m);
	    }
	}
	return false;
    }

    private void makeValues() {
	values.clear();
	values.addAll(monitor);
	values.addAll(highest);
	values.addAll(high);
	values.addAll(normal);
	values.addAll(low);
	values.addAll(lowest);
    }

    /**
     * Get the ListenerMethod from the list with the specified priority.
     * 
     * @param priority the priority
     * @return a copy of the list
     */
    @NonNull
    public List<ListenerMethod> get(@NonNull EventPriority priority) {
	switch (priority) {
	case LOWEST:
	    return new ArrayList<>(lowest); // Clone the List
	case LOW:
	    return new ArrayList<>(low);
	case NORMAL:
	    return new ArrayList<>(normal);
	case HIGH:
	    return new ArrayList<>(high);
	case HIGHEST:
	    return new ArrayList<>(highest);
	case MONITOR:
	    return new ArrayList<>(monitor);
	default:
	    return Collections.emptyList(); // Should never happen
	}
    }

    /**
     * Get all the values from the lists.
     * 
     * @return all the values
     */
    @NonNull
    public List<ListenerMethod> values() {
	return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public String toString() {
	return "EventPriorityMap [lowest=" + lowest + ", low=" + low + ", normal=" + normal + ", high=" + high
		+ ", highest=" + highest + ", monitor=" + monitor + ", values=" + values + "]";
    }

}
