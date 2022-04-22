import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MerkleTree {
    public MerkleNode root;

    public void constructMerkleTree(List<String> hashes) throws NoSuchAlgorithmException {
        //Create leaf nodes using hashes
        List<MerkleNode> nodes = hashes
                .stream()
                .map(p-> new MerkleNode(p))
                .collect(Collectors.toList());

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
                parent.hash = parent.computeHash();
            }

            nodes = parents;
        }

        root = nodes.get(0);
    }



    public static List<String> getHashList(List<String> values) {
        List<String> hashList = values
                .stream()
                .map(p-> {
                    MessageDigest mDigest = null;
                    try {
                        mDigest = MessageDigest.getInstance("SHA-256");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    byte[] byteHash = mDigest.digest(p.getBytes(StandardCharsets.UTF_8));
                    return new String(byteHash, StandardCharsets.UTF_8);
                })
                .collect(Collectors.toList());
        return hashList;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        //get string values and convert to list of hashes
        List<String> values = new ArrayList<>(Arrays.asList("1","2","3"));
        List<String> hashes = getHashList(values);

        //create merkle tree using list of hashes
        MerkleTree tree = new MerkleTree();
        tree.constructMerkleTree(hashes);
    }
}
