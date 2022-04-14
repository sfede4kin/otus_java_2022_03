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
        return treeMap.firstEntry();
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> next = treeMap.higherEntry(customer);
        if(next != null){
            //here we also create copies of entry objects
            return  Map.entry(
                        new Customer(next.getKey().getId(), next.getKey().getName(), next.getKey().getScores()),
                        next.getValue()
                    );
        }else{
            return null;
        }
    }

    public void add(Customer customer, String data) {
        treeMap.put(customer, data);
    }
}
