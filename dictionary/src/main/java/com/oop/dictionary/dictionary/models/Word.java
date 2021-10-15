package com.oop.dictionary.dictionary.models;

import javax.persistence.*;

@Entity
@Table(name = "word")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "Proc_FindWords", procedureName = "Proc_FindWords", parameters =
                {@StoredProcedureParameter(mode = ParameterMode.IN, name = "characters", type = String.class)}
        ),
})
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String word;
    private String html;
    private String description;
    private String pronounce;

    public Word() {
    }

    public Word(String word, String html, String description, String pronounce) {
        this.word = word;
        this.html = html;
        this.description = description;
        this.pronounce = pronounce;
    }

    public Word(int id, String word, String html, String description, String pronounce) {
        this.id = id;
        this.word = word;
        this.html = html;
        this.description = description;
        this.pronounce = pronounce;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPronounce() {
        return pronounce;
    }

    public void setPronounce(String pronounce) {
        this.pronounce = pronounce;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", html='" + html + '\'' +
                ", description='" + description + '\'' +
                ", pronounce='" + pronounce + '\'' +
                '}';
    }
}
