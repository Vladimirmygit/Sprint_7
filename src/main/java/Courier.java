public class Courier {
    private String name;
    private String login;
    private String password;

    public Courier(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    // Геттеры и сеттеры для полей (можно сгенерировать автоматически в IDE)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}