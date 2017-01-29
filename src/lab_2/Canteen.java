package lab_2;

public class Canteen {

    private int foodBank = 70;

    private int portionsQuantity = 0;

    public synchronized boolean feedAStudent(int foodAmount, int studNum) {
        if (foodBank - foodAmount >= 0) {
            foodBank -= foodAmount;
            System.out.println("Stud " + studNum + " ate " + foodAmount + " of food. " + foodBank + " of food remained");
            portionsQuantity++;
            return true;
        } else {
            System.out.println("Unsufficient food, stud " + studNum + " remains hungry");
            return false;
        }
    }

    public static void startThreads(Thread[] threads) {
        for (Thread thread :
                threads) {
            thread.start();
        }
    }

    public static void joinThreads(Thread[] threads) throws InterruptedException {
        for (Thread thread :
                threads) {
            thread.join();
        }
    }

    public static void main(String[] args) {
        Canteen khaiCanteen = new Canteen();
        Thread[] studz = new Thread[4];

        studz[0] = new Thread(new Student(2, 500, khaiCanteen));
        studz[1] = new Thread(new Student(4, 2000, khaiCanteen));
        studz[2] = new Thread(new Student(15, 3000, khaiCanteen));
        studz[3] = new Thread(new Student(3, 300, khaiCanteen));

        startThreads(studz);

        try {
            joinThreads(studz);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("Portions served: " + khaiCanteen.portionsQuantity);
    }
}
