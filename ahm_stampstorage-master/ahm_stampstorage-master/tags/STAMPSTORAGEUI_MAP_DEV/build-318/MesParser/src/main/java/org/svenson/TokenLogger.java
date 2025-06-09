package org.svenson;

import org.svenson.tokenize.JSONTokenizer;

/**
 * TypeMapper that just logs the calls
 *
 * @author fforw at gmx dot de
 */
public class TokenLogger
        implements TypeMapper {
    public Class getTypeHint(JSONTokenizer tokenizer, String parsePathInfo, Class typeHint) {
        return typeHint;
    }

}
