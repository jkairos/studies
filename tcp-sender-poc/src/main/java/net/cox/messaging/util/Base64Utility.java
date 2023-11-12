package net.cox.messaging.util;

import java.util.Base64;

public class Base64Utility {

    // Method to encode a byte array to a Base64-encoded string
    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    // Method to decode a Base64-encoded string to a byte array
    public static byte[] decode(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

}
