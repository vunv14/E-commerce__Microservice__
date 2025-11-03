package user_service.user_service.infrastructure.constants;

/**
 * Created by vunv on 10/28/2025
 */
public interface ApiPath {

    String API_BASE = "/api/v1";

    // 1. APi for User Authentication
    String AUTH = API_BASE + "/auth";
    String LOGIN = AUTH + "/login";

}
