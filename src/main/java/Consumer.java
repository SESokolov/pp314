import entity.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class Consumer {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String sessionId;

        String url = "http://94.198.50.185:7081/api/users";

        // 1. Получить список всех пользователей и извлечь sessionId
        ResponseEntity<String> getUsersResponse = restTemplate.getForEntity(url, String.class);
        System.out.println("Get Users Response: " + getUsersResponse.getBody());

        // Извлечение sessionId из заголовков ответа
        sessionId = getUsersResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        if (sessionId != null) {
            sessionId = sessionId.split(";")[0];
        }

        System.out.println("Extracted Session ID: " + sessionId);

        // Настройка заголовков с куки
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (sessionId != null) {
            headers.add(HttpHeaders.COOKIE, sessionId);
        }

        // 2. Добавить пользователя
        User newUser = new User();
        newUser.setId(3L);
        newUser.setName("James");
        newUser.setLastName("Brown");
        newUser.setAge((byte) 24);

        HttpEntity<User> addUserRequest = new HttpEntity<>(newUser, headers);
        ResponseEntity<String> addUserResponse = restTemplate.exchange(url, HttpMethod.POST, addUserRequest, String.class);
        System.out.println("Add User Response: " + addUserResponse.getBody());

        // 3. Изменить пользователя
        newUser.setName("Thomas");
        newUser.setLastName("Shelby");
        HttpEntity<User> updateEntity = new HttpEntity<>(newUser, headers);

        ResponseEntity<String> updateUserResponse = restTemplate.exchange(url, HttpMethod.POST, updateEntity, String.class); // Используем POST для обновления
        System.out.println("Update User Response: " + updateUserResponse.getBody());
        // 4. Удалить пользователя
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);
        ResponseEntity<String> deleteUserResponse = restTemplate.exchange(url + "/3", HttpMethod.DELETE, deleteRequest, String.class);
        System.out.println("Delete User Response: " + deleteUserResponse.getBody());


        String generalResponse = addUserResponse.getBody() + updateUserResponse.getBody() + deleteUserResponse.getBody();
        System.out.println("General response is: " + generalResponse);

    }
}
