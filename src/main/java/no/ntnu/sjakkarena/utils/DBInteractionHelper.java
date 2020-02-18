package no.ntnu.sjakkarena.utils;

/**
 * Collection of miscellaneous useful methods
 */
public class DBInteractionHelper {

    /**
     * Takes the input and returns a string containing values that can be used in database update queries.
     * E.g. input "abc", "def", 0 gives the output "(\"abc\", \"def\", 0)"
     *
     * @return values a string that can be used in database update queries
     */
    public static String toValuesString(Object... arguments) {
        String values = "(";
        for (Object argument : arguments) {
            if (!(argument instanceof Integer || argument instanceof String || argument instanceof Boolean)) {
                throw new IllegalArgumentException();
            } else if (argument instanceof Boolean) {
                values += (Boolean) argument ? 1 : 0;
            } else if (argument instanceof String) {
                values += "\"" + argument.toString() + "\"";
            } else {
                values += argument;
            }
            values += ", ";
        }
        return values.substring(0, values.length() - 2) + ")";
    }

    /**
     * Takes the input and returns a string containing attributes that can be used in database update queries.
     * E.g. input "abc", "def", "ghi" gives the output "(`abc`, `def, `ghi`)"
     *
     * @return attribute a string that can be used in database update queries
     */
    public static String toAttributeString(String... arguments){
        String attribtues = "(";
        for (String attribute : arguments){
            attribtues += "`" + attribute + "`," ;
        }
        return attribtues.substring(0, attribtues.length()-1) + ")";
    }
}
