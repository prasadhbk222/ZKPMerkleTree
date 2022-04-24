import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Util {

    static List<String> ENCRYPTION_ALGOS = new ArrayList<>(Arrays.asList("SHA-1", "SHA-256","SHA-384","SHA-512","MD5"));
    // static List<Integer> strLen = new ArrayList<>(Arrays.asList(25, 50, 100, 150, 200, 250));
    static List<Integer> STR_LEN = new ArrayList<>(Arrays.asList(50, 100));
    static List<Integer> NUM_OF_USERS = new ArrayList<>(Arrays.asList(100, 200));

    public static List<String> getRandom(int length, int users)
    {
       Random rand = new Random(); //instance of random class
       List<String> secret_ids = new ArrayList<>();
       String total_characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
       StringBuilder randomString = new StringBuilder();
       String curr = "";
       for(int j=0; j<users; j++){
            randomString = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int index = rand.nextInt(total_characters.length()-1);
                randomString.append(total_characters.charAt(index));
                curr = randomString.toString();           
            }
            if(!secret_ids.contains(curr)){
                secret_ids.add(curr);
            }else{
                j--;
            }
       }        
       return secret_ids;
    }

    public static void printList(List<String> list){
        for(String str: list){
            System.out.println(str);
        }
    }

    public static void main(String[] args) {        
        System.out.println("Java Generate Random String Fixed Length");          
        printList(getRandom(100, 100));
        printList(ENCRYPTION_ALGOS);
    }  
}
