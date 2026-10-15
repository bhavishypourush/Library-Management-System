
package com.library.model;

/**
 * Represents a library user.
 *
 * Inherits common personal details from Person
 * and adds username and role information.
 */
public class User extends Person {

    // User-specific fields.
    private String username;
    private String role;

    /**
     * Creates a user with personal and account details.
     */
    public User(int id, String name, String username,
                String email, String role) {

        // Initialize inherited fields.
        super(id, name, email);

        this.username = username;
        this.role = role;
    }

    // Getters

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    // Setter

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Overrides the parent method to describe a library user.
     */
    @Override
    public String getDescription() {
        return getName() + " (" + role + ")";
    }

    /**
     * Returns a readable representation of the user.
     */
    @Override
    public String toString() {
        return getDescription();
    }
}
