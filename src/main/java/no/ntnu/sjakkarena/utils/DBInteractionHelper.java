package no.ntnu.sjakkarena.utils;

/**
 * Collection of miscellaneous useful methods
 */
public class DBInteractionHelper {

    /**
     * Takes the input and returns a string that can be used in database update queries.
     * E.g. input "abc", "def", "ghi" gives the output "\"abc\", "\"def\", \"ghi\""
     *
     * @return databaseString a string that can be used in database update queries
     */
    public static String toDatabaseUpdateString(Object... arguments) {
        String databaseString = "";
        for (Object argument : arguments) {
            if (!(argument instanceof Integer || argument instanceof String || argument instanceof Boolean)) {
                throw new IllegalArgumentException();
            } else if (argument instanceof Boolean) {
                databaseString += (Boolean) argument ? 1 : 0;
            } else if (argument instanceof String) {
                databaseString += "\"" + argument.toString() + "\"";
            } else {
                databaseString += argument;
            }
            databaseString += ", ";
        }
        return databaseString.substring(0, databaseString.length() - 2);
    }
}
