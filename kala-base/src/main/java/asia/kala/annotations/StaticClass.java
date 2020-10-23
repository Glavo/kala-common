package asia.kala.annotations;

import java.lang.annotation.*;

/**
 * Annotate a class as a static class, which cannot be instantiated.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface StaticClass {
}
