import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class ProofOfInclusion {
    MerkleTree tree;
    List<String> hashes;
    Deque<Integer> bits;
    int numNodes;

    public ProofOfInclusion(MerkleTree tree){
        this.tree = tree;
        this.numNodes = tree.numNodes;
        hashes = new ArrayList<>();
        bits = new LinkedList<>();
    }

    public void generateProofOfInclusion(MerkleNode destination){
        hashes = new ArrayList<>();
        bits = new LinkedList<>();
        Set<MerkleNode> path = tree.findPathInformation(destination);
        
        traverseTree(tree.root, destination, path);

        //after generation of proof
        System.out.println("List of Hashes "+hashes.size());
        for(String hash : hashes){
            System.out.println("\n"+hash);
        }
        System.out.println("\nbits = "+bits);
    }

    public void traverseTree(MerkleNode node, MerkleNode destination, Set<MerkleNode> path){
        if(node == null){
            return;
        }
        if(node.equals(destination)){
            bits.add(1);
            hashes.add(node.hash);
            return;
        }else if(isInPath(path, node)){
            bits.add(1);
            traverseTree(node.left, destination, path);
            traverseTree(node.right, destination, path);
            return;
        }else{
            bits.add(0);
            hashes.add(node.hash);
            return;
        }
    }

    public boolean isInPath(Set<MerkleNode> path, MerkleNode node){
        return path.contains(node);
    }
  
}