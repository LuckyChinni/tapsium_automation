package utils;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;

public class Utils {
    public final String[] VISA_PEFIX_LIST = new String[]{"4539", "4556", "4916", "4532", "4929", "40240071", "4485", "4716", "4"};
    public final String[] MASTERCARD_PEFIX_LIST = new String[]{"51", "52", "53", "54", "55"};
    public final String[] AMEX_PEFIX_LIST = new String[]{"34", "37"};
    public final String[] DISCOVER_PEFIX_LIST = new String[]{"6011"};
    public final String[] DINERS_PEFIX_LIST = new String[]{"300", "301", "302", "303", "36", "38"};
    public final String[] ENROUTE_PEFIX_LIST = new String[]{"2014", "2149"};
    public final String[] JCB_16_PEFIX_LIST = new String[]{"3088", "3096", "3112", "3158", "3337", "3528"};
    public final String[] JCB_15_PEFIX_LIST = new String[]{"2100", "1800"};
    public final String[] VOYAGER_PEFIX_LIST = new String[]{"8699"};

    static InputStream inputStream;
    static BufferedReader bufferedReader;
    static String lineInFile;
    String datePattern;
    String result;
    DateFormat dateFormat;
    Date date;
    Random random;

    public static String getResourceContent(String resource) {
        StringBuilder res = new StringBuilder();
        try {
            inputStream = Utils.class.getResourceAsStream(resource);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((lineInFile = bufferedReader.readLine()) != null)
                res.append(lineInFile.trim());
            inputStream.close();
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res.toString();
    }

    public synchronized String getCurrentDateTime() {
        date = new Date();
        datePattern = "yyyy-MM-dd'T'HH:mm:ssZ";
        dateFormat = new SimpleDateFormat(datePattern);
        result = dateFormat.format(date);
        return result;
    }

    public synchronized String getCurrentDateTimeWithoutZone() {
        date = new Date();
        datePattern = "yyyy-MM-dd'T'HH:mm:ss";
        dateFormat = new SimpleDateFormat(datePattern);
        result = dateFormat.format(date);
        return result;
    }

    public synchronized String getTimeStamp() {
        date = new Date();
        datePattern = "yyyyMMddHHmmss";
        dateFormat = new SimpleDateFormat(datePattern);
        result = dateFormat.format(date);
        return result;
    }

    public long getRandValue(int min, int max) {
        long result = 0;
        random = new Random();
        result = ((random.nextInt(max - min)) + min);
        return result;
    }

    public String randomEmailID() {
        return "";
    }

    public String getCurrentDate(String format) {
        date = new Date();
        datePattern = format;
        dateFormat = new SimpleDateFormat(datePattern);
        result = dateFormat.format(date);
        return result;
    }

    public String getUrlWithQueryParams(String url, Map<String, String> queryParams) {
        String queryUrl = "&";
        String completeUrl = null;
        if (url.endsWith("?")) {
            queryUrl = "";
        }
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            queryUrl = queryUrl + entry.getKey() + "=" + entry.getValue() + "&";
        }
        completeUrl = url + queryUrl;
        return completeUrl;
    }

    public String getRandWord() {
        random = new Random();
        result = String.valueOf((char) (random.nextInt(26) + 'A') +
                (char) (random.nextInt(26) + 'A') +
                (char) (random.nextInt(26) + 'a'));
        return result;
    }

    public String getRandomPhone() {
        random = new Random();
        long randomNumber = 1000000000L + (long) (random.nextDouble() * 9000000000L);
        return Long.toString(randomNumber);
    }

    public String generateRandomCreditCardNum() {
        String value = String.valueOf((long) (Math.random() * 1_000_000_000_000_0000L));
        return value;
    }

    private String[] credit_card_number(String[] prefixList, int length, int howMany) {
        Stack<String> result = new Stack<>();
        for (int i = 0; i < howMany; i++) {
            int randomArrayIndex = (int) Math.floor(Math.random() * prefixList.length);
            String ccNum = prefixList[randomArrayIndex];
            result.push(completedNumber(ccNum, length));
        }
        return result.toArray(new String[result.size()]);
    }

    public String completedNumber(String prefix, int length) {
        String ccNumber = prefix;
        while (ccNumber.length() < (length - 1)) {
            ccNumber += new Double(Math.floor(Math.random() * 10)).intValue();
        }
        String reversedCCNumberString = reverseString(ccNumber);
        List<Integer> list = new Vector<>();
        for (int i = 0; i < reversedCCNumberString.length(); i++) {
            list.add(new Integer(String.valueOf(reversedCCNumberString.charAt(i))));
        }
        int sum = 0, pos = 0;
        Integer[] reversedCCNumber = list.toArray(new Integer[list.size()]);
        while (pos < length - 1) {
            int odd = reversedCCNumber[pos] * 2;
            if (odd > 9) {
                odd -= 9;
            }
            sum += odd;
            if (pos != (length - 2)) {
                sum += reversedCCNumber[pos + 1];
            }
            pos += 2;
        }
        int checkDigit = new Double(((Math.floor(sum / 10) + 1) * 10 - sum) % 10).intValue();
        ccNumber += checkDigit;
        return ccNumber;
    }

    private String reverseString(String value) {
        if (value == null) {
            return "";
        }
        for (int i = value.length() - 1; i >= 0; i--) {
            result += value.charAt(i);
        }
        return result;
    }

    public String generateCreditCard(String cardType) {
        String[] card;
        if (cardType.equalsIgnoreCase("VISA")) {
            card = credit_card_number(VISA_PEFIX_LIST, 16, 1);
        } else if (cardType.equalsIgnoreCase("DISCOVER")) {
            card = credit_card_number(DISCOVER_PEFIX_LIST, 16, 1);
        } else if (cardType.equalsIgnoreCase("MASTER")) {
            card = credit_card_number(MASTERCARD_PEFIX_LIST, 16, 1);
        } else if (cardType.equalsIgnoreCase("AMEX")) {
            card = credit_card_number(AMEX_PEFIX_LIST, 15, 1);
        } else if (cardType.equalsIgnoreCase("JCB_16")) {
            card = credit_card_number(JCB_16_PEFIX_LIST, 16, 1);
        } else if (cardType.equalsIgnoreCase("DINERS_16")) {
            card = credit_card_number(DINERS_PEFIX_LIST, 16, 1);
        } else {
            throw new IllegalArgumentException("Could not generate Credit Card of type : " + cardType);
        }
        return card[0];
    }

    public String generateCVV() {
        random = new Random();
        int cvv = random.nextInt(900) + 100;
        return String.valueOf(cvv);
    }

    public String generateExpiryDate() {
        YearMonth currentMonth = YearMonth.now();
        random = new Random();
        int minMonthsToAdd = 12;
        int maxMonthsToAdd = 4*12;
        int monthsToAdd = random.nextInt(maxMonthsToAdd - minMonthsToAdd +1) + minMonthsToAdd;
        YearMonth expiryMonth = currentMonth.plusMonths(monthsToAdd);
        int month = expiryMonth.getMonthValue();
        int year = expiryMonth.getYear();
        result =String.format("%02d/%04d", month, year);
        System.out.println(result);
        return result;
    }

    public static String convertJSONToString(String filePath){
        String output = "";
        try {
            output = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return output;
    }
}
