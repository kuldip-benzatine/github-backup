package com.sk.buildingmanagement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Note: HttpLoader itself is not an async task. It will be called from
 * particular async task to make http request.
 */
@SuppressWarnings("deprecation")
public class HttpLoader {

	public final int HTTP_CONNECTION_TIMEOUT = 60000;
	public final int HTTP_SO_TIMEOUT = 60000;

	static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	public class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);
			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
											   String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
											   String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
								   boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	public DefaultHttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params,
					HTTP_CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, HTTP_SO_TIMEOUT);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);
			DefaultHttpClient httpclient = new DefaultHttpClient(ccm, params);
			return httpclient;
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	/**
	 * This method will only handle errors that can occur at the network or HTTP
	 * level. application specific errors that are sent in XML/JSON payLoad
	 * should be handled by the activity calling this http loader.
	 *
	 * @param httpUrl
	 * @param params
	 * @return http response from server
	 * @throws Exception
	 */

	// public String loadDataByPost(String httpUrl, List<NameValuePair> params)
	// {
	// String httpResponseData = null;
	// try {
	// // request method
	// HttpPost httpPost = new HttpPost(httpUrl);
	// // create multiPart entity
	// MultipartEntity entity = new MultipartEntity(
	// HttpMultipartMode.BROWSER_COMPATIBLE);
	// for (int index = 0; index < params.size(); index++) {
	// System.out.println(params.get(index).getName() + ":"
	// + params.get(index).getValue());
	//
	// // if (params.get(index).getName()
	// // .contains(HttpConstants.FILE_TYPE_IMAGE)
	// // || params.get(index).getName()
	// // .equals(HttpConstants.PARAM_REG_PROFILE_IMAGE)) {
	// // // If the key equals to "image", we use FileBody to
	// // // transferthe data
	// // entity.addPart(params.get(index).getName(), new FileBody(
	// // new File(params.get(index).getValue())));
	// //
	// // Log.e("", "loadData : image name :"
	// // + params.get(index).getValue());
	// //
	// // } else {
	// // Normal string data
	// entity.addPart(params.get(index).getName(), new StringBody(
	// params.get(index).getValue()));
	// // }
	// }
	//
	// // for (int index = 0; index < paramsImage.size(); index++) {
	// // System.out.println(paramsImage.get(index).getName() + " : "
	// // + paramsImage.get(index).getValue());
	// // if (paramsImage.get(index).getName().length() > 0
	// // && paramsImage.get(index).getValue().length() > 0) {
	// // // If the key equals to "image", we use FileBody to
	// // // transferthe data
	// //
	// // // entity.addPart(params.get(index).getName(), new FileBody(
	// // // new File(params.get(index).getValue())));
	// // File binaryFile = new File(paramsImage.get(index)
	// // .getValue());
	// // System.out.println("binaryFile::" + binaryFile);
	// // if (binaryFile != null && binaryFile.exists()) {
	// // System.out.println("file exist");
	// // entityBuilder.addBinaryBody(paramsImage.get(index)
	// // .getName(), binaryFile);
	// // } else {
	// // System.out.println("binaryFile is null");
	// // }
	// //
	// // Log.e("", "loadData : image name--> image_key :"
	// // + paramsImage.get(index).getValue());
	// // }
	// //
	// // }
	//
	// // set HTTP request parameters if any
	// if (entity != null) {
	// httpPost.setEntity(entity);
	// }
	// // Perform the request and check the status code
	// HttpResponse httpResponse = getNewHttpClient().execute(httpPost);
	// StatusLine statusLine = httpResponse.getStatusLine();
	// if (statusLine != null
	// && statusLine.getStatusCode() == HttpStatus.SC_OK) {
	// HttpEntity httpEntity = httpResponse.getEntity();
	// httpResponseData = EntityUtils.toString(httpEntity);
	// } else {
	// System.out.println("ERROR........."
	// + "statusLine null or result not ok");
	// }
	// } catch (Exception e) {
	// System.out.println("ERROR........." + "connection error.");
	// e.printStackTrace();
	// }
	// // return response
	// // note: if there is any error - a null response is sent
	// return httpResponseData;
	// }

	public String loadDataByPost(String httpUrl, List<NameValuePair> params)
			throws Exception {
		String httpResponseData = null;
		try {
			// request method
			System.out.println("httpUrl::" + httpUrl);
			HttpPost httpPost = new HttpPost(httpUrl);
			// create multiPart entity
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder
					.create();
			entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			for (int index = 0; index < params.size(); index++) {
				System.out.println(params.get(index).getName() + " : "
						+ params.get(index).getValue());

				entityBuilder.addTextBody(params.get(index).getName(), params
						.get(index).getValue());
			}
			if (entityBuilder != null) {
				httpPost.setEntity(entityBuilder.build());
			} else {
				System.out.println("entityBuilder is null");
			}
			// Perform the request and check the status code
			HttpResponse httpResponse = getNewHttpClient().execute(httpPost);
			StatusLine statusLine = httpResponse.getStatusLine();
			if (statusLine != null
					&& statusLine.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = httpResponse.getEntity();
				httpResponseData = EntityUtils.toString(httpEntity);
			} else {
				System.out.println("ERROR........."
						+ "statusLine null or result not ok");
			}
		} catch (Exception ex) {
			System.out.println("ERROR........." + "connection error.");
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

		}
		// return response
		// note: if there is any error - a null response is sent
		System.out.println("httpResponseData:::" + httpResponseData);
		return httpResponseData;
	}

	public String loadDataByPost(String httpUrl, List<NameValuePair> params,
								 List<NameValuePair> paramsImage) throws Exception {
		// TODO Auto-generated method stub
		String httpResponseData = null;
		try {
			// request method
			System.out.println("httpUrl::" + httpUrl);
			HttpPost httpPost = new HttpPost(httpUrl);
			// create multiPart entity
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder
					.create();
			entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			for (int index = 0; index < params.size(); index++) {
				System.out.println(params.get(index).getName() + " : "
						+ params.get(index).getValue());

				entityBuilder.addTextBody("" + params.get(index).getName(), ""
						+ params.get(index).getValue());

			}

			// for image

			for (int index = 0; index < paramsImage.size(); index++) {
				System.out.println(paramsImage.get(index).getName() + " : "
						+ paramsImage.get(index).getValue());
				if (paramsImage.get(index).getName().length() > 0
						&& paramsImage.get(index).getValue().length() > 0) {

					if (paramsImage.get(index).getValue().contains("http://")||paramsImage.get(index).getValue().contains("https://")) {

						// Load image from url
						URL url = new URL(paramsImage.get(index).getValue());
						InputStream in = url.openConnection().getInputStream();
						BufferedInputStream bis = new BufferedInputStream(in,
								1024 * 8);
						ByteArrayOutputStream out = new ByteArrayOutputStream();

						int len = 0;
						byte[] buffer = new byte[1024];
						while ((len = bis.read(buffer)) != -1) {
							out.write(buffer, 0, len);
						}
						out.close();
						bis.close();

						byte[] data = out.toByteArray();

						System.out.println("data size::" + data.length);
						ByteArrayBody bab = new ByteArrayBody(data,new SimpleDateFormat("yyyyMMddhhmmss'.png'").format(new Date()));
						entityBuilder.addPart(paramsImage.get(index).getName(),
								bab);

					} else {

						// load image from local storage
						File binaryFile = new File(paramsImage.get(index)
								.getValue());
							if (binaryFile != null && binaryFile.exists()) {
							System.out.println("file exist");
							FileInputStream is=new FileInputStream(binaryFile);
							BufferedInputStream bis = new BufferedInputStream(is,
									1024 * 8);
							ByteArrayOutputStream out = new ByteArrayOutputStream();

							int len = 0;
							byte[] buffer = new byte[1024];
							while ((len = bis.read(buffer)) != -1) {
								out.write(buffer, 0, len);
							}
							out.close();
							bis.close();


							byte[] data = out.toByteArray();

							System.out.println("data size::" + data.length);
							ByteArrayBody bab = new ByteArrayBody(data,new SimpleDateFormat("yyyyMMddhhmmss'.png'").format(new Date()));
							entityBuilder.addPart(paramsImage.get(index).getName(),
									bab);

							//entityBuilder.addBinaryBody(paramsImage.get(index)
							//		.getName(), binaryFile);

						} else {
							System.out.println("binaryFile is null");
						}

					}

					Log.e("", "loadData : image name--> image_key :"
							+ paramsImage.get(index).getValue());
				}

			}

			if (entityBuilder != null) {
				httpPost.setEntity(entityBuilder.build());
			} else {
				System.out.println("entityBuilder is null");
			}
			// Perform the request and check the status code
			HttpResponse httpResponse = getNewHttpClient().execute(httpPost);
			StatusLine statusLine = httpResponse.getStatusLine();
			if (statusLine != null
					&& statusLine.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = httpResponse.getEntity();
				httpResponseData = EntityUtils.toString(httpEntity);
			} else {
				System.out.println("ERROR........."
						+ "statusLine null or result not ok");
			}
		} catch (Exception ex) {
			System.out.println("ERROR........." + "connection error.");
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

		}
		System.out.println("httpResponseData:::" + httpResponseData);
 		return httpResponseData;
	}

	public String loadAttachmentDataByPost(String httpUrl,
										   List<NameValuePair> paramsImage) throws Exception {
		String httpResponseData = null;
		try {
			System.out.println("httpUrl::" + httpUrl);
			HttpPost httpPost = new HttpPost(httpUrl);
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder
					.create();
			entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			// for image

			for (int index = 0; index < paramsImage.size(); index++) {
				System.out.println(paramsImage.get(index).getName() + " : "
						+ paramsImage.get(index).getValue());
				if (paramsImage.get(index).getName().length() > 0
						&& paramsImage.get(index).getValue().length() > 0) {
					File binaryFile = new File(paramsImage.get(index)
							.getValue());
					System.out.println("binaryFile::" + binaryFile);
					if (binaryFile != null && binaryFile.exists()) {
						System.out.println("file exist");
						entityBuilder.addBinaryBody(paramsImage.get(index)
								.getName(), binaryFile);
					} else {
						System.out.println("binaryFile is null");
					}

					Log.e("", "loadData : image name--> image_key :"
							+ paramsImage.get(index).getValue());
				}

			}

			if (entityBuilder != null) {
				httpPost.setEntity(entityBuilder.build());
			} else {
				System.out.println("entityBuilder is null");
			}
			// Perform the request and check the status code
			HttpResponse httpResponse = getNewHttpClient().execute(httpPost);
			StatusLine statusLine = httpResponse.getStatusLine();
			if (statusLine != null
					&& statusLine.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = httpResponse.getEntity();
				httpResponseData = EntityUtils.toString(httpEntity);
			} else {
				System.out.println("ERROR........."
						+ "statusLine null or result not ok");
			}
		} catch (Exception ex) {
			System.out.println("ERROR........." + "connection error.");
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

		}
		System.out.println("httpResponseData:::" + httpResponseData);
		return httpResponseData;
	}

	public String loadBitmapDataByPost(String httpUrl,
									   List<NameValuePair> params, List<NameValuePair> paramsImage)
			throws Exception {
		String httpResponseData = null;
		try {
			// request method
			HttpPost httpPost = new HttpPost(httpUrl);
			// create multiPart entity
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			System.out.println("httpUrl::" + httpUrl);

			// for text parameter

			for (int index = 0; index < params.size(); index++) {
				System.out.println(params.get(index).getName() + " : "
						+ params.get(index).getValue());

				entity.addPart(params.get(index).getName(), new StringBody(
						params.get(index).getValue()));

			}

			// for image

			for (int index = 0; index < paramsImage.size(); index++) {
				System.out.println(paramsImage.get(index).getName() + " : "
						+ paramsImage.get(index).getValue());

				// If the key equals to "image", we use FileBody to
				// transferthe data
				// URL imageUrl = new URL(params.get(index).getValue());
				// Bitmap bm = BitmapFactory.decodeStream(imageUrl
				// .openConnection().getInputStream());
				// ByteArrayOutputStream bos = new ByteArrayOutputStream();
				// if (bm == null) {
				// System.out.println("bm is null");
				// bm = getImageBitmapFromUrl(imageUrl);
				// if (bm == null) {
				// System.out.println("bm in  is null");
				// bm = getBitmapFromURL(params.get(index).getValue());
				// }
				// }
				// bm.compress(CompressFormat.PNG, 100, bos);
				//
				// byte[] data = bos.toByteArray();

				URL url = new URL(paramsImage.get(index).getValue());
				InputStream in = url.openConnection().getInputStream();
				BufferedInputStream bis = new BufferedInputStream(in, 1024 * 8);
				ByteArrayOutputStream out = new ByteArrayOutputStream();

				int len = 0;
				byte[] buffer = new byte[1024];
				while ((len = bis.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				out.close();
				bis.close();

				byte[] data = out.toByteArray();

				System.out.println("data size::" + data.length);
				ByteArrayBody bab = new ByteArrayBody(data, paramsImage.get(
						index).getValue()
						+ ".png");
				entity.addPart("" + paramsImage.get(index).getName(), bab);

				// entity.addPart(params.get(index).getName(), new FileBody(
				// new File(params.get(index).getValue())));

				Log.e("", "loadData image=" + paramsImage.get(index).getName()
						+ " : " + paramsImage.get(index).getValue());

			}

			// set HTTP request parameters if any
			if (entity != null) {
				httpPost.setEntity(entity);
			}
			// Perform the request and check the status code
			HttpResponse httpResponse = getNewHttpClient().execute(httpPost);
			StatusLine statusLine = httpResponse.getStatusLine();
			if (statusLine != null
					&& statusLine.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = httpResponse.getEntity();
				httpResponseData = EntityUtils.toString(httpEntity);
			} else {
				System.out.println("ERROR........."
						+ "statusLine null or result not ok");
			}
		} catch (Exception ex) {
			System.out.println("ERROR........." + "connection error.");
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
		// return response
		// note: if there is any error - a null response is sent
		System.out.println("httpResponseData:::" + httpResponseData);
		return httpResponseData;
	}

	public String loadBitmapByteArrayByPost(String httpUrl,
											List<NameValuePair> params, List<NameValuePair> paramsImage)
			throws Exception {
		String httpResponseData = null;
		try {
			// request method
			HttpPost httpPost = new HttpPost(httpUrl);
			// create multiPart entity

			System.out.println("httpUrl::" + httpUrl);
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder
					.create();
			entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			// for text parameter

			for (int index = 0; index < params.size(); index++) {
				System.out.println(params.get(index).getName() + " : "
						+ params.get(index).getValue());

				entityBuilder.addTextBody(params.get(index).getName(), params
						.get(index).getValue());

			}

			// for image

			for (int index = 0; index < paramsImage.size(); index++) {
				System.out.println(paramsImage.get(index).getName() + " : "
						+ paramsImage.get(index).getValue());

				URL url = new URL(paramsImage.get(index).getValue());
				InputStream in = url.openConnection().getInputStream();
				BufferedInputStream bis = new BufferedInputStream(in, 1024 * 8);
				ByteArrayOutputStream out = new ByteArrayOutputStream();

				int len = 0;
				byte[] buffer = new byte[1024];
				while ((len = bis.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				out.close();
				bis.close();

				byte[] data = out.toByteArray();

				System.out.println("data size::" + data.length);
				entityBuilder.addBinaryBody(paramsImage.get(index).getName(),
						data);

				Log.e("", "loadData image=" + paramsImage.get(index).getName()
						+ " : " + paramsImage.get(index).getValue());

			}

			// set HTTP request parameters if any
			if (entityBuilder != null) {
				httpPost.setEntity(entityBuilder.build());
			}
			// Perform the request and check the status code
			HttpResponse httpResponse = getNewHttpClient().execute(httpPost);
			StatusLine statusLine = httpResponse.getStatusLine();
			if (statusLine != null
					&& statusLine.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = httpResponse.getEntity();
				httpResponseData = EntityUtils.toString(httpEntity);
			} else {
				System.out.println("ERROR........."
						+ "statusLine null or result not ok");
			}
		} catch (Exception ex) {
			System.out.println("ERROR........." + "connection error.");
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
		System.out.println("httpResponseData:::" + httpResponseData);
		return httpResponseData;
	}

	public String loadDataByGet(String httpUrl) throws Exception {
		// TODO Auto-generated method stub
		String httpResponseData = null;
		try {
			httpUrl = httpUrl.replace(" ", "%20");
			// request method
			HttpGet httpGet = new HttpGet(httpUrl);

			System.out.println("httpUrl::" + httpUrl);

			HttpResponse httpResponse = getNewHttpClient().execute(httpGet);
			StatusLine statusLine = httpResponse.getStatusLine();
			if (statusLine != null
					&& statusLine.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = httpResponse.getEntity();
				httpResponseData = EntityUtils.toString(httpEntity);
			} else {
				System.out.println("ERROR........."
						+ "statusLine null or result not ok");
			}
		} catch (Exception ex) {
			System.out.println("ERROR........." + "connection error.");
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
		System.out.println("httpResponseData:::" + httpResponseData);
		return httpResponseData;
	}

	public boolean isValidUrl(String httpUrl) throws Exception {
		try {
			httpUrl = httpUrl.replace(" ", "%20");
			// request method
			HttpGet httpGet = new HttpGet(httpUrl);

			System.out.println("httpUrl::" + httpUrl);

			HttpResponse httpResponse = getNewHttpClient().execute(httpGet);
			StatusLine statusLine = httpResponse.getStatusLine();
			if (statusLine != null
					&& statusLine.getStatusCode() == HttpStatus.SC_OK) {
				System.out.println("statusLine.getStatusCode()::"
						+ statusLine.getStatusCode());
				return true;
			} else {
				System.out.println("ERROR........."
						+ "statusLine null or result not ok");
				return false;
			}
		} catch (Exception ex) {
			System.out.println("ERROR........." + "connection error.");
			ex.printStackTrace();
		}
		return false;
	}

	public static Bitmap getBitmapFromURL(String src) {
		System.out.println("photo src::" + src);
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			Bitmap myBitmap = BitmapFactory.decodeStream(input, new Rect(0, 0,
					0, 0), options);
			options.inSampleSize = calculateInSampleSize(options, 500, 500);
			options.inJustDecodeBounds = false;
			myBitmap = BitmapFactory.decodeStream(input, new Rect(0, 0, 0, 0),
					options);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap getImageBitmapFromUrl(URL url) {
		Bitmap bm = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (conn.getResponseCode() != 200) {
				return bm;
			}
			conn.connect();
			InputStream is = conn.getInputStream();

			BufferedInputStream bis = new BufferedInputStream(is);
			try {
				bm = BitmapFactory.decodeStream(bis);
			} catch (OutOfMemoryError ex) {
				bm = null;
			}
			bis.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bm;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

}
