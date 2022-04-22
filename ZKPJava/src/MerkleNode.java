import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MerkleNode {
    public MerkleNode left;
    public MerkleNode right;
    public String hash;

    public MerkleNode(String hash){
        this.hash = hash;
    }

    public MerkleNode() {

    }

    public String computeHash() throws NoSuchAlgorithmException {
        String leftHash = this.left.hash;
        String rightHash = this.right!=null ? this.right.hash : leftHash ;
        String concat = leftHash+rightHash;
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] byteHash = mDigest.digest(concat.getBytes(StandardCharsets.UTF_8));
        return new String(byteHash, StandardCharsets.UTF_8);
    }
}
