package com.example.hauntarl.smartfarming;

public class AddingNewQuery {
    public String desc;
    public String subject;
    public String imageUrl;
    public Integer solved;
    public String solution;
    public String ref;

    public AddingNewQuery(String desc, String subject, String imageUrl, Integer solved, String solution,String ref) {
        this.desc = desc;
        this.subject = subject;
        this.imageUrl = imageUrl;
        this.solved = solved;
        this.solution = solution;
        this.ref = ref;
    }
}
