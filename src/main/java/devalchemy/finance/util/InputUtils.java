package devalchemy.finance.util;

import java.math.BigDecimal;
import java.util.Scanner;

public class InputUtils {
    private static final Scanner SC = new Scanner(System.in);

    public static String nextLine(String prompt) {
        System.out.print(prompt);
        return SC.nextLine().trim();
    }

    public static int nextInt(String prompt) {
        while (true) {
            String s = nextLine(prompt);
            try { return Integer.parseInt(s); } catch (Exception e) { System.out.println("Please enter a number."); }
        }
    }

    public static BigDecimal nextBigDecimal(String prompt) {
        while (true) {
            String s = nextLine(prompt);
            try { return new BigDecimal(s); } catch (Exception e) { System.out.println("Please enter a decimal number like 12.34 (or -5.00)"); }
        }
    }
}
