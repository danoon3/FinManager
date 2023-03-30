public class MaxCategory {
    private String category;
    private int sum;

    public MaxCategory(String category, int sum){
        this.sum = sum;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
