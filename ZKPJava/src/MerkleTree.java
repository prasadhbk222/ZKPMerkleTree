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
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    byte[] byteHash = mDigest.digest(p.getBytes(StandardCharsets.UTF_8));
                    return new String(byteHash, StandardCharsets.UTF_8);
                })
                .collect(Collectors.toList());
        return hashList;
    }

    public List<List<MerkleNode>> findPathInformation(MerkleNode destination){
        List<List<MerkleNode>> output = new ArrayList<>();
        List<MerkleNode> path = new ArrayList<>();
        List<MerkleNode> pathNeighbors = new ArrayList<>();
        findPath(root, destination, path, pathNeighbors);
        // System.out.println("Path: "+ path);
        // System.out.println(pathNeighbors);
        output.add(path);
        output.add(pathNeighbors);
        return output;
    }

    public static String computeHashValue(String str) throws NoSuchAlgorithmException{
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] byteHash = mDigest.digest(str.getBytes(StandardCharsets.UTF_8));
        return new String(byteHash, StandardCharsets.UTF_8);
    }

    public boolean findPath(MerkleNode node, MerkleNode destination, List<MerkleNode> path, List<MerkleNode> pathNeighbors){
        if (node == null)
            return false;

        if (node.equals(destination)){
            return true;
        }

        if (findPath(node.left, destination, path, pathNeighbors)){
            path.add(node);
            if (node.right != null)
                pathNeighbors.add(node.right);
            return true;
        }
        else if (findPath(node.right, destination, path, pathNeighbors)) {
            path.add(node);
            if (node.left != null)
                pathNeighbors.add(node.left);
            return true;
        }

        return false;
    }

    public void printTree(MerkleNode node){
        if(node==null){
            return;
        }
        System.out.println("\n"+node.hash);
        printTree(node.left);
        printTree(node.right);
    }
    
    public static void main(String[] args) throws NoSuchAlgorithmException {
        //get string values and convert to list of hashes
        List<String> values = new ArrayList<>(Arrays.asList("1","2","3", "4", "5"));
        List<String> hashes = getHashList(values);

        //create merkle tree using list of hashes
        MerkleTree tree = new MerkleTree();
        tree.constructMerkleTree(hashes);
        // tree.printTree(tree.root);
        String hashDest = computeHashValue("5");
        // System.out.println(hashDest);
        // tree.findPathInformation(new MerkleNode(hashDest));

        //generate POI
        ProofOfInclusion poi = new ProofOfInclusion(tree);
        poi.generateProofOfInclusion(new MerkleNode(hashDest));

    }
}
