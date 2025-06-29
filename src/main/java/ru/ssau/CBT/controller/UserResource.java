package ru.ssau.CBT.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ssau.CBT.model.User;
import ru.ssau.CBT.model.UserDto;
import ru.ssau.CBT.model.UserMapper;
import ru.ssau.CBT.repository.UserRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/admin-ui/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<UserDto> getAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserDto> userDtoPage = users.map(userMapper::toUserDto);
        return new PagedModel<>(userDtoPage);
    }

    @GetMapping("/{id}")
    public UserDto getOne(@PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userMapper.toUserDto(userOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    @GetMapping("/by-ids")
    public List<UserDto> getMany(@RequestParam List<String> ids) {
        List<User> users = userRepository.findAllById(ids);
        return users.stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto dto) {
        User user = userMapper.toEntity(dto);
        User resultUser = userRepository.save(user);
        return userMapper.toUserDto(resultUser);
    }

    @PatchMapping("/{id}")
    public UserDto patch(@PathVariable String id, @RequestBody JsonNode patchNode) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        UserDto userDto = userMapper.toUserDto(user);
        objectMapper.readerForUpdating(userDto).readValue(patchNode);
        userMapper.updateWithNull(userDto, user);

        User resultUser = userRepository.save(user);
        return userMapper.toUserDto(resultUser);
    }

    @PatchMapping
    public List<String> patchMany(@RequestParam List<String> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<User> users = userRepository.findAllById(ids);

        for (User user : users) {
            UserDto userDto = userMapper.toUserDto(user);
            objectMapper.readerForUpdating(userDto).readValue(patchNode);
            userMapper.updateWithNull(userDto, user);
        }

        List<User> resultUsers = userRepository.saveAll(users);
        return resultUsers.stream()
                .map(User::getUsername)
                .toList();
    }

    @DeleteMapping("/{id}")
    public UserDto delete(@PathVariable String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
        return userMapper.toUserDto(user);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<String> ids) {
        userRepository.deleteAllById(ids);
    }
}
