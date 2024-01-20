public class Main {

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.out.println("Укажите минимум 2 файла");
                return;
            }

            UtiliteFilterClass util = new UtiliteFilterClass();
            util.processArguments(args);
            util.processInputFiles(args);
            util.writeOutputFiles();
            util.printStatistics();

        } catch (Exception e) {
            System.out.println("Что-то пошло не так: " + e.getMessage());
        }
    }
}