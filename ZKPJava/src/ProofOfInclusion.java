import java.util.ArrayList;
import java.util.List;


public class ProofOfInclusion {
    MerkleTree tree;
    List<String> hashes;
    List<Integer> bits;
    // int numNodes;

    public ProofOfInclusion(MerkleTree tree){
        this.tree = tree;
        hashes = new ArrayList<>();
        bits = new ArrayList<>();
    }

    public void generateProofOfInclusion(MerkleNode destination){
        hashes = new ArrayList<>();
        bits = new ArrayList<>();
        List<List<MerkleNode>> pathInfo = tree.findPathInformation(destination);
        List<MerkleNode> path = pathInfo.get(0);
        // System.out.println("Root = "+tree.root);
        // System.out.println("Destination = "+destination);
        traverseTree(tree.root, destination, path);

        System.out.println("List of Hashes "+hashes.size());
        for(String hash : hashes){
            System.out.println("\n"+hash);
        }

        //System.out.println(hashes);
        System.out.println(bits);
    }

    public void traverseTree(MerkleNode node, MerkleNode destination, List<MerkleNode> path){
        if(node == null){
            return;
        }
        if(node.equals(destination)){
            // System.out.println("Found Destination "+node.hash);
            bits.add(1);
            hashes.add(node.hash);
            return;
        }else if(isInPath(path, node)){
            bits.add(1);
            traverseTree(node.left, destination, path);
            traverseTree(node.right, destination, path);
            return;
        }else{
            // System.out.println("Not Inside Path "+ node.hash);
            bits.add(0);
            hashes.add(node.hash);
            return;
        }
    }

    public boolean isInPath(List<MerkleNode> path, MerkleNode node){
        return path.contains(node);
    }

}