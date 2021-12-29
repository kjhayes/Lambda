public class BetaReducer {
    public boolean single_reduction_per_run = false;

    public boolean reduction_has_occurred = false;

    public boolean ShouldContinue(){
        if(single_reduction_per_run){
            return !reduction_has_occurred;
        }
        return true;
    }

    public void InitForReduction(){
        reduction_has_occurred = false;
    }
}
