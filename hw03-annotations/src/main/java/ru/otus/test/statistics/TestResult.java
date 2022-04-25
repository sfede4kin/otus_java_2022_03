package ru.otus.test.statistics;

public class TestResult {
    private final String name;
    private int success;
    private int failed;

    public TestResult(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getSuccess() {
        return success;
    }

    public int getFailed() {
        return failed;
    }

    public int getTotal() {
        return success + failed;
    }

    public void addSuccess() {
        this.success++;
    }

    public void addFailed() {
        this.failed++;
    }

    @Override
    public String toString(){
        return "Class " + getName() + ": success = " + getSuccess()+ ", failed = " + getFailed() + ", total = " + getTotal();
    }
}
