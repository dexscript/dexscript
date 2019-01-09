package com.dexscript.observability;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

// Observability is the API for:
// to push event
// to let observer pull current state from resources
// The process is:
// class proxy => transaction => tx handler => filter => event => event handler
public class Observability {

    private static Map<Qualifier, WeakReference<Resource>> resources = new ConcurrentHashMap<>();
    private static List<Predicate<Transaction>> whilteList = new CopyOnWriteArrayList<>();
    private static List<Predicate<Transaction>> blackList = new CopyOnWriteArrayList<>();
    private static List<Consumer<Transaction>> txHandlers = new CopyOnWriteArrayList<>();
    private static List<Consumer<Event>> eventHandlers = new CopyOnWriteArrayList<>();

    // instrument wrap the class or interface to intercept the method invocation
    // static function is not supported yet
    public static <T> T instrument(Class<T> clazz) {
        throw new RuntimeException();
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
            throw new RuntimeException();
        }

        public static void reset() {
        }
    }
}
