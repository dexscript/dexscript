package com.dexscript.observability;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

// Observability is the API for:
// to push event
// to let observer pull current state from resources
// The process is:
// class proxy => transaction => tx handler => filter => event => event handler
public class Observability {

    private static final Map<Qualifier, WeakReference<Resource>> resources = new ConcurrentHashMap<>();
    private static final List<Predicate<Transaction>> whilteList = new CopyOnWriteArrayList<>();
    private static final List<Predicate<Transaction>> blackList = new CopyOnWriteArrayList<>();
    private static volatile boolean isFrozen = false;
    private static final List<Consumer<Transaction>> txHandlers = new ArrayList<>();
    private static final List<Consumer<Event>> eventHandlers = new ArrayList<>();
    private static Function<Object, String> formatter = Object::toString;

    // instrument wrap the class or interface to intercept the method invocation
    // static function is not supported yet
    public static <T> T instrument(Class<T> clazz, Object... initArgs) {
        Constructor[] ctors = Instrument.$(clazz).getConstructors();
        for (Constructor ctor : ctors) {
            try {
                return (T) ctor.newInstance(initArgs);
            } catch (IllegalArgumentException e) {
                // ignore
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new IllegalArgumentException("class " + clazz + " does not have constructor take specified init args");
    }

    public static void send(Transaction tx) {
        Event event = new Event();
        event.ns = tx.ns;
        event.attributes = tx.attributes;
        event.argNames = tx.argNames;
        event.argValues = new String[tx.argValues.length];
        for (int i = 0; i < tx.argValues.length; i++) {
            event.argValues[i] = formatter.apply(tx.argValues[i]);
        }
        for (Consumer<Event> eventHandler : eventHandlers) {
            eventHandler.accept(event);
        }
    }

    // register a resource to be invoked
    // normally the handler will use Observability to send one or many events back
    public static void register(Resource resource) {
        throw new RuntimeException();
    }

    public static void register(Qualifier qualifier, Resource resource) {
        throw new RuntimeException();
    }

    public static class SPI {

        public static void freeze() {
            isFrozen = true;
        }

        public static List<Qualifier> listResourceQualifiers() {
            throw new RuntimeException();
        }

        public static Resource getResource(Qualifier qualifier) {
            throw new RuntimeException();
        }

        public static void registerTransactionHandler(Consumer<Transaction> txHandler) {
            throw new RuntimeException();
        }

        public static List<String> listEventNamespaces() {
            throw new RuntimeException();
        }

        public static EventSchema getEventSchema(String ns) {
            throw new RuntimeException();
        }

        public static void registerFilterToWhiteList(Predicate<Transaction> filter) {
            throw new RuntimeException();
        }

        public static void registerFilterToBlackList(Predicate<Transaction> filter) {
            throw new RuntimeException();
        }

        public static void registerFilterToWhiteList(String ns, String expr) {
        }

        public static void registerFilterToBlackList(String ns, String expr) {
        }

        public static void removeAllFilters() {
        }

        public static void registerEventHandler(Consumer<Event> eventHandler) {
            if (isFrozen) {
                throw new IllegalStateException("Observability.SPI has been frozen");
            }
            eventHandlers.add(eventHandler);
        }

        public static void setFormatter(Function<Object, String> newFormatter) {
            if (isFrozen) {
                throw new IllegalStateException("Observability.SPI has been frozen");
            }
            formatter = newFormatter;
        }

        public static void reset() {
            eventHandlers.clear();
            isFrozen = false;
        }
    }
}
