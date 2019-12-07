package ros.hack.api.utils;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import javax.annotation.Nonnull;
import java.nio.charset.Charset;

public class HashUtils {

    @Nonnull
    @SuppressWarnings("all")
    public static String getHash(@Nonnull String string) {
        final Hasher hasher = Hashing.sha256().newHasher();
        hasher.putString(string, Charset.defaultCharset());
        return new String(hasher.hash().asBytes());
    }
}
