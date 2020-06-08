package domain.models;

import java.sql.Date;

public class User {
    private long id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private Date birthday;
    private String role;

    public User() {

    }

    public User(String name, String surname, String username, String password, Date birthday, String role) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        setRole(role);
    }

    public User(long id, String name, String surname, String username, String password, Date birthday, String role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        setRole(role);
    }

    public User(long id, String name, String surname, String username, Date birthday, String role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.birthday = birthday;
        setRole(role);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
