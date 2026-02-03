package anilcanmusmul;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Person implements Serializable {
    protected String name;
    protected final long national_id;

    public Person(String name, long national_id) {
        this.name = name;
        this.national_id = national_id;
    }

    public String getName() {
        return name;
    }

    public long getNationalId() {
        return national_id;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + '\'' + ", national_id=" + national_id + '}';
    }
}