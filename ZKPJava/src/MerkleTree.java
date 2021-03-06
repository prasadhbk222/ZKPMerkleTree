import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MerkleTree {
    public MerkleNode root;
    public int numNodes = 0;

    public void constructMerkleTree(List<String> hashes, String algo) throws NoSuchAlgorithmException {
        //Create leaf nodes using hashes
        List<MerkleNode> nodes = hashes
                .stream()
                .map(p-> new MerkleNode(p))
                .collect(Collectors.toList());
        numNodes = hashes.size();
        while(nodes.size()>1){
            List<MerkleNode> parents = new ArrayList<>();

            for(int i=0; i<nodes.size(); i+=2){
                //create parent
                MerkleNode parent = new MerkleNode();
                parents.add(parent);

                parent.left = nodes.get(i);
                if((i+1)<nodes.size()){
                    parent.right = nodes.get(i+1);
                }
                parent.hash = parent.computeHash(algo);
            }

            nodes = parents;
            numNodes += parents.size();
        }

        root = nodes.get(0);
    }



    public static List<String> getHashList(List<String> values, String algo) {
        List<String> hashList = values
                .stream()
                .map(p-> {
                    MessageDigest mDigest = null;
                    try {
                        mDigest = MessageDigest.getInstance(algo);
                    } catch (NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    byte[] byteHash = mDigest.digest(p.getBytes(StandardCharsets.UTF_8));
                    return new String(byteHash, StandardCharsets.UTF_8);
                })
                .collect(Collectors.toList());
        return hashList;
    }

    public Set<MerkleNode> findPathInformation(MerkleNode destination){
        Set<MerkleNode> path = new HashSet<>();
        findPath(root, destination, path);
        
        return path;
    }

    public static String computeHashValue(String str, String algo) throws NoSuchAlgorithmException{
        MessageDigest mDigest = MessageDigest.getInstance(algo);
        byte[] byteHash = mDigest.digest(str.getBytes(StandardCharsets.UTF_8));
        return new String(byteHash, StandardCharsets.UTF_8);
    }

    public boolean findPath(MerkleNode node, MerkleNode destination, Set<MerkleNode> path){
        if (node == null)
            return false;

        if (node.equals(destination)){
            return true;
        }

        if (findPath(node.left, destination, path)){
            path.add(node);
            return true;
        }
        else if (findPath(node.right, destination, path)) {
            path.add(node);
            return true;
        }

        return false;
    }

    public void printTree(MerkleNode node){
        if(node==null){
            return;
        }
        printTree(node.left);
        printTree(node.right);
    }

    public boolean validateProof(ProofOfInclusion poi, String algo) throws NoSuchAlgorithmException{
        //-1 as we start from 0
        int leafDepth = (int) Math.ceil(log2(poi.numNodes)) - 1;
        //first bit must be 1
        if(poi.hashes.size()==0 || poi.bits.size()==0 || poi.bits.peekFirst()==0) return false;
        try{
            MerkleNode proof = construct(leafDepth, 0, poi, algo);
            if(poi.bits.size()>0 || poi.hashes.size()>0){
                return false;
            }
            // System.out.println("\nProof root "+proof.hash);
            return root.equals(proof);
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }       
    }

    public MerkleNode construct(int leafDepth, int depth, ProofOfInclusion poi, String algo) throws NoSuchAlgorithmException{
        //least significant bit
        if(poi.bits.size()==0){
            return null;
        }
        int lsb = poi.bits.removeFirst();        
        MerkleNode node = new MerkleNode();
        if(depth == leafDepth){
            String hashStr = poi.hashes.remove(0);
            if(hashStr!=null){
               node.hash = hashStr;     
            }           
        }else if(lsb == 1){
            node.left = construct(leafDepth, depth+1, poi, algo);
            node.right = construct(leafDepth, depth+1, poi, algo);
            node.hash = node.computeHash(algo);
        } else{
            String hashStr = poi.hashes.remove(0);
            if(hashStr!=null){
                node.hash = hashStr;     
            }  
        }
        return node;
    }

    public static double log2(int N)
    { 
        // calculate log2 N indirectly
        // using log() method
        return (Math.log(N) / Math.log(2)); 
    }
 
    
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String algo = "SHA-512";
        //get string values and convert to list of hashes
        List<String> values = new ArrayList<>(Arrays.asList("1","2","3", "4", "5","6"));
        List<String> hashes = getHashList(values, algo);
        
        //create merkle tree using list of hashes
        MerkleTree tree = new MerkleTree();
        tree.constructMerkleTree(hashes, algo);
        System.out.println("Root of merkle tree = "+ tree.root);

        //compute hash of destination
        String hashDest = computeHashValue("5", algo);
        //generate POI
        ProofOfInclusion poi = new ProofOfInclusion(tree);
        poi.generateProofOfInclusion(new MerkleNode(hashDest));

        System.out.println("Validated "+tree.validateProof(poi, algo));
    }
}
