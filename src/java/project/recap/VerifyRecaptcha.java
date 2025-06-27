/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.recap;
/**
 *
 * @author TUAN
 */
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;

public class VerifyRecaptcha {

    private static final String SECRET_KEY = "6LeUgm4rAAAAAO3Azc3IRUWVUajRILOkAK1UK7TT";
    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public static boolean verify(String gRecaptchaResponse) {
        if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) return false;

        try {
            URL verifyUrl = new URL(VERIFY_URL);
            HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String postParams = "secret=" + SECRET_KEY + "&response=" + gRecaptchaResponse;
            OutputStream outStream = conn.getOutputStream();
            outStream.write(postParams.getBytes());
            outStream.flush();
            outStream.close();

            InputStream is = conn.getInputStream();
            Scanner scan = new Scanner(is);
            String jsonResponse = "";
            while (scan.hasNext()) {
                jsonResponse += scan.nextLine();
            }
            scan.close();

            return jsonResponse.contains("\"success\": true");

        } catch (Exception e) {
            System.out.println("VerifyRecaptcha Error: " + e.getMessage());
            return false;
        }
    }
}
