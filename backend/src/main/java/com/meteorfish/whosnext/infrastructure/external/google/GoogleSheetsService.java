package com.meteorfish.whosnext.infrastructure.external.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleSheetsService {

    private final String spreadsheetId;
    private final String credentialsPath;
    private final Sheets sheetsService;

    public GoogleSheetsService(
            @Value("${google.sheets.spreadsheet-id}") String spreadsheetId,
            @Value("${google.sheets.credentials-path}") String credentialsPath
    ) throws GeneralSecurityException, IOException {
        this.spreadsheetId = spreadsheetId;
        this.credentialsPath = credentialsPath;
        this.sheetsService = createSheetsService();
    }

    private Sheets createSheetsService() throws GeneralSecurityException, IOException {
        ClassPathResource resource = new ClassPathResource(credentialsPath);
        if (!resource.exists()) {
            throw new IOException("Credentials file not found in classpath: " + credentialsPath);
        }
        
        try (InputStream inputStream = resource.getInputStream()) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream)
                    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS_READONLY));

            return new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName("University-Job-Portal")
                    .build();
        }
    }

    public List<GoogleFormRow> getNewResponses(int lastReadRow) {
        try {
            String range = "A" + (lastReadRow + 1) + ":F";

            ValueRange response = sheetsService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();

            List<List<Object>> values = response.getValues();
            if (values == null || values.isEmpty()) {
                return Collections.emptyList();
            }

            return values.stream()
                    .map(GoogleFormRow::fromList)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("구글 시트 데이터를 가져오는 중 오류 발생", e);
        }
    }
}
