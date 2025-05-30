/* public int min() {
    if (numbers == null || numbers.isEmpty()) {
        throw new IllegalStateException("List is empty or not initialized");
    }

    int min = numbers.get(0);
    for (int num : numbers) {
        if (num < min) {
            min = num;
        }
    }
    return min;
}

public int max() {
    if (numbers == null || numbers.isEmpty()) {
        throw new IllegalStateException("List is empty or not initialized");
    }

    int max = numbers.get(0);
    for (int num : numbers) {
        if (num > max) {
            max = num;
        }
    }
    return max;
}

 */

import java.util.ArrayList;

public double getScore (ArrayList<Integer> corrects, int[] answers){
    int rights = 0;
    for (int i = 0; i < corrects.size(); i++){
        if (corrects.get(i) < 0 || (corrects.get(i)) > 3){
            throw new IllegalStateException("Number Not Allowed");
        }
        else{
            if (corrects.get(i).equals(answers[i])){
                rights++;
            }
        }
    }
    return (double)(rights / answers.length);
}