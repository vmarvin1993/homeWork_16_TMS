import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

/**
 * Написать пограмму со следующим функционалом:
 *
 * Программа на вход получает путь к папке (задается через консоль).
 * В заданной папке находятся текстовые файлы (формат тхт).
 * Каждый файл содержит произвольный текст. В этом тексте может быть номер документа(один или несколько), емейл и номер телефона.
 * 	номер документа в формате: xxxx-yyy-xxxx-yyy-xyxy, где x - это любая цифра, а y - это любая буква русского или латинского алфавита
 * 	номер телефона в формате: +(ХХ)ХХХХХХХ
 *
 * Документ может содержать не всю информацию, т.е. например, может не содержать номер телефона, или другое поле.
 * Необходимо извлечь информацию из N текстовых документов. Число документов для обработки N задается с консоли.
 * Если в папке содержится меньше документов, чем заданое число - следует обрабатывать все документы.
 * Извлеченную информацию необходимо сохранить в следующую стурктуру данных:
 * Map<String, Document>, где
 * 	ключ типа String - это имя документа без расширения,
 * 	значение типа Document - объект кастомного класса, поля которого содержат извлеченную из текстового документа информацию
 *
 * Учесть вывод сообщений на случаи если,
 * 	- на вход передан пусть к папке, в которой нет файлов
 * 	- все файлы имеют неполходящий формат (следует обрабатывать только тхт файлы)
 * 	- так же сообщения на случай других исключительных ситуаций
 *
 * В конце работы программы следует вывести сообщение о том, сколько документов обработано и сколько было документов невалидного формата.
 */

public class Runner extends Document {

    static Scanner input;
    static List<String> listDoc;
    static Pattern readNumDoc = Pattern.compile("\\d{4}[-][a-zа-я]{3}[-]\\d{4}[-][a-zа-я]{3}[-]\\d[a-zа-я]\\d[a-zа-я]", Pattern.CASE_INSENSITIVE);
    static Pattern readMobNum = Pattern.compile("^\\+\\d{3}\\-\\(\\d{2}\\)\\-\\d{3}\\-\\d{2}\\-\\d{2}$");
    static Pattern readMail = Pattern.compile("^([a-z0-9_\\.-]+)@([a-z0-9_\\.-]+)\\.([a-z\\.]{2,6})$");
    static List<File> numberOfFiles;
    static Map<String, Document> informationFile = new HashMap<>();
    static List<File> pathFileArray = new ArrayList<>();

    //Read and check pattern doc numbers
    private static List<String> docNumReader(File file) {
        listDoc = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String docOneLine;
            while ((docOneLine = br.readLine()) != null) {
                Matcher docNumMatcher = readNumDoc.matcher(docOneLine);
                if (docNumMatcher.find()) {
                    listDoc.add("Document number: " + docOneLine.substring(docNumMatcher.start(), docNumMatcher.end()));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listDoc;
    }

    //Read and check pattern mobile
    private static String mobNumReader(File file) {
        String result = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String docOneLine;
            while ((docOneLine = br.readLine()) != null) {
                Matcher mobNumMatcher = readMobNum.matcher(docOneLine);
                if (mobNumMatcher.find()) {
                    result = "Mobile number: " + docOneLine.substring(mobNumMatcher.start(), mobNumMatcher.end());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //Read and check pattern mail
    private static String mailReader(File file) {
        String result = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String docOneLine;
            while ((docOneLine = br.readLine()) != null) {
                Matcher gmailMatcher = readMail.matcher(docOneLine);
                if (gmailMatcher.find()) {
                    result = "Email " + docOneLine.substring(gmailMatcher.start(), gmailMatcher.end());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    //validate files
    private static void validator(List<File> collectionToWrite) {
        int i = 0;

        input = new Scanner(System.in);
        System.out.print("Chose directory: ");
        String path = input.nextLine();

        File file = new File(path);
        if (file.isDirectory() && file.list().length != 0) {
            File[] files = file.listFiles();
            for (File item : files) {
                if (item.getName().endsWith(".txt")) {
                    collectionToWrite.add(item);
                } else {
                    i++;
                    System.out.println(item.getName() + " - not .txt");
                }
            }
        } else {
            System.out.println("Directory is empty");
        }
        System.out.println("Number of non-valid documents: " + i);

    }
    //write information to collection
    private static void collectWriter(List<File> collectionOfPaths, Map<String, Document> collectionToWrite) {
        input = new Scanner(System.in);
        System.out.print("Chose number of files to process: ");
        int setNumberFiles = input.nextInt();

        numberOfFiles = collectionOfPaths.stream()
                .limit(setNumberFiles)
                .collect(Collectors.toList());

        for (File item2 : numberOfFiles) {
            Document g = new Document();
            g.setDocNum(docNumReader(item2));
            g.setMobNum(mobNumReader(item2));
            g.setEmail(mailReader(item2));

            String temp = item2.getName();
            temp = temp.substring(0, temp.lastIndexOf('.'));
            collectionToWrite.put(temp, new Document(g.getDocNum(), g.getMobNum(), g.getEmail()));
        }
        System.out.println("Number of valid documents: " + collectionToWrite.size());
        System.out.println(collectionToWrite);
        System.out.println();

        input.close();
    }

    public static void main(String[] args) {

        informationFile = new HashMap<>();
        pathFileArray = new ArrayList<>();

        validator(pathFileArray);
        collectWriter(pathFileArray, informationFile);

    }
}
// Result:
//    Chose number of files to process: 2
//        Number of valid documents: 2
//        {file2=Document Number: [Document number: 3333-qwe-4444-asd-6q5b], mobile: Mobile number: +375-(29)-114-99-29, email: Email test@mail.ru
//, file1=Document Number: [Document number: 4214-QQQ-4441-SSd-6j1z], mobile: Mobile number: +375-(33)-111-33-44, email: Email test@gmail.com
//}


