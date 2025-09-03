public class Greeter {
    public static void main(String[] args) {
        greetUser("Alice");
    }

    public static void greetUser(String name) {
        String greeting = "Hello, " + name;
        System.out.println(greeting);
    }
}