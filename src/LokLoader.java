/**
 * Created with IntelliJ IDEA.
 * User: carlos
 * Date: 8/10/13
 * Time: 21:36
 * To change this template use File | Settings | File Templates.
 */

import java.io.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class LokLoader
{
    public static void main(String args[])
    {
        try{
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream = new FileInputStream(args[0]);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            int cont=0;
            int LowSpeed = 5;
            int HighSpeed = 150;
            Random r = new Random();
            while ((strLine = br.readLine()) != null)   {
                // Print the content on the console
                //System.out.println (strLine);
                String[] str = strLine.split(",");

                cont ++;
                int speed = r.nextInt(HighSpeed-LowSpeed) + LowSpeed;
                int accuracy = r.nextInt(HighSpeed-LowSpeed) + LowSpeed;
                String resp = executePost("http://lokme.lextrendlabs.com/points/manual/","latitude=" + str[1] + "&longitude=" + str[0]+ "&speed=" + speed + "&accuracy=" + accuracy+ "&provider=gps"+ "&imei="+ (cont % 50) +args[1] );
                if ((cont %10)==0)
                {
                    System.out.printf("10 puntos mas ");

                }

            }
            //Close the input stream
            in.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        //String resp = executePost("http://localhost:3000/points/manual/","latitude=2.45&longitude=34.66&imei=1234554321");
        System.out.println("FIn");



    }



    public static String executePost(String targetURL, String urlParameters)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

}

