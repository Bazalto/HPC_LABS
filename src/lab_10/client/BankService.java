package lab_10.client;


public interface BankService {
    void deposit(String account, double sum);

    void withdraw(String account, double sum);

    void transfer(String accountFrom, String accountTo, double sum);
}
