import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int resumeCounter = 0;

    void clear() {
        for (int i = 0; i < resumeCounter; i++) {
            storage[i] = null;
        }
        resumeCounter = 0;
    }

    void save(Resume resume) {
        if (resumeCounter < storage.length) {
            storage[resumeCounter] = resume;
            ++resumeCounter;
        }
    }

    Resume get(String uuid) {
        for (int i = 0; i < resumeCounter; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < resumeCounter; i++) {
            if (storage[i].uuid.equals(uuid)) {
                for (int j = i; j < resumeCounter - 1; j++) {
                    storage[j] = storage[j + 1];
                }
                storage[resumeCounter - 1] = null;
                --resumeCounter;
                return;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, resumeCounter);
    }

    int size() {
        return resumeCounter;
    }
}
