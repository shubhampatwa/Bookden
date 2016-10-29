package com.den.bookden;

/**
 * Created by Shubhi on 5/10/2016.
 */
public class Book {
String title,author,publication,link;
    public Book(){

    }

    public Book(String title,String author,String publication,String link)
    {
        this.title=title;
        this.author=author;
        this.publication=publication;
        this.link=link;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
