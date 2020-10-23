package asia.kala.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE_PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface Covariant {
}
