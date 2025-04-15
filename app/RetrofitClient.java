import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.yourwebsite.com/";  // Replace with your base URL

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)  // Set the base URL of the API
                    .addConverterFactory(GsonConverterFactory.create())  // For JSON conversion
                    .build();
        }
        return retrofit.create(ApiService.class);  // Return the ApiService implementation
    }
}
