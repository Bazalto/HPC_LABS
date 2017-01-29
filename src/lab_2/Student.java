package lab_2;

public class Student implements Runnable {

    static int num = 0;

    private int foodConsumption;

    private int sleepTime;

    private int number;

    private Canteen canteen;

    Student(int foodConsumption, int sleepTime, Canteen canteen) {
        this.foodConsumption = foodConsumption;
        this.sleepTime = sleepTime;
        this.canteen = canteen;
        number = ++num;
    }

    @Override
    public void run() {
        while (canteen.feedAStudent(foodConsumption, number)) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
