import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


class JsonOperationsTest {

    @Test
    void sumCalculation() {
        JsonOperations jsonOperations = new JsonOperations();
        JsonFromClient jsonFromClient = new JsonFromClient();

        jsonOperations.createCategoriesFile();
        jsonOperations.createCategoryMap();

        jsonFromClient.setTitle("колбаса");
        jsonFromClient.setSum(200);

        jsonOperations.sumCalculation(jsonFromClient);

        jsonFromClient.setTitle("булка");
        jsonFromClient.setSum(500);

        Map<String, Integer> expected = jsonOperations.sumCalculation(jsonFromClient);

        Map<String, Integer> actual = new HashMap<>();
        actual.put("еда", 700);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findingMax() {
        JsonOperations jsonOperations = new JsonOperations();
        JsonFromClient jsonFromClient = new JsonFromClient();

        jsonOperations.createCategoriesFile();
        jsonOperations.createCategoryMap();

        jsonFromClient.setTitle("колбаса");
        jsonFromClient.setSum(200);

        jsonOperations.sumCalculation(jsonFromClient);

        jsonFromClient.setTitle("тапки");
        jsonFromClient.setSum(500);

        jsonOperations.sumCalculation(jsonFromClient);

        Root actual = jsonOperations.findingMax();

        Root expected = new Root();
        expected.setMaxCategory(new MaxCategory("одежда", 500));

        Assertions.assertEquals(expected.getMaxCategory().getCategory(), actual.getMaxCategory().getCategory());
        Assertions.assertEquals(expected.getMaxCategory().getSum(), actual.getMaxCategory().getSum());
    }
}