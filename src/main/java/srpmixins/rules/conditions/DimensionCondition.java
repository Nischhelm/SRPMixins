package srpmixins.rules.conditions;

public class DimensionCondition extends GenericCondition<Integer> {

    public DimensionCondition(String s){
        super("dim", s);
    }

    @Override
    public Integer parseConfigInput(String s){
        return Integer.parseInt(s.replaceFirst("=", "").trim());
    }
}
