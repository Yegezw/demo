package com.zzw.eg;

import io.netty.util.Recycler;
import lombok.Setter;

@Setter
public class Student
{

    private static final Recycler<Student> RECYCLER = new Recycler<>()
    {
        @Override
        protected Student newObject(Handle<Student> handle)
        {
            return new Student(handle);
        }
    };

    public static Student newInstance()
    {
        return RECYCLER.get();
    }

    // ==================================================================

    private       int                      id;
    private       String                   name;
    private final Recycler.Handle<Student> handle; // 用于对象回收的句柄

    private Student(Recycler.Handle<Student> handle)
    {
        this.handle = handle;
    }

    public void recycle()
    {
        id   = 0;
        name = null;
        handle.recycle(this);
    }

    @Override
    public String toString()
    {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    // ==================================================================

    public static void main(String[] args)
    {
        Student student1 = Student.newInstance();
        student1.setId(1);
        student1.setName("张三");
        System.out.println(student1);

        student1.recycle();
        Student student2 = Student.newInstance();
        System.out.println(student2);

        System.out.println(student1 == student2);
    }
}
