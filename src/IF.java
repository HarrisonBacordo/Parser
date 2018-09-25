public class IF extends STMT{
    private COND condition;
    private BLOCK ifBlock;
    private BLOCK elseBlock;

    public IF(COND condition, BLOCK ifBlock) {
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = null;
    }

    public IF(COND condition, BLOCK ifBlock, BLOCK elseBlock) {
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public void execute(Robot robot) {
        if(condition.evaluate(robot)) {
            this.ifBlock.execute(robot);
        } else if(elseBlock != null) {
            this.elseBlock.execute(robot);
        }
    }
}
