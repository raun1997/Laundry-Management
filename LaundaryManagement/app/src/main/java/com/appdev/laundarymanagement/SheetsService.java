package com.appdev.laundarymanagement;

import com.google.api.services.sheets.v4.model.ValueRange;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SheetsService {
        @GET("v4/spreadsheets/{spreadsheetId}/values/{range}")
        Call<ValueRange> getValues(@Path("spreadsheetId") String spreadsheetId, @Path("range") String range, @Query("key") String apiKey);
}
