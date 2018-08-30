package main.java;

import java.io.File;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class VoiceIt2 {
	
	private String BASE_URL = "https://api.voiceit.io";
	private HttpClient httpClient;
	
	public VoiceIt2(String apiKey, String apiToken){
			HttpClientBuilder clientBuilder = HttpClientBuilder.create();
			setup(clientBuilder, apiKey, apiToken);
			httpClient = clientBuilder.build();
	}
	
	public VoiceIt2(String apiKey, String apiToken, String customBaseURL) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException{
		// Setting custom host URL and SSL Context
		BASE_URL = customBaseURL;
    	SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new X509TrustManager[] { new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
		public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0];}}}, new SecureRandom());
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) { return true; }
		};
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setSSLContext(sslContext).setSSLHostnameVerifier(hostnameVerifier);
		setup(clientBuilder, apiKey, apiToken);
		httpClient = clientBuilder.build();
	}
	
	private void setup(HttpClientBuilder clientBuilder, String apiKey, String apiToken) {
	      CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	      credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(apiKey, apiToken));
	      clientBuilder
	      .setDefaultCredentialsProvider(credentialsProvider)
	      .setDefaultHeaders(Arrays.asList(new BasicHeader("platformId", "29")));
	}
	
	public String getPhrases() {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/phrases")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String getAllUsers() {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/users")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String createUser() {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpPost(BASE_URL + "/users")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String checkUserExists(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/users/" + userId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String deleteUser(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/users/" + userId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String getGroupsForUser(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/users/" + userId + "/groups")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String deleteAllEnrollments(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/enrollments/" + userId + "/all")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String getAllVoiceEnrollments(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/enrollments/voice/" + userId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String getAllFaceEnrollments(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/enrollments/face/" + userId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String getAllVideoEnrollments(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/enrollments/video/" + userId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String createVoiceEnrollment(String userId, String contentLanguage, String phrase, String recordingPath) {
    return createVoiceEnrollment(userId, contentLanguage, phrase, new File(recordingPath));
  }

	public String createVoiceEnrollment(String userId, String contentLanguage, String phrase, File recording) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase)
		    .addBinaryBody("recording", recording, ContentType.create("application/octet-stream"), "recording")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/voice");
		httpPost.setEntity(entity);

		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String createVoiceEnrollmentByUrl(String userId, String contentLanguage, String phrase, String fileUrl) {
			
			HttpEntity entity = MultipartEntityBuilder
			    .create()
			    .addTextBody("userId", userId)
			    .addTextBody("contentLanguage", contentLanguage)
			    .addTextBody("phrase", phrase)
			    .addTextBody("fileUrl", fileUrl)
			    .build();
			HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/voice/byUrl");
			httpPost.setEntity(entity);
			
			try {		
				return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
			} catch (Exception e) {
				return e.getMessage();
			}	
	}

	public String createFaceEnrollment(String userId, File video) {
    return createFaceEnrollment(userId, video, false);
	}
	
	public String createFaceEnrollment(String userId, String videoPath) {
    return createFaceEnrollment(userId, new File(videoPath), false);
  }

	public String createFaceEnrollment(String userId, String videoPath, boolean doBlinkDetection) {
    return createFaceEnrollment(userId, new File(videoPath), doBlinkDetection);
  }

	public String createFaceEnrollment(String userId, File video, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/face");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String createFaceEnrollmentByUrl(String userId, String fileUrl) {
		return createFaceEnrollmentByUrl(userId, fileUrl, false);
	}
	
	public String createFaceEnrollmentByUrl(String userId, String fileUrl, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/face/byUrl");
		httpPost.setEntity(entity);
	
		try {	
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}

	public String createVideoEnrollment(String userId, String contentLanguage, String phrase, File video) {
    return createVideoEnrollment(userId, contentLanguage, phrase, video, false);
	}

	public String createVideoEnrollment(String userId, String contentLanguage, String phrase, String videoPath) {
    return createVideoEnrollment(userId, contentLanguage, phrase, new File(videoPath), false);
	}

	public String createVideoEnrollment(String userId, String contentLanguage, String phrase, String videoPath, boolean doBlinkDetection) {
    return createVideoEnrollment(userId, contentLanguage, phrase, new File(videoPath), doBlinkDetection);
	}
	
	public String createVideoEnrollment(String userId, String contentLanguage, String phrase, File video, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/video");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String createVideoEnrollmentByUrl(String userId, String contentLanguage, String phrase, String fileUrl) {
    return createVideoEnrollmentByUrl(userId, contentLanguage, phrase, fileUrl, false);
	}
	
	public String createVideoEnrollmentByUrl(String userId, String contentLanguage, String phrase, String fileUrl, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/video/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String deleteVoiceEnrollment(String userId, int enrollmentId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/enrollments/voice/" + userId + "/" + Integer.toString(enrollmentId))).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String deleteFaceEnrollment(String userId, int faceEnrollmentId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/enrollments/face/" + userId + "/" + Integer.toString(faceEnrollmentId))).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String deleteVideoEnrollment(String userId, int enrollmentId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/enrollments/video/" + userId + "/" + Integer.toString(enrollmentId))).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String deleteAllVoiceEnrollments(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/enrollments/" + userId + "/voice")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String deleteAllFaceEnrollments(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/enrollments/" + userId + "/face")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String deleteAllVideoEnrollments(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/enrollments/" + userId + "/video")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String getAllGroups() {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/groups")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String getGroup(String groupId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/groups/" + groupId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String groupExists(String groupId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/groups/" + groupId + "/exists")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String createGroup(String description) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("description", description)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/groups");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String addUserToGroup(String groupId, String userId) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("userId", userId)
		    .build();
		HttpPut httpPut = new HttpPut(BASE_URL + "/groups/addUser");
		httpPut.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPut).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String removeUserFromGroup(String groupId, String userId) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("userId", userId)
		    .build();
		HttpPut httpPut = new HttpPut(BASE_URL + "/groups/removeUser");
		httpPut.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPut).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String deleteGroup(String groupId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/groups/" + groupId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String voiceVerification(String userId, String contentLanguage, String phrase, String recordingPath) {
    return voiceVerification(userId, contentLanguage, phrase, new File(recordingPath));
  }

	public String voiceVerification(String userId, String contentLanguage, String phrase, File recording) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase)
		    .addBinaryBody("recording", recording, ContentType.create("application/octet-stream"), "recording")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/voice");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String voiceVerificationByUrl(String userId, String contentLanguage, String phrase, String fileUrl) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase)
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/voice/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String faceVerification(String userId, File video) {
		return faceVerification(userId, video, false);
	}

	public String faceVerification(String userId, String videoPath) {
		return faceVerification(userId, new File(videoPath), false);
	}

	public String faceVerification(String userId, String videoPath, boolean doBlinkDetection) {
		return faceVerification(userId, new File(videoPath), doBlinkDetection);
	}
	
	public String faceVerification(String userId, File video, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/face");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String faceVerificationByUrl(String userId, String fileUrl) {
		return faceVerificationByUrl(userId, fileUrl, false);
	}
	
	public String faceVerificationByUrl(String userId, String fileUrl, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/face/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String faceIdentification(String groupId, File video) {
	    return faceIdentification(groupId, video, false);
	}

	public String faceIdentification(String groupId, String videoPath) {
		return faceIdentification(groupId, new File(videoPath), false);
	}

	public String faceIdentification(String groupId, String videoPath, boolean doBlinkDetection) {
		return faceIdentification(groupId, new File(videoPath), doBlinkDetection);
	}
	
	public String faceIdentification(String groupId, File video, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/face");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String faceIdentificationByUrl(String groupId, String fileUrl) {
		return faceIdentificationByUrl(groupId, fileUrl, false);
	}
	
	public String faceIdentificationByUrl(String groupId, String fileUrl, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/face/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String videoVerification(String userId, String contentLanguage, String phrase, File video) {
    return videoVerification(userId, contentLanguage, phrase, video, false);
	}

  public String videoVerification(String userId, String contentLanguage, String phrase, String videoPath) {
    return videoVerification(userId, contentLanguage, phrase, new File(videoPath), false);
  }

  public String videoVerification(String userId, String contentLanguage, String phrase, String videoPath, boolean doBlinkDetection) {
    return videoVerification(userId, contentLanguage, phrase, new File(videoPath), doBlinkDetection);
  }
	
	public String videoVerification(String userId, String contentLanguage, String phrase, File video, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/video");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String videoVerificationByUrl(String userId, String contentLanguage, String phrase, String fileUrl) {
    return videoVerificationByUrl(userId, contentLanguage, phrase, fileUrl, false);
	}

	public String videoVerificationByUrl(String userId, String contentLanguage, String phrase, String fileUrl, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/video/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String voiceIdentification(String groupId, String contentLanguage, String phrase, String recordingPath) {
    return voiceIdentification(groupId, contentLanguage, phrase, new File(recordingPath));
  }

	public String voiceIdentification(String groupId, String contentLanguage, String phrase, File recording) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase)
		    .addBinaryBody("recording", recording, ContentType.create("application/octet-stream"), "recording")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/voice");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String voiceIdentificationByUrl(String groupId, String contentLanguage, String phrase, String fileUrl) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase)
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/voice/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String videoIdentification(String groupId, String contentLanguage, String phrase, File video) {
    return videoIdentification(groupId, contentLanguage, phrase, video, false);
	}

	public String videoIdentification(String groupId, String contentLanguage, String phrase, String videoPath) {
    return videoIdentification(groupId, contentLanguage, phrase, new File(videoPath), false);
	}

	public String videoIdentification(String groupId, String contentLanguage, String phrase, String videoPath, boolean doBlinkDetection) {
    return videoIdentification(groupId, contentLanguage, phrase, new File(videoPath), doBlinkDetection);
	}
	
	public String videoIdentification(String groupId, String contentLanguage, String phrase, File video , boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/video");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String videoIdentificationByUrl(String groupId, String contentLanguage, String phrase, String fileUrl) {
    return videoIdentificationByUrl(groupId, contentLanguage, phrase, fileUrl, false);
	}

	public String videoIdentificationByUrl(String groupId, String contentLanguage, String phrase, String fileUrl, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/video/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
}
