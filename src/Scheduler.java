import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Scheduler {

    private Assignment[] assignmentArray;
    private Integer[] C;
    private Double[] max;
    private ArrayList<Assignment> solutionDynamic;
    private ArrayList<Assignment> solutionGreedy;

    /**
     * @throws IllegalArgumentException when the given array is empty
     */
    public Scheduler(Assignment[] assignmentArray) throws IllegalArgumentException {
        // Should be instantiated with an Assignment array
        // All the properties of this class should be initialized here
        if (assignmentArray.length == 0) {      // If the given json data contains no data, throw exception.
            throw new IllegalArgumentException();
        }
        Arrays.sort(assignmentArray);           // Sorts the array with compareTo method.
        this.assignmentArray = assignmentArray;         // Setting incoming array to attribute.
        solutionDynamic = new ArrayList<Assignment>();  // Instantiating arraylists.
        solutionGreedy = new ArrayList<Assignment>();

        C = new Integer[assignmentArray.length ];    // Creating arrays and filling them with default values.
        max = new Double[assignmentArray.length];
        Arrays.fill(C, -1);
        Arrays.fill(max, -1.0);

        calculateC();                                   // Calculating array values.
        calculateMax(assignmentArray.length - 1);
    }

    /**
     * @param index of the {@link Assignment}
     * @return Returns the index of the last compatible {@link Assignment},
     * returns -1 if there are no compatible assignments
     */
    private int binarySearch(int index) {
        // Default binary search algorithm but modified according to find right-most element.

        int left = 0;
        int right = index - 1;

        while (left <= right) {

            int mid = (left + right) / 2;
            int midfinishTime = Integer.parseInt(assignmentArray[mid].getFinishTime().split(":")[0] + assignmentArray[mid].getFinishTime().split(":")[1]);
            int indstartTime = Integer.parseInt(assignmentArray[index].getStartTime().split(":")[0] + assignmentArray[index].getStartTime().split(":")[1]);

            if (midfinishTime <= indstartTime) {

                int midPlus1FinishTime = Integer.parseInt(assignmentArray[mid + 1].getFinishTime().split(":")[0] + assignmentArray[mid + 1].getFinishTime().split(":")[1]);

                if (midPlus1FinishTime <= indstartTime) // Checking right value of middle index to obtain rightmost element.
                    left = mid + 1;
                else
                    return mid;
            } else
                right = mid - 1;
        }
        return -1;    // If there is no compatible job, return -1.
    }


    /**
     * {@link #C} must be filled after calling this method
     */
    private void calculateC() { // Calculating values of C array indexes with the help of binarySearch method.
        this.C[0] = -1;         // Setting first index of C array -1 as default.
        for (int i = 0; i < assignmentArray.length ; i++) {
            int compatible = binarySearch(i);
            this.C[i] = compatible;
        }
    }


    /**
     * Uses {@link #assignmentArray} property
     *
     * @return Returns a list of scheduled assignments
     */
    public ArrayList<Assignment> scheduleDynamic() { // Dynamic schedule method using findSolutionDynamic.
        findSolutionDynamic(assignmentArray.length - 1);
        Collections.reverse(solutionDynamic);   // Reversing array because of recursion's reverse adding strategy.
        return solutionDynamic;
    }

    /**
     * {@link #solutionDynamic} must be filled after calling this method
     */
    private void findSolutionDynamic(int i) {
        System.out.println("findSolutionDynamic(" + i + ")");
        Integer compatVal = C[i];
        Double maxVal;
        if (assignmentArray.length == 1) { // BASE CASE
            solutionDynamic.add(assignmentArray[0]);    // Adding assignment to solution arraylist
            System.out.println("Adding " + assignmentArray[0] + " to the dynamic schedule");
        }
        if (i == 0) {    // BASE CASE
            solutionDynamic.add(assignmentArray[0]);    // Adding assignment to solution arraylist
            System.out.println("Adding " + assignmentArray[0] + " to the dynamic schedule");
            return;
        }
        else {
            if (compatVal == -1) {
                maxVal = 0.0;
            } else {
                maxVal = max[C[i]];
            }
            // Finds compatible and first finishing assignment before assignment i
            if (assignmentArray[i].getWeight() + maxVal > max[i - 1]) {    // Case 1 where assignment i was included
                solutionDynamic.add(assignmentArray[i]);    // Adding assignment to solution arraylist
                System.out.println("Adding " + assignmentArray[i] + " to the dynamic schedule");
                 if(C[i] == 0){
                    System.out.println("findSolutionDynamic(" + 0 + ")");
                    solutionDynamic.add(assignmentArray[0]);    // Adding assignment to solution arraylist
                    System.out.println("Adding " + assignmentArray[0] + " to the dynamic schedule");
                }
                else if (C[i] != -1) {
                    findSolutionDynamic(C[i]);    // Finds remaining assignments starting the the latest compatible recursively
                }
            } else {    // Case 2 where assignment i was NOT included, removes assignment i from the possible asgs. in solution.
                findSolutionDynamic(i - 1);
            }
        }
    }

    /**
     * {@link #max} must be filled after calling this method
     */
    private Double calculateMax(int i) {    // Recursive function to calculate values of max array.
        if (i == 0 && max[0] != 0) {
            System.out.println("calculateMax(" + i + "): Zero");
            max[0] = assignmentArray[0].getWeight();
            return max[0];
        } else if (i == 0 && max[0] == 0) {
            System.out.println("calculateMax(" + i + "): Zero");
            return max[0];
        } else if (i > 0 && max[i] == -1.0) {
            System.out.println("calculateMax(" + i + "): Prepare");
            max[i] = Math.max(calculateMax(C[i]) + assignmentArray[i].getWeight(), calculateMax(i - 1)); // Choosing maximum of 2 cases.
            return max[i];
        } else if (i > 0 && max[i] != -1.0) {
            System.out.println("calculateMax(" + i + "): Present");
            return max[i];
        } else {
            return 0.0;
        }
    }

    /**
     * {@link #solutionGreedy} must be filled after calling this method
     * Uses {@link #assignmentArray} property
     *
     * @return Returns a list of scheduled assignments
     */

    public ArrayList<Assignment> scheduleGreedy() {

        if (assignmentArray.length == 0) {      // If there is no assignment in the array, pass.
            // no operation
        } else if (assignmentArray.length == 1) {   // If assignment array contains only one value, just add it.
            System.out.println("Adding " + assignmentArray[0] + " to the greedy schedule");
            solutionGreedy.add(assignmentArray[0]);
        } else if (assignmentArray.length == 2) {   // If assignment array contains 2 values, compare them.
            int finishTime = Integer.parseInt(assignmentArray[0].getFinishTime().split(":")[0] + assignmentArray[0].getFinishTime().split(":")[1]);
            int startTime = Integer.parseInt(assignmentArray[1].getFinishTime().split(":")[0] + assignmentArray[1].getFinishTime().split(":")[1]);
            System.out.println("Adding " + assignmentArray[0] + " to the greedy schedule");
            solutionGreedy.add(assignmentArray[0]);
            if (finishTime <= startTime) {  // After adding first assignment in every way, check the second value's availability.
                System.out.println("Adding " + assignmentArray[1] + " to the greedy schedule");
                solutionGreedy.add(assignmentArray[1]);
            }
        } else {
            int firstInd = 0;
            for (int i = 1; i < assignmentArray.length; i++) {      // With iteration, add by checking the availability of the assignments.
                int finishTime = Integer.parseInt(assignmentArray[firstInd].getFinishTime().split(":")[0] + assignmentArray[firstInd].getFinishTime().split(":")[1]);
                int firstIndexStartTime = Integer.parseInt(assignmentArray[firstInd].getStartTime().split(":")[0] + assignmentArray[firstInd].getStartTime().split(":")[1]);
                int startTime = Integer.parseInt(assignmentArray[i].getStartTime().split(":")[0] + assignmentArray[i].getStartTime().split(":")[1]);
                if (solutionGreedy.size() !=0){
                    int lastFinishTime = Integer.parseInt(solutionGreedy.get(solutionGreedy.size()-1).getFinishTime().split(":")[0] + solutionGreedy.get(solutionGreedy.size()-1).getFinishTime().split(":")[1]);
                    if (i == assignmentArray.length-1 && lastFinishTime <= firstIndexStartTime){
                        System.out.println("Adding " + assignmentArray[firstInd] + " to the greedy schedule");
                        solutionGreedy.add(assignmentArray[firstInd]);
                        int val = Integer.parseInt(solutionGreedy.get(solutionGreedy.size()-1).getFinishTime().split(":")[0] + solutionGreedy.get(solutionGreedy.size()-1).getFinishTime().split(":")[1]);
                        if (val <= startTime){
                            System.out.println("Adding " + assignmentArray[i] + " to the greedy schedule");
                            solutionGreedy.add(assignmentArray[i]);
                            break;
                        }
                    }
                }
                if (finishTime <= startTime) {
                    System.out.println("Adding " + assignmentArray[firstInd] + " to the greedy schedule");
                    solutionGreedy.add(assignmentArray[firstInd]);
                    firstInd = i;
                }
            }
        }
        return solutionGreedy;
    }
}
