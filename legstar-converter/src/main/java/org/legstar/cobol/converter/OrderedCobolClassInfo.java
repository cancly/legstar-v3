package org.legstar.cobol.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public final class OrderedCobolClassInfo implements CobolClassInfo {

    private final ClassValue<CobolFieldInfo[]> cache = new ClassValue<>() {

        @Override
        protected CobolFieldInfo[] computeValue(Class<?> clazz) {
            return inspect(clazz);
        }
    }

    @Override
    public CobolFieldInfo[] fieldInfos(Class<?> clazz) {
        return new cache.get(clazz).clone();
    }

    @Override
    public <Z> Z newInstance(Class<Z> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Annotation getCobolItemType(Class<?> clazz) {
        return findItemType(clazz.getAnnotations());
    }
}
