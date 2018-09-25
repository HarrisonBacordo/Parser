public class WHILE extends STMT{
    private COND condition;
    private BLOCK whileBlock;

    public WHILE(COND condition, BLOCK whileBlock) {
        this.condition = condition;
        this.whileBlock = whileBlock;
    }

    @Override
    public void execute(Robot robot) {

    }
}
