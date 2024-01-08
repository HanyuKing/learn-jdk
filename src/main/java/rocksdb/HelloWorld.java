package rocksdb;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

/**
 * @Author Hanyu.Wang
 * @Date 2024/1/4 19:34
 * @Description
 * @Version 1.0
 **/
public class HelloWorld {
    static {
        RocksDB.loadLibrary();
    }
    private static RocksDB rocksDB;
    private static String pathSuffix = "data/rowdb";

    public static void main(String[] args) throws RocksDBException {
        Options options = new Options();
        options.setCreateIfMissing(true);

        rocksDB = RocksDB.open(options, getPath(pathSuffix));

        rocksDB.put("aaa".getBytes(),"bbb".getBytes());

        System.out.println("-----");

        byte[] bytes = rocksDB.get("aaa".getBytes());

        System.out.println(new String(bytes));

        RocksIterator iter = rocksDB.newIterator();
        for (iter.seekToFirst();iter.isValid();iter.next()) {
            System.out.println("iter key: " + new String(iter.key()) + ",iter value: " +
                    new String(iter.value()));
        }

        rocksDB.close();
    }
    private static String getPath(String pathSuffix) {
        String classPath = HelloWorld.class.getResource("/").getPath();
        return classPath.substring(0, classPath.lastIndexOf("target")) + "src/main/java/rocksdb/"
                + pathSuffix;
    }
}
