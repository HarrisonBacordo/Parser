public class IF extends STMT{
    private COND condition;
    private BLOCK ifBlock;
    private BLOCK elseBlock;

    public IF(COND condition, BLOCK ifBlock) {
        this.condition = condition;
        this.ifBlock = ifBlock;
    }

    public IF(COND condition, BLOCK ifBlock, BLOCK elseBlock) {
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public void execute(Robot robot) {

    }
}
