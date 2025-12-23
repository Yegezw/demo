package zk.modeled;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Person {

    private String name;

    private int age;

    private String email;

    public Person() {
        this(null, 0, null);
    }
}
