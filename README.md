#Listening an event

You can create a simple listener like that:
```
public class MyListener implements Listener {
    
    @EventHandler
    public void someRandomMethodName(MyEvent event) {
    }
    
}
```
The method name does not matter.
Don't forget the `@EventHandler` annotation!

You add parameters to the `@EventHandler` annotation like this:
```
@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
```

Then, all you need is to register the listener to the EventManager:
```
EventManager.registerListener(new MyListener());
```
You can add multiple listener methods per listener.

#Creating an event

To create an event, you simply need to create a class which extends Event.
Here is an exemple:

```
public class ChatEvent extends Event {
    private String chatted;

    public MyEvent(String chatted) {
        this.chatted = chatted;
    }

    public String getChatted() {
        return chatted;
    }
}
```
Then, to call the event, just do this:
```
EventManager.callEvent(new ChatEvent("Hi!"));
```
The event will be dispatched between the listeners, the higher priority the sooner.
Then, the event will be dispatched between the listeners listening the parents of the Event.

So both these methods will fire, but method1 will be called sooner.
```
@EventHandler
public void method1(ChatEvent event) {
}

@EventHandler
public void method2(Event event) {
}
```

#Cancellable event

You can create cancellable events like this:
```
public class MyCancellableEvent extends Event implements Cancellable {
    private boolean cancel;

    @Override
    public boolean isCancelled() {
	return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
	this.cancel = cancel;
    }
}
```
Then all the listeners can cancel it:
```
@EventHandler
public void listen(MyCancellableEvent event) {
	event.setCancellable(true);
}
```
If an event is cancelled, then the listeners cannot listen to it, except if they ignoreCancelled: 
```
@EventHandler(ignoreCancelled = true)
public void thisIgnoreCancelled(MyCancellableEvent event) {
	event.setCancellable(true);
}
```
If a listener use the `MONITOR` priority, the listener cannot cancel the event.
```
@EventHandler(priority = EventPriority.MONITOR)
public void thisIgnoreCancelled(MyCancellableEvent event) {
	event.setCancellable(true); // This will not throw any exception, but this will not work.
}
```
