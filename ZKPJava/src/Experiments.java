import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Experiments {

    public static void testEncryptionAlgosForIncreasingLength(int numUsers) throws NoSuchAlgorithmException{
        
        Map<String, Long> constructionMap = new HashMap<>();
        Map<String, Long> proofGenerationMap = new HashMap<>();
        Map<String, Long> proofValidationMap = new HashMap<>();

        for(int length: Util.STR_LEN){
            //generate secret ids
            List<String> secret_ids = Util.getRandom(length, numUsers);
            long startTime = 0, endTime = 0, totalTime = 0;

            for(String algo : Util.ENCRYPTION_ALGOS){
                String key = algo+"_"+length;
                //create merkle tree using list of hashes
                MerkleTree tree = new MerkleTree();
                List<String> hashes = MerkleTree.getHashList(secret_ids, algo);
                
                startTime = System.nanoTime();
                tree.constructMerkleTree(hashes, algo);
                endTime = System.nanoTime();
                totalTime = endTime - startTime;
                constructionMap.put(key, totalTime);
                
                //generate POI
                startTime = System.nanoTime();
                List<ProofOfInclusion> poiList = new ArrayList<>();
                for(String hash: hashes){
                    ProofOfInclusion poi = new ProofOfInclusion(tree);
                    poi.generateProofOfInclusion(new MerkleNode(hash));
                }
                endTime = System.nanoTime();
                totalTime = endTime - startTime;
                proofGenerationMap.put(key, totalTime/numUsers);

                //validate POI
                startTime = System.nanoTime();
                for(ProofOfInclusion poi: poiList){
                    tree.validateProof(poi, algo);
                }
                endTime = System.nanoTime();
                totalTime = endTime - startTime;
                proofValidationMap.put(key, totalTime/numUsers);
            }
        }  
        System.out.println("\nTree Construction: "+ constructionMap);
        System.out.println("\nProof Generation: "+ proofGenerationMap);
        System.out.println("\nProof Validation: "+proofValidationMap);
    }

    public static void testEncryptionAlgosForIncreasingNumOfUsers(int length) throws NoSuchAlgorithmException{
        
        Map<String, Long> constructionMap = new HashMap<>();
        Map<String, Long> proofGenerationMap = new HashMap<>();
        Map<String, Long> proofValidationMap = new HashMap<>();

        for(int numUsers: Util.NUM_OF_USERS){
            //generate secret ids
            List<String> secret_ids = Util.getRandom(length, numUsers);
            long startTime = 0, endTime = 0, totalTime = 0;

            for(String algo : Util.ENCRYPTION_ALGOS){
                String key = algo+"_"+numUsers;
                //create merkle tree using list of hashes
                MerkleTree tree = new MerkleTree();
                List<String> hashes = MerkleTree.getHashList(secret_ids, algo);
                
                startTime = System.nanoTime();
                tree.constructMerkleTree(hashes, algo);
                endTime = System.nanoTime();
                totalTime = endTime - startTime;
                constructionMap.put(key, totalTime);
                
                //generate POI
                startTime = System.nanoTime();
                List<ProofOfInclusion> poiList = new ArrayList<>();
                for(String hash: hashes){
                    ProofOfInclusion poi = new ProofOfInclusion(tree);
                    poi.generateProofOfInclusion(new MerkleNode(hash));
                }
                endTime = System.nanoTime();
                totalTime = endTime - startTime;
                proofGenerationMap.put(key, totalTime/numUsers);

                //validate POI
                startTime = System.nanoTime();
                for(ProofOfInclusion poi: poiList){
                    tree.validateProof(poi, algo);
                }
                endTime = System.nanoTime();
                totalTime = endTime - startTime;
                proofValidationMap.put(key, totalTime/numUsers);
            }
        }  
        System.out.println("\nTree Construction: "+ constructionMap);
        System.out.println("\nProof Generation: "+ proofGenerationMap);
        System.out.println("\nProof Validation: "+proofValidationMap);

    }

    public static void testEncryptionAlgosForIncreasingUsers(int length) throws NoSuchAlgorithmException{
        
        Map<Integer, Map<String, Long>> constructionMap = new HashMap<>();
        Map<Integer, Map<String, Long>> generationMap = new HashMap<>();
        Map<Integer, Map<String, Long>> validationMap = new HashMap<>();

        for(int numUsers: Util.NUM_OF_USERS){
            //generate secret ids
            List<String> secret_ids = Util.getRandom(length, numUsers);
            long startTime = 0, endTime = 0, totalTime = 0;
            constructionMap.put(numUsers, new HashMap<>());
            generationMap.put(numUsers, new HashMap<>());
            validationMap.put(numUsers, new HashMap<>());

            for(String algo : Util.ENCRYPTION_ALGOS){
                // String key = algo+"_"+numUsers;
                //create merkle tree using list of hashes
                MerkleTree tree = new MerkleTree();
                List<String> hashes = MerkleTree.getHashList(secret_ids, algo);
                
                startTime = System.nanoTime();
                tree.constructMerkleTree(hashes, algo);
                endTime = System.nanoTime();
                totalTime = endTime - startTime;

                constructionMap.get(numUsers).put(algo, totalTime);
                
                //generate POI
                startTime = System.nanoTime();
                List<ProofOfInclusion> poiList = new ArrayList<>();
                for(String hash: hashes){
                    ProofOfInclusion poi = new ProofOfInclusion(tree);
                    poi.generateProofOfInclusion(new MerkleNode(hash));
                    poiList.add(poi);
                }
                endTime = System.nanoTime();
                totalTime = endTime - startTime;
                generationMap.get(numUsers).put(algo, totalTime/numUsers);

                //validate POI
                startTime = System.nanoTime();
                for(ProofOfInclusion poi: poiList){
                    tree.validateProof(poi, algo);
                }
                endTime = System.nanoTime();
                totalTime = endTime - startTime;
                validationMap.get(numUsers).put(algo, totalTime/numUsers);
            }
        }  
        System.out.println("\nTree Construction: "+ constructionMap);
        System.out.println("\nProof Generation: "+ generationMap);
        System.out.println("\nProof Validation: "+validationMap);

    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // if(args[0]!=null){
        //     int numUsers = Integer.parseInt(args[0]);
        //     testEncryptionAlgosForIncreasingLength(numUsers);
        // }

        // testEncryptionAlgosForIncreasingNumOfUsers(10);
        testEncryptionAlgosForIncreasingUsers(10);

    }
}
