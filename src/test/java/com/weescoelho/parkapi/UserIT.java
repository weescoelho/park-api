package com.weescoelho.parkapi;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.weescoelho.parkapi.controllers.exceptions.StandardError;
import com.weescoelho.parkapi.dto.UserCreateDTO;
import com.weescoelho.parkapi.dto.UserPasswordDTO;
import com.weescoelho.parkapi.dto.UserResponseDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {
  @Autowired
  WebTestClient webTestClient;

  @Test
  public void createUser_ShouldReturnCreated_WithUsernameAndPassword() {
    UserResponseDTO responseBody = webTestClient.post()
        .uri("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("toddy@email.com", "123456"))
        .exchange()
        .expectStatus().isCreated()
        .expectBody(UserResponseDTO.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getId()).isNotNull();
    Assertions.assertThat(responseBody.getUsername()).isEqualTo("toddy@email.com");
    Assertions.assertThat(responseBody.getRole()).isEqualTo("CUSTOMER");
  }

  @Test
  public void createUser_ShouldReturnErrorMessage_WithUsernameInvalid() {
    StandardError responseBody = webTestClient.post()
        .uri("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("", "123456"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(StandardError.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    responseBody = webTestClient.post()
        .uri("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("toddy@", "123456"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(StandardError.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    responseBody = webTestClient.post()
        .uri("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("toddy@email", "123456"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(StandardError.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
  }

  @Test
  public void createUser_ShouldReturnErrorMessage_WithPasswordInvalid() {
    StandardError responseBody = webTestClient.post()
        .uri("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("toddy@email.com", ""))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(StandardError.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    responseBody = webTestClient.post()
        .uri("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("toddy@email.com", "1234"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(StandardError.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    responseBody = webTestClient.post()
        .uri("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("toddy@email.com", "123456789"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(StandardError.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
  }

  @Test
  public void createUser_ShouldReturnErrorMessage_WithUsernameHasBeenUsed() {
    StandardError responseBody = webTestClient.post()
        .uri("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("ana@test.com", "123456"))
        .exchange()
        .expectStatus().isEqualTo(409)
        .expectBody(StandardError.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
  }

  @Test
  public void findUser_ShouldReturnUser_WithStatus200() {
    UserResponseDTO responseBody = webTestClient.get()
        .uri("/api/v1/users/100")
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserResponseDTO.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getId()).isEqualTo("100");
    Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@test.com");
    Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");
  }

  @Test
  public void findUser_ShouldReturnStatus404_WithIdNonExistent() {
    StandardError responseBody = webTestClient.get()
        .uri("/api/v1/users/200")
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(StandardError.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
  }

  @Test
  public void updateUserPassword_ShouldBeReturnStatus204() {
    webTestClient.put()
        .uri("/api/v1/users/100")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPasswordDTO("123456", "654321", "654321"))
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  public void updateUserPassword_ShouldReturnStatus404_WithIdNonExistent() {
    StandardError responseBody = webTestClient.put()
        .uri("/api/v1/users/1")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPasswordDTO("123456", "123456", "123456"))
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(StandardError.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
  }

  @Test
  public void updateUserPassword_ShouldReturnStatus404_WithInvalidFields() {
    StandardError responseBody = webTestClient.put()
        .uri("/api/v1/users/100")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPasswordDTO("", "", ""))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(StandardError.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    responseBody = webTestClient.put()
        .uri("/api/v1/users/100")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPasswordDTO("12345", "12345", "12345"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(StandardError.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    responseBody = webTestClient.put()
        .uri("/api/v1/users/100")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPasswordDTO("1234578", "1234578", "1234578"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(StandardError.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
  }

  @Test
  public void updateUserPassword_ShouldReturnStatus400_WithPasswordWrong() {
    StandardError responseBody = webTestClient.put()
        .uri("/api/v1/users/100")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPasswordDTO("123456", "123451", "123452"))
        .exchange()
        .expectStatus().isEqualTo(400)
        .expectBody(StandardError.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

    responseBody = webTestClient.put()
        .uri("/api/v1/users/100")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPasswordDTO("000000", "123456", "123456"))
        .exchange()
        .expectStatus().isEqualTo(400)
        .expectBody(StandardError.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
  }

  @Test
  public void listUsers_ShouldReturnListUsers() {
    List<UserResponseDTO> responseBody = webTestClient.get()
        .uri("/api/v1/users")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(UserResponseDTO.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.size()).isEqualTo(3);
    Assertions.assertThat(responseBody.get(0).getUsername()).isEqualTo("ana@test.com");
  }
}
