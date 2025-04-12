import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface ApiService {

    // Example: Fetch Dog Breeds (Replace with your actual endpoint)
    @GET("breeds")
    Call<List<Breed>> getDogBreeds();

    // Example: Fetch Allergies (Replace with your actual endpoint)
    @GET("allergies")
    Call<List<String>> getAllergies();
}
