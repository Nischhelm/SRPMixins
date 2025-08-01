package srpmixins.rules.conditions;

import java.util.function.BiFunction;

public enum EnumOperation {
    EQUAL((v1, v2) -> v1 == v2),
    NOT_EQUAL((v1, v2) -> v1 != v2),
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

    public static EnumOperation getBySign(String sign) {
        switch (sign) {
            case "=": return EQUAL;
            case "!=": return NOT_EQUAL;
            case "<": return LESS;
            case ">": return GREATER;
            case "<=": return LESS_EQUAL;
            case ">=": return GREATER_EQUAL;
        }
        return null;
    }

    public String toString(){
        switch (this) {
            case EQUAL: return "=";
            case NOT_EQUAL: return "!=";
            case LESS: return "<";
            case GREATER: return ">";
            case LESS_EQUAL: return "<=";
            case GREATER_EQUAL: return ">=";
            default: return "";
        }
    }
}