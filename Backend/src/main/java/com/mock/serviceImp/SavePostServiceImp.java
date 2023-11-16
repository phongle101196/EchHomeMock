package com.mock.serviceImp;

import com.mock.entity.SavePost;
import com.mock.entity.User;
import com.mock.exception.ResourceNotFoundException;
import com.mock.payload.request.SavePostDto;
import com.mock.repository.SavePostRepository;
import com.mock.repository.UserRepository;
import com.mock.service.SavePostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class SavePostServiceImp implements SavePostService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SavePostRepository savePostRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<SavePostDto> getSavePostsByUser(Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
       List<SavePost> savePosts = this.savePostRepository.findAllByUser(user);

       List<SavePostDto> savePostDtos = savePosts.stream().map(savePost -> this.modelMapper.map(savePost,SavePostDto.class)).collect(Collectors.toList());

       return savePostDtos;
    }

    @Override
    public void deleteSavePost(Integer id) {
        SavePost savePost = this.savePostRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("SavePost","Id",id));
        this.savePostRepository.delete(savePost);
    }

    @Override
    public boolean isUserOwnsSavePost(String username, Integer id) {
        Optional<SavePost> optionalSavePost = savePostRepository.findById(id);

        if (optionalSavePost.isPresent()) {
            SavePost savePost = optionalSavePost.get();
            String savePostUsername = savePost.getUser().getUsername();

            return savePostUsername.equals(username);
        }

        return false;
    }

}
