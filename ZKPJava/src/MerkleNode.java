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

    public String computeHash(String algo) throws NoSuchAlgorithmException {
        String leftHash = this.left.hash;
        String rightHash = this.right!=null ? this.right.hash : leftHash ;
        String concat = leftHash+rightHash;
        MessageDigest mDigest = MessageDigest.getInstance(algo);
        byte[] byteHash = mDigest.digest(concat.getBytes(StandardCharsets.UTF_8));
        return new String(byteHash, StandardCharsets.UTF_8);
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        MerkleNode second =  (MerkleNode)obj;
        return this.hash.equals(second.hash);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return this.hash.hashCode();
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.hash;
    }
}
