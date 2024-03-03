package Estruturas;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SafeDataInputStream {

    private final DataInputStream dataInputStream;
    private final Lock lock;

    // Constructor
    public SafeDataInputStream(InputStream inputStream) {
        this.dataInputStream = new DataInputStream(inputStream);
        this.lock = new ReentrantLock();
    }

    // functions to read data from the stream
    public int readInt() throws IOException {
        lock.lock();
        try {
            return dataInputStream.readInt();
        } finally {
            lock.unlock();
        }
    }

    public long readLong() throws IOException {
        lock.lock();
        try {
            return dataInputStream.readLong();
        } finally {
            lock.unlock();
        }
    }

    public String readUTF() throws IOException {
        lock.lock();
        try {
            return dataInputStream.readUTF();
        } finally {
            lock.unlock();
        }
    }

    public double readDouble() throws IOException {
        lock.lock();
        try {
            return dataInputStream.readDouble();
        } finally {
            lock.unlock();
        }
    }

    public float readFloat() throws IOException {
        lock.lock();
        try {
            return dataInputStream.readFloat();
        } finally {
            lock.unlock();
        }
    }

    public boolean readBoolean() throws IOException {
        lock.lock();
        try {
            return dataInputStream.readBoolean();
        } finally {
            lock.unlock();
        }
    }

    public short readShort() throws IOException {
        lock.lock();
        try {
            return dataInputStream.readShort();
        } finally {
            lock.unlock();
        }
    }

    public byte readByte() throws IOException {
        lock.lock();
        try {
            return dataInputStream.readByte();
        } finally {
            lock.unlock();
        }
    }

    public char readChar() throws IOException {
        lock.lock();
        try {
            return dataInputStream.readChar();
        } finally {
            lock.unlock();
        }
    }

    public int readUnsignedByte() throws IOException {
        lock.lock();
        try {
            return dataInputStream.readUnsignedByte();
        } finally {
            lock.unlock();
        }
    }

    public int readUnsignedShort() throws IOException {
        lock.lock();
        try {
            return dataInputStream.readUnsignedShort();
        } finally {
            lock.unlock();
        }
    }

    public String readLine() throws IOException {
        lock.lock();
        try {
            return dataInputStream.readLine();
        } finally {
            lock.unlock();
        }
    }

    public int available() throws IOException {
        lock.lock();
        try {
            return dataInputStream.available();
        } finally {
            lock.unlock();
        }
    }

    public void mark(int readlimit) {
        lock.lock();
        try {
            dataInputStream.mark(readlimit);
        } finally {
            lock.unlock();
        }
    }

    public void reset() throws IOException {
        lock.lock();
        try {
            dataInputStream.reset();
        } finally {
            lock.unlock();
        }
    }

    public boolean markSupported() {
        lock.lock();
        try {
            return dataInputStream.markSupported();
        } finally {
            lock.unlock();
        }
    }

    public void readFully(byte[] b) throws IOException {
        lock.lock();
        try {
            dataInputStream.readFully(b);
        } finally {
            lock.unlock();
        }
    }

    public void readFully(byte[] b, int off, int len) throws IOException {
        lock.lock();
        try {
            dataInputStream.readFully(b, off, len);
        } finally {
            lock.unlock();
        }
    }

    public int skipBytes(int n) throws IOException {
        lock.lock();
        try {
            return dataInputStream.skipBytes(n);
        } finally {
            lock.unlock();
        }
    }

    public void close() throws IOException {
        lock.lock();
        try {
            dataInputStream.close();
        } finally {
            lock.unlock();
        }
    }

    public int read(byte[] buffer, int i, int min) throws IOException {
        lock.lock();
        try {
            return dataInputStream.read(buffer, i, min);
        } finally {
            lock.unlock();
        }
    }
}
