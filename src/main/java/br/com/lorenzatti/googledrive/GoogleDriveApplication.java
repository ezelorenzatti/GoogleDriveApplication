package br.com.lorenzatti.googledrive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class GoogleDriveApplication {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    private static String APPLICATION_NAME = "Upload Google Drive";

    private static String CREDENTIALS = "D:/jfc/portal/client_credentials.json";
    private static String DIRECTORY_ID = "11tNrhIRSyQFc41KiY04Q-9nl4-nheyDo";
    private static String TOKEN = "0f03dc9e1bbf0c144ec630376958ebbc42c00100";

    private static String UPLOAD_FILE = "UploadByAPI.txt";

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials())
                .setApplicationName(APPLICATION_NAME)
                .build();

        URL source = GoogleDriveApplication.class.getClassLoader().getResource(UPLOAD_FILE);
        File metadata = new File();
        metadata.setName(UPLOAD_FILE);
        metadata.setParents(Collections.singletonList(DIRECTORY_ID));

        FileContent fileContent = new FileContent(null, new java.io.File(source.getFile()));

        File upload = service.files()
                .create(metadata, fileContent)
                .setFields("name, id")
                .execute();
    }

    private static Credential getCredentials() throws IOException {
        java.io.File credentialsFile = new java.io.File(CREDENTIALS);
        if (!credentialsFile.exists()) {
            throw new FileNotFoundException("Resource not found: " + credentialsFile);
        }
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(credentialsFile))
                .createScoped(SCOPES).setAccessToken(TOKEN);
        return credential;
    }

}
