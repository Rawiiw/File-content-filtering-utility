import java.io.*;
import java.util.ArrayList;
import java.util.List;


class UtiliteFilterClass {
    private boolean appendMode = false;
    private boolean shortStatistics = false;
    private boolean fullStatistics = false;
    private String outputPath = "";
    private String prefix = "";

    private String integersOutputPath;
    private String floatsOutputPath;
    private String stringsOutputPath;

    private List<String> integerData;
    private List<String> floatData;
    private List<String> stringData;

    public void processArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-a":
                    appendMode = true;
                    break;
                case "-s":
                    shortStatistics = true;
                    break;
                case "-f":
                    fullStatistics = true;
                    break;
                case "-o":
                    outputPath = extractOptionValue(args, i);
                    i++;
                    break;
                case "-p":
                    prefix = extractOptionValue(args, i);
                    i++;
                    break;
            }
        }

        //выходные
        integersOutputPath = outputPath + prefix + "integers.txt";
        floatsOutputPath = outputPath + prefix + "floats.txt";
        stringsOutputPath = outputPath + prefix + "strings.txt";

        //входные данные
        integerData = new ArrayList<>();
        floatData = new ArrayList<>();
        stringData = new ArrayList<>();
    }

    public void processInputFiles(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                continue;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(args[i]))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.matches("-?\\d+")) {
                        integerData.add(line);
                    } else if (line.matches("-?\\d+(\\.\\d+([eE][-+]?\\d+)?)?")) {
                        floatData.add(line);
                    } else {
                        stringData.add(line);
                    }
                }
            } catch (IOException e) {
                System.out.println("Ошибка при чтении файла " + args[i] + ": " + e.getMessage());
                continue;
            }
        }
    }

    public void writeOutputFiles() {
        writeToFile(integersOutputPath, integerData, appendMode);
        writeToFile(floatsOutputPath, floatData, appendMode);
        writeToFile(stringsOutputPath, stringData, appendMode);
    }

    public void printStatistics() {
        if (shortStatistics || fullStatistics) {
            printStatistics("Целые числа", integerData, shortStatistics, fullStatistics);
            printStatistics("Вещественные числа", floatData, shortStatistics, fullStatistics);
            printStatistics("Строки", stringData, shortStatistics, fullStatistics);
        }
    }

    private static String extractOptionValue(String[] args, int index) {
        if (index + 1 < args.length) {
            return args[index + 1];
        } else {
            System.out.println("Укажите значение для опции.");
            System.exit(1);
            return ""; //не достигнется но нужно для компиляции
        }
    }
    private static void writeToFile(String outputPath, List<String> data, boolean appendMode) {
        if (!data.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, appendMode))) {
                for (String item : data) {
                    writer.write(item);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Ошибка при записи в файл " + outputPath + ": " + e.getMessage());
            }
        }
    }

    private static void printStatistics(String dataType, List<String> data, boolean shortStatistics, boolean fullStatistics) {
        System.out.println("Статистика для " + dataType + ":");
        System.out.println("Количество элементов: " + data.size());

        if (fullStatistics) {
            if (!data.isEmpty()) {
                if (data.get(0).matches("-?\\d+")) {
                    //если это целые числа
                    int sum = 0;
                    int min = Integer.MAX_VALUE;
                    int max = Integer.MIN_VALUE;

                    for (String item : data) {
                        int value = Integer.parseInt(item);
                        sum += value;
                        min = Math.min(min, value);
                        max = Math.max(max, value);
                    }

                    double average = (double) sum / data.size();
                    System.out.println("Минимальное значение: " + min);
                    System.out.println("Максимальное значение: " + max);
                    System.out.println("Сумма: " + sum);
                    System.out.println("Среднее: " + average);

                } else if (data.get(0).matches("-?\\d+(\\.\\d+([eE][-+]?\\d+)?)?")) {
                    // Если это вещественные числа
                    double sum = 0;
                    double min = Double.MAX_VALUE;
                    double max = Double.MIN_VALUE;

                    for (String item : data) {
                        double value = Double.parseDouble(item);
                        sum += value;
                        min = Math.min(min, value);
                        max = Math.max(max, value);
                    }

                    double average = sum / data.size();
                    System.out.println("Минимальное значение: " + min);
                    System.out.println("Максимальное значение: " + max);
                    System.out.println("Сумма: " + sum);
                    System.out.println("Среднее: " + average);
                } else {
                    //если это строки
                    int minLength = Integer.MAX_VALUE;
                    int maxLength = Integer.MIN_VALUE;

                    for (String item : data) {
                        int length = item.length();
                        minLength = Math.min(minLength, length);
                        maxLength = Math.max(maxLength, length);
                    }

                    System.out.println("Минимальная длина строки: " + minLength);
                    System.out.println("Максимальная длина строки: " + maxLength);
                }
            }
        }
    }
}

