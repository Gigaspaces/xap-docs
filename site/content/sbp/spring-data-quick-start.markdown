---
type: post
title:  Quick Start
categories: SBP
weight: 100
parent: spring-data.html
---


{{%ssummary%}}{{%/ssummary%}}

This guide will walk you through the steps of building a Spring Data application with   XAP. You will use the   Spring Data XAP library to store and retrieve POJOs.

# Installation

Before using this guide, you have to download [GigaSpaces XAP](http://www.gigaspaces.com/xap-download) and install the maven plugin. If you're not familiar with Maven, refer to [Building Java Projects with Maven](https://spring.io/guides/gs/maven/).


To install the XAP maven plugin run the `installmavenrep` script:

{{%tabs%}}
{{%tab " Windows"%}}


```bash
/tools/maven/installmavenrep.bat
```
{{%/tab%}}

{{%tab " Unix"%}}

```bash
/tools/maven/installmavenrep.sh
```
{{%/tab%}}

{{%/tabs%}}

Download the `spring-data-xap` project and build it with maven using `mvn clean install`.

The recommended way to get started using spring-data-xap in your project is with a dependency management system - the snippet below can be copied and pasted into your build.


```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-xap</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```


# Define a simple entity

GigaSpaces XAP is an IMC platform with a powerful In-Memory Data Grid. It stores data in Spaces, and each Space can be configured in a different manner (replicated, partitioned etc). With this example we will use an `Embedded Space` that is running as part of your program. There is no need to run a separate grid.

With this example, we store `Book` objects.


```java

package hello;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

import java.io.Serializable;

@SpaceClass
public class Book implements Serializable {

    String id;

    String author;

    Integer copies;

    public Book(){}

    public Book(String id, String author, Integer copies) {
        this.id = id;
        this.author = author;
        this.copies = copies;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @SpaceId(autoGenerate = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", copies=" + copies +
                '}';
    }
}
```

Here you have a `Book` class with three attributes, the `id`, the `author` and the `copies`. You also have the constructor to populate the entities when creating a new instance and the default constructor.

Notice that this class is annotated `@SpaceClass`. This annotation is not mandatory, you can write objects to the Space without it, but it's used to provide an additional [metadata]({{%latestjavaurl%}}/modeling-your-data.html).
This class also has a getter for `id` marked with `@SpaceId`. This property uniquely identifies the object within the Space, and is similar to a primary key in a database.

The next important piece is the number of book copies. Later in this guide, you will use it to demonstrate some queries.
The convenient `toString()` method will print out the book's id, author and copies.

# Create simple queries
Spring Data XAP focuses on storing data in XAP. It also inherits powerful functionality from the `Spring Data Commons` project, such as the ability to derive queries. Essentially, you don't have to learn the query language of   XAP; you can simply write a handful of methods and the queries are generated for you.

To see how this works, lets create an interface that queries `Book` space objects.


```java

package hello;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, String> {

    List<Book> findByAuthor(String author);

    List<Book> findByCopiesLessThan(Integer copies);

    List<Book> findByAuthorOrCopiesGreaterThan(String author, Integer copies);

}
```

`BookRepository` extends the `CrudRepository` interface and plugs in the type of values and keys with: `Book` and `String`. Out-of-the-box, this interface comes with many operations, including standard CRUD (create-read-update-delete).

You can define other queries as needed by simply declaring their method signature. In this case, you add `findByAuthor`, which essentially seeks entities of type `Book` and find the ones that matches on `Author`.

You also have:

- `findByCopiesLessThan` to find books with a number of copies below a certain number
- `findByAuthorOrCopiesGreaterThan` to find books with a certain author or number of copies above a certain number

Let's wire this up and see what it looks like!

# Create your Program
Here you create the Program class with all the components.


```java

package hello;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import org.springframework.data.xap.repository.config.EnableXapRepositories;
import org.springframework.data.xap.repository.support.XapRepositoryFactory;

import java.io.IOException;

@Configuration
@EnableXapRepositories
public class Application implements CommandLineRunner {

    @Autowired
    BookRepository bookRepository;

    @Bean
    GigaSpace spaceClient(){
        UrlSpaceConfigurer urlConfigurer = new UrlSpaceConfigurer("/./testSpace");
        return new GigaSpaceConfigurer(urlConfigurer).gigaSpace();
    }

    @Bean
    BookRepository bookRepository(){
        RepositoryFactorySupport factory = new XapRepositoryFactory(spaceClient(), null);
        return factory.getRepository(BookRepository.class);
    }

    @Override
    public void run(String... strings) throws Exception {

        Book thinkingInJava = new Book("1234", "Eccel", 10000);
        Book effectiveJava = new Book("2345", "Bloch", 20000);
        Book springInAction = new Book("3456", "Walls", 50000);

        System.out.println("Before writing objects to space...");
        for (Book book : new Book[] { thinkingInJava, effectiveJava, springInAction }) {
            System.out.println("\t" + book);
        }

        bookRepository.save(thinkingInJava);
        bookRepository.save(effectiveJava);
        bookRepository.save(springInAction);

        System.out.println("Lookup books by author...");
        for (String name : new String[] { thinkingInJava.author, effectiveJava.author, effectiveJava.author}) {
            System.out.println("\t" + bookRepository.findByAuthor(name));
        }

        System.out.println("Lookup for less popular books...");
        for (Book book : bookRepository.findByCopiesLessThan(15000)) {
            System.out.println("\t" + book);
        }

        System.out.println("Lookup for popular books or books of specific author...");
        for (Book book : bookRepository.findByAuthorOrCopiesGreaterThan("Bloch", 30000)) {
            System.out.println("\t" + book);
        }

        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
    }
}
```


- In the configuration, you need to add the `@EnableXapRepositories` annotation. The XAP Space is required to store all data. For that, you have the Spring Data XAP   `SpaceClient` bean. <br>
- The types are `<String, Book>`, matching the key type (`String`) with the value type (`Book`). <br>
- The `public static void main` uses Spring Boot's `SpringApplication.run()` to launch the application and invoke the `CommandLineRunner` that builds the relationships. <br>
- The application autowires an instance of `BookRepository` that you just defined. Spring Data XAP will dynamically create a concrete class that implements that interface and will plug in the needed query code to meet the interface's obligations. This repository instance is the used by the `run()` method to demonstrate the functionality.

{{%note%}}
The Space is created locally using built-in components and an evaluation license. For a production solutions, it is recommends that you use the production version of XAP, where you can create distributed Spaces across multiple nodes.
{{%/note%}}

# Write and Read Objects

Lets create some `Book` instances, **SpringInAction**, **ThinkingInJava**, and **EffectiveJava**. Initially, they only exist in memory. After creating them, we store them in the Space.

Now we are ready to run some query:

- look up books by author<br>
- execute query to find less popular books<br>
- query to find popular books or books written by specific author

You should see something like this:


```bash
    Before writing objects to space...
        Book{id='1234', author='Eccel', copies=10000}
        Book{id='2345', author='Bloch', copies=20000}
        Book{id='3456', author='Walls', copies=50000}
    Lookup books by author...
        [Book{id='1234', author='Eccel', copies=10000}]
        [Book{id='2345', author='Bloch', copies=20000}]
        [Book{id='2345', author='Bloch', copies=20000}]
    Lookup for less popular books...
        Book{id='1234', author='Eccel', copies=10000}
    Lookup for popular books or books of specific author...
        Book{id='2345', author='Bloch', copies=20000}
        Book{id='3456', author='Walls', copies=50000}
```

