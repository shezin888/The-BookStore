import java.io.*;

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

class InvalidPriceException extends Exception {
    public InvalidPriceException(String message) {
        super(message);
    }
}

class InvalidIsbnException extends Exception {
    public InvalidIsbnException(String message) {
        super(message);
    }
}

class InvalidYearException extends Exception {
    public InvalidYearException(String message) {
        super(message);
    }
}


public class BookStore {
    // Simplified version of do_part1() method

    private static final String WRITE_BASE_PATH = "/home/shezin/Desktop/gitrepo/The-BookStore/BookStore/output/"; // Example write path
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
                    } catch (TooManyFieldsException | TooFewFieldsException | MissingFieldException |
                             UnknownGenreException | InvalidPriceException | InvalidYearException | InvalidIsbnException e) {
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
        // Assuming an upper limit for the number of files
        String[] tempFileNames = new String[100];
        int count = 0;

        String pathPrefix = "/home/shezin/Desktop/gitrepo/The-BookStore/BookStore/data/";

        try (BufferedReader reader = new BufferedReader(new FileReader(configFileName))) {
            // Skipping the first line as it contains the number of files, not a file name
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null && count < tempFileNames.length) {
                tempFileNames[count++] = pathPrefix + line.trim();
            }
        } catch (IOException e) {
            System.out.println("Error reading config file: " + configFileName);
            // Return an empty array of size 0 if there's an error
            return new String[0];
        }

        // Copy the read file names into an array of the exact size
        String[] fileNames = new String[count];
        System.arraycopy(tempFileNames, 0, fileNames, 0, count);

        return fileNames;
    }



    private static void processLine(String line, String fileName) throws TooManyFieldsException, TooFewFieldsException, MissingFieldException, UnknownGenreException, InvalidPriceException, InvalidYearException, InvalidIsbnException {
        // Split the line into fields. Considering a record might have double quotes for title field with commas.
        String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        if (fields.length < 6) {
            throw new TooFewFieldsException("Too many fields");
        }
        else if (fields.length >6) {
            throw new TooManyFieldsException("Too few fields");
        }

        // Remove potential double quotes from title and genre
        fields[0] = fields[0].replace("\"", "");
        fields[4] = fields[4].replace("\"", "");

        if (fields[0].trim().isEmpty()) { // title
            throw new MissingFieldException("Missing field (title)");
        }
        if (fields[1].trim().isEmpty()) { // authors
            throw new MissingFieldException("Missing field (authors)");
        }
        if (fields[2].trim().isEmpty()) { // price
            throw new MissingFieldException("Missing field (price)");
        }
        if (fields[3].trim().isEmpty()) { // isbn
            throw new MissingFieldException("Missing field (ISBN)");
        }
        if (fields[4].trim().isEmpty()) { // genre
            throw new UnknownGenreException("Missing field (genre)");
        }
        if (fields[5].trim().isEmpty()) { // year
            throw new MissingFieldException("Missing field (year)");
        }
        // Validations
        validatePrice(fields[2]);
        validateYear(fields[5]);
        validateISBN(fields[3]);
        if (!isValidGenre(fields[4])) throw new UnknownGenreException("Unknown genre: " + fields[4]);

        writeRecordToGenreFile(fields, fileName);
    }

    private static void validatePrice(String priceStr) throws InvalidPriceException {
        try {
            double price = Double.parseDouble(priceStr);
            if (price < 0) throw new InvalidPriceException("Invalid Price: " + priceStr);
        } catch (NumberFormatException e) {
            throw new InvalidPriceException("Invalid price format: " + priceStr);
        }
    }

    private static void validateYear(String yearStr) throws InvalidYearException {
        try {
            int year = Integer.parseInt(yearStr);
            if (year < 1995 || year > 2024) throw new InvalidYearException("Invalid year: " + yearStr);
        } catch (NumberFormatException e) {
            throw new InvalidYearException("Invalid year: " + yearStr);
        }
    }

    private static void validateISBN(String isbn) throws InvalidIsbnException {
        if (isbn.length() == 10) {
            // Implement 10-digit ISBN validation
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                // Assuming that the ISBN contains only digits
                if (!Character.isDigit(isbn.charAt(i))) {
                    throw new InvalidIsbnException("Invalid ISBN-10: " + isbn);
                }
                sum += (isbn.charAt(i) - '0') * (10 - i);
            }
            if (sum % 11 != 0) {
                throw new InvalidIsbnException("Invalid ISBN-10: " + isbn);
            }
        } else if (isbn.length() == 13) {
            // Implement 13-digit ISBN validation
            int sum = 0;
            for (int i = 0; i < 13; i++) {
                // Assuming that the ISBN contains only digits
                if (!Character.isDigit(isbn.charAt(i))) {
                    throw new InvalidIsbnException("Invalid ISBN-13: " + isbn);
                }
                int digit = isbn.charAt(i) - '0';
                sum += (i % 2 == 0) ? digit : digit * 3;
            }
            if (sum % 10 != 0) {
                throw new InvalidIsbnException("Invalid ISBN-13: " + isbn);
            }
        } else {
            throw new InvalidIsbnException("Invalid ISBN length: " + isbn);
        }
    }



    private static boolean isValidGenre(String genre) {
        switch (genre) {
            case "CCB":
            case "HCB":
            case "MTV":
            case "MRB":
            case "NEB":
            case "OTR":
            case "SSM":
            case "TPA":
                return true;
            default:
                return false;
        }
    }

    private static void writeRecordToGenreFile(String[] fields, String fileName) throws UnknownGenreException {
        String genreFileName = getGenreFileName(fields[4]);
        if (genreFileName == null) {
            throw new UnknownGenreException("Unknown genre: " + fields[4]);
        }
        genreFileName = WRITE_BASE_PATH + genreFileName; // Prepend the write path
        try (PrintWriter out = new PrintWriter(new FileWriter(genreFileName, true))) {
            out.println(String.join(",", fields));
        } catch (IOException e) {
            System.out.println("Error writing to genre file: " + genreFileName);
        }
    }

    private static String getGenreFileName(String genreCode) {
        switch (genreCode) {
            case "CCB":
                return "Cartoons_Comics_Books.csv";
            case "HCB":
                return "Hobbies_Collectibles_Books.csv";
            case "MTV":
                return "Movies_TV_Books.csv";
            case "MRB":
                return "Music_Radio_Books.csv";
            case "NEB":
                return "Nostalgia_Eclectic_Books.csv";
            case "OTR":
                return "Old_Time_Radio_Books.csv";
            case "SSM":
                return "Sports_Sports_Memorabilia.csv";
            case "TPA":
                return "Trains_Planes_Automobiles.csv";
            default:
                // Handle unknown genre code or throw an exception if that's the requirement
                return null;
        }
    }

    private static void logSyntaxError(String errorMessage, String record, String fileName) {
        String errorFileName = WRITE_BASE_PATH + "syntax_error_file.txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(errorFileName, true))) { // true to append
            out.printf("Syntax error in file: %s%nError: %s%nRecord: %s%n%n", fileName, errorMessage, record);
        } catch (IOException e) {
            System.out.println("Error logging syntax error.");
        }
    }

    public static void main(String[] args) {
        do_part1();
    }
}