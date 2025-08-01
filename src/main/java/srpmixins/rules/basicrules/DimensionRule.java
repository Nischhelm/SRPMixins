package srpmixins.rules.basicrules;

public class DimensionRule extends GenericRule<Integer> {

    public DimensionRule(String s){
        super("dim", s);
    }

    @Override
    public Integer parseConfigInput(String s){
        return Integer.parseInt(s.replaceFirst("=", "").trim());
    }
}
