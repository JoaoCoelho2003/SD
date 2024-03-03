package Estruturas;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Message {
    private String type;
    private String payload;

    // Constructor
    public Message(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    // Getters and setters
    public String getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    // creates the payload of the message with the given array of strings, the payload is a string with the arguments separated by a tab
    public static String createPayload(String[] args) {
        String payload = "";
        for (int i = 0; i < args.length; i++) {
            payload += args[i];
            if (i != args.length - 1) {
                payload += "\t";
            }
        }
        return payload;
    }

    // takes the payload of the message and returns an array of strings
    public static String[] parsePayload(String payload) {
        return payload.split("\t");
    }

    // serialize and deserialize
    public static void serialize(SafeDataOutputStream out, String type, String payload) throws IOException {
        out.writeUTF(type);
        out.writeInt(payload.length());

        // if the payload is too big for the buffer, then it needs to keep writing in a loop until it has written all the payload

        byte[] payload_bytes = payload.getBytes(StandardCharsets.UTF_8);
        int written = 0;
        while (written < payload_bytes.length) {
            out.write(payload_bytes, written, payload_bytes.length - written);
            written += payload_bytes.length - written;
        }
    }

    public static Message deserialize(SafeDataInputStream in) throws IOException {
        try {
            String type = in.readUTF();
            int payload_length = in.readInt();

            // if the payload is too big for the buffer, then it needs to keep reading in a loop until it has read all the payload

            byte[] payload = new byte[payload_length];
            int read = 0;
            while (read < payload_length) {
                read += in.read(payload, read, payload_length - read);
            }

            String payload_string = new String(payload, StandardCharsets.UTF_8);
            return new Message(type, payload_string);
        } catch (IOException e) {
            return null;
        }
    }
}
