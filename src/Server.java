import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Server {
    static KnockingSequence knockingSequence;
    
    public static void main(String[] args){
        configKnocking();
    }

    public static void configKnocking(){
        Scanner scanner = new Scanner(System.in);
        List<Integer> rightsequenceport = new ArrayList<>();

        System.out.println("provide the length of sequence");
        int length = scanner.nextInt();

        for(int i = 0; i < length; i++){
            System.out.println("enter the sequence of port, one by one");
            int port = scanner.nextInt();
            //store the sequence of port that user want into list
            rightsequenceport.add(port);
        }

        knockingSequence = new KnockingSequence(rightsequenceport);
        
        //distinct the list if there is repeated value
        List<Integer> udpports = rightsequenceport.stream()
                    .distinct()
                    .collect(Collectors.toList());

        //run the number of port (distinct already) into new port to make udp connection
        for(Integer i : udpports){
            new Thread(new ServerThread(i)).start();
        }

    }

}
