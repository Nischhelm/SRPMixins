package srpmixins.rules.basicrules;

public class ParaGroupRule extends GenericRule<String> {

    public ParaGroupRule(String s){
        super("group", s);
    }

    @Override
    public String parseConfigInput(String s){
        return s.replaceFirst("=","").trim();
    }
}
