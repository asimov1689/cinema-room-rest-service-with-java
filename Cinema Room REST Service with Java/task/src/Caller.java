class Caller {
    public static void main(String[] args) {
        startConversation();
    }

    private static void startConversation() {
        String reply = Responder.getResponse("Hello from the Caller!");
        System.out.println(reply);
    }
}