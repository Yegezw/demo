package com.zzw.entity;

import com.google.common.base.MoreObjects;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Student
{

    public int age;
    public int score;

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(Student.class)
                .add("age", age)
                .add("score", score)
                .toString();
    }
}
