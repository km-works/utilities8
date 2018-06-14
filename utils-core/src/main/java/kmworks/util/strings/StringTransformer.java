package kmworks.util.strings;

import java.io.IOException;
import java.io.Writer;

public interface StringTransformer {

    String translate(final CharSequence input);

    void translate(final CharSequence input, final Writer out) throws IOException;

    int translate(CharSequence input, int index, Writer out) throws IOException;

    StringTransformer with(final StringTransformer... transformers);
}
