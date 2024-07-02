package org.utils;

public class ISBNValidator {

    public static boolean isValidISBN(String isbn) {
        isbn = isbn.replace("-", "").replace(" ", ""); // Remove hyphens and spaces

        return (isbn.length() == 10) ? isValidISBN10(isbn) : (isbn.length() == 13) && isValidISBN13(isbn);
    }

    private static boolean isValidISBN10(String isbn) {
        if (isbn.length() != 10)
            return false;
        try {
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += Integer.parseInt(String.valueOf(isbn.charAt(i))) * (10 - i);
            }
            char check = isbn.charAt(9);
            sum += (check == 'X') ? 10 : Integer.parseInt(String.valueOf(check));
            return sum % 11 == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidISBN13(String isbn) {
        if (isbn.length() != 13)
            return false;
        try {
            int sum = 0;
            for (int i = 0; i < 12; i++) {
                sum += Integer.parseInt(String.valueOf(isbn.charAt(i))) * (i % 2 == 0 ? 1 : 3);
            }
            int checkDigit = 10 - (sum % 10);
            return checkDigit == 10 ? 0 == Integer.parseInt(String.valueOf(isbn.charAt(12)))
                    : checkDigit == Integer.parseInt(String.valueOf(isbn.charAt(12)));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String isbn10 = "0-306-40615-2";
        String isbn13 = "978-0-306-40615-7";

        System.out.println(isbn10 + " is valid: " + isValidISBN(isbn10));
        System.out.println(isbn13 + " is valid: " + isValidISBN(isbn13));
    }
}
