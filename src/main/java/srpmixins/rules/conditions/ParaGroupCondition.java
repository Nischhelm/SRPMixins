package srpmixins.rules.conditions;

public class ParaGroupCondition extends GenericCondition<String> {

    public ParaGroupCondition(String s){
        super("group", s);
    }

    @Override
    public String parseConfigInput(String s){
        return s.replaceFirst("=","").trim();
    }
}
