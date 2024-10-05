package com.zzw.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Student
{

    public int age;
    public int score;

    @Override
    public String toString()
    {
        return String.format("Student{age = %s, score = %s}", age, score);
    }
}
