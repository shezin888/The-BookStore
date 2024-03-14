import java.io.*;
import java.util.Scanner;

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

class  BadPriceException extends Exception {
    public  BadPriceException(String message) {
        super(message);
    }
}

class  BadIsbn10Exception extends Exception {
    public  BadIsbn10Exception(String message) {
        super(message);
    }
}
class  BadIsbn13Exception extends Exception {
    public  BadIsbn13Exception(String message) {
        super(message);
    }
}


class BadYearException extends Exception {
    public BadYearException(String message) {
        super(message);
    }
}


class Book implements Serializable{

    String title;
    String authors;
    double price;
    String isbn;
    String genre;
    int year;

    public Book(String title, String authors, double price, String isbn, String genre, int year) {
        this.title = title;
        this.authors = authors;
        this.price = price;
        this.isbn = isbn;
        this.genre = genre;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", price=" + price +
                ", isbn='" + isbn + '\'' +
                ", genre='" + genre + '\'' +
                ", year=" + year +
                '}';
    }
}


public class BookStore {
    // Simplified version of do_part1() method
    private static final String READ_BASE_PATH = "C:\\Users\\HP\\Documents\\GitHub\\The-BookStore\\BookStore\\data\\"; // Example write path
    private static final String WRITE_BASE_PATH = "C:\\Users\\HP\\Documents\\GitHub\\The-BookStore\\BookStore\\output\\"; // Example write path

    private static final String[] genres_filenames = {"Cartoons_Comics.csv",
            "Hobbies_Collectibles.csv",
            "Movies_TV_Books.csv",
            "Music_Radio_Books.csv",
            "Nostalgia_Eclectic_Books.csv",
            "Old_Time_Radio_Books.csv",
            "Sports_Sports_Memorabilia.csv",
            "Trains_Planes_Automobiles.csv"};

    private static final int[] books_count = new int[genres_filenames.length];



    public static void do_part1() {
        // Assume the existence of a method to get the list of file names
        String path = READ_BASE_PATH + "part1_input_file_names.txt";
        String[] fileNames = getFileNames(path);

        for(String fileName : genres_filenames){
            File file = new File(WRITE_BASE_PATH + fileName);
            if(file.exists()){
                file.delete();
            }
        }

        File file = new File(WRITE_BASE_PATH + "syntax_error_file.txt");
        if(file.exists()){
            file.delete();
        }

        for (String fileName : fileNames) {
            //System.out.println(fileName+'\n');
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        // Process each line
                        processLine(line, fileName);
                    } catch (TooManyFieldsException | TooFewFieldsException | MissingFieldException |
                             UnknownGenreException e) {
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


        try (BufferedReader reader = new BufferedReader(new FileReader(configFileName))) {
            // Skipping the first line as it contains the number of files, not a file name
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null && count < tempFileNames.length) {
                tempFileNames[count++] = READ_BASE_PATH + line.trim();
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



    private static void processLine(String line, String fileName) throws TooManyFieldsException, TooFewFieldsException, MissingFieldException, UnknownGenreException {
        // Split the line into fields. Considering a record might have double quotes for title field with commas.
        String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);


        if (fields.length < 6) {
            throw new TooFewFieldsException("Too many fields");
        }
        else if (fields.length >6) {
            throw new TooManyFieldsException("Too few fields");
        }

        // Remove potential double quotes from title and genre
        //fields[0] = fields[0].replace("\"", "");
        //fields[4] = fields[4].replace("\"", "");

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

        if (!isValidGenre(fields[4])) throw new UnknownGenreException("Unknown genre: " + fields[4]);

        writeRecordToGenreFile(fields, fileName);
    }

    private static void validatePrice(String priceStr) throws  BadPriceException {
        try {
            double price = Double.parseDouble(priceStr);
            if (price < 0) throw new  BadPriceException("Invalid Price: " + priceStr);
        } catch (NumberFormatException e) {
            throw new  BadPriceException("Invalid price format: " + priceStr);
        }
    }

    private static void validateYear(String yearStr) throws BadYearException {
        try {
            int year = Integer.parseInt(yearStr);
            if (year < 1995 || year > 2024) throw new BadYearException("Invalid year: " + yearStr);
        } catch (NumberFormatException e) {
            throw new BadYearException("Invalid year: " + yearStr);
        }
    }

    private static void validateISBN(String isbn) throws  BadIsbn10Exception, BadIsbn13Exception {
        if (isbn.length() == 10) {
            // Implement 10-digit ISBN validation
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                // Assuming that the ISBN contains only digits
                if (!Character.isDigit(isbn.charAt(i))) {
                    throw new  BadIsbn10Exception("Invalid ISBN-10: " + isbn);
                }
                sum += (isbn.charAt(i) - '0') * (10 - i);
            }
            if (sum % 11 != 0) {
                throw new  BadIsbn10Exception("Invalid ISBN-10: " + isbn);
            }
        } else if (isbn.length() == 13) {
            // Implement 13-digit ISBN validation
            int sum = 0;
            for (int i = 0; i < 13; i++) {
                // Assuming that the ISBN contains only digits
                if (!Character.isDigit(isbn.charAt(i))) {
                    throw new  BadIsbn13Exception("Invalid ISBN-13: " + isbn);
                }
                int digit = isbn.charAt(i) - '0';
                sum += (i % 2 == 0) ? digit : digit * 3;
            }
            if (sum % 10 != 0) {
                throw new  BadIsbn13Exception("Invalid ISBN-13: " + isbn);
            }
        } else {
            throw new  BadIsbn10Exception("Invalid ISBN length: " + isbn);
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

    private static int getGenreIndex(String genreFileName) {

        for(int i = 0; i < genres_filenames.length; i++){
            if (genres_filenames[i].equals(genreFileName))
                return i;
        }
        return -1;

    }

    private static String getGenreFileName(String genreCode) {
        switch (genreCode) {
            case "CCB":
                return "Cartoons_Comics.csv";
            case "HCB":
                return "Hobbies_Collectibles.csv";
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

    public static void do_part2(){

        Book book = null;
        ObjectOutputStream oos = null;
        int index;


        File file = new File(WRITE_BASE_PATH + "semantic_error_file.txt");
        if(file.exists()){
            file.delete();
        }

        for (String fileName : genres_filenames) {
            try (BufferedReader reader = new BufferedReader(new FileReader(WRITE_BASE_PATH + fileName))) {
                String line;
                // Create an ObjectOutputStream to write into the binary file
                try{
                    oos = new ObjectOutputStream(new FileOutputStream(WRITE_BASE_PATH + fileName + ".ser"));

                    while ((line = reader.readLine()) != null) {
                        try{
                            String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                            validatePrice(fields[2]);
                            validateYear(fields[5]);
                            validateISBN(fields[3]);

                            index = getGenreIndex(getGenreFileName(fields[4]));
                            book = new Book(fields[0], fields[1], Double.parseDouble(fields[2]), fields[3], fields[4], Integer.parseInt(fields[5]));
                            oos.writeObject(book);
                            ++books_count[index];
                        }
                        catch (BadPriceException | BadYearException |  BadIsbn10Exception | BadIsbn13Exception e) {
                            logSemanticError(e.getMessage(), line, fileName);
                        }
                    }
                }
                catch(IOException e)
                {
                    System.out.println("IO Exception: " + e.getMessage());
                }
                finally {
                    assert oos != null;
                    oos.close();
                }

            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + fileName);
            } catch (IOException e) {
                System.out.println("IO Exception while reading: " + fileName);
            }

        }

        for(int i = 0; i < genres_filenames.length; i++){
            System.out.println(genres_filenames[i] + " count: " + books_count[i]);
        }
    }

    private static void logSemanticError(String errorMessage, String record, String fileName) {
        String errorFileName = WRITE_BASE_PATH + "semantic_error_file.txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(errorFileName, true))) { // true to append
            out.printf("Semantic error in file: %s%nError: %s%nRecord: %s%n%n", fileName, errorMessage, record);
        } catch (IOException e) {
            System.out.println("Error logging syntax error.");
        }
    }

    public static void main(String[] args) {
        do_part1();
        do_part2();
        do_part3();
    }

    private static void do_part3() {
        ObjectInputStream ois = null;

        Book[][] books = new Book[genres_filenames.length][];
        int total_count = 0;
        for (int i = 0; i < genres_filenames.length; i++)
            books[i] = new Book[books_count[i]];


        int j = 0;
        int i = 0;
        try {
            for (i = 0; i < genres_filenames.length; i++) {

                ois = new ObjectInputStream(new FileInputStream(WRITE_BASE_PATH + genres_filenames[i] + ".ser"));
                j = 0;
                try {
                    while (true) {
                        books[i][j] = (Book) ois.readObject();        // Notice the type cast here; this is the reason
                        j++;
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("Error has occurred while reading the file: " + genres_filenames[i] + ".ser");
                } catch (EOFException e) {
                    continue;
                }
                ois.close();        // Close the file
            }
        } catch (FileNotFoundException e) {
            System.out.println("File: " + genres_filenames[i] + ".ser could not been found.");
            System.out.println("Program will terminate.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Error: Problem Reading from file: " + genres_filenames[i] + ".ser");
            System.out.println("Program will terminate.");
            System.exit(0);
        }

        Scanner scan = new Scanner(System.in);

        int choice = 0;
        int recordCount;
        String in;
        do {
            if(choice<0){
                choice = 0;
            }
            System.out.println("\n-------------------------------------");
            System.out.println("              Main Menu              ");
            System.out.println("-------------------------------------");
            System.out.println("v View the selected file: " + genres_filenames[choice] + ".ser (" + books_count[choice] + " records)");
            System.out.println("s Select a file to view");
            System.out.println("x Exit");
            System.out.println("-------------------------------------");
            System.out.println("Enter Your Choice: ");

            in = scan.nextLine();

            switch (in){
                case "v":
                    System.out.println("Viewing " + genres_filenames[choice] + ".ser (" + books_count[choice] + " records)");
                    int index = 0;
                    do{
                        System.out.println("\nEnter number of records to view: ");
                        recordCount = scan.nextInt();

                        if(recordCount == 0){
                            System.out.println("Exiting to main menu!");
                        } else if (recordCount > 0) {
                            int endCount = index + recordCount - 1;

                            while (index <= endCount && index < books_count[choice]){
                                System.out.println(books[choice][index]);
                                if (index + 1 <= endCount && index + 1 < books_count[choice])
                                    index++;
                                else break;
                            }
                            if(endCount >= books_count[choice]){
                                System.out.println("EOF has been reached!");
                            }



                        } else {

                            int initialIndex = index - ((-1*recordCount) - 1);
                            if(initialIndex < 0){
                                initialIndex = 0;
                            }

                            while (initialIndex <= index) {
                                System.out.println(books[choice][initialIndex]);
                                initialIndex++;
                            }

                            initialIndex = index - ((-1*recordCount) - 1);
                            if(initialIndex < 0){
                                index = 0;
                                System.out.println("BOF has been reached!");
                            }
                            else index = initialIndex;

                        }

                    }while (recordCount != 0);

                    break;
                case "s":
                    do{

                        System.out.println("\n---------------------------------------");
                        System.out.println("              File Sub-Menu              ");
                        System.out.println("---------------------------------------");

                        for(int k = 0; k < genres_filenames.length; k++){
                            System.out.println(k+1 + " " + genres_filenames[k] + ".ser\t(" + books_count[k] + " records)");
                        }
                        System.out.println("9 Exit");
                        System.out.println("---------------------------------------");
                        System.out.println("Enter Your Choice: ");

                        in = scan.nextLine();

                        switch (in){
                            case "1":
                                choice = 0;
                                break;
                            case "2":
                                choice = 1;
                                break;
                            case "3":
                                choice = 2;
                                break;
                            case "4":
                                choice = 3;
                                break;
                            case "5":
                                choice = 4;
                                break;
                            case "6":
                                choice = 5;
                                break;
                            case "7":
                                choice = 6;
                                break;
                            case "8":
                                choice = 7;
                                break;
                            case "9":
                                choice = -1;
                                break;
                            default:
                                choice = -2;
                                System.out.println("Invalid choice!!!\n");
                                break;
                        }
                    }while (choice < -1);
                    break;
                case "x":
                    System.out.println("\nExiting...");
                    break;
            }
        }
        while (!in.equals("x") && !in.equals("X"));
    }
}