package asia.kala.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Sealed {
    Class<?>[] subclasses();
}
