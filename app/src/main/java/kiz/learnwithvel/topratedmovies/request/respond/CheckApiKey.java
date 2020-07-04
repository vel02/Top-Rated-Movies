package kiz.learnwithvel.topratedmovies.request.respond;

public class CheckApiKey {

    public static boolean isMovieApiKeyValid(MovieResponse response) {
        if (response.getSuccess() == null) return true;
        return response.getSuccess().equals("false");
    }

    public static boolean isMovieApiKeyValid(VideoResponse response) {
        if (response.getSuccess() == null) return true;
        return response.getSuccess().equals("false");
    }

}
