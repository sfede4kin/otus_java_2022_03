package ru.otus.solid.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.solid.atm.ATM;
import ru.otus.solid.atm.ATMImpl;
import ru.otus.solid.banknote.Banknote;
import ru.otus.solid.banknote.Banknote10;
import ru.otus.solid.banknote.Banknote100;
import ru.otus.solid.banknote.Banknote1000;
import ru.otus.solid.banknote.Banknote500;
import ru.otus.solid.banknote.BanknoteFactory;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ATMRunnerTest {

    @Test
    @DisplayName("Инициализация АТМ")
    void initATMTest(){

        ATM atm = new ATMImpl.Builder()
                .withHolder10(List.of(BanknoteFactory.getBanknote(10),
                        BanknoteFactory.getBanknote(10),
                        BanknoteFactory.getBanknote(10)))
                .withHolder100(List.of(BanknoteFactory.getBanknote(100),
                        BanknoteFactory.getBanknote(100)))
                .withHolder500(List.of(BanknoteFactory.getBanknote(500),
                        BanknoteFactory.getBanknote(500),
                        BanknoteFactory.getBanknote(500)))
                .withHolder1000(List.of(BanknoteFactory.getBanknote(1000),
                        BanknoteFactory.getBanknote(1000)))
                .build();

        assertThat(atm.getBalance()).isEqualTo(3730);

    }

    @Test
    @DisplayName("Пополнение")
    void cashInTest(){

        ATM atm = new ATMImpl.Builder()
                .withHolder10(List.of(BanknoteFactory.getBanknote(10),
                        BanknoteFactory.getBanknote(10),
                        BanknoteFactory.getBanknote(10)))
                .withHolder100(List.of(BanknoteFactory.getBanknote(100),
                        BanknoteFactory.getBanknote(100)))
                .withHolder500(List.of(BanknoteFactory.getBanknote(500),
                        BanknoteFactory.getBanknote(500),
                        BanknoteFactory.getBanknote(500)))
                .withHolder1000(List.of(BanknoteFactory.getBanknote(1000),
                        BanknoteFactory.getBanknote(1000)))
                .build();

        atm.addCash(List.of(BanknoteFactory.getBanknote(10),
                BanknoteFactory.getBanknote(100),
                BanknoteFactory.getBanknote(500),
                BanknoteFactory.getBanknote(1000)
        ));

        assertThat(atm.getBalance()).isEqualTo(5340);

    }

    @Test
    @DisplayName("Сумма снятия доступна")
    void cashOutTest(){

        ATM atm = new ATMImpl.Builder()
                .withHolder10(List.of(BanknoteFactory.getBanknote(10),
                        BanknoteFactory.getBanknote(10),
                        BanknoteFactory.getBanknote(10)))
                .withHolder100(List.of(BanknoteFactory.getBanknote(100),
                        BanknoteFactory.getBanknote(100)))
                .withHolder500(List.of(BanknoteFactory.getBanknote(500),
                        BanknoteFactory.getBanknote(500),
                        BanknoteFactory.getBanknote(500)))
                .withHolder1000(List.of(BanknoteFactory.getBanknote(1000),
                        BanknoteFactory.getBanknote(1000)))
                .build();

        atm.getCash(2620);

        assertThat(atm.getBalance()).isEqualTo(1110);

    }

    @Test
    @DisplayName("Сумма снятия больше доступной")
    void cashOutSumExceptionTest(){

        ATM atm = new ATMImpl.Builder()
                .withHolder10(List.of(BanknoteFactory.getBanknote(10),
                        BanknoteFactory.getBanknote(10),
                        BanknoteFactory.getBanknote(10)))
                .withHolder100(List.of(BanknoteFactory.getBanknote(100),
                        BanknoteFactory.getBanknote(100)))
                .withHolder500(List.of(BanknoteFactory.getBanknote(500),
                        BanknoteFactory.getBanknote(500),
                        BanknoteFactory.getBanknote(500)))
                .withHolder1000(List.of(BanknoteFactory.getBanknote(1000),
                        BanknoteFactory.getBanknote(1000)))
                .build();

        assertThrows(RuntimeException.class, ()-> atm.getCash(4000));

    }

    @Test
    @DisplayName("Сумма выдана минимальным количеством банкнот верного номинала")
    void cashOutBanknoteCheckTest(){

        ATM atm = new ATMImpl.Builder()
                .withHolder10(List.of(BanknoteFactory.getBanknote(10),
                        BanknoteFactory.getBanknote(10),
                        BanknoteFactory.getBanknote(10)))
                .withHolder100(List.of(BanknoteFactory.getBanknote(100),
                        BanknoteFactory.getBanknote(100)))
                .withHolder500(List.of(BanknoteFactory.getBanknote(500),
                        BanknoteFactory.getBanknote(500),
                        BanknoteFactory.getBanknote(500)))
                .withHolder1000(List.of(BanknoteFactory.getBanknote(1000),
                        BanknoteFactory.getBanknote(1000)))
                .build();

        List<Banknote> cash = atm.getCash(1620);

        int b10 = 0, b100 = 0, b500 = 0, b1000 = 0;

        for(Banknote b : cash){
            if(b instanceof Banknote10) b10++;
            if(b instanceof Banknote100) b100++;
            if(b instanceof Banknote500) b500++;
            if(b instanceof Banknote1000) b1000++;
        }

        assertThat(b10).isEqualTo(2);
        assertThat(b100).isEqualTo(1);
        assertThat(b500).isEqualTo(1);
        assertThat(b1000).isEqualTo(1);

    }

    @Test
    @DisplayName("Номинал банкноты не доступен")
    void cashOutBanknoteCountExceptionTest(){

        ATM atm = new ATMImpl.Builder()
                .withHolder10(List.of(BanknoteFactory.getBanknote(10),
                        BanknoteFactory.getBanknote(10),
                        BanknoteFactory.getBanknote(10)))
                .withHolder100(List.of(BanknoteFactory.getBanknote(100),
                        BanknoteFactory.getBanknote(100)))
                .withHolder500(List.of(BanknoteFactory.getBanknote(500),
                        BanknoteFactory.getBanknote(500),
                        BanknoteFactory.getBanknote(500)))
                .withHolder1000(List.of(BanknoteFactory.getBanknote(1000),
                        BanknoteFactory.getBanknote(1000)))
                .build();

        assertThrows(RuntimeException.class, ()-> atm.getCash(15));

    }

}
