package zip;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressUtils {

    // 预留4位表示版本号，目前为1
    private static byte[] compressVesion = {1, 0, 0, 0};

    // deflate压缩
    public static byte[] deflateCompress(String param) throws IOException {
        byte[] srcBytes = param.getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DeflateParameters deflateParam = new DeflateParameters();
        deflateParam.setCompressionLevel(9);
        DeflateCompressorOutputStream bcos = new DeflateCompressorOutputStream(out, deflateParam);
        bcos.write(srcBytes);
        bcos.close();
        // 预留4位表示版本号
        return ArrayUtils.addAll(compressVesion, out.toByteArray());
    }

    // deflate解压
    public static String deflateUncompress(byte[] bytes) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] srcBytes = ArrayUtils.subarray(bytes, 4, bytes.length);
        ByteArrayInputStream in = new ByteArrayInputStream(srcBytes);
        try {
            DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(in);
            byte[] buffer = new byte[2048];
            int n;
            while ((n = inputStream.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            throw e;
        }
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    public static String compress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        GZIPOutputStream gzip = new GZIPOutputStream(out);

        gzip.write(str.getBytes ( ) ) ; gzip.close();
        return out.toString("ISO-8859-1");

    }
    public static String unCompress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes ("ISO-8859-1")) ;

        GZIPInputStream gzip = new GZIPInputStream(in); byte[] buffer = new byte[256];

        int n = 0;

        while ((n = gzip.read(buffer)) >=  0) {
            out.write(buffer, 0, n);
        }

        return out.toString("GBK");

    }
}
