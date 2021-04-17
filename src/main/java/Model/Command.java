package Model;

import Enums.CommandFieldType;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
    private HashMap<String, String> fieldValues;

    public Command(HashMap<String, String> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public static Command parseCommand(String commandString, HashMap<String, CommandFieldType> fields){
        HashMap<String, String> gottenFieldValues = new HashMap<>();
        Matcher matcher;
        for(Map.Entry<String, CommandFieldType> entry : fields.entrySet()){
            switch (entry.getValue()){
                case INT:
                    matcher = Pattern.compile("--" + entry.getKey() + " (\\d+)").matcher(commandString);
                    if(matcher.find()){
                        gottenFieldValues.put(entry.getKey(), matcher.group(1));
                    }
                    else {
                        // TODO: ۱۷/۰۴/۲۰۲۱ throw Exception
                    }
                    break;
                case STRING:
                    matcher = Pattern.compile("--" + entry.getKey() + " (\\S+)").matcher(commandString);
                    if(matcher.find()){
                        gottenFieldValues.put(entry.getKey(), matcher.group(1));
                    }
                    else {
                        // TODO: ۱۷/۰۴/۲۰۲۱ throw Exception
                    }
                    break;
                case BOOLEAN:
                    matcher = Pattern.compile("--" + entry.getKey()).matcher(commandString);
                    if(matcher.find()){
                        gottenFieldValues.put(entry.getKey(), "true");
                    }
                    else {
                        gottenFieldValues.put(entry.getKey(), "false");
                    }
                    break;
            }
        }

        return new Command(gottenFieldValues);
    }

    public String getField(String fieldName){
        return fieldValues.get(fieldName);
    }
}
