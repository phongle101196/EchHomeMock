package com.mock.service;

import com.mock.payload.request.SavePostDto;

import java.util.List;

public interface SavePostService {
    // GET SINGLE SAVEPOST
    List<SavePostDto> getSavePostsByUser(Integer userId);

    // DELETE SAVE POST WITH SAVEPOSTID
    void deleteSavePost(Integer id);
    boolean isUserOwnsSavePost(String username, Integer id);
}
