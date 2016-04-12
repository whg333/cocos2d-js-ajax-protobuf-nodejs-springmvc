package com.why.game.http;

import static com.why.game.http.ParseUtil.printHex;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.protobuf.InvalidProtocolBufferException;
import com.why.game.protobuf.TestProtobuf.TestProto;

public class HttpServiceCaller {
	
	private static final String host = "http://localhost:3000/";
	
	public static void main(String[] args) {
		postJson();
		postProto();
	}
	
	/** 具体的httpclient的使用请参见http://blog.csdn.net/wangpeng047/article/details/19624529 */
	private static void postParam() {  
        // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        // 创建httppost    
        HttpPost httppost = new HttpPost(host+"json.why");  
        // 创建参数队列    
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();  
        formParams.add(new BasicNameValuePair("requestJson", "{\"name\":\"whg32433测试一下\",\"id\":12243}"));  
        UrlEncodedFormEntity uefEntity;  
        try {  
            uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httppost.setEntity(uefEntity);  
            System.out.println("executing request " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                HttpEntity entity = response.getEntity();  
    			System.out.println(EntityUtils.toString(entity));
//                if (entity != null) {  
//                    System.out.println("--------------------------------------");  
//                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
//                    System.out.println("--------------------------------------");  
//                }  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }
	
	/** 具体的httpclient的使用请参见http://blog.csdn.net/wangpeng047/article/details/19624529 */
	private static void postJson() {  
        // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        // 创建httppost    
        HttpPost httpPost = new HttpPost(host+"json.why");
        httpPost.addHeader("Content-Type","application/json;charset=utf-8");
        httpPost.addHeader("Accept", "application/json;charset=utf-8");
        // 创建参数队列    
        //List<NameValuePair> formParams = new ArrayList<NameValuePair>();  
        //formParams.add(new BasicNameValuePair("requestJson", "{\"openid\":\"whg32433测试一下\",\"id\":12243}"));  
        //UrlEncodedFormEntity uefEntity;  
        try {  
            //uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httpPost.setEntity(new StringEntity("{\"name\":\"whg32433测试一下\",\"id\":12243}", ContentType.APPLICATION_JSON));
            System.out.println("executing request " + httpPost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {  
                HttpEntity entity = response.getEntity();  
    			System.out.println(EntityUtils.toString(entity));
//                if (entity != null) {  
//                    System.out.println("--------------------------------------");  
//                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
//                    System.out.println("--------------------------------------");  
//                }  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }
	
	private static void postProto() {  
        // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        // 创建httppost    
        HttpPost httpPost = new HttpPost(host+"/protobuf.why");  
        httpPost.addHeader("Content-Type","application/x-protobuf");
        httpPost.addHeader("Accept", "application/x-protobuf");
        
        // 创建参数队列 ，下面这个有点绕，显示创建input流读入byte数组数据，才setEntity
        //其实可以直接使用ByteArrayEntity，就像下面所示的
        //ByteArrayInputStream inputStream = new ByteArrayInputStream(newTestProto().toByteArray());
        //InputStreamEntity inputStreamEntity = new InputStreamEntity(inputStream);
        
        try {  
            //httppost.setEntity(inputStreamEntity);  
            httpPost.setEntity(new ByteArrayEntity(newTestProto().toByteArray()));  
            System.out.println("executing request " + httpPost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httpPost);  
            try {  
                HttpEntity entity = response.getEntity();  
                
                byte[] bytes = EntityUtils.toByteArray(entity);
                printHex(bytes);
                TestProto newTestProto = newTestProto();
    			byte[] bytes2 = newTestProto.toByteArray();
    			printHex(bytes2);
    			
    			System.out.println();
    			printTestProto("testProto=", bytes2);
    			printTestProto("response=", bytes);
                
//                if (entity != null) {  
//                    System.out.println("--------------------------------------");  
//                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
//                    System.out.println("--------------------------------------");  
//                }  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
	
	private static void printTestProto(String s, byte[] bytes){
		TestProto testProto = null;
		try {
			testProto = TestProto.parseFrom(bytes);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		if(testProto != null){
			System.out.println(s+testProto);
			System.out.println(testProto.getId());
			System.out.println(testProto.getName());
		}
	}
	
	public static TestProto newTestProto(){
		TestProto.Builder builder = TestProto.newBuilder();
		builder.setId(23434);
		builder.setName("testProtobuf_whg333444测试一下");
		return builder.build();
	}
	
	public static void printProtoStr(String s){
		List<Byte> l = new ArrayList<Byte>();
		for(int i=0;i<s.length();i++){
			l.add((byte)s.charAt(i));
		}
		System.out.println(l);
	}
	
}
