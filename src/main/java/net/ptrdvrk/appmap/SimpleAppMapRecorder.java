/*
 * Author: ptrdvrk
 * https://opensource.org/licenses/MIT
 */
package net.ptrdvrk.appmap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
 * Helper class for appmap-java Recorder that can programmatically start and stop recording in
 * Java applications without a compile-time dependency on appmap-java.
 *
 * Instructions:
 *  1. Setup appmap-java, see https://appland.com/docs/reference/appmap-java.html
 *  2. Start application with -javaagent:appmap.jar
 *  3. Test if appmap agent is available with isAgentPresent()
 *  4. Call start() and stop() to record AppMaps from running apps, both return true when successful
 *  5. If something goes wrong, check getLastException()
 *
 *  Join the AppLand Discord server and share your experience with the AppMap community!
 *   -> https://discord.com/invite/N9VUap6
 */

public class SimpleAppMapRecorder {

    public final static String APPMAP_NAME = "scenarioName";
    public final static String RECORDER_NAME = "recorderName";

    private final static String DEFAULT_APPMAP_NAME = "MyAppMap";
    private final static String DEFAULT_RECORDER_NAME = "SimpleAppMapRecorder";
    private final static String CLASS_RECORDER = "com.appland.appmap.record.Recorder";
    private final static String CLASS_METADATA = "com.appland.appmap.record.IRecordingSession$Metadata";
    private final static String METHOD_START = "start";
    private final static String METHOD_STOP = "stop";

    private static Object oRecorder = null;
    private static Throwable lastException = null;


    //returns true if appmap-java classes can be loaded
    public static boolean isAgentPresent() {
        return (createMetadata() != null);
    }

    //returns true when recording started successfully
    public static boolean startRecording() {
        Object metadata = createMetadata();
        if(metadata == null) {
            return false;
        }
        setMetadataProperty(metadata, APPMAP_NAME, DEFAULT_APPMAP_NAME);
        setMetadataProperty(metadata, RECORDER_NAME, DEFAULT_RECORDER_NAME);
        return startRecording(metadata);
    }

    //returns true when recording started successfully
    public static boolean startRecording(Object metadata) {
        try {
            Object recorder = getRecorder();
            if (recorder == null) return false;
            if(metadata == null) {
                return startRecording();
            }
            Method mInstance = recorder.getClass().getDeclaredMethod(METHOD_START, String.class, metadata.getClass());
            Object result = mInstance.invoke(recorder, getMetadataProperty(metadata, APPMAP_NAME), metadata);
        } catch (Exception e) {
            setLastException(e);
            return false;
        }
        return true;
    }

    //returns true when recording stopped successfully
    public static boolean stopRecording() {
        try {
            if(oRecorder == null) throw new RuntimeException("AppMap recorder not started.");
            Method mInstance = oRecorder.getClass().getDeclaredMethod(METHOD_STOP);
            Object result = mInstance.invoke(oRecorder);
        } catch (Exception e) {
            oRecorder = null;
            setLastException(e);
            return false;
        }
        oRecorder = null;
        return true;
    }

    //returns null when a new Metadata instance cannot be created
    public static Object createMetadata() {
        try {
            Class<?> c = Class.forName(CLASS_METADATA);
            return c.getConstructor().newInstance();
        } catch (Exception e) {
            setLastException(e);
            return null;
        }
    }

    //sets value of property in metadata object
    public static boolean setMetadataProperty(Object metadata, String property, String value) {
        if(metadata == null || property == null || value == null) {
            throw new IllegalArgumentException("Invalid metadata|property|value.");
        }
        Class<?> c = metadata.getClass();
        try {
            if (c != null) {
                Field field = c.getDeclaredField(property);
                field.setAccessible(true);
                field.set(metadata, value);
            } else {
                throw new RuntimeException("AppMap agent not available.");
            }
        } catch (Exception e) {
                setLastException(e);
                return false;
        }
        return true;
    }

    //returns last encountered exception
    public static Throwable getLastException() {
        return lastException;
    }

    //returns null when a Recorder instance isn't available
    private static Object getRecorder() {
        try {
            Class<?> c = Class.forName(CLASS_RECORDER);
            Method mInstance = c.getDeclaredMethod("getInstance");
            oRecorder = mInstance.invoke(null);
            return oRecorder;
        } catch (Exception e) {
            setLastException(e);
            return null;
        }
    }

    //returns null when property value is not available
    private static String getMetadataProperty(Object metadata, String property) {
        if(metadata == null || property == null) {
            throw new IllegalArgumentException("Invalid metadata|property.");
        }
        Class<?> c = metadata.getClass();
        try {
            if (c != null) {
                Field field = c.getDeclaredField(property);
                return (String) field.get(metadata);
            } else {
                throw new RuntimeException("AppMap agent not available.");
            }
        } catch (Exception e) {
            setLastException(e);
            return null;
        }
    }

    //sets the last thrown exception
    private static void setLastException(Throwable e) {
        if (e instanceof InvocationTargetException) {
            lastException = ((InvocationTargetException) e).getTargetException();
        } else {
            lastException = e;
        }
    }
}
