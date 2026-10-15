
package com.library.model;

/**
 * Base class representing a person in the library system.
 *
 * Stores common details that can be inherited by subclasses,
 * such as User.
 */
public class Person {

    // Private fields demonstrate encapsulation.
    private int id;
    private String name;
    private String email;

    /**
     * Creates a person with the supplied details.
     */
    public Person(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns a general description of the person.
     * Subclasses can override this method.
     */
    public String getDescription() {
        return "Person: " + name;
    }
}
