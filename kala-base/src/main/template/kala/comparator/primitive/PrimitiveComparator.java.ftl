package kala.comparator.primitive;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@FunctionalInterface
public interface ${Type}Comparator extends PrimitiveComparator<${WrapperType}, ${Type}Comparator> {

    static @NotNull ${Type}Comparator naturalOrder() {
        return ${Type}Comparators.NaturalOrderComparator.INSTANCE;
    }

    static @NotNull ${Type}Comparator reverseOrder() {
        return ${Type}Comparators.ReverseOrderComparator.INSTANCE;
    }

    int compare(${PrimitiveType} ${var}1, ${PrimitiveType} ${var}2);

    @Override
    default int compare(${WrapperType} ${var}1, ${WrapperType} ${var}2) {
        return compare(${var}1.${PrimitiveType}Value(), ${var}2.${PrimitiveType}Value());
    }

    default @NotNull ${Type}Comparator nullsFirst() {
        return new ${Type}Comparators.NullComparator(true, this);
    }

    default @NotNull ${Type}Comparator nullsLast() {
        return new ${Type}Comparators.NullComparator(false, this);
    }

    @Override
    default @NotNull ${Type}Comparator reversed() {
        return (${Type}Comparator & Serializable) (${var}1, ${var}2) -> compare(${var}2, ${var}1);
    }
}
