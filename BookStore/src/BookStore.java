import java.io.*;
import java.util.ArrayList;
import java.util.List;

class TooManyFieldsException extends Exception {
    public TooManyFieldsException(String message) {
        super(message);
    }
}

class TooFewFieldsException extends Exception {
    public TooFewFieldsException(String message) {
        super(message);
    }
}

class MissingFieldException extends Exception {
    public MissingFieldException(String message) {
        super(message);
    }
}

class UnknownGenreException extends Exception {
    public UnknownGenreException(String message) {
        super(message);
    }
}

public class BookStore {
    // Simplified version of do_part1() method
    public static void do_part1() {
        // Assume the existence of a method to get the list of file names
        String[] fileNames = getFileNames("/home/shezin/Desktop/java/PPS_2_BookStore/Comp6481_W24_Assg2-Needed-Files/part1_input_file_names.txt");
        for (String fileName : fileNames) {
            //System.out.println(fileName+'\n');
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        // Process each line
                        processLine(line, fileName);
                    } catch (TooManyFieldsException | TooFewFieldsException | MissingFieldException | UnknownGenreException e) {
                        // Log syntax errors
                        logSyntaxError(e.getMessage(), line, fileName);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + fileName);
            } catch (IOException e) {
                System.out.println("IO Exception while reading: " + fileName);
            }
        }
    }

    // Dummy method placeholders
    private static String[] getFileNames(String configFileName) {
        // Prefix with the correct path based on the current environment
        String pathPrefix = "/home/shezin/Desktop/java/PPS_2_BookStore/Comp6481_W24_Assg2-Needed-Files/";
        //configFileName = pathPrefix + configFileName; // Adjusting the config file name with path
        List<String> fileNamesList = new ArrayList<>();
        //System.out.println(configFileName+'\n');
        try (BufferedReader reader = new BufferedReader(new FileReader(configFileName))) {
            String line;
            reader.readLine(); // Skip the first line that contains the number of file names
            while ((line = reader.readLine()) != null) {
                fileNamesList.add(pathPrefix + line.trim()); // Adding trimmed file names with path prefix
            }
        } catch (IOException e) {
            System.out.println("Error reading config file: " + configFileName);
        }
        return fileNamesList.toArray(new String[0]);
    }


    private static void processLine(String line, String fileName) throws TooManyFieldsException, TooFewFieldsException, MissingFieldException, UnknownGenreException {
        String[] fields = line.split(",", -1); // -1 to include trailing empty strings
        if (fields.length != 6) {
            throw new TooManyFieldsException("Too many or too few fields in record.");
        }

        // Example checks, implement other validations as needed
        if (fields[0].isEmpty()) throw new MissingFieldException("Missing title.");
        if (!isValidGenre(fields[4])) throw new UnknownGenreException("Unknown genre.");

        writeRecordToGenreFile(fields, fileName);
    }

    private static boolean isValidGenre(String genre) {
        // Implement this method based on your genre validation logic
        return true; // Placeholder return
    }

    private static void writeRecordToGenreFile(String[] fields, String fileName) {
        String genreFileName = getGenreFileName(fields[4]);
        try (PrintWriter out = new PrintWriter(new FileWriter(genreFileName, true))) { // true to append
            out.println(String.join(",", fields));
        } catch (IOException e) {
            System.out.println("Error writing to genre file: " + genreFileName);
        }
    }

    private static String getGenreFileName(String genre) {
        // Implement this method to map genre codes to CSV file names
        return genre + ".csv"; // Placeholder implementation
    }

    private static void logSyntaxError(String errorMessage, String record, String fileName) {
        try (PrintWriter out = new PrintWriter(new FileWriter("syntax_error_file.txt", true))) { // true to append
            out.printf("Syntax error in file: %s%nError: %s%nRecord: %s%n%n", fileName, errorMessage, record);
        } catch (IOException e) {
            System.out.println("Error logging syntax error.");
        }
    }

    public static void main(String[] args) {
        do_part1();
    }
}
