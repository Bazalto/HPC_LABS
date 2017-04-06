package lab_10.server;


import java.util.HashMap;
import java.util.Map;

public class BankServiceImpl implements BankService {

    private Map<String, Double> accounts;

    BankServiceImpl() {
        accounts = new HashMap<>();
        accounts.put("acc-01", 100.0);
        accounts.put("acc-02", 100.0);
    }

    @Override
    public void deposit(String account, double sum) {
        double currentSum = accounts.get(account);
        accounts.put(account, currentSum + sum);
        System.out.println("Account: " + account +
                "; deposit: " + sum +
                "; Current sum: " +
                accounts.get(account)
        );
    }

    @Override
    public void withdraw(String account, double sum) {
        double currentSum = accounts.get(account);
        double sumLeft = currentSum - sum;
        if (sumLeft >= 0) {
            accounts.put(account, sumLeft);
            System.out.println("Acc :" + account +
                    "; withdrawal: " + sum +
                    "; Current sum: " + sumLeft
            );
        } else {
            System.out.println("Insufficient funds");
        }
    }

    @Override
    public void transfer(String accountFrom, String accountTo, double sum) {
        if (accounts.get(accountTo) == null) {
            System.out.println("Recipient account does not exist");
            return;
        }

        double senderAccountSum = accounts.get(accountFrom);
        double senderMoneyLeft = senderAccountSum - sum;
        if (senderMoneyLeft < 0) {
            System.out.println("Insufficient funds on sender's account");
            return;
        }
        accounts.put(accountFrom, senderMoneyLeft);

        double receiverCurrentSum = accounts.get(accountTo);
        accounts.put(accountTo, receiverCurrentSum + sum);

        System.out.println("Transfer " + sum +
                " from " + accountFrom +
                " to " + accountTo +
                ";\nSender money left: " + senderMoneyLeft +
                "\nReceiver money now: " + accounts.get(accountTo)
        );
    }
}
