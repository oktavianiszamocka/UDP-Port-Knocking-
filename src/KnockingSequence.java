
import java.util.*;

public class KnockingSequence {
   private List<Integer> rightsequenceport;
   static Map<Integer, List<Integer>> sequence;

    public KnockingSequence(List<Integer> rightsequenceport){
    	//initiate the right sequence
        this.rightsequenceport = rightsequenceport;
        sequence = new HashMap<>();
    }

    synchronized void add(Integer i, int port){
    	//if the key is not exist, insert an entry
        sequence.computeIfAbsent(i, list -> new ArrayList<Integer>());
        //if key is exist, input the port into the list (value)
        if(sequence.containsKey(i)) {
            sequence.get(i).add(port);
        }
    }

    synchronized boolean equal(Integer i){
    	//check whether the list of port he knocked is equal with the right sequence
        return sequence.get(i).equals(rightsequenceport);
    }
    
    synchronized void remove(Integer i){
    	//removed the previous trial if the sender want to try to knock again 
        sequence.remove(i);
    }


}
