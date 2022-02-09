public class Assignment implements Comparable<Assignment> {
    private String name;
    private String start;
    private int duration;
    private int importance;
    private boolean maellard;

    public Assignment(String name, String start, int duration, int importance, boolean maellard) {
        this.name = name;
        this.start = start;
        this.duration = duration;
        this.importance = importance;
        this.maellard = maellard;
    }

    /*
            Getter methods
         */
    public String getName() {

        return this.name;
    }

    public String getStartTime() {
        return this.start;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getImportance() {
        return this.importance;
    }

    public boolean isMaellard() {
        return this.maellard;
    }

    /**
     * Finish time should be calculated here
     *
     * @return calculated finish time as String
     */
    public String getFinishTime() {
        int hour = Integer.parseInt(getStartTime().split(":")[0]);  // Parsing string "start time" to integer
        hour += duration;                                            // Adding duration to obtain finish time
        String finishTime = "";
        if(hour<10)
            finishTime += "0";
        finishTime += hour + ":" + getStartTime().split(":")[1];
        return finishTime;
                 // Returning string "finish time"
    }

    /**
     * Weight calculation should be performed here
     *
     * @return calculated weight
     */
    public double getWeight() {
        // Calculating weights.
        return ((double)(importance * (isMaellard() ? 1001 : 1))) / duration;
    }

    /**
     * This method is needed to use {@link java.util.Arrays#sort(Object[])} ()}, which sorts the given array easily
     *
     * @param asg Assignment to compare to
     * @return If self > object, return > 0 (e.g. 1)
     * If self == object, return 0
     * If self < object, return < 0 (e.g. -1)
     */
    @Override
    public int compareTo(Assignment asg) {
        // Comparing assignments according to their finish time (integer)
        if (this.getFinishTime().equalsIgnoreCase(asg.getFinishTime()))
            return 0;
        else if (Integer.parseInt(this.getFinishTime().split(":")[0] + this.getFinishTime().split(":")[1]) > Integer.parseInt(asg.getFinishTime().split(":")[0] + asg.getFinishTime().split(":")[1]))
            return 1;
        else
            return -1;
    }

    /**
     * @return Should return a string in the following form:
     * Assignment{name='Refill vending machines', start='12:00', duration=1, importance=45, maellard=false, finish='13:00', weight=45.0}
     */
    @Override
    public String toString() {
        return "Assignment{name='" + name + "', start='" + start + "', duration="+ duration +", importance=" + importance + ", maellard="+ maellard + ", finish='"+ getFinishTime()+ "', weight="+ getWeight() +"}";
    }
}
