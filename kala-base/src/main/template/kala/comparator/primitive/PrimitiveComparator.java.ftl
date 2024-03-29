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

    int compare(${PrimitiveType} ${Var}1, ${PrimitiveType} ${Var}2);

    @Override
    default int compare(${WrapperType} ${Var}1, ${WrapperType} ${Var}2) {
        return compare(${Var}1.${PrimitiveType}Value(), ${Var}2.${PrimitiveType}Value());
    }

    default @NotNull ${Type}Comparator nullsFirst() {
        return new ${Type}Comparators.NullComparator(true, this);
    }

    default @NotNull ${Type}Comparator nullsLast() {
        return new ${Type}Comparators.NullComparator(false, this);
    }

    @Override
    default @NotNull ${Type}Comparator reversed() {
        return (${Type}Comparator & Serializable) (${Var}1, ${Var}2) -> compare(${Var}2, ${Var}1);
    }
}
