import java.io.*;
import java.util.*;

public class JsonOperations implements Serializable {
    private Map<String, String> categories = new HashMap<>();
    private Map<String, Integer> sumCategories = new HashMap<>();
    private String category;

    public void createCategoriesFile() {
        File file = new File("./categories.tsv");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false))) {
            String[] cat = {"булка" + '\t' + "еда", "колбаса" + '\t' + "еда",
                    "сухарики" + '\t' + "еда", "курица" + '\t' + "еда", "тапки" + '\t' + "одежда",
                    "шапка" + '\t' + "одежда", "мыло" + '\t' + "быт", "акции" + '\t' + "финансы"};

            StringBuilder sb = new StringBuilder();
            for (String i : cat) {
                sb.append(i).append("\n");
            }
            String result = sb.toString();
            bufferedWriter.write(result);
            bufferedWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> createCategoryMap() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("./categories.tsv"))) {
            String[] words;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                words = line.split("\t");
                categories.put(words[0], words[1]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return categories;
    }

    public Map<String, Integer> sumCalculation(JsonFromClient jsonFromClient) {
        if (categories.get(jsonFromClient.getTitle()) == null) {
            category = "другое";
        } else {
            category = categories.get(jsonFromClient.getTitle());
        }

        if (sumCategories.get(category) == null) {
            sumCategories.put(category, jsonFromClient.getSum());
        } else {
            int sum = sumCategories.get(category);
            int number = sum + jsonFromClient.getSum();
            sumCategories.put(category, number);
        }
        return sumCategories;
    }

    public Root findingMax() {
        Map.Entry<String, Integer> maxEntry = sumCategories.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElse(null);
        String key = maxEntry.getKey();
        int value = maxEntry.getValue();
        MaxCategory maxCategory = new MaxCategory(key, value);

        Root root = new Root();
        root.setMaxCategory(maxCategory);

        return root;
    }

    public void saveBin(File binFile) {
        categories = getCategories();
        sumCategories = getSumCategories();
        category = getCategory();

        try (FileOutputStream fileOutputStream = new FileOutputStream(binFile);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static JsonOperations loadFromBinFile(File binFile) {
        JsonOperations jsonOperations = null;
        try (FileInputStream fileInputStream = new FileInputStream(binFile);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            jsonOperations = (JsonOperations) objectInputStream.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return jsonOperations;
    }

    public Map<String, String> getCategories() {
        return categories;
    }

    public Map<String, Integer> getSumCategories() {
        return sumCategories;
    }

    public String getCategory() {
        return category;
    }
}
