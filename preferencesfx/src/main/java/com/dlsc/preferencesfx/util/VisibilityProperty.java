package com.dlsc.preferencesfx.util;

import java.util.function.Function;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

@FunctionalInterface
public interface VisibilityProperty {
    BooleanProperty get();

    /**
     * Creates a {@link VisibilityProperty} that is based on the given {@link Property}.
     *
     * The {@link VisibilityProperty} value will be set automatically when the given {@link Property} value changes.
     * The new value of the {@link VisibilityProperty} will be the result of the given {@link Function} applied to the
     * new value of the {@link Property}.
     *
     * @param property
     * @param visibilityFunc
     * @return
     * @param <T>
     */
    static <T> VisibilityProperty of(ObservableValue<T> property, Function<T, Boolean> visibilityFunc) {
        return new VisibilityProperty() {
            private BooleanProperty cachedProperty;

            @Override
            public BooleanProperty get() {
                if (cachedProperty == null) {
                    cachedProperty = new SimpleBooleanProperty(true);
                    property.addListener((observable, oldValue, newValue) -> cachedProperty.set(visibilityFunc.apply(newValue)));
                    // set the initial value of the visibility property properly
                    cachedProperty.set(visibilityFunc.apply(property.getValue()));
                }
                return cachedProperty;
            }
        };
    }

    /**
     * Simplified constructor for {@link VisibilityProperty} that is based on the given Boolean type {@link Property}
     *
     * The value of the {@link VisibilityProperty} will be set to the value of the given {@link Property}, i.e.,
     * whenever the referenced {@link Property} is true, the value of the @link VisibilityProperty} will be true as well.
     *
     * @param property
     * @return
     * @param <T>
     */
    static <T> VisibilityProperty of(ObservableValue<Boolean> property) {
        return VisibilityProperty.of(property, (newValue) -> newValue);
    }

}
