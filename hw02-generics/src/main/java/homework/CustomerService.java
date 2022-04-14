package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    private final TreeMap<Customer, String> treeMap;

    public CustomerService(){
        this.treeMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));
    }

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return copyCustomerMapEntry(treeMap.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> next = treeMap.higherEntry(customer);
        //here we also create copies of entry objects
        return next != null ? copyCustomerMapEntry(next) : null;
    }

    public void add(Customer customer, String data) {
        treeMap.put(customer, data);
    }

    private static Map.Entry<Customer, String> copyCustomerMapEntry(Map.Entry<Customer, String> entry){
        return  Map.entry(
                    new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores()),
                    entry.getValue()
        );
    }
}
