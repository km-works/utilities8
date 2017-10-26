package kmworks.util.fs;

import kmworks.util.io.FileUtil;

import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;

public final class DirUtil {

    private DirUtil() {}

    public static void deepDelete(@Nonnull Path dir) {
        checkNotNull(dir);
        FileUtil.deleteDirTree(dir.toFile());
    }
}
