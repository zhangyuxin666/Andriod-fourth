package cn.edu.sicnu.stu.zhangyuxin.fourth;

import android.content.Context;
import android.util.AtomicFile;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

final class History {
    private static AtomicFile file(Context context, String name) {
        return new AtomicFile(new File(context.getFilesDir(), name));
    }

    static <T> T read(Context context, String name, Class<T> type) {
        try (ObjectInputStream input = new ObjectInputStream(file(context, name).openRead())) {
            Object value = input.readObject();
            return type.isInstance(value) ? type.cast(value) : null;
        } catch (java.io.FileNotFoundException missing) {
            return null;
        } catch (IOException | ClassNotFoundException error) {
            Log.w("History", "Could not restore " + name, error);
            return null;
        }
    }

    static boolean write(Context context, String name, Serializable value) {
        AtomicFile file = file(context, name);
        FileOutputStream stream = null;
        try {
            stream = file.startWrite();
            ObjectOutputStream output = new ObjectOutputStream(stream);
            output.writeObject(value);
            output.flush();
            file.finishWrite(stream);
            return true;
        } catch (IOException error) {
            file.failWrite(stream);
            Log.e("History", "Could not save " + name, error);
            return false;
        }
    }

    private History() {}
}
