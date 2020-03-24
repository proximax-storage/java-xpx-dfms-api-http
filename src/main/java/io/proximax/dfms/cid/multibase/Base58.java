package io.proximax.dfms.cid.multibase;

/**
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.math.BigInteger;

/**
 * <p>A custom form of base58 is used to encode BitCoin addresses. Note that this is not the same base58 as used by
 * Flickr, which you may see reference to around the internet.</p>
 *
 * <p>Satoshi says: why base-58 instead of standard base-64 encoding?</p>
 *
 * <ul>
 * <li>Don't want 0OIl characters that look the same in some fonts and
 *    could be used to create visually identical looking account numbers.</li>
 * <li>A string with non-alphanumeric characters is not as easily accepted as an account number.</li>
 * <li>E-mail usually won't line-break if there's no punctuation to break at.</li>
 * <li>Double-clicking selects the whole number as one word if it's all alphanumeric.</li>
 * </ul>
 */
public class Base58 {
    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final BigInteger BASE = BigInteger.valueOf(58);

    /**
     * to be used as utility class via static methods
     */
    private Base58() { /* hide implicit constructor */ }
    
    /**
     * encode byte array using Base58
     * 
     * @param input the byte array to encode
     * @return string representation of input
     */
    public static String encode(final byte[] input) {
        return BaseN.encode(ALPHABET, BASE, input);
    }

    /**
     * decode content from String to byte array
     * @param input string encoded using Base58
     * @return the byte array representing he content
     */
    public static byte[] decode(final String input) {
        return BaseN.decode(ALPHABET, BASE, input);
    }
}
