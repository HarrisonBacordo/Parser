public class NUM extends EXP{
    private int num;

    public NUM(int num) {
        this.num = num;
    }

    @Override
    public void execute(Robot robot) {

    }

    @Override
    public int evaluate(Robot robot) {
        return this.num;
    }
}
