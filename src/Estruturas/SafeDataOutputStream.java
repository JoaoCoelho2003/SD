package Estruturas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SafeDataOutputStream {

    private final DataOutputStream dataOutputStream;
    private final Lock lock;

    // Constructor
    public SafeDataOutputStream(OutputStream outputStream) {
        this.dataOutputStream = new DataOutputStream(outputStream);
        this.lock = new ReentrantLock();
    }

    // functions to write data to the stream
    public void writeInt(int value) throws IOException {
        lock.lock();
        try {
            dataOutputStream.writeInt(value);
            dataOutputStream.flush();
        } finally {
            lock.unlock();
        }
    }

    public void writeLong(long value) throws IOException {
        lock.lock();
        try {
            dataOutputStream.writeLong(value);
            dataOutputStream.flush();
        } finally {
            lock.unlock();
        }
    }

    public void writeUTF(String value) throws IOException {
        lock.lock();
        try {
            dataOutputStream.writeUTF(value);
            dataOutputStream.flush();
        } finally {
            lock.unlock();
        }
    }

    public void writeDouble(double value) throws IOException {
        lock.lock();
        try {
            dataOutputStream.writeDouble(value);
            dataOutputStream.flush();
        } finally {
            lock.unlock();
        }
    }

    public void writeFloat(float value) throws IOException {
        lock.lock();
        try {
            dataOutputStream.writeFloat(value);
            dataOutputStream.flush();
        } finally {
            lock.unlock();
        }
    }

    public void writeBoolean(boolean value) throws IOException {
        lock.lock();
        try {
            dataOutputStream.writeBoolean(value);
            dataOutputStream.flush();
        } finally {
            lock.unlock();
        }
    }

    public void writeShort(short value) throws IOException {
        lock.lock();
        try {
            dataOutputStream.writeShort(value);
            dataOutputStream.flush();
        } finally {
            lock.unlock();
        }
    }

    public void writeByte(byte value) throws IOException {
        lock.lock();
        try {
            dataOutputStream.writeByte(value);
            dataOutputStream.flush();
        } finally {
            lock.unlock();
        }
    }

    public void writeBytes(String value) throws IOException {
        lock.lock();
        try {
            dataOutputStream.writeBytes(value);
            dataOutputStream.flush();
        } finally {
            lock.unlock();
        }
    }

    public void writeChars(String value) throws IOException {
        lock.lock();
        try {
            dataOutputStream.writeChars(value);
            dataOutputStream.flush();
        } finally {
            lock.unlock();
        }
    }

    public void writeChar(int value) throws IOException {
        lock.lock();
        try {
            dataOutputStream.writeChar(value);
            dataOutputStream.flush();
        } finally {
            lock.unlock();
        }
    }

    public void write(byte[] value) throws IOException {
        lock.lock();
        try {
            dataOutputStream.write(value);
            dataOutputStream.flush();
        } finally {
            lock.unlock();
        }
    }

    public void write(byte[] value, int offset, int length) throws IOException {
        lock.lock();
        try {
            dataOutputStream.write(value, offset, length);
            dataOutputStream.flush();
        } finally {
            lock.unlock();
        }
    }

    public void flush() throws IOException {
        lock.lock();
        try {
            dataOutputStream.flush();
        } finally {
            lock.unlock();
        }
    }

    public void close() throws IOException {
        lock.lock();
        try {
            dataOutputStream.close();
        } finally {
            lock.unlock();
        }
    }
}
