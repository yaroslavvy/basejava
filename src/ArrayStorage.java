import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int arraySize = 0;

    void clear() {
        for (int i = 0; i < arraySize; i++) {
            storage[i] = null;
        }
        arraySize = 0;
    }

    void save(Resume resume) {
        if (arraySize < 10000) {
            storage[arraySize] = resume;
            ++arraySize;
        }
    }

    Resume get(String uuid) {
        Resume resume = null;
        for (int i = 0; i < arraySize; i++) {
            if (storage[i].uuid.equals(uuid)) {
                resume = storage[i];
                break;
            }
        }
        return resume;
    }

    void delete(String uuid) {
        for (int i = 0; i < arraySize; i++) {
            if (storage[i].uuid.equals(uuid)) {
                for (int y = i; y < arraySize - 1; y++) {
                    storage[y] = storage[y + 1];
                }
                storage[arraySize - 1] = null;
                --arraySize;
                return;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, arraySize);
    }

    int size() {
        return arraySize;
    }
}
