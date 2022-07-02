package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
<#if IsSpecialized || Type == "Boolean">

import java.util.function.${Type}Supplier;
</#if>

@FunctionalInterface
public interface Checked${Type}Supplier<Ex extends Throwable> extends ${Type}Supplier {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <Ex extends Throwable> Checked${Type}Supplier<Ex> of(Checked${Type}Supplier<? extends Ex> consumer) {
        return (Checked${Type}Supplier<Ex>) consumer;
    }

    ${PrimitiveType} getAs${Type}Checked();

    default ${PrimitiveType} getAs${Type}() {
        try {
            return getAs${Type}Checked();
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }

        throw new AssertionError();
    }
}
