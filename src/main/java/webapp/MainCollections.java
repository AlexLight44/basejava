package main.java.webapp;

import main.java.webapp.model.Resume;

import java.util.*;

public class MainCollections {
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final String FULL_NAME_1 = "Alex";
    private static final String FULL_NAME_2 = "Alex";
    private static final String FULL_NAME_3 = "Alex";
    private static final String FULL_NAME_4 = "Alex";

    private static final Resume RESUME_1 = new Resume(UUID_1, FULL_NAME_1);
    private static final Resume RESUME_2 = new Resume(UUID_2, FULL_NAME_2);
    private static final Resume RESUME_3 = new Resume(UUID_3, FULL_NAME_3);
    private static final Resume RESUME_4 = new Resume(UUID_4, FULL_NAME_4);

    public static void main(String[] args) {
        Collection<Resume> collection = new ArrayList<>();
        collection.add(RESUME_1);
        collection.add(RESUME_2);
        collection.add(RESUME_3);

        for (Resume r : collection) {
            System.out.println(r);
            if (Objects.equals(r.getUuid(), UUID_1)) {
//                collection.remove(r);
            }
        }
        Iterator<Resume> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Resume r = iterator.next();
            System.out.println(r);
            if (Objects.equals(r.getUuid(), UUID_1)) {
                iterator.remove();
            }
        }
        System.out.println(collection);

        Map<String, Resume> map = new HashMap<>();
        map.put(UUID_1, RESUME_1);
        map.put(UUID_2, RESUME_2);
        map.put(UUID_3, RESUME_3);
        //Bad!
        for (String uuid : map.keySet()){
            System.out.println(map.get(uuid));
        }
        for (Map.Entry<String, Resume> entry : map.entrySet()){
            System.out.println(entry.getValue());
        }
//        List<Resume> resumes = Arrays.asList(RESUME_1, RESUME_2, RESUME_3);
//        resumes = new ArrayList<>();
//
//        resumes.remove(1);
//        System.out.println(resumes);
        N_Resume nResume = new N_Resume("d","ww2", 214);
        Ignore<? super Resume> resumeIgnore = new Ignore<>();
        resumeIgnore.vv(nResume);
    }
}
class Ignore<SD>{

    void vv(SD slack){
        System.out.println(slack);
    }
}
class N_Resume extends Resume{
    int anti;

    public N_Resume(String uuid, String fullName, int anti) {
        super(uuid, fullName);
        this.anti = anti;
    }

}
