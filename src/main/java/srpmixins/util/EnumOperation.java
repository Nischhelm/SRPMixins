package srpmixins.util;

import java.util.function.BiFunction;

public enum EnumOperation {
    EQUAL((v1, v2) -> v1 == v2),
    LESS((v1, v2) -> v1 < v2),
    GREATER((v1, v2) -> v1 > v2),
    LESS_EQUAL((v1, v2) -> v1 <= v2),
    GREATER_EQUAL((v1, v2) -> v1 >= v2);

    private final BiFunction<Integer, Integer, Boolean> function;

    EnumOperation(BiFunction<Integer, Integer, Boolean> function) {
        this.function = function;
    }

    public boolean evaluate(int v1, int v2) {
        return function.apply(v1, v2);
    }

    static EnumOperation getBySign(String sign) {
        switch (sign) {
            case "=": return EQUAL;
            case "<": return LESS;
            case ">": return GREATER;
            case "<=": return LESS_EQUAL;
            case ">=": return GREATER_EQUAL;
        }
        return null;
    }
}