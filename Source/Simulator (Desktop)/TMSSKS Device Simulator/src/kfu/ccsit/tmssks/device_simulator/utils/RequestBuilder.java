package kfu.ccsit.tmssks.device_simulator.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class RequestBuilder extends Thread {

    public static interface OnRequestListener {
        void onFinished(String id, String response, boolean error);
    }

    private OnRequestListener callback;
    private DataQuery query;
    private String id;
    private String url;
    private String method;
    
    public RequestBuilder(String url) {
        this.url = url;
        method = "GET";
    }
    
    public static RequestBuilder instance(String url) {
        return new RequestBuilder(url);
    }
    
    public RequestBuilder query(DataQuery query) {
        this.query = query;
        return this;
    }
    
    public RequestBuilder put(String key, Object value) {
        if (query == null) {
            query = new DataQuery();
        }
        query.add(key, value.toString());
        return this;
    }
    
    public RequestBuilder method(String method) {
        this.method = method.toUpperCase();
        return this;
    }
    
    public RequestBuilder callback(OnRequestListener callback) {
        this.callback = callback;
        return this;
    }
    
    public RequestBuilder id(String id) {
        this.id = id;
        return this;
    }

    @Override
    public void run() {
        boolean doOutput = !method.equals("GET") && query != null;
        boolean doInput = callback != null;

        try {
            if (method.equals("GET") && query != null) {
                url += "?" + query.getQuery();
            }

            HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
            huc.setDoOutput(query != null);
            huc.setDoInput(true);
            huc.setRequestMethod(method);

            if (doOutput) {
                assert query != null;
                OutputStream os = huc.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bw.write(query.getQuery());
                bw.close();
                os.close();
            }

            String response = "";
            if (doInput) {
                InputStream is = huc.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line;

                while ((line = br.readLine()) != null) {
                    response += line;
                }

                br.close();
            }

            huc.disconnect();

            if (callback != null) {
                callback.onFinished(id, response, false);
            }
        } catch (IOException ex) {
            if (callback != null) {
                callback.onFinished(id, ex.getMessage(), true);
            }
        }
    }
            
    public void request() {
        start();
    }

    public static class DataQuery {

        private int count = 0;
        private ArrayList<String> keys, values;

        public DataQuery() {
            keys = new ArrayList<>();
            values = new ArrayList<>();
        }

        public DataQuery add(String key, String value) {
            int i = keys.indexOf(key);
            if (i > -1) {
                values.set(i, value);
            } else {
                keys.add(key);
                values.add(value);
                count++;
            }
            return this;
        }

        public DataQuery remove(String key) {
            return remove(keys.indexOf(key));
        }

        public DataQuery remove(int index) {
            if (index > -1) {
                keys.remove(index);
                values.remove(index);
                count--;
            }
            return this;
        }

        public String getQuery() throws UnsupportedEncodingException {
            String data = "";
            for (int i = 0; i < count; i++) {
                if (i > 0) {
                    data += "&";
                }
                data += URLEncoder.encode(keys.get(i), "UTF-8") + "="
                        + URLEncoder.encode(values.get(i), "UTF-8");
            }
            return data;
        }

        @Override
        public String toString() {
            try {
                return getQuery();
            } catch (UnsupportedEncodingException e) {
                System.err.println(e.getMessage());
            }
            return "UnsupportedEncoding!";
        }
    }

}
